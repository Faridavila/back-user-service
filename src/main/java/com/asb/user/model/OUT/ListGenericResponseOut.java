package com.asb.user.model.OUT;

import java.util.List;


public class ListGenericResponseOut extends GenericResponseOut {

    private List<Object> data;
    private long currentPage;
    private long totalItems;
    private long totalPages;

    public ListGenericResponseOut() {
    }

    public ListGenericResponseOut(List<Object> data, long currentPage, long totalItems, long totalPages) {
        this.data = data;
        this.currentPage = currentPage;
        this.totalItems = totalItems;
        this.totalPages = totalPages;
    }

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }

    public long getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(long currentPage) {
        this.currentPage = currentPage;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(long totalItems) {
        this.totalItems = totalItems;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

}
