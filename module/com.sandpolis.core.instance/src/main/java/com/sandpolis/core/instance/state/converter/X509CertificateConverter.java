// This source file was automatically generated by the Sandpolis codegen plugin.
package com.sandpolis.core.instance.state.converter;

import java.security.cert.X509Certificate;
import java.util.function.Function;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.sandpolis.core.foundation.util.CertUtil;

@Converter
public class X509CertificateConverter implements AttributeConverter<X509Certificate, byte[]> {
	public static final X509CertificateConverter INSTANCE = new X509CertificateConverter();

	public static final Function<byte[], X509Certificate> DESERIALIZER = INSTANCE::convertToEntityAttribute;

	public static final Function<X509Certificate, byte[]> SERIALIZER = INSTANCE::convertToDatabaseColumn;

	@Override
	public byte[] convertToDatabaseColumn(X509Certificate value) {
		try {
			return value.getEncoded();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public X509Certificate convertToEntityAttribute(byte[] value) {
		try {
			return CertUtil.parseCert(value);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
