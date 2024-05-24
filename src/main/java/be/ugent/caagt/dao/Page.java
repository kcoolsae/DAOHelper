/*
 * Page.java
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Copyright © 2016-2024 Kris Coolsaet (Universiteit Gent)
 *
 * This software is distributed under the MIT License - see files LICENSE and AUTHORS
 * in the top level project directory.
 */

package be.ugent.caagt.dao;

import java.util.List;

/**
 * Represents a page of a data set of objects of type T. Apart from containing a collection
 * containing the data, it also keeps track of the page size, and of the full size of the original
 * data set it is a page from.
 * <p>Used for pagination: the full size is the size of the data set of which only
 * one page is shown.</p>
 */
public class Page<T>  {

    private final List<T> list;

    private final int pageSize;

    private final int fullSize;

     public Page(List<T> list, int pageSize, int fullSize) {
        this.list = list;
        this.fullSize = fullSize;
        this.pageSize = pageSize;

    }

    public int getFullSize() {
        return fullSize;
    }

    public int getPageSize() {
        return pageSize;
    }

    /**
     * Number of pages in the original data set. Computed from the full size and the page size.
     */
    public int getNrOfPages() {
        return (fullSize + pageSize - 1) / pageSize;
    }

    /**
     * The list containing the data.
     */
    public List<T> getList() {
        return list;
    }
}
