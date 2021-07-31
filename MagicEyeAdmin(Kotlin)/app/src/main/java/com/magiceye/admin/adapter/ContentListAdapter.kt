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
import com.magiceye.admin.model.Content
import com.magiceye.admin.model.Course



class ContentListAdapter(private var contentList: ArrayList<Content>,private val course: Course, private val context: Context) :
    RecyclerView.Adapter<ContentListAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_content, parent, false)
        )
    }


    override fun getItemCount(): Int {
        return contentList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.name.text = contentList[position].name
        if (contentList[position].visible!!) {
            holder.indicator.setBackgroundColor(context.resources.getColor(R.color.colorPrimary))
        } else {
            holder.indicator.setBackgroundColor(context.resources.getColor(R.color.red))
        }
        holder.main.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable(CollectionName.bCourse,course)
            bundle.putSerializable(CollectionName.bContent,contentList[position])
            Navigation.findNavController(it)
                .navigate(R.id.action_contentFragment_to_chooseFragment,bundle)
         }
        holder.menu.setOnClickListener {
            showPopUpMenu(holder.menu,contentList[position], position)
        }
    }

    private fun showPopUpMenu(view: View, content: Content, int: Int) {
        val popupMenu = PopupMenu(context, view)
        popupMenu.inflate(R.menu.edit)
        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->
            when (item!!.itemId) {
                R.id.action_edit -> {
                    val bundle = Bundle()
                    bundle.putSerializable(CollectionName.bCourse, course)
                    bundle.putSerializable(CollectionName.bContent, content)
                    Navigation.findNavController(view).navigate(R.id.action_contentFragment_to_editContentFragment,bundle)
                }
                R.id.action_delete -> {
                    contentList[int].id?.let {contentID->
                        course.id?.let { courseID ->
                            FireStore.instance().collection(CollectionName.dataByCourse).document(
                                courseID
                            ).collection(CollectionName.content).document(contentID)
                                .delete()
                                .addOnSuccessListener {
                                    contentList.removeAt(int)
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
            }
            true
        })
        popupMenu.show()
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tv_content_name)
        val indicator: View = itemView.findViewById(R.id.view_content_indicator)
        val menu: ImageView = itemView.findViewById(R.id.iv_content_menu)
        val main: LinearLayout = itemView.findViewById(R.id.ll_content_main)
    }

}




