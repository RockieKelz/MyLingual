package com.example.mylingual.data;

import com.example.mylingual.data.room_db.RoomEntity;

import java.util.List;

public interface GetAllOFTYPETranslations {
    void onPostExecute (List<RoomEntity> roomEntity);
}
