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

/**
 * Bottom level hierarchy entity.
 * 
 * @author SANJAY
 */
public class DownLeval {
	private long id;
	private String mailId;
	private String mobileNo;

	/**
	 * Constructor to initialize fields.
	 * 
	 * @param id
	 * @param mailId
	 * @param mobileNo
	 */
	public DownLeval(long id, String mailId, String mobileNo) {
		super();
		this.id = id;
		this.mailId = mailId;
		this.mobileNo = mobileNo;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DownLeval [id=" + id + ", mailId=" + mailId + ", mobileNo=" + mobileNo + "]";
	}
}
