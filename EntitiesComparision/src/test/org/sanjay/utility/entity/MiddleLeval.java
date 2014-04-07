/*
 * Copyright (C) 2014, 2015 Sanjay Madnani
 *
 * This file is free to use: you can redistribute it and/or modify it under the terms of the 
 * GNU General Public License as published by the Free Software Foundation, subject to the following conditions:
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
 * Middle level hierarchy entity.
 * 
 * @author SANJAY
 * @see DownLeval
 */
public class MiddleLeval {

	private long id;
	private String address;
	private Set<DownLeval> downLevelSet;

	/**
	 * Constructor to initialize fields.
	 * 
	 * @param id
	 * @param address
	 * @param downLevelSet
	 */
	public MiddleLeval(long id, String address, Set<DownLeval> downLevelSet) {
		super();
		this.id = id;
		this.address = address;
		this.downLevelSet = downLevelSet;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MiddleLeval [id=" + id + ", address=" + address + ", downLevelSet=" + downLevelSet + "]";
	}
}
