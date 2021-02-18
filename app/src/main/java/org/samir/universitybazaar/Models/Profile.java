package org.samir.universitybazaar.Models;

public class Profile {
    private int id;
    private String memberId;
    private String email;
    private String fullName;
    private String gender;
    private String address;
    private String phone;
    private String dob;
    private String avatar;

    public Profile( String memberId, String email, String fullName, String gender, String address, String phone, String dob, String avatar) {
        this.memberId = memberId;
        this.email = email;
        this.fullName = fullName;
        this.gender = gender;
        this.address = address;
        this.phone = phone;
        this.dob = dob;
        this.avatar = avatar;
    }
    public Profile(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address= address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", memberId='" + memberId + '\'' +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", gender='" + gender + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", dob='" + dob + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
