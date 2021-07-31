package com.magiceye.learningcenter.ui.mycourse

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.magiceye.learningcenter.R
import com.magiceye.learningcenter.activity.MainActivity
import com.magiceye.learningcenter.adapter.ContentListAdapter
import com.magiceye.learningcenter.helper.CollectionName
import com.magiceye.learningcenter.model.Content
import com.magiceye.learningcenter.model.Course
import kotlinx.android.synthetic.main.fragment_content.*


class ContentFragment : Fragment() {
    lateinit var course: Course
    private lateinit var contentViewModel: ContentViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        course = if (savedInstanceState != null) {
            savedInstanceState.getSerializable(CollectionName.bCourse) as Course
        } else {
            requireArguments().getSerializable(CollectionName.bCourse) as Course
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        contentViewModel = ViewModelProviders.of(this).get(ContentViewModel::class.java)

        contentViewModel.getData(course)
        return inflater.inflate(R.layout.fragment_content, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity?)?.supportActionBar?.title = course.name

        contentViewModel.contentList.observe(viewLifecycleOwner, Observer { k ->
           setUpCourseList(view, k)

        })

    }

    private fun setUpCourseList(view: View, list: ArrayList<Content>) {
        pb_content.visibility=View.GONE
        val contentListAdapter = ContentListAdapter(list,course, view.context)
        rv_content.adapter = contentListAdapter
        rv_content.layoutManager = LinearLayoutManager(context)
    }


}