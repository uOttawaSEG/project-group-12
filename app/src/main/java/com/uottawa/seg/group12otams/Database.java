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
import java.util.List;
import java.util.Objects;
import java.util.HashMap;
import java.util.Map;


import kotlin.NotImplementedError;

public class Database<E> {
    private String TAG;
    private Class<E> userClass;
    private FirebaseFirestore db;
    private ArrayList<E> users;
    private String dbCollection;
    private ArrayList<TimeSlot> timeSlots;
    private ArrayList<TimeSlotRequest> timeSlotRequests;
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

    // NEW: Create a registration request
    public void createRegistrationRequest(User user, String role) {
        Map<String, Object> request = new HashMap<>();
        request.put("firstName", user.getFirstName());
        request.put("lastName", user.getLastName());
        request.put("email", user.getEmail());
        request.put("password", user.getPassword());
        request.put("phoneNumber", user.getPhoneNumber());
        request.put("role", role);
        request.put("status", "Pending");
        if (user instanceof Student) {
            request.put("programOfStudy", ((Student) user).getProgramOfStudy());
        } else if (user instanceof Tutor) {
            request.put("highestDegree", ((Tutor) user).getHighestDegree());
            request.put("coursesOffered", ((Tutor) user).getCoursesOffered());
        }

        db.collection("registration_requests")
                .document(user.getEmail())
                .set(request)
                .addOnSuccessListener(aVoid -> Log.e(TAG, "Registration request created"))
                .addOnFailureListener(e -> Log.e(TAG, "Error creating registration request", e));
    }

    // NEW: Get registration requests by status
    public void getRegistrationRequests(String status, OnCompleteListener<QuerySnapshot> listener) {
        db.collection("registration_requests")
                .whereEqualTo("status", status)
                .get()
                .addOnCompleteListener(listener);
    }

    // NEW: Update request status
    public void updateRequestStatus(String email, String status, OnCompleteListener<Void> listener) {
        db.collection("registration_requests")
                .document(email)
                .update("status", status)
                .addOnCompleteListener(listener);
    }

    // NEW: Approve user and move to appropriate collection
    public void approveUser(User user, String role) {
        String collection = role.equals("Student") ? "students" : "tutors";
        db.collection(collection)
                .document(user.getEmail())
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    Log.e(TAG, "User approved");
                    updateRequestStatus(user.getEmail(), "Approved", task -> {});
                });
    }

    // NEW: Get registration request by email
    public void getRegistrationRequest(String email, OnCompleteListener<DocumentSnapshot> listener) {
        db.collection("registration_requests")
                .document(email)
                .get()
                .addOnCompleteListener(listener);
    }

    // Deliverable 3
    // Retrieves all timeslots
    private void retrieveAllTimeSlots() {
        // Retrieve all current tutoring timeslot data and store into memory
        db.collection("time_slots")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Save all timeslot data into "timeslot"
                                TimeSlot timeslot = document.toObject(TimeSlot.class);
                                if (timeslot != null) {
                                    timeSlots.add(timeslot);
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
        Log.e(TAG, "Successfully retrieved all timeslots");
    }

    // Returns timeslots of a specific tutor if specified, and all timeslots if not
    public ArrayList<TimeSlot> getTimeSlots(Tutor tutor) {
        // Get timeslots if null
        if (timeSlots == null) retrieveAllTimeSlots();

        // Return all timeslots if tutor is not specified
        if (tutor == null) return timeSlots;

        // Get and return timeslots of specified tutor
        ArrayList<TimeSlot> specificTimeSlots = new ArrayList<>();

        // Add all timeslots of a specific tutor
        for (TimeSlot timeSlot : timeSlots) {
            if (Objects.equals(timeSlot.getTutor(), tutor)) {
                specificTimeSlots.add(timeSlot);
            }
        }

        return specificTimeSlots;
    }

    // Create/update a specific timeslot
    public String modifyTimeSlot(TimeSlot timeSlot) {
        // Add to database
        db.collection("time_slots")
                .document(timeSlot.getTimeSlotId())
                .set(timeSlot);

        // Re-fetch all timeslots
        retrieveAllTimeSlots();

        // Log method success
        Log.e(TAG, "Successfully modified time slot");
        return timeSlot.getTimeSlotId();
    }

    // Delete a specific timeslot
    public void deleteTimeSlot(String timeSlotId) {
        // Get the specified timeSlot
        DocumentReference docRef = db
                .collection("time_slots")
                .document(timeSlotId);

        docRef.delete()
                .addOnCompleteListener(aVoid -> Log.e(TAG, "Successfully deleted user"))
                .addOnFailureListener(error -> Log.e(TAG, "Error deleting user", error));
    }

    // Create timeslot request
    public void createTimeSlotRequest(Student student, TimeSlot timeSlot) {
        // Create a simple HashMap to structure data (no need for class)
        Map<String, Object> request = new HashMap<>();
        request.put("studentId", student.getEmail());
        request.put("timeSlotId", timeSlot.getTimeSlotId());
        if (timeSlot.getTutor().getAutoApproveTimeSlotSessions() == true) {
            request.put("status", "Approved");
        } else {
            request.put("status", "Pending");
        }


        // Add timeSlot request to db
        db.collection("time_slot_requests")
                .document(timeSlot.getTimeSlotId())
                .set(request)
                .addOnSuccessListener(aVoid -> Log.e(TAG, "Time slot request created"))
                .addOnFailureListener(e -> Log.e(TAG, "Error creating time slot request", e));
    }

    // Delete timeslot request
    public void deleteTimeSlotRequest(String timeSlotId) {
        // Get the specified timeSlot
        DocumentReference docRef = db
                .collection("time_slot_requests")
                .document(timeSlotId);

        // Delete from db
        docRef.delete()
                .addOnCompleteListener(aVoid -> Log.e(TAG, "Successfully deleted time slot request"))
                .addOnFailureListener(error -> Log.e(TAG, "Error deleting time slot request", error));
    }

    // Approve or reject a timeslot request
    public void approveTimeSlotRequest(String timeSlotId, boolean isApproved) {
        // Update status in db
        String status = "Approved";
        if (!isApproved) status = "Rejected";

        db.collection("time_slot_requests")
                .document(timeSlotId)
                .update("status", status)
                .addOnSuccessListener(aVoid -> Log.e(TAG, "Time slot request updated"))
                .addOnFailureListener(e -> Log.e(TAG, "Error updating time slot request", e));
    }

    // Returns all timeSlotRequests
    private void retrieveAllTimeSlotRequests() {
        // Retrieve all current tutoring timeslot data and store into memory
        db.collection("time_slot_requests")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Save all timeslot data into "timeslot"
                                TimeSlotRequest timeSlotRequest = document.toObject(TimeSlotRequest.class);
                                if (timeSlotRequest != null) {
                                    timeSlotRequests.add(timeSlotRequest);
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
        Log.e(TAG, "Successfully retrieved all timeslots requests");
    }

    // Return the timeSlotRequests for a specific tutor. Returns all timeSlotRequests if tutor is not specified (= null)
    public ArrayList<TimeSlotRequest> getTimeSlotRequests(Tutor tutor) {
        // Get timeslots requests if null
        if (timeSlotRequests == null) retrieveAllTimeSlotRequests();

        // Return all timeslots requests if tutor is not specified
        if (tutor == null) return timeSlotRequests;

        // Get and return timeslots requests of specified tutor
        ArrayList<TimeSlotRequest> specificTimeSlotsRequests = new ArrayList<>();

        // Add all timeslots of a specific tutor
        for (TimeSlotRequest timeSlotRequest : timeSlotRequests) {
            if (Objects.equals(timeSlotRequest.getTutorId(), tutor.getEmail())) {
                specificTimeSlotsRequests.add(timeSlotRequest);
            }
        }

        return specificTimeSlotsRequests;
    }
}
