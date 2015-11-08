package com.example.pearlsantos.project;

/**
 * Created by elysi_000 on 10/24/2015.
 */
public class Receipt {
    private String busNum, plateNum, from, to, depTime, seatsAvailable, cost;

    public Receipt(String busNum, String plateNum, String from,
                       String to, String depTime, String seatsAvailable, String cost){
        this.busNum = busNum;
        this.plateNum = plateNum;
        this.from = from;
        this.to = to;
        this.depTime = depTime;
        this.seatsAvailable = seatsAvailable;
        this.cost = cost;
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

    public String getSeatsAvailable() {
        return seatsAvailable;
    }

    public void setSeatsAvailable(String seatsAvailable) {
        this.seatsAvailable = seatsAvailable;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

}
