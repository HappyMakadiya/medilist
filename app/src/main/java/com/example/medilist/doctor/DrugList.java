package com.example.medilist.doctor;

public class DrugList {
    private String DiseaceName,DrugName,DrugType,DrugQuantity,DrugDirec,DrugFreq;

    public DrugList() {
    }

    public DrugList(String diseaceName, String drugName, String drugType, String drugQuantity, String drugDirec, String drugFreq) {
        DiseaceName = diseaceName;
        DrugName = drugName;
        DrugType = drugType;
        DrugQuantity = drugQuantity;
        DrugDirec = drugDirec;
        DrugFreq = drugFreq;
    }

    public String getDiseaceName() {
        return DiseaceName;
    }

    public void setDiseaceName(String diseaceName) {
        DiseaceName = diseaceName;
    }

    public String getDrugName() {
        return DrugName;
    }

    public void setDrugName(String drugName) {
        DrugName = drugName;
    }

    public String getDrugType() {
        return DrugType;
    }

    public void setDrugType(String drugType) {
        DrugType = drugType;
    }

    public String getDrugQuantity() {
        return DrugQuantity;
    }

    public void setDrugQuantity(String drugQuantity) {
        DrugQuantity = drugQuantity;
    }

    public String getDrugDirec() {
        return DrugDirec;
    }

    public void setDrugDirec(String drugDirec) {
        DrugDirec = drugDirec;
    }

    public String getDrugFreq() {
        return DrugFreq;
    }

    public void setDrugFreq(String drugFreq) {
        DrugFreq = drugFreq;
    }
}
