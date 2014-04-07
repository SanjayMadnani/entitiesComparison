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
package org.sanjay.utility;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Compare two entity and returns modified values and newly added values in a map.
 * 
 * @Limitations: Each entity must have id field of type long. New Entity must have 0 as value of id. In case of modification on existing entity:
 *               previous Entity and Modified(new) Entity must have same id value.
 * @author SANJAY
 */
public class ComparisonHelper {
	private Map<String, Object> modification = new HashMap<String, Object>();
	private int k;
	private List<Class<?>> checkedClass = new ArrayList<Class<?>>();
	private List<Field> fieldList = null;

	/**
	 * If value is updated then it calls to updatedValues for getting modification of values else if value is new added then it calls to
	 * addedDeletedValues for getting new values.
	 * 
	 * @param previousObj
	 * @param newObject
	 * @return Map with name and value of entities newly added fields and updated fields.
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public <T> Map<String, Object> modifiedValues(T previousObj, T newObject) throws NoSuchFieldException,
			SecurityException, IllegalArgumentException, IllegalAccessException {

		Field idFiled = supperClassOperation(newObject);
		if (idFiled == null)
			idFiled = previousObj.getClass().getDeclaredField("id");
		idFiled.setAccessible(true);
		if (idFiled.getLong(previousObj) == idFiled.getLong(newObject)) {
			parentFieldOperations(previousObj, newObject);
			updatedValues(previousObj, newObject);
		} else if (idFiled.getLong(newObject) == 0) {
			addedDeletedValues(newObject);
			parentOperationForNewEntity(newObject);
		}
		idFiled.setAccessible(false);
		return modification;
	}

	/**
	 * For parent class fields comparisons.
	 * 
	 * @param previousObj
	 * @param newObject
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	private Field supperClassOperation(Object newObject) throws IllegalArgumentException, IllegalAccessException,
			NoSuchFieldException, SecurityException {
		fieldList = new ArrayList<Field>();
		Field fld = null;
		if (!checkedClass.contains(newObject.getClass())) {
			Class<? extends Object> parent = newObject.getClass();
			while (parent != null && parent != Object.class) {
				parent = parent.getSuperclass();
				Field[] fields = parent.getDeclaredFields();
				for (Field field : fields) {
					fieldList.add(field);
					field.setAccessible(true);
					if (field.getName().equals("id")) {
						fld = field;
					}
					field.setAccessible(false);
				}
			}
			checkedClass.add(newObject.getClass());
		}
		return fld;
	}

	/**
	 * For changing parent classes fields accessibility in case of fields values modified.
	 * 
	 * @param previousObj
	 * @param newObject
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	private void parentFieldOperations(Object previousObj, Object newObject) throws IllegalArgumentException,
			IllegalAccessException, NoSuchFieldException, SecurityException {
		if (fieldList != null && !fieldList.isEmpty()) {
			for (Field field : fieldList) {
				field.setAccessible(true);
				compareDifference(field.get(previousObj), field.get(newObject), field);
				field.setAccessible(false);
			}
		}
	}

	/**
	 * For changing parent classes fields accessibility in case of values newly added.
	 * 
	 * @param newObject
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	private void parentOperationForNewEntity(Object newObject) throws IllegalArgumentException, IllegalAccessException,
			NoSuchFieldException, SecurityException {
		for (Field field : fieldList) {
			field.setAccessible(true);
			putNewValues(newObject, field);
			field.setAccessible(false);
		}
	}

	/**
	 * If value is modified then it gives access to fields till field operations and sends per field existed and modified object for comparison.
	 * 
	 * @param previousObj
	 * @param newObject
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws Exception
	 */
	private void updatedValues(Object previousObj, Object newObject) throws IllegalArgumentException,
			IllegalAccessException, NoSuchFieldException, SecurityException {
		Field[] fields = newObject.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			compareDifference(field.get(previousObj), field.get(newObject), field);
			field.setAccessible(false);
		}
	}

	/**
	 * If existed and new filed is not same then it adds values to map. else if field is instance of Collection(List, Set and so on) then it pass the
	 * value to listSetOperation for further operation. else if field is instance of a another class then it calls updatedValues (caller) to operate
	 * on its fields.
	 * 
	 * @param previous
	 * @param latest
	 * @param field
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws Exception
	 */
	private void compareDifference(Object previous, Object latest, Field field) throws IllegalArgumentException,
			IllegalAccessException, NoSuchFieldException, SecurityException {
		if (latest != null && !latest.equals(previous)) {
			if (field.getType().isPrimitive() || latest instanceof Number || latest instanceof String) {
				modification.put(field.getName() + "_" + k++, latest);
			} else if (latest instanceof Collection<?>) {
				listSetOperation(previous, latest, field);
			} else {
				updatedValues(previous, latest);
			}
		}
	}

	/**
	 * If Collection holds new object then it calls addedDeletedValues so that new all values can be put on map to show new object fields. else if
	 * collection object is just modified then it calls updatedValues to do complete procedure again for getting modified fields.
	 * 
	 * @param previous
	 * @param latest
	 * @param field
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws Exception
	 */
	private void listSetOperation(Object previous, Object latest, Field field) throws NoSuchFieldException,
			SecurityException, IllegalArgumentException, IllegalAccessException {
		Collection<?> preCollection = ((Collection<?>) previous);
		Iterator<?> newIterator = ((Collection<?>) latest).iterator();
		while (newIterator.hasNext()) {
			Object newObj = newIterator.next();
			Field idFiled = supperClassOperation(latest);
			if (idFiled == null)
				idFiled = newObj.getClass().getDeclaredField("id");
			if (idFiled == null)
				return;
			parentFieldOperations(previous, latest);
			idFiled.setAccessible(true);
			if (idFiled.getLong(newObj) == 0) {
				addedDeletedValues(newObj);
			} else {
				for (Object preOb : preCollection) {
					if (idFiled.getLong(preOb) == idFiled.getLong(newObj)) {
						updatedValues(preOb, newObj);
					}
				}
			}
			field.setAccessible(false);
		}
	}

	/**
	 * If new Value is added then it gives access to class fields till putNewValues method work and then close access permission.
	 * 
	 * @param newObject
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws Exception
	 */
	private void addedDeletedValues(Object newObject) throws IllegalArgumentException, IllegalAccessException,
			NoSuchFieldException, SecurityException {
		Field[] fields = newObject.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			putNewValues(field.get(newObject), field);
			field.setAccessible(false);
		}
	}

	/**
	 * Adds field value to map. If field is instance of Collection(List, Set and so on) then it pass the value to putCollectionValue Iteration and
	 * addition purpose. else if field is instance of a another class then it calls addedDeletedValues (caller) to operate on its fields.
	 * 
	 * @param latest
	 * @param field
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws Exception
	 */
	private void putNewValues(Object latest, Field field) throws IllegalArgumentException, IllegalAccessException,
			NoSuchFieldException, SecurityException {
		if (latest != null) {
			if (field.getType().isPrimitive() || latest instanceof Number || latest instanceof String) {
				modification.put(field.getName() + "_New_" + k++, latest);
			} else if (latest instanceof Collection<?>) {
				putCollectionValue(latest, field);
			} else {
				addedDeletedValues(latest);
			}
		}
	}

	/**
	 * Calls addedDeletedValues so that new all values can be kept on map to show new object fields.
	 * 
	 * @param latest
	 * @param field
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws Exception
	 */
	private void putCollectionValue(Object latest, Field field) throws NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException {
		Iterator<?> newIterator = ((Collection<?>) latest).iterator();
		while (newIterator.hasNext()) {
			Object newObj = newIterator.next();

			Field idFiled = supperClassOperation(latest);
			if (idFiled == null)
				idFiled = newObj.getClass().getDeclaredField("id");
			if (idFiled == null)
				return;
			idFiled.setAccessible(true);
			if (idFiled.getLong(newObj) == 0) {
				addedDeletedValues(newObj);
				parentOperationForNewEntity(latest);
			}
			field.setAccessible(false);
		}
	}
}
