package com.magiceye.admin.ui.course

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.magiceye.admin.R
import com.magiceye.admin.adapter.CourseListAdapter
import com.magiceye.admin.model.Course
import kotlinx.android.synthetic.main.fragment_course.*


class CourseFragment : Fragment() {
    private lateinit var courseViewModel: CourseViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        courseViewModel = ViewModelProviders.of(this).get(CourseViewModel::class.java)
        courseViewModel.getData()
        return inflater.inflate(R.layout.fragment_course, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_add_course.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_nav_course_to_uploadCourseFragment)
        }
        courseViewModel.courseList.observe(viewLifecycleOwner, Observer { k ->
            setUpCourseList(view, k)
        })

    }

    private fun setUpCourseList(view: View, list: ArrayList<Course>) {
        pb_course.visibility=View.GONE
        val courseListAdapter = CourseListAdapter(list, view.context)
        rv_course.adapter = courseListAdapter
        rv_course.layoutManager = LinearLayoutManager(context)
    }


}