package com.example.facade;

/**
 * Created by romanl on 03.02.14.
 */

public interface TestBCI {
    public void setFailOnCreate(boolean fail);

    public void createCar(String uuid);

    public class LolcatsException extends RuntimeException {
    }
}
