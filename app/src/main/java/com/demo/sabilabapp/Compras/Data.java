package com.demo.sabilabapp.Compras;


import java.util.List;

public class Data {
    private List<Compras> results;
    private Pagination pagination;

    public Data(List<Compras> results,Pagination pagination) {
        this.results = results;
        this.pagination=pagination;
    }

    public List<Compras> getResults() {
        return results;
    }

    public void setResults(List<Compras> results) {
        this.results = results;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
}
