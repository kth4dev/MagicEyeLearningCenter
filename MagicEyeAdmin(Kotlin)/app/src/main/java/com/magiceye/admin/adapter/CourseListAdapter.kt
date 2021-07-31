package com.magiceye.admin.adapter


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.magiceye.admin.helper.CollectionName
import com.magiceye.admin.helper.FireStore
import com.magiceye.admin.R
import com.magiceye.admin.model.Course


class CourseListAdapter(private var courseList: ArrayList<Course>, private val context: Context) :
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
        holder.name.text = courseList[position].name
        if (courseList[position].visible!!) {
            holder.indicator.setImageDrawable(context.resources.getDrawable(R.drawable.ic_course_active))
        } else {
            holder.indicator.setImageDrawable(context.resources.getDrawable(R.drawable.ic_course_disable))
        }
        holder.main.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable(CollectionName.bCourse, courseList[position])
            Navigation.findNavController(it).navigate(R.id.action_nav_course_to_contentFragment,bundle)
        }
        holder.menu.setOnClickListener {
            showPopUpMenu(holder.menu,courseList[position], position)
        }
    }

    private fun showPopUpMenu(view: View, course: Course, int: Int) {
        val popupMenu = PopupMenu(context, view)
        popupMenu.inflate(R.menu.edit)
        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->
            when (item!!.itemId) {
                R.id.action_edit -> {
                    /*    courseList[int].id?.let {
                        FireStore.instance().collection(CollectionName.course).document(it)
                            .set(Course("Edited",false))
                            .addOnSuccessListener {
                               Toast.makeText(context, "Updated!", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(
                                    context,
                                    "Error deleting document$e",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }*/
                    val bundle = Bundle()
                    bundle.putSerializable(CollectionName.bCourse, course)
                    Navigation.findNavController(view).navigate(R.id.action_nav_course_to_editCourseFragment,bundle)
                }
                R.id.action_delete -> {
                    courseList[int].id?.let {
                        FireStore.instance().collection(CollectionName.course).document(it)
                            .delete()
                            .addOnSuccessListener {
                                courseList.removeAt(int)
                                notifyDataSetChanged()
                                Toast.makeText(context, "Deleted!", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(
                                    context,
                                    "Error deleting document$e",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }


                }
            }
            true
        })
        popupMenu.show()
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tv_course_name)
        val indicator: ImageView = itemView.findViewById(R.id.iv_course_indicator)
        val menu: ImageView = itemView.findViewById(R.id.iv_course_menu)
        val main: LinearLayout = itemView.findViewById(R.id.ll_course_main)
    }

}




