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
import com.magiceye.admin.adapter.PdfListAdapter
import com.magiceye.admin.model.Content
import com.magiceye.admin.model.Course
import com.magiceye.admin.model.Pdf
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
        btn_add_pdf.setOnClickListener{
            val bundle = Bundle()
            bundle.putSerializable(CollectionName.bCourse,course)
            bundle.putSerializable(CollectionName.bContent,content)
            Navigation.findNavController(it).navigate(R.id.action_pdfFragment_to_uploadPdfFragment,bundle)
        }
        pdfViewModel.pdfList.observe(viewLifecycleOwner, Observer { k ->
            setUpPdfList(view, k)
        })
    }
    private fun setUpPdfList(view: View, list: ArrayList<Pdf>) {
        pb_pdf.visibility=View.GONE
        activity?.let {
            val pdfListAdapter =  PdfListAdapter(list,course,content, view.context, it)
            rv_pdf.adapter = pdfListAdapter
            rv_pdf.layoutManager = LinearLayoutManager(context)
        }

    }


}