package com.example.cafe;
public class Customer {
    private final String id;
    private String number;


    private static int counter = 1;

    public Customer(String number) {

        this.id = "C" + (counter++);
        this.number = number;
    }

    public Customer(String id, String number) {
        this.id = id;
        this.number = number;
    }


    public String getId() {
        return id;
    }

    public String getNumber() {
        return this.number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}

