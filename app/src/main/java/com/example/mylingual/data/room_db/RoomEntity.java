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

    public RoomEntity(String DATA_TYPE,String TIMESTAMP,String PRIMARY_LANG, String ORIGINAL_TEXT, String SECONDARY_LANG, String TRANSLATED_TEXT) {
        this.DATA_TYPE = DATA_TYPE;
        this.TIMESTAMP = TIMESTAMP;
        this.PRIMARY_LANG = PRIMARY_LANG;
        this.ORIGINAL_TEXT = ORIGINAL_TEXT;
        this.SECONDARY_LANG = SECONDARY_LANG;
        this.TRANSLATED_TEXT = TRANSLATED_TEXT;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

