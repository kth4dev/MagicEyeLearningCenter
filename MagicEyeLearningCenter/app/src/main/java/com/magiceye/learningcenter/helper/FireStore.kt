package com.magiceye.learningcenter.helper

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


object FireStore {
    private var firestore: FirebaseFirestore?= null

    fun instance(): FirebaseFirestore {
        if (firestore == null) {
           firestore =Firebase.firestore
        }
        return firestore!!
    }
}