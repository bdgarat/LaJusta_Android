package com.example.lajusta.model;

public class CartComplete {
    public boolean canceled;
    public CartProduct[] cartProducts;
    public String deletedAt;
    public General general;
    public int id;
    public AvailableNode nodeDate;
    public String observation;
    public String posibleDeliveryDate;
    public String saleDate;
    public double total;
    public User user;

    public CartComplete() {
    }


    public CartProduct[] getCartProducts() {
        return cartProducts;
    }

    public void setCartProducts(CartProduct[] cartProducts) {
        this.cartProducts = cartProducts;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }


    public General getGeneral() {
        return general;
    }

    public void setGeneral(General general) {
        this.general = general;
    }

    public AvailableNode getNodeDate() {
        return nodeDate;
    }

    public void setNodeDate(AvailableNode nodeDate) {
        this.nodeDate = nodeDate;
    }

    public String getPosibleDeliveryDate() {
        return posibleDeliveryDate;
    }

    public void setPosibleDeliveryDate(String posibleDeliveryDate) {
        this.posibleDeliveryDate = posibleDeliveryDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(String saleDate) {
        this.saleDate = saleDate;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }
}
