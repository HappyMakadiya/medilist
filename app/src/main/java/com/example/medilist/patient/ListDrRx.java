package com.example.medilist.patient;

class ListDrRx {
    String DrName,Month,Day,Year;
    String RxURI;

    public ListDrRx() {
    }

    public ListDrRx(String DrName,  String Day, String Month,String Year) {
        this.DrName = DrName;
        this.Month = Month;
        this.Day = Day;
        this.Year = Year;
    }

    public ListDrRx(String DrName, String Day, String Month, String Year, String RxURI) {
        this.DrName = DrName;
        this.Month = Month;
        this.Day = Day;
        this.Year = Year;
        this.RxURI = RxURI;
    }

    public String getDrName() {
        return DrName;
    }

    public void setDrName(String DrName) {
        this.DrName = DrName;
    }

    public String getMonth() {
        return Month;
    }

    public void setMonth(String Month) {
        this.Month = Month;
    }

    public String getDay() {
        return Day;
    }

    public void setDay(String Day) {
        this.Day = Day;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String Year) {
        this.Year = Year;
    }

    public String getRxURI() {
        return RxURI;
    }

    public void setRxURI(String RxURI) {
        this.RxURI = RxURI;
    }
}
