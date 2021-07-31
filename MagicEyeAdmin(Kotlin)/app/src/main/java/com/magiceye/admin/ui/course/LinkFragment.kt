package com.magiceye.admin.ui.course

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.magiceye.admin.helper.CollectionName
import com.magiceye.admin.R
import com.magiceye.admin.adapter.LinkListAdapter
import com.magiceye.admin.model.Content
import com.magiceye.admin.model.Course
import com.magiceye.admin.model.Link
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
        btn_add_link.setOnClickListener{
            val bundle = Bundle()
            bundle.putSerializable(CollectionName.bCourse,course)
            bundle.putSerializable(CollectionName.bContent,content)
            Navigation.findNavController(it).navigate(R.id.action_linkFragment_to_uploadLinkFragment,bundle)
        }
        linkViewModel.linkList.observe(viewLifecycleOwner, Observer { k ->
            setUpLinkList(view, k)
        })
    }

    private fun setUpLinkList(view: View, list: ArrayList<Link>) {
        pb_link.visibility=View.GONE
        val linkListAdapter =  LinkListAdapter(list,course,content, view.context)
            rv_link.adapter = linkListAdapter
            rv_link.layoutManager = LinearLayoutManager(context)


    }

}