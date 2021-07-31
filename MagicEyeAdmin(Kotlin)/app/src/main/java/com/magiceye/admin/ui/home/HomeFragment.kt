package com.magiceye.admin.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.QuerySnapshot
import com.magiceye.admin.helper.CollectionName
import com.magiceye.admin.helper.FireStore
import com.magiceye.admin.R
import com.magiceye.admin.adapter.StudentListAdapter
import com.magiceye.admin.helper.MSharePreference
import com.magiceye.admin.model.Course
import com.magiceye.admin.model.Student
import kotlinx.android.synthetic.main.fragment_admin.*
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        homeViewModel.getData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.courseList.observe(viewLifecycleOwner, Observer { courseList ->
            homeViewModel.courseNameList.observe(viewLifecycleOwner, Observer { courseNameList ->
                if (courseNameList != null) {
                    setUpSpinner(view.context, courseList, courseNameList)
                }

            })
        })
        if(MSharePreference.isAdmin(context)){
            fab_goto_registration.visibility=View.VISIBLE
            fab_goto_registration.setOnClickListener {
                Navigation.findNavController(it).navigate(R.id.action_nav_home_to_student_registration)
            }
        }else{
            fab_goto_registration.visibility=View.GONE
        }


    }

    private fun setUpSpinner(
        c: Context,
        courseList: ArrayList<Course>,
        courseNameList: ArrayList<String>
    ) {
        val aryAdapter = ArrayAdapter<String>(
            c,
            android.R.layout.simple_spinner_dropdown_item,
            courseNameList
        )
        spinner_home_course.adapter = aryAdapter
        spinner_home_course.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                courseList[position]?.let {
                    it.id?.let { it1 ->
                        FireStore.instance().collection(CollectionName.dataByCourse).document(it1)
                            .collection(
                                CollectionName.student
                            ).get()
                            .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                                if (task.isSuccessful) {
                                    var list = ArrayList<Student>()
                                    for (document in task.result!!) {
                                        val sID: Student = document.toObject(Student::class.java)
                                        if (sID != null) {
                                            list.add(sID)
                                        }
                                    }
                                    val studentListAdapter= StudentListAdapter(courseList[position],c)
                                    rv_student.adapter = studentListAdapter
                                    rv_student.layoutManager = LinearLayoutManager(c)
                                    studentListAdapter.setData(list)
                                }
                            })
                    }
                }

            }
        }
    }

}


