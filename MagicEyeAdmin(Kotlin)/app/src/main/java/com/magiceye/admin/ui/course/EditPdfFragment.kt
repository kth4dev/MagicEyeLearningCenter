package com.magiceye.admin.ui.course

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.magiceye.admin.helper.CollectionName
import com.magiceye.admin.helper.FireStore
import com.magiceye.admin.R
import com.magiceye.admin.model.Content
import com.magiceye.admin.model.Course
import com.magiceye.admin.model.Pdf
import com.magiceye.admin.model.Video
import kotlinx.android.synthetic.main.fragment_upload_video.*


class EditPdfFragment : Fragment() {
    lateinit var course:Course
    lateinit var content:Content
    lateinit var pdf:Pdf
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        pdf = if (savedInstanceState != null) {
            savedInstanceState.getSerializable(CollectionName.bPdf) as Pdf
        } else {
            requireArguments().getSerializable(CollectionName.bPdf) as Pdf
        }
        return inflater.inflate(R.layout.fragment_upload_video, container, false)
    }



        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            init()
            super.onViewCreated(view, savedInstanceState)
            btn_uploadVideo.setOnClickListener {
                val visibleValue= rb_uploadVideo_true.isChecked
                if (et_uploadVideo_name.text.toString() !=pdf.name || visibleValue!=pdf.visible) {
                    uploadVideo(it)
                } else {
                    Toast.makeText(context, "Nothing to change!", Toast.LENGTH_LONG).show()
                }
            }
        }


    private fun init() {
        tv_video_path.text="${course.name} / ${content.name}"

        et_uploadVideo_name.setText(pdf.name)
        et_uploadVideo_name.hint="Pdf Name"
        et_uploadVideo_link.setText(pdf.url)
        et_uploadVideo_link.hint="Pdf Link"
        if(pdf.visible!!){
            rb_uploadVideo_true.isChecked=true
        }else{
            rb_uploadVideo_false.isChecked=true
        }
        btn_uploadVideo.text="Save"
    }
    private fun uploadVideo(view: View) {
        val progressDialog = ProgressDialog(context)
        progressDialog.setTitle("Uploading...")
        progressDialog.show()
        progressDialog.setCancelable(false)
        btn_uploadVideo.isEnabled=false
        val name=et_uploadVideo_name.text.toString()
        val url=et_uploadVideo_link.text.toString()
        val visible= rb_uploadVideo_true.isChecked
        val toUploadVideo = Video(name , visible,url)


        course.id?.let {
            FireStore.instance().collection(CollectionName.pdfByContent).document(it).collection(content.id!!).document(pdf.id!!)
                .set(toUploadVideo)
                .addOnSuccessListener { documentReference ->
                    progressDialog.dismiss()
                    val bundle = Bundle()
                    bundle.putSerializable(CollectionName.bCourse, course)
                    bundle.putSerializable(CollectionName.bContent, content)
                    Navigation.findNavController(view).navigate(R.id.action_editPdfFragment_to_pdfFragment,bundle)
                }
                .addOnFailureListener { e ->
                    progressDialog.dismiss()
                    Toast.makeText(context,"Fail!",Toast.LENGTH_LONG).show()
                    Log.w("Kyawthiha", "Error adding document", e)
                }
        }

    }
}