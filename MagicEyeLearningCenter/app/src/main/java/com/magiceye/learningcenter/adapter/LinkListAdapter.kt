package com.magiceye.learningcenter.adapter


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
import com.magiceye.learningcenter.R
import com.magiceye.learningcenter.model.Content
import com.magiceye.learningcenter.model.Course
import com.magiceye.learningcenter.model.Link

import java.lang.Exception


class LinkListAdapter(
    private var linkList: ArrayList<Link>,
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
        holder.itemView.setOnClickListener {
            try {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://"+linkList[position].link))
                context.startActivity(browserIntent)
            }catch (e:Exception){
                Toast.makeText(context,"Can't Open, Please check your link!",Toast.LENGTH_LONG).show()
            }

        }

    }




    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.link_name)
        val link: TextView = itemView.findViewById(R.id.link_url)
    }

}




