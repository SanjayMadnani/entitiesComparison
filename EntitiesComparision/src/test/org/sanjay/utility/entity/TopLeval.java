/*
 * Copyright (C) 2014, 2015 Sanjay Madnani
 *
 * This file is free to use: you can redistribute it and/or modify it under the terms of the 
 * GPL General Public License V3 as published by the Free Software Foundation, subject to the following conditions:
 *                                                                                          
 * The above copyright notice should never be changed and should always included wherever this file is used.
 *                                                                                          
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY.  
 * See the GNU General Public License for more details.                                       
 *
 */
package org.sanjay.utility.entity;

import java.util.Set;

/**
 * Top level hierarchy entity.
 * 
 * @author SANJAY
 * @see MiddleLeval
 */
public class TopLeval {

	private long id;
	private String firstName;
	private String lastName;
	private Set<MiddleLeval> MiddleLevalSet;

	/**
	 * Default constructor.
	 */
	public TopLeval() {

	}

	/**
	 * Constructor to initialize fields.
	 * 
	 * @param id
	 * @param firstName
	 * @param lastName
	 * @param middleLevalSet
	 */
	public TopLeval(long id, String firstName, String lastName, Set<MiddleLeval> middleLevalSet) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		MiddleLevalSet = middleLevalSet;
	}

	/**
	 * Overridden toString method to show all values
	 */
	@Override
	public String toString() {
		return "TopLeval [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", MiddleLevalSet="
				+ MiddleLevalSet + "]";
	}
}
