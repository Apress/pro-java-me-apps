/*
 * QSort.java
 * 05/06/2010
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.util;

/**
 * <p>
 * This class implements QSort algorithm.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.3
 */
public class QSort {
	/**
	 * <p>
	 * Ascending sort or descending.
	 * </p>
	 */
	private boolean isAscending;
	
	/**
	 * <p>
	 * Default constructor.
	 * </p>
	 */
	public QSort() {
		isAscending = true;
	}
	
	/**
	 * <p>
	 * Set enable the ascending sort or descending.
	 * </p>
	 * @param enabled Enabled or disabled.
	 */
	public void setAscendingSortEnabled(boolean enabled) {
		isAscending = enabled;
	}
	
	/**
	 * <p>
	 * Return if the sort is set up for ascending or descending.
	 * </p>
	 * @return Ascending sort or descending.
	 */
	public boolean isAscendingSort() {
		return isAscending;
	}
	
	/**
	 * <p>
	 * Start sorting a given array of objects.
	 * </p>
	 * @param items Objects to be sorted.
	 * @param s Start index.
	 * @param e End index.
	 */
	public void quicksort(Object[] items, int s, int e) {
	    if (e <= s) {
	    	return;
	    }else {
		    int i = partition(items, s, e);
		    quicksort(items, s, i-1);
		    quicksort(items, i+1, e);
		}
	}

	/**
	 * <p>
	 * Get an element to be sorted from a given object.
	 * </p>
	 * @param ob Object.
	 * @return Object to be sorted.
	 */
	protected Object getElement(Object ob) {
		return ob;
	}

	/**
	 * <p>
	 * Verify if a given object (1st param) is lesser than another object
	 * (2nd param).
	 * </p>
	 * @param i1 Object 1.
	 * @param i2 Object 2.
	 * @return If 1s object is lesser thant 2nd object.
	 */
	protected boolean lesser(Object i1, Object i2) {
		if (i1 != null && i2 == null) {
			return true;
		} else if (i1 == null) {
			return false;
		} else {
			return i1.toString().compareTo(i2.toString()) < 0;
		}
	}

	/**
	 * <p>
	 * Verify if a given object (1st param) is greater than another object
	 * (2nd param).
	 * </p>
	 * @param i1 Object 1.
	 * @param i2 Object 2.
	 * @return If 1s object is greater thant 2nd object.
	 */
	protected boolean greater(Object i1, Object i2) {
		if (i1 != null && i2 == null) {
			return true;
		} else if (i1 == null) {
			return false;
		} else {
			return i1.toString().compareTo(i2.toString()) > 0;
		}
	}

	/**
	 * <p>
	 * Swap two objects from the array according to the given indexes.
	 * </p>
	 * @param its Array of objects.
	 * @param i1 Index of the 1st object.
	 * @param i2 Index of the 2nd object.
	 */
	protected void swap(Object[] its, int i1, int i2) {
		Object auxo = its[i1];
		its[i1] = its[i2];
		its[i2] = auxo;
	}

	/**
	 * <p>
	 * Partition the objects from a given array of objects according to a given
	 * range.
	 * </p>
	 * @param a Array of objects.
	 * @param l Start index of the range.
	 * @param r End index of the range.
	 * @return Start index of the range.
	 */
	private int partition(Object[] a, int l, int r) {
	    int i = l-1;
	    int j = r;
	    Object v = getElement(a[r]);
	    if (isAscending) {
		    for (;;) {
		        while (lesser(getElement(a[++i]), v));
		        while (lesser(v, getElement(a[--j]))) if (j == l) break;
		        if (i >= j) break;
		        swap(a, i, j);
		    }
	    } else { //descending sort.
		    for (;;) {
		        while (greater(getElement(a[++i]), v)) ;
		        while (greater(v, getElement(a[--j]))) if (j == l) break;
		        if (i >= j) break;
		        swap(a, i, j);
		    }
	    }
	    swap(a, i, r);
	    return i;
	}
}