//============================================================================//
//                                                                            //
//                         Copyright © 2015 Sandpolis                         //
//                                                                            //
//  This source file is subject to the terms of the Mozilla Public License    //
//  version 2. You may not use this file except in compliance with the MPL    //
//  as published by the Mozilla Foundation.                                   //
//                                                                            //
//============================================================================//
package com.sandpolis.core.server.state;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;

import com.sandpolis.core.instance.state.oid.Oid;
import com.sandpolis.core.instance.state.st.AbstractSTDocument;
import com.sandpolis.core.instance.state.st.STDocument;

/**
 * {@link HibernateDocument} allows documents to be persistent.
 *
 * @since 5.1.1
 */
@Entity
public class HibernateDocument extends AbstractSTDocument implements STDocument {

	@Id
	private String db_id;

	@Column(nullable = true)
	private HibernateDocument parent;

	@MapKeyColumn
	@OneToMany(cascade = CascadeType.ALL)
	private Map<Integer, HibernateDocument> documents;

	@MapKeyColumn
	@OneToMany(cascade = CascadeType.ALL)
	private Map<Integer, HibernateAttribute<?>> attributes;

	public HibernateDocument(STDocument parent, Oid oid) {
		super(parent, oid);
		this.db_id = UUID.randomUUID().toString();

		documents = new HashMap<>();
		attributes = new HashMap<>();
	}

//	protected HibernateDocument() {
//		// JPA CONSTRUCTOR
//	}

}
