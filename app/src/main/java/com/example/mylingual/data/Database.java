package com.example.mylingual.data;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class Database {
    public static String speechInput;
    public static void AddUser(Map<String, Object> currentUser){

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        FirebaseAuth user = FirebaseAuth.getInstance();

        CollectionReference dbUsers = database.collection("User_Data");
        dbUsers.document(Objects.requireNonNull(user.getUid())).set(currentUser);

    }
    public static void SetSpeechInput(String speech)
    {
        speechInput = speech;
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        FirebaseAuth user = FirebaseAuth.getInstance();
        Map<String, String> input = new HashMap<>();
        input.put("speechinput", speechInput);
        CollectionReference speechCollect = database.collection("User_Data")
                .document(Objects.requireNonNull(user.getUid()))
                .collection("speech");
        speechCollect.add(input);

    }
    public static String GetSpeechInput(){
        return speechInput;
    }
}

