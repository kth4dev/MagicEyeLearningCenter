package com.magiceye.learningcenter.ui.mycourse

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.magiceye.admin.ui.course.PdfViewModel
import com.magiceye.learningcenter.R
import com.magiceye.learningcenter.adapter.PdfListAdapter
import com.magiceye.learningcenter.helper.CollectionName
import com.magiceye.learningcenter.model.Content
import com.magiceye.learningcenter.model.Course
import com.magiceye.learningcenter.model.Pdf
import kotlinx.android.synthetic.main.fragment_pdf.*


class PdfFragment : Fragment() {
    lateinit var course: Course
    lateinit var content: Content
    private lateinit var pdfViewModel: PdfViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pdfViewModel = ViewModelProviders.of(this).get(PdfViewModel::class.java)
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
        pdfViewModel.getData(course,content)
        return inflater.inflate(R.layout.fragment_pdf, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_pdf_path.text="${course.name} / ${content.name}"
        pdfViewModel.pdfList.observe(viewLifecycleOwner, Observer { k ->
            setUpPdfList(view, k)
        })
    }
    private fun setUpPdfList(view: View, list: ArrayList<Pdf>) {
        pb_pdf.visibility=View.GONE
        activity?.let {
            val pdfListAdapter =  PdfListAdapter(list,view.context)
            rv_pdf.adapter = pdfListAdapter
            rv_pdf.layoutManager = LinearLayoutManager(context)
        }

    }


}