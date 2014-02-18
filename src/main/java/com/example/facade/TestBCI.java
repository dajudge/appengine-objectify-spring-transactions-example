package com.example.facade;

public interface TestBCI {
    public void setFailOnCreate(boolean fail);

    public void createCar(String uuid);

    public class LolCatsException extends RuntimeException {
    }
}
