package com.example.elysi_000.ubus;

public class BusSchedule {
    private String busNum, plateNum, from, to, depTime;
    private int seatsAvailable;
    private double cost;

    public BusSchedule(String busNum, String plateNum, String from,
                       String to, String depTime, int seatsAvailable, double cost){
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
    public int getSeatsAvailable() {
        return seatsAvailable;
    }
    public void setSeatsAvailable(int seatsAvailable) {
        this.seatsAvailable = seatsAvailable;
    }
    public double getCost() {
        return cost;
    }
    public void setCost(double cost) {
        this.cost = cost;
    }


}
