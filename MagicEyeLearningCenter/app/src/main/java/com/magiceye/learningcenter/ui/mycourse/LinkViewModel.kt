package com.magiceye.learningcenter.ui.mycourse

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.QuerySnapshot
import com.magiceye.learningcenter.helper.CollectionName
import com.magiceye.learningcenter.helper.FireStore
import com.magiceye.learningcenter.model.Content
import com.magiceye.learningcenter.model.Course
import com.magiceye.learningcenter.model.Link


class LinkViewModel : ViewModel() {
      val linkList: MutableLiveData<ArrayList<Link>> = MutableLiveData()
     fun getData(course: Course, content: Content) {
         course.id?.let {
             content.id?.let { it1 ->
                 FireStore.instance().collection(CollectionName.linkByContent).document(it).collection(
                     it1
                 ).whereEqualTo(CollectionName.visible,true).get()
                     .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                         if (task.isSuccessful) {
                             var list=ArrayList<Link>()
                             for (document in task.result!!) {
                                 val link = document.toObject(Link::class.java)
                                 link.id=document.id
                                 list.add(link)
                             }
                             linkList.value=list
                         }
                     })
             }
         }
    }
}