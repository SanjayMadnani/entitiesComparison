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
package org.sanjay.utility;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sanjay.utility.ComparisonHelper;
import org.sanjay.utility.entity.DownLeval;
import org.sanjay.utility.entity.MiddleLeval;
import org.sanjay.utility.entity.TopLeval;

/**
 * Test class for ComparisonHelper.
 * 
 * @author SANJAY
 * @see ComparisonHelper
 * @see TopLeval
 * @see MiddleLeval
 * @see DownLeval
 */
public class ComparisonHelperTest {
	private TopLeval oldEntity;
	private TopLeval newEntity;

	/**
	 * Initialize entities in the same way as it happens in real time scenario.
	 * 
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		Set<DownLeval> bottomLevelSet = new HashSet<DownLeval>();
		bottomLevelSet.add(new DownLeval(31, "Email_31", "Mobile_31"));
		bottomLevelSet.add(new DownLeval(32, "Email_32", "Mobile_32"));
		Set<MiddleLeval> middleLevelSet = new HashSet<MiddleLeval>();
		middleLevelSet.add(new MiddleLeval(21, "Kundalahalli", bottomLevelSet));
		oldEntity = new TopLeval(11, "Sanjay", "Madnani", middleLevelSet);

		bottomLevelSet = new HashSet<DownLeval>();
		bottomLevelSet.add(new DownLeval(0, "Email_311", "Mobile_311"));
		bottomLevelSet.add(new DownLeval(32, "Email_32", "Mobile_322"));
		middleLevelSet = new HashSet<MiddleLeval>();
		middleLevelSet.add(new MiddleLeval(21, "Market", bottomLevelSet));
		newEntity = new TopLeval(11, "XYZ", "Madnani", middleLevelSet);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		oldEntity = null;
		newEntity = null;
	}

	/**
	 * Test method for {@link org.sanjay.utility.ComparisonHelper#modifiedValues(java.lang.Object, java.lang.Object)}.
	 * 
	 * @throws java.lang.Exception
	 */
	@Test
	public void testModifiedValues() throws Exception {
		ComparisonHelper helper = new ComparisonHelper();
		Map<String, Object> map = helper.modifiedValues(oldEntity, newEntity);
		assertNotNull(map);
		for (Map.Entry<String, Object> entrySet : map.entrySet()) {
			System.out.println("Key: " + entrySet.getKey() + ", Value: " + entrySet.getValue());
		}
		assertEquals(6, map.size());
	}

}
