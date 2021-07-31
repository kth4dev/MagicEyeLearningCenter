package com.magiceye.admin.ui.course

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import com.magiceye.admin.helper.CollectionName
import com.magiceye.admin.helper.FireStore
import com.magiceye.admin.R
import com.magiceye.admin.model.Content
import com.magiceye.admin.model.Course
import kotlinx.android.synthetic.main.fragment_upload_content.*


class UploadContentFragment : Fragment() {
    lateinit var course: Course


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        course = if (savedInstanceState != null) {
            savedInstanceState.getSerializable(CollectionName.bCourse) as Course
        } else {
            requireArguments().getSerializable(CollectionName.bCourse) as Course
        }
        return inflater.inflate(R.layout.fragment_upload_content, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        tv_uploadContent_courseName.text=course.name
        super.onViewCreated(view, savedInstanceState)
        btn_uploadContent.setOnClickListener {
            if (et_uploadContent_contentName.text.toString() != "" && rg_uploadContent.checkedRadioButtonId != -1) {
                uploadContent()
            } else {
                Toast.makeText(context, "Please Fill Completely!", Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun uploadContent() {
        pb_uploadContent.isVisible=true
        btn_uploadContent.isEnabled=false
        val name=et_uploadContent_contentName.text.toString()
        val visible= rb_uploadContent_true.isChecked
        val toUploadCourse = Content(name , visible)


        course.id?.let {
                FireStore.instance().collection(CollectionName.dataByCourse).document(it).collection(
                    CollectionName.content)
                    .add(toUploadCourse)
                    .addOnSuccessListener { documentReference ->
                        btn_uploadContent.isEnabled=false
                        pb_uploadContent.isVisible=false
                        et_uploadContent_contentName.setText("")
                        if(visible){
                            rb_uploadContent_true.isChecked=false
                        }else{
                            rb_uploadContent_false.isChecked=false
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.w("Kyawthiha", "Error adding document", e)
                    }
        }

    }
}