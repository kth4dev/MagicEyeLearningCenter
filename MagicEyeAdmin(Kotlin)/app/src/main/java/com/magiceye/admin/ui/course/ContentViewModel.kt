package com.magiceye.admin.ui.course

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.QuerySnapshot
import com.magiceye.admin.helper.CollectionName
import com.magiceye.admin.helper.FireStore
import com.magiceye.admin.model.Content
import com.magiceye.admin.model.Course

class ContentViewModel : ViewModel() {
      val contentList: MutableLiveData<ArrayList<Content>> = MutableLiveData()
     fun getData(course: Course) {
         course.id?.let {
             FireStore.instance().collection(CollectionName.dataByCourse).document(it).collection(
                 CollectionName.content).get()
                 .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                     if (task.isSuccessful) {
                         var list=ArrayList<Content>()
                         for (document in task.result!!) {
                             val course = document.toObject(Content::class.java)
                             course.id=document.id
                             list.add(course)
                         }
                         contentList.value=list
                     }
                 })
         }
    }
}