package com.magiceye.admin.adapter


import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.htetznaing.xgetter.Model.XModel
import com.htetznaing.xgetter.XGetter
import com.magiceye.admin.*
import com.magiceye.admin.helper.CollectionName
import com.magiceye.admin.helper.FireStore
import com.magiceye.admin.model.Content
import com.magiceye.admin.model.Course
import com.magiceye.admin.model.Video
import com.magiceye.admin.ui.course.VideoViewActivity


class VideoListAdapter(
    private var videoList: ArrayList<Video>,
    private val course: Course,
    private val content: Content,
    private val context: Context,
    private val activity: Activity
) :
    RecyclerView.Adapter<VideoListAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_content, parent, false)
        )
    }


    override fun getItemCount(): Int {
        return videoList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.name.text = videoList[position].name
        if (videoList[position].visible!!) {
            holder.indicator.setBackgroundColor(context.resources.getColor(R.color.colorPrimary))
        } else {
            holder.indicator.setBackgroundColor(context.resources.getColor(R.color.red))
        }
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
        holder.menu.setOnClickListener {
            showPopUpMenu(holder.menu, videoList[position], position)
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

    private fun showPopUpMenu(view: View, video: Video, position: Int) {
        val popupMenu = PopupMenu(context, view)
        popupMenu.inflate(R.menu.edit)
        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->
            when (item!!.itemId) {
                R.id.action_edit -> {
                    val bundle = Bundle()
                    bundle.putSerializable(CollectionName.bCourse, course)
                    bundle.putSerializable(CollectionName.bContent, content)
                    bundle.putSerializable(CollectionName.bVideo, video)
                    Navigation.findNavController(view).navigate(
                        R.id.action_videoFragment_to_editVideoFragment,
                        bundle
                    )
                }
                R.id.action_delete -> {
                    deleteVideo(video,position)
                }
            }
            true
        })
        popupMenu.show()
    }

    fun deleteVideo(video: Video, int: Int) {
        val progressDialog = ProgressDialog(context)
        progressDialog.setTitle("Deleting")
        progressDialog.setMessage("Loading...")
        progressDialog.show()
        progressDialog.setCancelable(false)

        content.id?.let { contentID ->
            course.id?.let { courseID ->
                video.id?.let { videoID ->
                    FireStore.instance().collection(CollectionName.videoByContent)
                        .document(
                            courseID
                        ).collection(contentID).document(videoID)
                        .delete()
                        .addOnSuccessListener {
                            videoList.removeAt(int)
                            notifyDataSetChanged()
                            Toast.makeText(context, "Deleted!", Toast.LENGTH_SHORT)
                                .show()
                            progressDialog.dismiss()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(
                                context,
                                "Error deleting document$e",
                                Toast.LENGTH_SHORT
                            ).show()
                            progressDialog.dismiss()
                        }
                }
            }
        }
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tv_content_name)
        val indicator: View = itemView.findViewById(R.id.view_content_indicator)
        val menu: ImageView = itemView.findViewById(R.id.iv_content_menu)
        val main: LinearLayout = itemView.findViewById(R.id.ll_content_main)
    }

}




