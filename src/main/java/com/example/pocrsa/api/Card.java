package com.example.pocrsa.api;

public record Card(String pan, String expiryMonth, String expiryYear) {

    private static final String MASK = "*";

    @Override
    public String toString() {
        return "Card{" +
                "pan='" + Mask.pan(pan, MASK) + '\'' +
                ", expiryMonth='" + Mask.all(expiryMonth, MASK) + '\'' +
                ", expiryYear='" + Mask.all(expiryYear, MASK) + '\'' +
                '}';
    }
}
