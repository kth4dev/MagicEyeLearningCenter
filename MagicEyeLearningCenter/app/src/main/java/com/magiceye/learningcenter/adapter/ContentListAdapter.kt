package com.magiceye.learningcenter.adapter


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView

import com.magiceye.learningcenter.R
import com.magiceye.learningcenter.helper.CollectionName
import com.magiceye.learningcenter.model.Content
import com.magiceye.learningcenter.model.Course


class ContentListAdapter(private var contentList: ArrayList<Content>, private val course: Course, private val context: Context) :
    RecyclerView.Adapter<ContentListAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_course_content, parent, false)
        )
    }


    override fun getItemCount(): Int {
        return contentList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.name.text = contentList[position].name
        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable(CollectionName.bCourse,course)
            bundle.putSerializable(CollectionName.bContent,contentList[position])
            Navigation.findNavController(it)
                .navigate(R.id.action_contentFragment_to_tutorialFragment









                    ,bundle)
         }

    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.course_content_name)

    }

}




