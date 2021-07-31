package com.magiceye.admin.ui.course

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.magiceye.admin.R
import com.magiceye.admin.model.Course
import kotlinx.android.synthetic.main.fragment_upload_course.*


class UploadCourseFragment : Fragment() {
    private val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_upload_course, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_uploadCourse.setOnClickListener {
            if (et_uploadCourse_name.text.toString() != "" && rg_uploadCourse.checkedRadioButtonId != -1) {
                uploadCourse()
            } else {
                Toast.makeText(context, "Please Fill Completely!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun uploadCourse() {
        pb_uploadCourse.isVisible=true
        btn_uploadCourse.isEnabled=false
        val name=et_uploadCourse_name.text.toString()
        val visible= rb_uploadCourse_true.isChecked
        val course = Course(name , visible)


        db.collection("courses")
            .add(course)
            .addOnSuccessListener { documentReference ->
                btn_uploadCourse.isEnabled=true
                pb_uploadCourse.isVisible=false
                et_uploadCourse_name.setText("")
                if(visible){
                    rb_uploadCourse_true.isChecked=false
                }else{
                    rb_uploadCourse_false.isChecked=false
                }
              }
            .addOnFailureListener { e ->
                Log.w("Kyawthiha", "Error adding document", e)
            }

    }
}