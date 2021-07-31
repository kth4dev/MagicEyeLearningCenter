package com.magiceye.admin.ui.course

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.magiceye.admin.helper.CollectionName
import com.magiceye.admin.helper.FireStore
import com.magiceye.admin.R
import com.magiceye.admin.model.Content
import com.magiceye.admin.model.Course
import com.magiceye.admin.model.Video
import kotlinx.android.synthetic.main.fragment_upload_video.*
import kotlinx.android.synthetic.main.fragment_upload_video.tv_video_path
import java.util.*


class UploadVideoFragment : Fragment() {
    lateinit var course: Course
    lateinit var content: Content
    lateinit var progressDialog:ProgressDialog
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
        return inflater.inflate(R.layout.fragment_upload_video, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_video_path.text="${course.name} / ${content.name}"


        btn_uploadVideo.setOnClickListener{
            if (et_uploadVideo_name.text.toString() != "" && et_uploadVideo_link.text.toString() != "" && rg_uploadVideo.checkedRadioButtonId != -1) {
                progressDialog = ProgressDialog(context)
                progressDialog.setTitle("Uploading...")
                progressDialog.show()
                uploadData(Video(et_uploadVideo_name.text.toString(),rb_uploadVideo_true.isChecked,et_uploadVideo_link.text.toString()))
            } else {
                Toast.makeText(context, "Please Fill Completely!", Toast.LENGTH_LONG).show()
            }

        }

    }



    private fun uploadData(video:Video){
        course.id?.let {courseId->
            content.id?.let { contentId ->
                FireStore.instance().collection(CollectionName.videoByContent).document(courseId).collection(
                    contentId)
                    .add(video)
                    .addOnSuccessListener { documentReference ->
                       et_uploadVideo_name.setText("")
                        et_uploadVideo_link.setText("")
                        if(video.visible == true){
                            rb_uploadVideo_true.isChecked=false
                        }else{
                            rb_uploadVideo_false.isChecked=false
                        }
                        progressDialog.dismiss()
                        Toast.makeText(context,"Uploaded!",Toast.LENGTH_LONG).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(context,"Failed!",Toast.LENGTH_LONG).show()
                        progressDialog.dismiss()
                    }
            }
        }
    }



}