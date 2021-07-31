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
import com.magiceye.learningcenter.adapter.VideoListAdapter
import com.magiceye.learningcenter.helper.CollectionName
import com.magiceye.learningcenter.model.Content
import com.magiceye.learningcenter.model.Course
import com.magiceye.learningcenter.model.Video
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
        videoViewModel.videoList.observe(viewLifecycleOwner, Observer { k ->
            setUpVideoList(view, k)
        })
    }
    private fun setUpVideoList(view: View, list: ArrayList<Video>) {
        pb_video.visibility=View.GONE
       activity?.let {
           val videoListAdapter =  VideoListAdapter(list, view.context, it)
            rv_video.adapter = videoListAdapter
            rv_video.layoutManager = LinearLayoutManager(context)
        }

    }
}