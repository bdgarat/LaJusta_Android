package com.example.lajusta.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Product implements Serializable{
    public Long id;
    public String title;
    public String description;
    public ArrayList<Category> categories;
    public double price;
    public Image images[];

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }


    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public Image[] getImages() {
        return images;
    }

    public Image getImage(int index) {
        return images[index];
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

}
