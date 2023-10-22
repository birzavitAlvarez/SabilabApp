package com.demo.sabilabapp.Compras;

public class Pagination {
    private int currentPage;
    private int totalItems;
    private int totalPages;

    public Pagination() {
    }

    public Pagination(int currentPage,int totalItems,int totalPages) {
        this.currentPage = currentPage;
        this.totalItems=totalItems;
        this.totalPages=totalPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
