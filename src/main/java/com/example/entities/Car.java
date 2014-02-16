package com.example.entities;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * Created by romanl on 03.02.14.
 */
@Entity
public class Car {

    @Id private String series;
    private String name;

    public Car (){}

    public Car(String series, String name) {
        this.series = series;
        this.name = name;
    }

    public String getSeries() {
        return series;
    }

    public String getName() {
        return name;
    }
}
