package com.magiceye.admin.ui.course

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.magiceye.admin.helper.CollectionName
import com.magiceye.admin.MainActivity
import com.magiceye.admin.R
import com.magiceye.admin.adapter.ContentListAdapter
import com.magiceye.admin.model.Content
import com.magiceye.admin.model.Course
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
        fab_goto_uploadContent.setOnClickListener {
            goToUploadContent(it)
        }
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

    private fun goToUploadContent(it:View){
        val bundle = Bundle()
        bundle.putSerializable(CollectionName.bCourse, course)
        Navigation.findNavController(it).navigate(
            R.id.action_contentFragment_to_uploadContentFragment,
            bundle
        )
    }
}