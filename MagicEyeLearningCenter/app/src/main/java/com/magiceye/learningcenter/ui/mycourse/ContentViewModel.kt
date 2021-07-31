package com.magiceye.learningcenter.ui.mycourse

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.QuerySnapshot
import com.magiceye.learningcenter.helper.CollectionName
import com.magiceye.learningcenter.helper.FireStore
import com.magiceye.learningcenter.model.Content
import com.magiceye.learningcenter.model.Course

class ContentViewModel : ViewModel() {
      val contentList: MutableLiveData<ArrayList<Content>> = MutableLiveData()
     fun getData(course: Course) {
         course.id?.let {
             FireStore.instance().collection(CollectionName.dataByCourse).document(it).collection(
                 CollectionName.content).whereEqualTo(CollectionName.visible,true).get()
                 .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                     if (task.isSuccessful) {

                         var list=ArrayList<Content>()
                         for (document in task.result!!) {
                             val course = document.toObject(Content::class.java)
                             course.id=document.id
                             list.add(course)
                         }
                         contentList.value=list
                     }else{

                     }
                 })
         }
    }
}