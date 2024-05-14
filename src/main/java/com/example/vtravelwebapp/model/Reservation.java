package com.example.vtravelwebapp.model;

import java.math.BigDecimal;
import java.util.Date;

public class Reservation {
    private Long customerId;
    private Long voyageId;
    private Date bookingDate;
    private BigDecimal totalPrice;
    private String status;

    public Reservation() {
    }

    public Reservation(Long customerId, Long voyageId, Date bookingDate, BigDecimal totalPrice, String status) {
        this.customerId = customerId;
        this.voyageId = voyageId;
        this.bookingDate = bookingDate;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getVoyageId() {
        return voyageId;
    }

    public void setVoyageId(Long voyageId) {
        this.voyageId = voyageId;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
