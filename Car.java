package model;

import java.io.Serializable;

public class Car implements Serializable {
    private String id;
    private String make;
    private String model;
    private int year;
    private boolean isAvailable;

    public Car(String id, String make, String model, int year, boolean isAvailable) {
        this.id = id;
        this.make = make;
        this.model = model;
        this.year = year;
        this.isAvailable = isAvailable;
    }

    // Getters and Setters
    public String getId() { return id; }
    public String getMake() { return make; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    public boolean isAvailable() { return isAvailable; }

    public void setMake(String make) { this.make = make; }
    public void setModel(String model) { this.model = model; }
    public void setYear(int year) { this.year = year; }
    public void setAvailable(boolean available) { isAvailable = available; }

    @Override
    public String toString() {
        return id + " | " + make + " " + model + " (" + year + ") - " + (isAvailable ? "Available" : "Rented");
    }
}
