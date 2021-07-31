package com.magiceye.admin.ui.course

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.magiceye.admin.helper.CollectionName
import com.magiceye.admin.MainActivity
import com.magiceye.admin.R
import com.magiceye.admin.model.Content
import com.magiceye.admin.model.Course
import kotlinx.android.synthetic.main.fragment_choose.*


class ChooseFragment : Fragment() {
    lateinit var course: Course
    lateinit var content: Content
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        return inflater.inflate(R.layout.fragment_choose, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity?)?.supportActionBar?.title = content.name
        ll_video.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable(CollectionName.bCourse,course)
            bundle.putSerializable(CollectionName.bContent,content)
            Navigation.findNavController(it)
                .navigate(R.id.action_chooseFragment_to_videoFragment,bundle)

        }
        ll_link.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable(CollectionName.bCourse,course)
            bundle.putSerializable(CollectionName.bContent,content)
            Navigation.findNavController(it)
                .navigate(R.id.action_chooseFragment_to_linkFragment,bundle)
        }

        ll_pdf.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable(CollectionName.bCourse,course)
            bundle.putSerializable(CollectionName.bContent,content)
            Navigation.findNavController(it)
                .navigate(R.id.action_chooseFragment_to_pdfFragment,bundle)

        }
    }
}