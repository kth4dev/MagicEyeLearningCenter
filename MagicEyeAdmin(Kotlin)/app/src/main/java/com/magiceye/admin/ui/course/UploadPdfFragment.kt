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
import com.magiceye.admin.helper.CollectionName
import com.magiceye.admin.helper.FireStore
import com.magiceye.admin.R
import com.magiceye.admin.model.Content
import com.magiceye.admin.model.Course
import com.magiceye.admin.model.Pdf
import kotlinx.android.synthetic.main.fragment_upload_video.*
import java.util.*


class UploadPdfFragment : Fragment() {
    private lateinit var videoUri: Uri
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
        et_uploadVideo_name.hint="Pdf Name"
        et_uploadVideo_link.hint="Pdf Link"


        btn_uploadVideo.setOnClickListener{
            if (et_uploadVideo_name.text.toString() != "" && rg_uploadVideo.checkedRadioButtonId != -1) {
                progressDialog = ProgressDialog(context)
                progressDialog.setTitle("Uploading...")
                progressDialog.show()
                uploadData(Pdf(et_uploadVideo_name.text.toString(), rb_uploadVideo_true.isChecked,et_uploadVideo_link.text.toString()))
            } else {
                Toast.makeText(context, "Please Fill Completely!", Toast.LENGTH_LONG).show()
            }

        }

    }


    private fun uploadData(video: Pdf){
        course.id?.let { courseId->
            content.id?.let { contentId ->
                FireStore.instance().collection(CollectionName.pdfByContent).document(courseId).collection(
                    contentId
                )
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
                        Toast.makeText(context, "Uploaded!", Toast.LENGTH_LONG).show()
                    }
                    .addOnFailureListener { e ->
                        progressDialog.dismiss()
                        Toast.makeText(context, "Fail!", Toast.LENGTH_LONG).show()

                    }
            }
        }
    }
/*

    private fun uploadVideo(vName: String, visible: Boolean) {
        if (videoUri != null) {
             progressDialog = ProgressDialog(context)
            progressDialog.setTitle("Uploading...")
            progressDialog.show()

            val firebaseStorage = FirebaseStorage.getInstance()
            val storage = firebaseStorage.reference.child(course.id!!).child(content.id!!).child("pdf")

            val ref: StorageReference = storage.child("/" + UUID.randomUUID().toString())
            ref.putFile(videoUri)
                .addOnSuccessListener(OnSuccessListener<Any?> {
                    ref.downloadUrl.addOnSuccessListener(OnSuccessListener<Uri> { uri ->
                        val video = Video(vName, visible, uri.toString())
                        uploadData(video)
                    })
                        .addOnFailureListener(OnFailureListener { e ->
                            progressDialog.dismiss()
                            Toast.makeText(context, "Failed " + e.message, Toast.LENGTH_SHORT)
                                .show()
                        })
                })
                .addOnProgressListener { taskSnapshot ->
                    val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot
                        .totalByteCount
                    progressDialog.setMessage("Uploaded " + progress.toInt() + "%")
                }

        }else{
            Toast.makeText(context, "Please Fill Completely!", Toast.LENGTH_LONG).show()
        }
    } // end upload products

*/

}