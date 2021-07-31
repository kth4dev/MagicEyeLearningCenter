package com.magiceye.learningcenter.ui.catalog

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.QuerySnapshot
import com.magiceye.learningcenter.helper.CollectionName
import com.magiceye.learningcenter.helper.FireStore
import com.magiceye.learningcenter.model.Course

class CatalogViewModel : ViewModel() {

    val courseList: MutableLiveData<ArrayList<Course>> = MutableLiveData()
    fun getData() {
        FireStore.instance().collection(CollectionName.course).whereEqualTo(CollectionName.visible,true).get()
            .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                if (task.isSuccessful) {
                    var list=ArrayList<Course>()
                    for (document in task.result!!) {
                        val course = document.toObject(Course::class.java)
                        course.id=document.id
                        list.add(course)
                        Log.i("KyawThiha", course.name.toString())
                    }
                    courseList.value=list
                }
            })
    }
}