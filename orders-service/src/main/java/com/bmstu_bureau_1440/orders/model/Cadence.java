package com.bmstu_bureau_1440.orders.model;

public enum Cadence {

    DAILY(1),
    WEEKLY(7);

    private final int intervalDays;

    Cadence(int intervalDays) {
        this.intervalDays = intervalDays;
    }

    public int getIntervalDays() {
        return intervalDays;
    }

}
