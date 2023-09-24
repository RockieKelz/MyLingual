package com.example.mylingual.data;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mylingual.data.room_db.RoomEntity;

import java.util.List;

public class ViewModal extends AndroidViewModel {

    private RoomRepository repository;
    private LiveData<List<RoomEntity>> allTranslations;
    private MutableLiveData<List<RoomEntity>> allTypeTranslations;
    private MutableLiveData<List<RoomEntity>> searchedTranslations;


    //view modal constructor
    public ViewModal(@NonNull Application application) {
        super(application);
        repository = new RoomRepository(application);
        allTranslations = repository.getAllTranslations();
        allTypeTranslations = repository.getAllOFTYPETranslations();
        searchedTranslations = repository.getSearchedTranslations();
    }

    public void insert(RoomEntity entity) {
        repository.insert(entity);
    }

    public void update(RoomEntity entity) {
        repository.update(entity);
    }
    //delete translation from saved database
    public void deleteSavedTranslation(RoomEntity entity) {
        repository.deleteSavedTranslation(entity);
    }

    public void deleteAllTranslations() {
        repository.deleteAllTranslations();
    }

    public void deleteTranslations(String entity) { repository.deleteTranslationFromDatabase(entity); }

    //for finding either recent or bookmarked translations
    public void findTYPETranslations(String name) { repository.findTYPETranslations(name); }

    //for finding either recent or bookmarked translations
    public void findTranslations(String type, String name) { repository.findTranslations(type, name); }

    public LiveData<List<RoomEntity>> getAllTranslations() {
        return allTranslations;
    }

    //for retrieving either recent or bookmarked translations
    public MutableLiveData<List<RoomEntity>> getAllOFTYPETranslations() {
        return allTypeTranslations;
    }
    //for retrieving searched translations
    public MutableLiveData<List<RoomEntity>> getSearchedTranslations() {
        return searchedTranslations;
    }
}