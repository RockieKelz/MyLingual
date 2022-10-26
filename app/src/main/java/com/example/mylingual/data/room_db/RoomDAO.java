package com.example.mylingual.data.room_db;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
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
    void delete(RoomEntity e);

    // delete all data from database.
    @Query("DELETE FROM Translations")
    void deleteAll();

    // read all the data
    @Query("SELECT * FROM translations ORDER BY id ASC")
    LiveData<List<RoomEntity>> getAllTranslations();

    //looks for specific translation data type
    @Query("SELECT * FROM translations WHERE DATA_TYPE = :dataType")
    List<RoomEntity> findTYPETranslations(String dataType);
}
