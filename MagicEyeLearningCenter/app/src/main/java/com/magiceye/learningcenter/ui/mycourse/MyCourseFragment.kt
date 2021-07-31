package com.magiceye.learningcenter.ui.mycourse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.magiceye.learningcenter.helper.CollectionName
import com.magiceye.learningcenter.R
import com.magiceye.learningcenter.adapter.CourseListAdapter
import com.magiceye.learningcenter.helper.MSharePreference
import kotlinx.android.synthetic.main.fragment_mycourse.*

class MyCourseFragment : Fragment() {

    private lateinit var homeViewModel: MyCourseViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProviders.of(this).get(MyCourseViewModel::class.java)
        return inflater.inflate(R.layout.fragment_mycourse, container, false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.getCourseID(MSharePreference.getSavingString(view.context, CollectionName.sPhone))
        homeViewModel.courseIDList.observe(viewLifecycleOwner, Observer { k ->
            setUpCourseList(view, k)
        })
    }

    private fun setUpCourseList(view: View, list: ArrayList<String>) {
        pb_course.visibility=View.GONE
        val courseListAdapter = CourseListAdapter(list, view.context)
        rv_myCourse.adapter = courseListAdapter
        rv_myCourse.layoutManager = LinearLayoutManager(context)
    }
}