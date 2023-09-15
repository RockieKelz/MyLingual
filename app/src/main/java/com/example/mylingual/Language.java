package com.example.mylingual;

public class Language {
    //variable to store Language's tags
    private String languageTag;
    //variable to store Language's name
    private String language;

    //empty constructor
    public Language() {}

    //constructor
    public Language(String languageTag, String language) {
        this.languageTag = languageTag;
        this.language = language;
    }

    //getter and setters
    public String getLanguageTag(){
        return languageTag;
    }
    public String getLanguage(){
        return language;
    }
    public void setLanguageTag(String languageTag) {
        this.languageTag = languageTag;
    }
    public void setLanguage(String language) {
        this.language = language;
    }
}
