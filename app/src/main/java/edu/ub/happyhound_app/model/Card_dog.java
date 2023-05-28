package edu.ub.happyhound_app.model;

public class Card_dog {
    private String dog_url;
    private String dog_name;
    private String dog_age;

    public Card_dog(String name, String uri) {
        dog_name = name;
        dog_url = uri;
    }

    public String getDog_url() {
        return dog_url;
    }

    public void setDog_url(String dog_url) {
        this.dog_url = dog_url;
    }

    public String getDog_name() {
        return dog_name;
    }

    public void setDog_name(String dog_name) {
        this.dog_name = dog_name;
    }

    public String getDog_age() {
        return dog_age;
    }

    public void setDog_age(String dog_age) {
        this.dog_age = dog_age;
    }
}
