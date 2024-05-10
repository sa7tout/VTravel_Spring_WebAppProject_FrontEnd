package com.example.vtravelwebapp.model;

import java.math.BigDecimal;
import java.util.Date;

public class Travel {
    private Long voyageId;
    private Long destinationId;
    private String destinationName;
    private String destinationImage;
    private Date departureDate;
    private Date returnDate;
    private BigDecimal price;
    private Integer availableSeats;

    // Constructors

    // Getters
    public Long getVoyageId() {
        return voyageId;
    }

    public Long getDestinationId() {
        return destinationId;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public String getDestinationImage() {
        return destinationImage;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public void setDestinationImage(String destinationImage) {
        this.destinationImage = destinationImage;
    }

    // toString() method
    @Override
    public String toString() {
        return "Travel{" +
                "voyageId=" + voyageId +
                ", destinationId=" + destinationId +
                ", destinationName='" + destinationName + '\'' +
                ", destinationImage='" + destinationImage + '\'' +
                ", departureDate=" + departureDate +
                ", returnDate=" + returnDate +
                ", price=" + price +
                ", availableSeats=" + availableSeats +
                '}';
    }

}
