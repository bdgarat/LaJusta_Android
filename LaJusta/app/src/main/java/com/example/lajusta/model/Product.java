package com.example.lajusta.model;

import java.util.ArrayList;

public class Product{
   private Long id;
   private String title;
   private String description;
   private ArrayList<Category> categories;
   private double price;
   private Image images[];

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
