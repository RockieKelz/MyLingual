package com.example.mylingual.data.room_db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "Translations")
public class RoomEntity {
    @PrimaryKey (autoGenerate = true)
    private int id;
    @ColumnInfo (name = "DATA_TYPE")
    public  String DATA_TYPE;
    @ColumnInfo (name = "TIMESTAMP")
    public  String TIMESTAMP;
    @ColumnInfo (name = "PRIMARY_LANG")
    public  String PRIMARY_LANG;
    @ColumnInfo (name = "ORIGINAL_TEXT")
    public  String ORIGINAL_TEXT;
    @ColumnInfo (name = "SECONDARY_LANG")
    public  String SECONDARY_LANG;
    @ColumnInfo (name = "TRANSLATED_TEXT")
    public  String TRANSLATED_TEXT;
    @ColumnInfo (name = "BOOKMARKED")
    public  Boolean BOOKMARKED;

    public RoomEntity(String DATA_TYPE,String TIMESTAMP,String PRIMARY_LANG, String ORIGINAL_TEXT, String SECONDARY_LANG, String TRANSLATED_TEXT, Boolean BOOKMARKED) {
        this.DATA_TYPE = DATA_TYPE;
        this.TIMESTAMP = TIMESTAMP;
        this.PRIMARY_LANG = PRIMARY_LANG;
        this.ORIGINAL_TEXT = ORIGINAL_TEXT;
        this.SECONDARY_LANG = SECONDARY_LANG;
        this.TRANSLATED_TEXT = TRANSLATED_TEXT;
        this.BOOKMARKED = BOOKMARKED;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPrimary() {
        return PRIMARY_LANG;
    }

    public void setPRIMARY_LANG(String PRIMARY_LANG) {
        this.PRIMARY_LANG = PRIMARY_LANG;
    }

    public String getSecondary() {
        return SECONDARY_LANG;
    }

    public void setSECONDARY_LANG(String SECONDARY_LANG) { this.SECONDARY_LANG = SECONDARY_LANG; }

    public String getOriginal() { return ORIGINAL_TEXT; }

    public void setORIGINAL_TEXT(String ORIGINAL_TEXT) {
        this.ORIGINAL_TEXT = ORIGINAL_TEXT;
    }

    public String getTranslated() { return TRANSLATED_TEXT; }

    public void setTRANSLATED_TEXT(String TRANSLATED_TEXT) { this.TRANSLATED_TEXT = TRANSLATED_TEXT; }

    public String getType() { return DATA_TYPE; }

    public void setDATA_TYPE(String DATA_TYPE) {
        this.DATA_TYPE = DATA_TYPE;
    }

    public Boolean getBookmarkedStatus() { return BOOKMARKED; }

    public void setBOOKMARKED(Boolean status) {
        this.BOOKMARKED = status;
    }
}

