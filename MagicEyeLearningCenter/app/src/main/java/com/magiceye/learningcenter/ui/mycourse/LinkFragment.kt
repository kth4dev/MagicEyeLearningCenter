package com.magiceye.learningcenter.ui.mycourse

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.magiceye.learningcenter.R
import com.magiceye.learningcenter.adapter.LinkListAdapter

import com.magiceye.learningcenter.helper.CollectionName
import com.magiceye.learningcenter.model.Content
import com.magiceye.learningcenter.model.Course
import com.magiceye.learningcenter.model.Link
import kotlinx.android.synthetic.main.fragment_link.*

class LinkFragment : Fragment() {

    lateinit var course: Course
    lateinit var content: Content
    private lateinit var linkViewModel: LinkViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        linkViewModel = ViewModelProviders.of(this).get(LinkViewModel::class.java)
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

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        linkViewModel.getData(course,content)
        return inflater.inflate(R.layout.fragment_link, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_link_path.text="${course.name} / ${content.name}"
        linkViewModel.linkList.observe(viewLifecycleOwner, Observer { k ->
            setUpLinkList(view, k)
        })
    }

    private fun setUpLinkList(view: View, list: ArrayList<Link>) {
        pb_link.visibility=View.GONE
        val linkListAdapter =  LinkListAdapter(list,view.context)
            rv_link.adapter = linkListAdapter
            rv_link.layoutManager = LinearLayoutManager(context)

    }

}