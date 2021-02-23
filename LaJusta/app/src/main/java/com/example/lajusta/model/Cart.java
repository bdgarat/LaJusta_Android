package com.example.lajusta.model;

import java.util.ArrayList;

public class Cart {
    public String approvedAt;
    public ArrayList<CartProduct> cartProducts;
    public String deletedAt;
    public String delivered;
    public General general;
    public AvailableNode nodeDate;
    public String posibleDeliveryDate;
    public String saleDate;
    public double total;
    public User user;

    public Cart() {
        cartProducts= new ArrayList<>();
    }

    public void convertirCartParaGuardar(ArrayList<ProductoEnCarrito> carrito){
        Cart cart = new Cart();
        for(ProductoEnCarrito p: carrito){
            cart.agregarProducto(p);
        }
    }

    public void agregarProducto(ProductoEnCarrito p){
        CartProduct cartProduct = new CartProduct();
        cartProduct.setProduct(p.getProducto());
        cartProduct.setQuantity(p.getCantidad());
        cartProducts.add(cartProduct);
    }

    public double calcularPrecio(){
        double total=0;
        for(CartProduct c: cartProducts){
            total+=(c.getProduct().getPrice()*c.getQuantity());
        }
        return total;
    }

    public String getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(String approvedAt) {
        this.approvedAt = approvedAt;
    }

    public ArrayList<CartProduct> getCartProducts() {
        return cartProducts;
    }

    public void setCartProducts(ArrayList<CartProduct> cartProducts) {
        this.cartProducts = cartProducts;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getDelivered() {
        return delivered;
    }

    public void setDelivered(String delivered) {
        this.delivered = delivered;
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
}
