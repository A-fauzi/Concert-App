package com.example.concert_app.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

object FirebaseServiceInstance {

    /**
     * declaration for firebase authentication plus getInstance()
     */
    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    /**
     * Declaration firebase user
     */
    var user: FirebaseUser? = auth.currentUser

    /**
     * declaration for firebase realtimeDatabase plus getInstance()
     */
    val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()

    /**
     * declaration databaseReference of firebaseDatabase
     */
    lateinit var databaseReference: DatabaseReference

    /**
     * declaration for firebase storage plus getInstance()
     */
    val firebaseStorage: FirebaseStorage = FirebaseStorage.getInstance()

    /**
     * declaration for storage of firebaseStorage
     */
    lateinit var storageReference: StorageReference
}