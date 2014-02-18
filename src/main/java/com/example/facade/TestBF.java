package com.example.facade;

import com.example.entities.Car;
import org.springframework.stereotype.Component;

import static com.googlecode.objectify.ObjectifyService.ofy;

@Component
public class TestBF implements TestBCI {

    private boolean fail;

    @Override
    public void setFailOnCreate(boolean fail) {
        this.fail = fail;
    }

    @Override
    public void createCar(String series) {
        Car car = createSampleEntity(series);
        ofy().save().entity(car).now();
        if (fail) {
            throw new LolCatsException();
        }
    }

    private Car createSampleEntity(String series) {
        return new Car(series, "TestCar " + series);
    }
}
