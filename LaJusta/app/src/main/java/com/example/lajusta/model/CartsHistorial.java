package com.example.lajusta.model;

import java.util.ArrayList;
import java.util.List;

public class CartsHistorial {
    public int totalElements;
    public List<CartComplete> page;

    public CartsHistorial(){

    }

    public CartsHistorial(int totalElements, ArrayList<CartComplete> page) {
        this.totalElements = totalElements;
        this.page = page;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public List<CartComplete> getPage() {
        return page;
    }

    public void setPage(List<CartComplete> page) {
        this.page = page;
    }
}
