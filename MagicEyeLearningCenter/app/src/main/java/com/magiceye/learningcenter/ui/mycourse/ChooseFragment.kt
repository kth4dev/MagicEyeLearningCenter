package com.magiceye.learningcenter.ui.mycourse

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.magiceye.learningcenter.R
import com.magiceye.learningcenter.activity.MainActivity
import com.magiceye.learningcenter.helper.CollectionName
import com.magiceye.learningcenter.model.Content
import com.magiceye.learningcenter.model.Course
import kotlinx.android.synthetic.main.fragment_tutorial.*


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
        val view= inflater.inflate(R.layout.fragment_tutorial, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity?)?.supportActionBar?.title = content.name

        btn_choose_link.setOnClickListener{
            val bundle = Bundle()
            bundle.putSerializable(CollectionName.bCourse,course)
            bundle.putSerializable(CollectionName.bContent,content)
            Navigation.findNavController(it).navigate(R.id.action_tutorialFragment_to_linkFragment,bundle)
        }
        btn_choose_pdf.setOnClickListener{
            val bundle = Bundle()
            bundle.putSerializable(CollectionName.bCourse,course)
            bundle.putSerializable(CollectionName.bContent,content)
            Navigation.findNavController(it).navigate(R.id.action_tutorialFragment_to_pdfFragment,bundle)
        }
        btn_choose_video.setOnClickListener{
            val bundle = Bundle()
            bundle.putSerializable(CollectionName.bCourse,course)
            bundle.putSerializable(CollectionName.bContent,content)
            Navigation.findNavController(it).navigate(R.id.action_tutorialFragment_to_videoFragment,bundle)
        }
    }
}