package com.magiceye.admin.ui.course

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import com.magiceye.admin.helper.CollectionName
import com.magiceye.admin.helper.FireStore
import com.magiceye.admin.R
import com.magiceye.admin.model.Content
import com.magiceye.admin.model.Course
import com.magiceye.admin.model.Link
import kotlinx.android.synthetic.main.fragment_upload_link.*


class EditLinkFragment : Fragment() {
    lateinit var course:Course
    lateinit var content:Content
    lateinit var link:Link
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
        link = if (savedInstanceState != null) {
            savedInstanceState.getSerializable(CollectionName.bLink) as Link
        } else {
            requireArguments().getSerializable(CollectionName.bLink) as Link
        }
        return inflater.inflate(R.layout.fragment_upload_link, container, false)
    }



        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            init()
            super.onViewCreated(view, savedInstanceState)
            btn_uploadLink.setOnClickListener {
                val visibleValue= rb_uploadLink_true.isChecked
                if (et_uploadLink_name.text.toString() !=link.name || et_uploadLink_link.text.toString() !=link.link || visibleValue!=link.visible) {
                    uploadLink(view)
                } else {
                    Toast.makeText(context, "Nothing to change!", Toast.LENGTH_LONG).show()
                }
            }
        }


    private fun init() {
        et_uploadLink_name.setText(link.name)
        et_uploadLink_link.setText(link.link)
        if(link.visible!!){
            rb_uploadLink_true.isChecked=true
        }else{
            rb_uploadLink_false.isChecked=true
        }
    }
    private fun uploadLink(view: View) {
        pb_uploadLink.isVisible=true
        btn_uploadLink.isEnabled=false
        val name=et_uploadLink_name.text.toString()
        val url=et_uploadLink_link.text.toString()
        val visible= rb_uploadLink_true.isChecked
        val editedLink = Link(name,url , visible)
        course.id?.let {courseId->
            content.id?.let { contentId ->
                FireStore.instance().collection(CollectionName.linkByContent).document(courseId).collection(
                    contentId).document(link.id!!)
                    .set(editedLink)
                    .addOnSuccessListener { documentReference ->
                        val bundle = Bundle()
                        bundle.putSerializable(CollectionName.bCourse, course)
                        bundle.putSerializable(CollectionName.bContent, content)
                        Navigation.findNavController(view).navigate(R.id.action_editLinkFragment_to_linkFragment,bundle)
                        Toast.makeText(context,"Uploaded!",Toast.LENGTH_LONG).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(context,"Fail!",Toast.LENGTH_LONG).show()
                        Log.w("Kyawthiha", "Error adding document", e)
                    }
            }
        }

    }
}