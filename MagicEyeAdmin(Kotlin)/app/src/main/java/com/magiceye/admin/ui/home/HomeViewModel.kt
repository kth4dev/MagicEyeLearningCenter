package com.magiceye.admin.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.QuerySnapshot
import com.magiceye.admin.helper.FireStore
import com.magiceye.admin.model.Course

class HomeViewModel : ViewModel() {
    val courseList: MutableLiveData<ArrayList<Course>> = MutableLiveData()
    val courseNameList: MutableLiveData<ArrayList<String>> = MutableLiveData()
    val idList: MutableLiveData<ArrayList<String>> = MutableLiveData()
    fun getData() {
        FireStore.instance().collection("courses").get()
            .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                if (task.isSuccessful) {
                    var list=ArrayList<Course>()
                    var listName=ArrayList<String>()
                    for (document in task.result!!) {
                        val course = document.toObject(Course::class.java)
                        course.id=document.id
                        list.add(course)
                        listName.add(course.name!!)
                        Log.i("KyawThiha", course.name.toString())
                    }
                    courseList.value=list
                    courseNameList.value=listName
                }
            })
    }



}