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
import com.magiceye.admin.adapter.VideoListAdapter
import com.magiceye.admin.model.Content
import com.magiceye.admin.model.Course
import com.magiceye.admin.model.Video
import kotlinx.android.synthetic.main.fragment_video.*


class VideoFragment : Fragment() {
    lateinit var course: Course
    lateinit var content: Content
    private lateinit var videoViewModel: VideoViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        videoViewModel = ViewModelProviders.of(this).get(VideoViewModel::class.java)
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
        videoViewModel.getData(course,content)
        return inflater.inflate(R.layout.fragment_video, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_video_path.text="${course.name} / ${content.name}"
        btn_add_video.setOnClickListener{
            val bundle = Bundle()
            bundle.putSerializable(CollectionName.bCourse,course)
            bundle.putSerializable(CollectionName.bContent,content)
            Navigation.findNavController(it).navigate(R.id.action_videoFragment_to_uploadVideoFragment,bundle)
        }
        videoViewModel.videoList.observe(viewLifecycleOwner, Observer { k ->
                setUpVideoList(view, k)

        })
    }
    private fun setUpVideoList(view: View, list: ArrayList<Video>) {
        pb_video.visibility=View.GONE
       activity?.let {
           val videoListAdapter =  VideoListAdapter(list,course,content, view.context, it)
            rv_video.adapter = videoListAdapter
            rv_video.layoutManager = LinearLayoutManager(context)
        }

    }
}