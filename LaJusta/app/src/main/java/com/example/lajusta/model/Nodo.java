package com.example.lajusta.model;

public class Nodo {
    public Address address;
    public String deleteAt;
    public String description;
    public boolean hasFridge;
    public int id;
    public Image image;
    public String name;
    public String phone;

    public Nodo(Address address, String deleteAt, String description, boolean hasFridge, int id, Image image, String name, String phone) {
        this.address = address;
        this.deleteAt = deleteAt;
        this.description = description;
        this.hasFridge = hasFridge;
        this.id = id;
        this.image = image;
        this.name = name;
        this.phone = phone;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getDeleteAt() {
        return deleteAt;
    }

    public void setDeleteAt(String deleteAt) {
        this.deleteAt = deleteAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isHasFridge() {
        return hasFridge;
    }

    public void setHasFridge(boolean hasFridge) {
        this.hasFridge = hasFridge;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
