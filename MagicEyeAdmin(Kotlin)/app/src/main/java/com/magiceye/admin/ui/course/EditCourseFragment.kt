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
import com.magiceye.admin.R
import com.magiceye.admin.model.Course
import kotlinx.android.synthetic.main.fragment_upload_course.*


class EditCourseFragment : Fragment() {
    private val db = Firebase.firestore
    lateinit var course: Course
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
        return inflater.inflate(R.layout.fragment_upload_course, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        btn_uploadCourse.setOnClickListener {
            val visibleValue = rb_uploadCourse_true.isChecked
            if (et_uploadCourse_name.text.toString() != course.name || visibleValue != course.visible) {
                uploadCourse(it)
            } else {
                Toast.makeText(context, "Nothing to change!", Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun init() {
        et_uploadCourse_name.setText(course.name)
        if (course.visible!!) {
            rb_uploadCourse_true.isChecked = true
        } else {
            rb_uploadCourse_false.isChecked = true
        }
    }

    private fun uploadCourse(view: View) {
        pb_uploadCourse.isVisible = true
        btn_uploadCourse.isEnabled = false
        val name = et_uploadCourse_name.text.toString()
        val visible = rb_uploadCourse_true.isChecked
        val c = Course(name, visible)
        db.collection(CollectionName.course).document(course.id!!)
            .set(c)
            .addOnSuccessListener {
                Navigation.findNavController(view)
                    .navigate(R.id.action_editCourseFragment_to_nav_course)
            }
            .addOnFailureListener { e ->
                Log.w("Kyawthiha", "Error adding document", e)
            }

    }
}