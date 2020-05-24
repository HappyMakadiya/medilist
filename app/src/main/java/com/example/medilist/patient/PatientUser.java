package com.example.medilist.patient;

public class PatientUser {
    private String Name,dob,PhNo,Email,Gender,ID,ProfilePhoto;

    public PatientUser() {
    }

    public PatientUser(String Name, String Email, String Gender, String PhNo, String dob,String ID,String ProfilePhoto) {
        this.Name = Name;
        this.dob = dob;
        this.PhNo = PhNo;
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

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPhNo() {
        return PhNo;
    }

    public void setPhNo(String phNo) {
        PhNo = phNo;
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
