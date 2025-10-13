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
import java.util.Objects;

import kotlin.NotImplementedError;

public class Database<E> {
    private String TAG;
    private Class<E> userClass;
    private FirebaseFirestore db;
    private ArrayList<E> users;
    private String dbCollection;
    public Database(Class<E> userClass, String dbCollection) {
        // Pass in the class, used to convert Firebase responses to objects later
        this.userClass = userClass;
        // Create a Firebase instance
        db = FirebaseFirestore.getInstance();
        users = new ArrayList<E>();
        this.dbCollection = dbCollection;
        TAG = "Database-" + dbCollection;

        // Retrieve all users from DB
        this.retrieveAllUsers();

        Log.e(TAG, TAG + " object created");
    }

    // Retrieves all users and updates the "users" class variable
    public void retrieveAllUsers() {
        // Retrieve all current user data and store into memory
        db.collection(dbCollection)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Save all user data into "users"
                                E user = document.toObject(userClass);
                                if (user != null) {
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

    // Retrieves a specific user with the given email in the given collection, and updates the corresponding object in the "users" class variable
    public void retrieveUser(String email) {
        // Get the specified user
        DocumentReference docRef = db
                .collection(dbCollection)
                .document(email);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    // Get the user data
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        E user = document.toObject(userClass);

                        // Update "users" List
                        // Find the original object
                        E originalUser = users.stream()
                                .filter(u -> Objects.equals(((User) u).email, email))
                                .findFirst()
                                .orElse(null);
                        // Add if it did not exist before
                        if (originalUser == null) users.add(user);
                        else {
                            ((User) originalUser).update((User) user);
                            // users.remove(originalUser);
                            // users.add(user);
                        }

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

    // Returns a user with the given specified email. If no user is found, returns null
    public E getUser(String email) {
        E filteredUser = users.stream()
            .filter(u -> Objects.equals(((User) u).email, email))
            .findFirst()
            .orElse(null);
        // Log method success
        Log.e(TAG, "Successfully got user");
        return filteredUser;
    }

    // Creates a user of the specified collection in the database, and returns its assigned id (email) in the database.
    public String createUser(E user) {
        // Add to database
        db.collection(dbCollection)
                .document(((User) user).email)
                .set(user);

        // Add to List
        users.add(user);
        // Log method success
        Log.e(TAG, "Successfully created user");
        return ((User) user).email;
    }

    // Updates a user of the specified collection.
    // Throws an error if the user could not be found
    public void updateUser(E user) throws Exception {
        // NOTE: after some testing, it looks like update does not work properly
        // We need to do async/await, and block the call so that we fully load in
        // all users, then we can run update. Will check back on this in the future
        throw new NotImplementedError();

//        // Find the original object in List
//        E originalUser = users.stream()
//                .filter(u -> Objects.equals(((User) u).email, ((User) user).email))
//                .findFirst()
//                .orElse(null);
//        // Throw error if it did not exist before
//        if (originalUser == null) {
//            throw new Exception("User not found while updating");
//        }
//
//        // Update in database
//        db.collection(dbCollection)
//                .document(((User) user).email)
//                .set(user);
//
//        // Update in List
//        // TODO: replace with "update" method
//        users.remove(originalUser);
//        users.add(user);
//
//        // Log method success
//        Log.e(TAG, "Successfully updated user");
    }

    // Deletes a user with the given email of the specified collection.
    // Throws an error if a problem occurs.
    public void deleteUser(String email) {
        // Get the specified user
        DocumentReference docRef = db
                .collection(dbCollection)
                .document(email);

        docRef.delete()
                .addOnCompleteListener(aVoid -> Log.e(TAG, "Successfully deleted user"))
                .addOnFailureListener(error -> Log.e(TAG, "Error deleting user", error));
    }
}
