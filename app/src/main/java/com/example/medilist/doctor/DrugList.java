package com.example.medilist.doctor;

public class DrugList {
    private String Drug_Name ,Drug_Quantity;

    public DrugList(String drug_Name, String drug_Quantity) {
        Drug_Name = drug_Name;
        Drug_Quantity = drug_Quantity;
    }

    public String getDrug_Name() {
        return Drug_Name;
    }

    public String getDrug_Quantity() {
        return Drug_Quantity;
    }
}
