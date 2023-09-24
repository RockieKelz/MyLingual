package com.example.mylingual.data.room_db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RoomDAO {
    // add data to database.
    @Insert
    void insert(RoomEntity e);

    // update the data in database.
    @Update
    void update(RoomEntity e);

    //delete specific row in database.
    @Delete
    void deleteBookmarked(RoomEntity e);

    //looks for specific translation to delete
    @Query("DELETE FROM translations WHERE ORIGINAL_TEXT = :original")
    void deleteTranslation(String original);

    // delete all data from database.
    @Query("DELETE FROM Translations")
    void deleteAll();

    // read all the data
    @Query("SELECT * FROM translations ORDER BY id ASC")
    LiveData<List<RoomEntity>> getAllTranslations();

    //looks for specific translation data type
    @Query("SELECT * FROM translations WHERE DATA_TYPE = :dataType ORDER BY TIMESTAMP DESC")
    List<RoomEntity> findTYPETranslations(String dataType);

    //looks for specific translation data type
    @Query("SELECT * FROM translations WHERE DATA_TYPE = :dataType AND  ORIGINAL_TEXT LIKE '%' || :text || '%' OR TRANSLATED_TEXT LIKE '%' || :text || '%' ORDER BY TIMESTAMP DESC")
    List<RoomEntity> findTranslations(String dataType, String text);


}
