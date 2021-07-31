package com.magiceye.admin.ui.course

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.magiceye.admin.helper.CollectionName
import com.magiceye.admin.helper.FireStore
import com.magiceye.admin.MainActivity
import com.magiceye.admin.R
import com.magiceye.admin.model.Content
import com.magiceye.admin.model.Course
import kotlinx.android.synthetic.main.fragment_upload_content.*


class EditContentFragment : Fragment() {
    private val db = Firebase.firestore
    lateinit var course:Course
    lateinit var content:Content
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        course = if (savedInstanceState != null) {
            savedInstanceState.getSerializable(CollectionName.bCourse) as Course
        } else {
            requireArguments().getSerializable(CollectionName.bCourse) as Course
        }
        content = if (savedInstanceState != null) {
            savedInstanceState.getSerializable(CollectionName.bContent) as Content
        } else {
            requireArguments().getSerializable(CollectionName.bContent) as Content
        }
        return inflater.inflate(R.layout.fragment_upload_content, container, false)
    }



        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

            init()
            super.onViewCreated(view, savedInstanceState)
            btn_uploadContent.setOnClickListener {
                val visibleValue= rb_uploadContent_true.isChecked
                if (et_uploadContent_contentName.text.toString() !=content.name || visibleValue!=content.visible) {
                    uploadContent(it)
                } else {
                    Toast.makeText(context, "Nothing to change!", Toast.LENGTH_LONG).show()
                }
            }
        }


    private fun init() {
        (activity as MainActivity?)?.supportActionBar?.title = course.name
        tv_uploadContent_courseName.visibility=View.GONE
        et_uploadContent_contentName.setText(content.name)
        if(content.visible!!){
            rb_uploadContent_true.isChecked=true
        }else{
            rb_uploadContent_false.isChecked=true
        }
    }
    private fun uploadContent(view: View) {
        pb_uploadContent.isVisible=true
        btn_uploadContent.isEnabled=false
        val name=et_uploadContent_contentName.text.toString()
        val visible= rb_uploadContent_true.isChecked
        val toUploadCourse = Content(name , visible)


        course.id?.let {
            FireStore.instance().collection(CollectionName.dataByCourse).document(it).collection(
                CollectionName.content).document(content.id!!)
                .set(toUploadCourse)
                .addOnSuccessListener { documentReference ->
                    val bundle = Bundle()
                    bundle.putSerializable(CollectionName.bCourse, course)
                    Navigation.findNavController(view).navigate(R.id.action_editContentFragment_to_contentFragment,bundle)
                }
                .addOnFailureListener { e ->
                    Log.w("Kyawthiha", "Error adding document", e)
                }
        }

    }
}