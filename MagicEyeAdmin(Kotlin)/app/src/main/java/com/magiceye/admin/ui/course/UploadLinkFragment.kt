package com.magiceye.admin.ui.course

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import com.magiceye.admin.helper.CollectionName
import com.magiceye.admin.helper.FireStore
import com.magiceye.admin.R
import com.magiceye.admin.model.Content
import com.magiceye.admin.model.Course
import com.magiceye.admin.model.Link
import kotlinx.android.synthetic.main.fragment_upload_link.*


class UploadLinkFragment : Fragment() {
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

        return inflater.inflate(R.layout.fragment_upload_link, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_uploadLink.setOnClickListener {
            if (et_uploadLink_link.text.toString() != "" && et_uploadLink_name.text.toString() != "" && rg_uploadLink.checkedRadioButtonId != -1) {
                uploadLink()
            } else {
                Toast.makeText(context, "Please Fill Completely!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun uploadLink() {
        pb_uploadLink.isVisible=true
        btn_uploadLink.isEnabled=false
        val name=et_uploadLink_name.text.toString()
        val url=et_uploadLink_link.text.toString()
        val visible= rb_uploadLink_true.isChecked
        val link = Link(name ,url, visible)
        course.id?.let {courseId->
            content.id?.let { contentId ->
                FireStore.instance().collection(CollectionName.linkByContent).document(courseId).collection(
                    contentId)
                    .add(link)
                    .addOnSuccessListener { documentReference ->
                        et_uploadLink_name.setText("")
                        pb_uploadLink.isVisible=false
                        if(link.visible == true){
                            rb_uploadLink_true.isChecked=false
                        }else{
                            rb_uploadLink_false.isChecked=false
                        }
                        Toast.makeText(context,"Uploaded!",Toast.LENGTH_LONG).show()
                    }
                    .addOnFailureListener { e ->
                        Log.w("Kyawthiha", "Error adding document", e)
                    }
            }
        }

    }
}