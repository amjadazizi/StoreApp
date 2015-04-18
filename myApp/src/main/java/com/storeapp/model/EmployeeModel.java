package com.storeapp.model;

/**
 * Created by Amjad on 12-03-2015.
 */
public class EmployeeModel extends BaseModel {

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private  String password;

    public EmployeeModel() {
    }

    public EmployeeModel(String firstName, String lastName, String email, String phoneNumber, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /*public boolean isValid(){

        boolean valid = true;

        valid = valid ? (Utils.isNullOrEmpty(firstName) ? false: valid) : valid;
        valid = valid ? (Utils.isNullOrEmpty(lastName) ? false: valid) : valid;
        valid = valid ? (Utils.isNullOrEmpty(email) ? false: valid) : valid;
        valid = valid ? (Utils.isNullOrEmpty(phoneNumber) ? false: valid) : valid;
        valid = valid ? (Utils.isNullOrEmpty(password) ? false: valid) : valid;



        return  valid;


    }*/


}
