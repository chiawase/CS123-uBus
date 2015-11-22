package com.example.pearlsantos.project;

/**
 * Created by elysi_000 on 10/24/2015.
 */
public class Receipt {
    private String busNum;
    private String plateNum;
    private String from;
    private String to;
    private String depTime;
    private String arrival;
    private String seatsPurchased;
    private String totalCost;

    public Receipt(String busNum, String plateNum, String from,
                       String to, String depTime, String arrival, String seatsPurchased, String totalCost){
        this.busNum = busNum;
        this.plateNum = plateNum;
        this.from = from;
        this.to = to;
        this.depTime = depTime;
        this.arrival = arrival;
        this.seatsPurchased = seatsPurchased;
        this.totalCost = totalCost;
    }

    public String getBusNum() {
        return busNum;
    }

    public void setBusNum(String busNum) {
        this.busNum = busNum;
    }

    public String getPlateNum() {
        return plateNum;
    }

    public void setPlateNum(String plateNum) {
        this.plateNum = plateNum;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getDepTime() {
        return depTime;
    }

    public void setDepTime(String depTime) {
        this.depTime = depTime;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public String getSeatsPurchased() {
        return seatsPurchased;
    }

    public void setSeatsPurchased(String seatsPurchased) {
        this.seatsPurchased = seatsPurchased;
    }

    public String getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(String totalCost) {
        this.totalCost = totalCost;
    }

}
