package com.example.medilist.doctor;

public class DocterUser {
   private String Name,Degree,PhNo,HptName,HptAdd,Email,Gender,ID,ProfilePhoto;

    public DocterUser() {
    }

    public DocterUser(String Name, String Degree, String PhNo, String HptName, String HptAdd, String Email, String Gender,String ID,String ProfilePhoto) {
        this.Name = Name;
        this.Degree = Degree;
        this.PhNo = PhNo;
        this.HptName = HptName;
        this.HptAdd = HptAdd;
        this.Email = Email;
        this.Gender = Gender;
        this.ID = ID;
        this.ProfilePhoto = ProfilePhoto;
    }

    public String getProfilePhoto() {
        return ProfilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        ProfilePhoto = profilePhoto;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDegree() {
        return Degree;
    }

    public void setDegree(String degree) {
        Degree = degree;
    }

    public String getPhNo() {
        return PhNo;
    }

    public void setPhNo(String phNo) {
        PhNo = phNo;
    }

    public String getHptName() {
        return HptName;
    }

    public void setHptName(String hptName) {
        HptName = hptName;
    }

    public String getHptAdd() {
        return HptAdd;
    }

    public void setHptAdd(String hptAdd) {
        HptAdd = hptAdd;
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

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
