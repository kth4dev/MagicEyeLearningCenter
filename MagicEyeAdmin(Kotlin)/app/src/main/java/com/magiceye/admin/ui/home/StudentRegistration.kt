package com.magiceye.admin.ui.home

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.magiceye.admin.helper.CollectionName
import com.magiceye.admin.helper.FireStore
import com.magiceye.admin.R
import com.magiceye.admin.model.Course
import com.magiceye.admin.model.Student
import kotlinx.android.synthetic.main.fragment_student_registration.*

class StudentRegistration : Fragment() {
    private lateinit var studentRegistrationViewModel: StudentRegistrationViewModel
    lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        studentRegistrationViewModel =
            ViewModelProviders.of(this).get(StudentRegistrationViewModel::class.java)
        studentRegistrationViewModel.getData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_student_registration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        studentRegistrationViewModel.courseList.observe(viewLifecycleOwner, Observer { courseList ->
            studentRegistrationViewModel.courseNameList.observe(
                viewLifecycleOwner,
                Observer { courseNameList ->
                    if (courseNameList != null) {
                        setUpSpinner(view.context, courseList, courseNameList)
                    }
                })
        })
        btn_register.setOnClickListener {
            val name = et_studentRegistration_name.text.toString()
            val phone = et_studentRegistration_phone.text.toString()
            if (name != "" && phone != "") {
                studentRegistrationViewModel.courseList.observe(
                    viewLifecycleOwner,
                    Observer { courseList ->
                        showDialog(
                            name,
                            phone,
                            courseList[spinner_studentRegistration_course.selectedItemPosition],
                            it.context
                        )
                    })
            } else {
                Toast.makeText(it.context, "Please Fill Completely!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showDialog(name: String, phone: String, course: Course, context: Context) {
        val message = "Course: ${course.name}\nName: $name\nPhone: $phone"
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle("Student Info")
        alertDialogBuilder.setMessage(message)
        alertDialogBuilder.setPositiveButton(android.R.string.yes) { _, _ ->
            register(course, Student(name, phone))
        }
        alertDialogBuilder.setNegativeButton(android.R.string.no) { _, _ ->

        }
        alertDialogBuilder.create()
        alertDialogBuilder.show()
    }

    private fun register(course: Course, student: Student) {
         progressDialog = ProgressDialog(context)
        progressDialog.setTitle("Student Registration")
        progressDialog.setMessage("Loading...")
        progressDialog.show()
        progressDialog.setCancelable(false)
        FireStore.instance().collection(CollectionName.student).document(student.phone!!).set(
            student
        )
            .addOnSuccessListener { documentReference ->
                uploadCourseInStudent(course, student)
            }
            .addOnFailureListener { e ->
                Log.w("Kyawthiha", "Error adding document", e)
            }
        uploadCourseInStudent(course, student)
    }

    private fun uploadCourseInStudent(course: Course, student: Student) {
        course.id?.let { courseID->
            val data = hashMapOf("courseId" to courseID)
            FireStore.instance().collection(CollectionName.courseByStudent).document(student.phone!!)
                .collection(CollectionName.course).document(courseID)
                .set(data)
                .addOnSuccessListener { documentReference ->
                    uploadStudentInCourse(course, student)
                }
                .addOnFailureListener { e ->
                    Log.w("Kyawthiha", "Error adding document", e)
                }
        }
    }

    private fun uploadStudentInCourse(course: Course, student: Student) {
        course.id?.let {
            FireStore.instance().collection(CollectionName.dataByCourse).document(it)
                .collection(CollectionName.student).document(student.phone!!).set(student)
                .addOnSuccessListener { documentReference ->
                    et_studentRegistration_phone.setText("")
                    et_studentRegistration_name.setText("")
                    progressDialog.dismiss()
                }
                .addOnFailureListener { e ->
                    Log.w("Kyawthiha", "Error adding document", e)
                }
        }
    }

    private fun setUpSpinner(
        c: Context,
        courseList: ArrayList<Course>?,
        courseNameList: ArrayList<String>
    ) {
        val aryAdapter = ArrayAdapter<String>(
            c,
            android.R.layout.simple_spinner_dropdown_item,
            courseNameList
        )
        spinner_studentRegistration_course.adapter = aryAdapter
    }


}