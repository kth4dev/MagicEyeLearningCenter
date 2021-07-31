package com.magiceye.admin.ui.course

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.QuerySnapshot
import com.htetznaing.xgetter.Model.XModel
import com.htetznaing.xgetter.XGetter
import com.magiceye.admin.helper.CollectionName
import com.magiceye.admin.helper.FireStore
import com.magiceye.admin.model.Content
import com.magiceye.admin.model.Course
import com.magiceye.admin.model.Video

class VideoViewModel : ViewModel() {
      val videoList: MutableLiveData<ArrayList<Video>> = MutableLiveData()
    val videoUrl:MutableLiveData<String> = MutableLiveData()
     fun getData(course: Course,content: Content) {
         course.id?.let {
             content.id?.let { it1 ->
                 FireStore.instance().collection(CollectionName.videoByContent).document(it).collection(
                     it1
                 ).get()
                     .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                         if (task.isSuccessful) {
                             var list=ArrayList<Video>()
                             for (document in task.result!!) {
                                 val course = document.toObject(Video::class.java)
                                 course.id=document.id
                                 list.add(course)
                             }
                             videoList.value=list
                         }
                     })
             }
         }
    }




}