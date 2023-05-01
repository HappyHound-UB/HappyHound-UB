package edu.ub.happyhound_app;

public class Card_dog {
    private String dog_url;
    private String dog_name;

    public Card_dog(String d, String u){
        dog_name = d;
        dog_url = u;
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
}
