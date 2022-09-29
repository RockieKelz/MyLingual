package com.example.mylingual.data;

public class UserAccount {

    // Properties
    public String id;
    public String userName;
    public String primaryLanguage;
    public String secondaryLanguage;

    // Constructors
    public UserAccount(){}

    public UserAccount(String _userName){
        userName = _userName;
        SetPrimaryLanguage("eng");
        SetSecondaryLanguage("span");
    }

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