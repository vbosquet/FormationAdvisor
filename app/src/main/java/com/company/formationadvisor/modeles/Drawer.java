package com.company.formationadvisor.modeles;

public class Drawer {
    private String item;
    private Integer image;

    public Drawer(String item, Integer image) {
        this.item = item;
        this.image = image;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }
}
