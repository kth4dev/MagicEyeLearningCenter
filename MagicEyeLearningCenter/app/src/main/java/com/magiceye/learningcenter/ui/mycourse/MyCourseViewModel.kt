package com.magiceye.learningcenter.ui.mycourse

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.QuerySnapshot
import com.magiceye.learningcenter.R
import com.magiceye.learningcenter.helper.CollectionName
import com.magiceye.learningcenter.helper.FireStore
import com.magiceye.learningcenter.model.Course

class MyCourseViewModel : ViewModel() {
    val courseIDList: MutableLiveData<ArrayList<String>> = MutableLiveData()
    fun getCourseID(ph:String) {


        FireStore.instance().collection(CollectionName.courseByStudent).document(ph).collection(
            CollectionName.course).get()
            .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                if (task.isSuccessful) {
                    val idList=ArrayList<String>()
                    for (document in task.result!!) {
                        val courseID = document.getString("courseId")
                        if (courseID != null) {
                            idList.add(courseID)
                        }

                    }

                    courseIDList.value=idList

                }

            })


    }


}