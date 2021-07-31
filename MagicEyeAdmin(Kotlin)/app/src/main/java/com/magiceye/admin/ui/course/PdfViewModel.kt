package com.magiceye.admin.ui.course

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.QuerySnapshot
import com.magiceye.admin.helper.CollectionName
import com.magiceye.admin.helper.FireStore
import com.magiceye.admin.model.Content
import com.magiceye.admin.model.Course
import com.magiceye.admin.model.Pdf

class PdfViewModel : ViewModel() {
      val pdfList: MutableLiveData<ArrayList<Pdf>> = MutableLiveData()
     fun getData(course: Course,content: Content) {
         course.id?.let {
             content.id?.let { it1 ->
                 FireStore.instance().collection(CollectionName.pdfByContent).document(it).collection(
                     it1
                 ).get()
                     .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                         if (task.isSuccessful) {
                             var list = ArrayList<Pdf>()
                             for (document in task.result!!) {
                                 val course = document.toObject(Pdf::class.java)
                                 course.id = document.id
                                 list.add(course)
                             }
                             pdfList.value = list
                         }
                     })
             }
         }
    }
}