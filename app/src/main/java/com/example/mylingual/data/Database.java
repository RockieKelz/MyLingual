package com.example.mylingual.data;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;


public class Database {
    public static void AddUser(UserAccount currentUser){

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        FirebaseAuth user = FirebaseAuth.getInstance();

        CollectionReference dbUsers = database.collection("User_Data");
        dbUsers.document(Objects.requireNonNull(user.getUid())).set(currentUser);

    }
}
