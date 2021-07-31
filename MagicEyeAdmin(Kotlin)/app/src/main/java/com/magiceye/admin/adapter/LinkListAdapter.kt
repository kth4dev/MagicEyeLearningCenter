package com.magiceye.admin.adapter


import android.content.Context
import android.content.Intent
import android.net.Uri
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
import com.magiceye.admin.model.Link
import java.lang.Exception


class LinkListAdapter(
    private var linkList: ArrayList<Link>, private val course: Course,
    private val content: Content,
    private val context: Context
) :
    RecyclerView.Adapter<LinkListAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_link, parent, false)
        )
    }


    override fun getItemCount(): Int {
        return linkList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.name.text = linkList[position].name
        holder.link.text = linkList[position].link
        if (linkList[position].visible!!) {
            holder.indicator.setImageDrawable(context.resources.getDrawable(R.drawable.ic_course_active))
        } else {
            holder.indicator.setImageDrawable(context.resources.getDrawable(R.drawable.ic_course_disable))
        }
        holder.itemView.setOnClickListener {
            try {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(linkList[position].link))
                context.startActivity(browserIntent)
            }catch (e:Exception){
                Toast.makeText(context,"Can't Open, Please check your link!",Toast.LENGTH_LONG).show()
            }

        }
        holder.menu.setOnClickListener {
            showPopUpMenu(holder.menu, linkList[position], position)
        }
    }

    private fun showPopUpMenu(view: View, link: Link, int: Int) {
        val popupMenu = PopupMenu(context, view)
        popupMenu.inflate(R.menu.edit)
        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->
            when (item!!.itemId) {
                R.id.action_edit -> {
                    val bundle = Bundle()
                    bundle.putSerializable(CollectionName.bCourse, course)
                    bundle.putSerializable(CollectionName.bContent, content)
                    bundle.putSerializable(CollectionName.bLink, link)
                    Navigation.findNavController(view).navigate(
                        R.id.action_linkFragment_to_editLinkFragment,
                        bundle
                    )
                }
                R.id.action_delete -> {
                    content.id?.let { contentID ->
                        course.id?.let { courseID ->
                            link.id?.let { linkID ->
                                FireStore.instance().collection(CollectionName.linkByContent)
                                    .document(
                                        courseID
                                    ).collection(contentID).document(linkID)
                                    .delete()
                                    .addOnSuccessListener {
                                        linkList.removeAt(int)
                                        notifyDataSetChanged()
                                        Toast.makeText(context, "Deleted!", Toast.LENGTH_SHORT)
                                            .show()
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
            }
            true
        })
        popupMenu.show()
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tv_course_name)
        val link: TextView = itemView.findViewById(R.id.tv_course_link)
        val indicator: ImageView = itemView.findViewById(R.id.iv_course_indicator)
        val menu: ImageView = itemView.findViewById(R.id.iv_course_menu)
        val main: LinearLayout = itemView.findViewById(R.id.ll_course_main)
    }

}




