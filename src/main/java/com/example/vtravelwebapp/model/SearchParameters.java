package com.example.vtravelwebapp.model;

import java.util.ArrayList;
import java.util.List;

public class SearchParameters {
    private String destination;
    private String dateRange;
    private int numPeople;

    // Constructor, getters, and setters

    public SearchParameters() {
    }

    public SearchParameters(String destination, String dateRange, int numPeople) {
        this.destination = destination;
        this.dateRange = dateRange;
        this.numPeople = numPeople;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDateRange() {
        return dateRange;
    }

    public void setDateRange(String dateRange) {
        this.dateRange = dateRange;
    }

    public int getNumPeople() {
        return numPeople;
    }

    public void setNumPeople(int numPeople) {
        this.numPeople = numPeople;
    }

    @Override
    public String toString() {
        return "SearchParameters{" +
                "destination='" + destination + '\'' +
                ", dateRange='" + dateRange + '\'' +
                ", numPeople=" + numPeople +
                '}';
    }
    public List<String> toStringList() {
        List<String> stringList = new ArrayList<>();
        stringList.add(destination);
        stringList.add(dateRange);
        stringList.add(String.valueOf(numPeople));
        return stringList;
    }
}
