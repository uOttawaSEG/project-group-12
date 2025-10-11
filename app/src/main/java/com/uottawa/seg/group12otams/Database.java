// Taha Rashid
// Oct 10, 2025
// Database utility class
// Parts of the code were created using information and snippets from Firebase docs at https://firebase.google.com/docs/firestore/

package com.uottawa.seg.group12otams;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import kotlin.NotImplementedError;
public class Database {
    private static String TAG = "Database";
    // TODO: change class type to custom type
    private static ArrayList<Object> users;
    private static FirebaseFirestore db;
    public Database() {
        Log.e(TAG, "Database object created");
    }

    // Retrieves all users and updates the "users" class variable
    // Throws an error if a problem occurs
    public void retrieveAllUsers() {
        // Sets users and Firebase class variables
        users = new ArrayList<Object>();
        db = FirebaseFirestore.getInstance();

        // Retrieve all current user data and store into memory
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Save all user data into "users"
                                Object user = document.toObject(Object.class);
                                // TODO: uncomment when using custom class
                                // user.id = document.getId();
                                if (user != null) {
                                    Log.d(TAG, user.toString());
                                    users.add(user);
                                }

                                // Print to Log
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        // Log method success
        Log.e(TAG, "Successfully retrieved all users");
    }

    // Retrieves a specific user with the given id in the given collection, and updates the corresponding object in the "users" class variable
    // Throws an error if a problem occurs
    public void retrieveUser(String id, String collection) {
        // Get the specified user
        DocumentReference docRef = db
                .collection("users")
                .document(collection)
                .collection(collection)
                .document(id);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    // Get the user data
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        Object user = document.toObject(Object.class);
                        // user.id = id;

                        // Update "users" List
                        // TODO: uncomment when User class is implemented
//                        // Find the original object
//                        Object originalUser = users.stream()
//                                .filter(u -> u.id == user.id)
//                                .findFirst()
//                                .orElse(null);
//                        // Add if it did not exist before
//                        if (originalUser == null) users.add(user);
//                        else {
//                            // Else update the object
//                            // TODO: implement "update" method in User class. Check if we can simply do "this = updatedUser"?
//                            // originalUser.update(user);
//                        }

                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    // Log errors
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        // Log method success
        Log.e(TAG, "Successfully retrieved user");
    }

    // Returns a user with the given specified id. If no user is found, returns null
    // Throws an error if a problem occurs
    public Object getUser(String id) {
        // TODO: uncomment when User class is implemented
//        return users.stream()
//            .filter(u -> u.id == user.id)
//            .findFirst()
//            .orElse(null);
        // Log method success
//        Log.e(TAG, "Successfully got user");
        throw new NotImplementedError();
    }

    // Creates a user of the specified collection in the database, and returns its assigned id in the database.
    // Throws an error if a problem occurs
    // NOTE: we may want to manually set the id of the object to a PK, like an email
    public String createUser(Object user, String collection) {
//        TODO: uncomment when User class is implemented
//        // Add to database
//        db.collection("users")
//                .document(collection)
//                .collection(collection)
//                .document(user.id)
//                .set(user);
//
//        // Add to List
//        users.add(user);
        // Log method success
//        Log.e(TAG, "Successfully created user");
        throw new NotImplementedError();
    }

    // Updates a user of the specified collection.
    // Throws an error if the user is not found, or if a problem occurs
    public void updateUser(Object user, String collection) throws Exception {
        // TODO: uncomment when User class is implemented
//        // Find the original object in List
//        Object originalUser = users.stream()
//                .filter(u -> u.id == user.id)
//                .findFirst()
//                .orElse(null);
//        // Throw error if it did not exist before
//        if (originalUser == null) throw new Exception("");
//
//        // Update in database
//        db.collection("users")
//                .document(collection)
//                .collection(collection)
//                .document(user.id)
//                .set(user);

        // Update in List
        // TODO: implement "update" method in User class. Check if we can simply do "this = updatedUser"?
        // originalUser.update(user);

        // Log method success
        //  Log.e(TAG, "Successfully updated user");
        throw new NotImplementedError();
    }

    // Deletes a user with the given id of the specified collection.
    // Throws an error if a problem occurs.
    public void deleteUser(String id, String collection) {
        // Get the specified user
        DocumentReference docRef = db
                .collection("users")
                .document(collection)
                .collection(collection)
                .document(id);

        docRef.delete()
                .addOnCompleteListener(aVoid -> Log.e(TAG, "Successfully deleted user"))
                .addOnFailureListener(error -> Log.e(TAG, "Error deleting user", error));
    }
}
