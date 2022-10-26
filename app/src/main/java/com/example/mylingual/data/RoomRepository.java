package com.example.mylingual.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mylingual.data.room_db.RoomDAO;
import com.example.mylingual.data.room_db.RoomDatabase;
import com.example.mylingual.data.room_db.RoomEntity;

import java.util.List;

public class RoomRepository {
    private final RoomDAO dao;
    private final LiveData<List<RoomEntity>> allTranslations;
    private final MutableLiveData<List<RoomEntity>> typeResults = new MutableLiveData<>();

    //repository constructor
    public RoomRepository(Application application) {
        RoomDatabase database = RoomDatabase.getInstance(application);
        dao = database.Dao();
        allTranslations = dao.getAllTranslations();
    }

    public void insert(RoomEntity entity) {
        new Insert(dao).execute(entity);
    }

    public void update(RoomEntity entity) {
        new Update(dao).execute(entity);
    }

    public void delete(RoomEntity entity) {
        new DeleteTranslation(dao).execute(entity);
    }

    public void deleteAllTranslations() {
        new DeleteAllTranslations(dao).execute();
    }

    public LiveData<List<RoomEntity>> getAllTranslations() {
        return allTranslations;
    }

    public MutableLiveData<List<RoomEntity>> getAllOFTYPETranslations() {
        return typeResults;
    }

    //finding a specific type (recent or saved) translation
    public void findTYPETranslations(String name) {
        QueryAllOFTYPETranslations task = new QueryAllOFTYPETranslations(dao);
        task.select = this;
        task.execute(name);
    }
    //add the translation to list of that specified type
    public void addSpecifiedTYPETranslations(List<RoomEntity> roomEntity){
        typeResults.setValue(roomEntity);
    }

    // Async Task to insert new translation
    private static class Insert extends AsyncTask<RoomEntity, Void, Void> {
        private final RoomDAO dao;

        private Insert(RoomDAO dao) {
            this.dao = dao;
        }
        @Override
        protected Void doInBackground(RoomEntity... entities) {
            dao.insert(entities[0]);
            return null;
        }
    }

    // Async Task to update translation
    private static class Update extends AsyncTask<RoomEntity, Void, Void> {
        private final RoomDAO dao;

        private Update(RoomDAO dao) {
            this.dao = dao;
        }
        @Override
        protected Void doInBackground(RoomEntity... entities) {
            dao.update(entities[0]);
            return null;
        }
    }

    // Async Task to insert delete a specific translation
    private static class DeleteTranslation extends AsyncTask<RoomEntity, Void, Void> {
        private final RoomDAO dao;

        private DeleteTranslation(RoomDAO dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(RoomEntity... entities) {
            dao.delete(entities[0]);
            return null;
        }
    }

    // Async Task to delete all translations for clearing data
    private static class DeleteAllTranslations extends AsyncTask<Void, Void, Void> {
        private final RoomDAO dao;
        private DeleteAllTranslations(RoomDAO dao) {
            this.dao = dao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            dao.deleteAll();
            return null;
        }
    }

    // Async Task to search for a translation type (recent or saved)
    private static class QueryAllOFTYPETranslations extends AsyncTask <String, Void, List<RoomEntity>> {
        private final RoomDAO R_dao;
        private RoomRepository select = null;
        private QueryAllOFTYPETranslations(RoomDAO dao) { this.R_dao = dao;}

        @Override
        protected List<RoomEntity> doInBackground(final String... params) {
            return (List<RoomEntity>) R_dao.findTYPETranslations(params[0]);
        }
        @Override
        public void onPostExecute(List<RoomEntity> resultEntity)
        {
            select.addSpecifiedTYPETranslations(resultEntity);
        }
    }

}
