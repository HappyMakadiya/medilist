package com.example.medilist.pharmacist;

public class PharmacistUser {
    String Name,Email,Gender,StrName,StrAdd,PhNo,ID;

    public PharmacistUser() {
    }

    public PharmacistUser(String name, String email, String gender, String strName, String strAdd, String phNo , String id) {
        Name = name;
        Email = email;
        Gender = gender;
        StrName = strName;
        StrAdd = strAdd;
        PhNo = phNo;
        ID = id;
    }


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getStrName() {
        return StrName;
    }

    public void setStrName(String strName) {
        StrName = strName;
    }

    public String getStrAdd() {
        return StrAdd;
    }

    public void setStrAdd(String strAdd) {
        StrAdd = strAdd;
    }

    public String getPhNo() {
        return PhNo;
    }

    public void setPhNo(String phNo) {
        PhNo = phNo;
    }
}
