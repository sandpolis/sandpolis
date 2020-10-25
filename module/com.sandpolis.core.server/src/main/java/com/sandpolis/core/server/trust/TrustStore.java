//============================================================================//
//                                                                            //
//                Copyright © 2015 - 2020 Subterranean Security               //
//                                                                            //
//  This source file is subject to the terms of the Mozilla Public License    //
//  version 2. You may not use this file except in compliance with the MPL    //
//  as published by the Mozilla Foundation at:                                //
//                                                                            //
//    https://mozilla.org/MPL/2.0                                             //
//                                                                            //
//=========================================================S A N D P O L I S==//
package com.sandpolis.core.server.trust;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertPathValidator;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.PKIXParameters;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sandpolis.core.foundation.ConfigStruct;
import com.sandpolis.core.foundation.util.CertUtil;
import com.sandpolis.core.instance.state.VirtTrustAnchor;
import com.sandpolis.core.instance.state.st.STCollection;
import com.sandpolis.core.instance.state.vst.VirtCollection;
import com.sandpolis.core.instance.store.ConfigurableStore;
import com.sandpolis.core.instance.store.STCollectionStore;
import com.sandpolis.core.server.trust.TrustStore.TrustStoreConfig;

/**
 * The {@link TrustStore} contains trust anchors for plugin certificate
 * authorities.
 *
 * @author cilki
 * @since 5.0.0
 */
public final class TrustStore extends STCollectionStore<TrustAnchor> implements ConfigurableStore<TrustStoreConfig> {

	private static final Logger log = LoggerFactory.getLogger(TrustStore.class);

	public TrustStore() {
		super(log);
	}

	/**
	 * Verify a plugin certificate against the trust anchors in the store.
	 *
	 * @param cert The plugin's certificate
	 * @return Whether the certificate could be validated
	 */
	public boolean verifyPluginCertificate(X509Certificate cert) {
		Objects.requireNonNull(cert);

		PKIXParameters params;
		try (Stream<TrustAnchor> stream = values().stream()) {
			params = new PKIXParameters(stream.map(t -> new java.security.cert.TrustAnchor(t.getCertificate(), null))
					.collect(Collectors.toSet()));
			params.setRevocationEnabled(false);
		} catch (InvalidAlgorithmParameterException e) {
			throw new RuntimeException(e);
		}

		try {
			CertPathValidator.getInstance("PKIX")
					.validate(CertificateFactory.getInstance("X.509").generateCertPath(List.of(cert)), params);
		} catch (CertPathValidatorException | CertificateException e) {
			return false;
		} catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
			throw new RuntimeException(e);
		}

		log.debug("Successfully verified certificate: {}", cert.getSerialNumber());
		return true;
	}

	@Override
	public void init(Consumer<TrustStoreConfig> configurator) {
		var config = new TrustStoreConfig();
		configurator.accept(config);

		collection = new VirtCollection<>(config.collection);

		// Install root CA if required
		if (getMetadata().getInitCount() == 1) {
			create(anchor -> {
				anchor.name().set("PLUGIN CA");
				anchor.certificate().set(CertUtil.getPluginRoot());
			});
		}
	}

	public TrustAnchor create(Consumer<VirtTrustAnchor> configurator) {
		var anchor = add(TrustAnchor::new);
		configurator.accept(anchor);
		return anchor;
	}

	@ConfigStruct
	public static final class TrustStoreConfig {

		public STCollection collection;
	}

	public static final TrustStore TrustStore = new TrustStore();
}
