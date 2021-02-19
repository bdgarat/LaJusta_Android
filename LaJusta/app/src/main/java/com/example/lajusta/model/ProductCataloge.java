package com.example.lajusta.model;

public class ProductCataloge {
    public int id;
    public int price;
    public Product product;
    public int quantity;

    public ProductCataloge(int id, int price, Product product, int quantity) {
        this.id = id;
        this.price = price;
        this.product = product;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
