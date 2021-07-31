package com.magiceye.learningcenter.adapter


import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.htetznaing.xgetter.Model.XModel
import com.htetznaing.xgetter.XGetter
import com.magiceye.learningcenter.R
import com.magiceye.learningcenter.helper.CollectionName
import com.magiceye.learningcenter.model.Video
import com.magiceye.learningcenter.ui.mycourse.VideoViewActivity


class VideoListAdapter(
    private var videoList: ArrayList<Video>,
    private val context: Context,
    private val activity: Activity
) :
    RecyclerView.Adapter<VideoListAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false)
        )
    }


    override fun getItemCount(): Int {
        return videoList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.name.text = videoList[position].name
        holder.itemView.setOnClickListener {
            val progressDialog = ProgressDialog(context)
            try {

                progressDialog.setMessage("Loading...")
                progressDialog.show()
                progressDialog.setCancelable(false)
                val xgetter= XGetter(it.context)
                xgetter.find(videoList[position].url)
                xgetter.onFinish(object : XGetter.OnTaskCompleted {
                    override fun onTaskCompleted(vidURL: ArrayList<XModel>, multiple_quality: Boolean) {
                        Log.i("FILTER_URL",vidURL[0].url.toString())
                        Log.i("FILTER_URL",vidURL[0].quality.toString())
                        Log.i("FILTER_URL_M",multiple_quality.toString())
                        progressDialog.dismiss()
                        val i = Intent(activity, VideoViewActivity::class.java)
                        val bundle = Bundle()
                        bundle.putString(CollectionName.bVideoUrl, vidURL[0].url.toString())

                        i.putExtras(bundle)
                        context.startActivity(i)
                    }

                    override fun onError() {
                        progressDialog.dismiss()
                        showErrorDialog()

                    }
                })
            }catch (e:Exception){
                progressDialog.dismiss()
                showErrorDialog()
            }

        }
    }


    fun showErrorDialog(){
        val alertDialog: AlertDialog = AlertDialog.Builder(context).create() //Read Update
        alertDialog.setTitle("Can't Play")
        alertDialog.setMessage("Sorry, this video can't be played.")
        alertDialog.setButton("OK",
            DialogInterface.OnClickListener { dialog, _ ->
                dialog.dismiss()
            })
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.link_name)
    }

}




