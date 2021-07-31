package com.magiceye.learningcenter.ui.mycourse

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.QuerySnapshot
import com.magiceye.learningcenter.helper.CollectionName
import com.magiceye.learningcenter.helper.FireStore
import com.magiceye.learningcenter.model.Content
import com.magiceye.learningcenter.model.Course
import com.magiceye.learningcenter.model.Video


class VideoViewModel : ViewModel() {
      val videoList: MutableLiveData<ArrayList<Video>> = MutableLiveData()
     fun getData(course: Course, content: Content) {
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