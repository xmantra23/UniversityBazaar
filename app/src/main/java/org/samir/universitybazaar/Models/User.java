package org.samir.universitybazaar.Models;


// A basic class to hold all the information about a particular user. Matches one to one with the "users" table in the database.
public class User {
    private int id;
    private String memberId;
    private String email;
    private String password;
    private String firstSecurityQuestion;
    private String secondSecurityQuestion;
    private String thirdSecurityQuestion;

    public User(int id, String memberId, String email, String password, String firstSecurityQuestion, String secondSecurityQuestion, String thirdSecurityQuestion) {
        this.id = id;
        this.memberId = memberId;
        this.email = email;
        this.password = password;
        this.firstSecurityQuestion = firstSecurityQuestion;
        this.secondSecurityQuestion = secondSecurityQuestion;
        this.thirdSecurityQuestion = thirdSecurityQuestion;
    }
    public User(){

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstSecurityQuestion() {
        return firstSecurityQuestion;
    }

    public void setFirstSecurityQuestion(String firstSecurityQuestion) {
        this.firstSecurityQuestion = firstSecurityQuestion;
    }

    public String getSecondSecurityQuestion() {
        return secondSecurityQuestion;
    }

    public void setSecondSecurityQuestion(String secondSecurityQuestion) {
        this.secondSecurityQuestion = secondSecurityQuestion;
    }

    public String getThirdSecurityQuestion() {
        return thirdSecurityQuestion;
    }

    public void setThirdSecurityQuestion(String thirdSecurityQuestion) {
        this.thirdSecurityQuestion = thirdSecurityQuestion;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", memberId='" + memberId + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstSecurityQuestion='" + firstSecurityQuestion + '\'' +
                ", secondSecurityQuestion='" + secondSecurityQuestion + '\'' +
                ", thirdSecurityQuestion='" + thirdSecurityQuestion + '\'' +
                '}';
    }
}
