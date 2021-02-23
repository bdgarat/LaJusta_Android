package com.example.lajusta.model;

public class CartProduct{
    private Cart cart;
    private boolean isCanceled;
    private Product producto;
    private int quantity;

    public CartProduct(){
        isCanceled=false;
    }

    public CartProduct(Cart cart,Product product){
        this.cart=cart;
        this.quantity=0;
        this.producto=product;
        this.isCanceled=false;
    }

    public void agregar(){
        quantity+=1;
    }

    public void restar(){
        if(quantity>0){
            quantity=quantity-1;
        }
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public boolean isCanceled() {
        return isCanceled;
    }

    public void setCanceled(boolean canceled) {
        isCanceled = canceled;
    }

    public Product getProduct() {
        return producto;
    }

    public void setProduct(Product product) {
        this.producto = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


}
