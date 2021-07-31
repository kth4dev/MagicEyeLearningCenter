package com.magiceye.learningcenter.adapter


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.magiceye.learningcenter.helper.CollectionName
import com.magiceye.learningcenter.helper.FireStore
import com.magiceye.learningcenter.model.Course
import com.magiceye.learningcenter.R


class CourseListAdapter(private var courseList: ArrayList<String>, private val context: Context) :
    RecyclerView.Adapter<CourseListAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_course, parent, false)
        )
    }


    override fun getItemCount(): Int {
        return courseList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val id=courseList[position]
        val docRef = FireStore.instance().collection(CollectionName.course).document(id)
        docRef.get()
            .addOnSuccessListener { d ->
                if (d != null) {
                    var course: Course = d.toObject(Course::class.java)!!
                    course.id=id
                    holder.name.text = course.name
                    holder.main.setOnClickListener {
                        val bundle = Bundle()
                        bundle.putSerializable(CollectionName.bCourse,course)
                        Navigation.findNavController(it).navigate(
                            R.id.action_navigation_home_to_contentFragment,
                            bundle
                        )
                    }
                }
            }
            .addOnFailureListener {

            }



    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.course_name)
        val main: RelativeLayout = itemView.findViewById(R.id.rl_course_main)
    }

}




