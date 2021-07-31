package com.magiceye.admin.ui.admin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.QuerySnapshot
import com.magiceye.admin.helper.CollectionName
import com.magiceye.admin.helper.FireStore
import com.magiceye.admin.model.Admin

class AdminViewModel : ViewModel() {
    val adminList: MutableLiveData<ArrayList<Admin>> = MutableLiveData()
    fun getData() {
        FireStore.instance().collection(CollectionName.admin).get()
            .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                if (task.isSuccessful) {
                    var list=ArrayList<Admin>()
                    for (document in task.result!!) {
                        val admin = document.toObject(Admin::class.java)
                        admin.id=document.id
                        list.add(admin)
                    }
                    adminList.value=list
                }
            })
    }
}