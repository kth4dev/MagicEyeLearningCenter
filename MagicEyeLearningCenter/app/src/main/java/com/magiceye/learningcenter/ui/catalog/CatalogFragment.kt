package com.magiceye.learningcenter.ui.catalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.magiceye.learningcenter.R
import com.magiceye.learningcenter.adapter.CatalogListAdapter
import com.magiceye.learningcenter.adapter.CourseListAdapter
import com.magiceye.learningcenter.helper.CollectionName
import com.magiceye.learningcenter.helper.MSharePreference
import com.magiceye.learningcenter.model.Course
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_mycourse.*

class CatalogFragment : Fragment() {

    private lateinit var dashboardViewModel: CatalogViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
                ViewModelProviders.of(this).get(CatalogViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)

        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dashboardViewModel.getData()
        dashboardViewModel.courseList.observe(viewLifecycleOwner, Observer { k ->
            setUpCourseList(view, k)
        })
    }

    private fun setUpCourseList(view: View, list: ArrayList<Course>) {
        pb_catalog.visibility=View.GONE
        val courseListAdapter = CatalogListAdapter(list, view.context)
        rv_catalog.adapter = courseListAdapter
        rv_catalog.layoutManager = LinearLayoutManager(context)
    }
}