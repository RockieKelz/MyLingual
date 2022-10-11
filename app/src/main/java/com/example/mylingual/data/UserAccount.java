package com.example.mylingual.data;

public class UserAccount {

    // Properties
    private String id;
    private String userName;
    private String primaryLanguage;
    private String secondaryLanguage;

    // Constructors
    public UserAccount(){}

    // Accessors
    public String GetId() { return id; }
    public String GetUserName() { return userName; }
    public String GetPrimaryLanguage() { return primaryLanguage; }
    public String GetSecondaryLanguage() { return secondaryLanguage; }


    // Mutators
    public void SetId(String _id) { id = _id; }
    public void SetUserName(String _userName) { this.userName = _userName; }
    public void SetPrimaryLanguage(String _primaryLanguage) { this.primaryLanguage = _primaryLanguage; }
    public void SetSecondaryLanguage(String _secondaryLanguage) { this.secondaryLanguage = _secondaryLanguage; }

}