package com.example.myapp;

public class Contact_Model {

    private String contactName, contactNumber;

    public Contact_Model(String contactName, String contactNumber) {
        this.contactName = contactName;
        this.contactNumber = contactNumber;

    }


    public String getContactName() {
        return contactName;
    }


    public String getContactNumber() {
        return contactNumber;
    }


}