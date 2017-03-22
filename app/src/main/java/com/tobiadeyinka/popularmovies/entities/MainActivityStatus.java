package com.tobiadeyinka.popularmovies.entities;

public enum MainActivityStatus {
    POPULAR(1),
    RATING(2),
    FAVORITES(3);

    int value;

    MainActivityStatus(int n){
        this.value = n;
    }

    public int getValue(){
        return value;
    }
}
