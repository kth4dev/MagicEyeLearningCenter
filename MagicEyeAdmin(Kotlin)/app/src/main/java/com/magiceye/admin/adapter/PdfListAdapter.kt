package com.magiceye.admin.adapter


import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
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
import com.htetznaing.xgetter.Model.XModel
import com.htetznaing.xgetter.XGetter
import com.magiceye.admin.*
import com.magiceye.admin.helper.CollectionName
import com.magiceye.admin.helper.FireStore
import com.magiceye.admin.model.Content
import com.magiceye.admin.model.Course
import com.magiceye.admin.model.Pdf



class PdfListAdapter(
    private var pdfList: ArrayList<Pdf>,
    private val course: Course,
    private val content: Content,
    private val context: Context,
    private val activity: Activity
) :
    RecyclerView.Adapter<PdfListAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_content, parent, false)
        )
    }


    override fun getItemCount(): Int {
        return pdfList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.name.text = pdfList[position].name
        if (pdfList[position].visible!!) {
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
                xgetter.find(pdfList[position].url)
                xgetter.onFinish(object : XGetter.OnTaskCompleted {
                    override fun onTaskCompleted(vidURL: ArrayList<XModel>, multiple_quality: Boolean) {
                        progressDialog.dismiss()
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.setDataAndType(Uri.parse(vidURL[0].url), "application/pdf")
                        intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                        context.startActivity(intent)
                    }

                    override fun onError() {
                        progressDialog.dismiss()
                      showErrorDialog()

                    }
                })

            } catch (e: Exception) {
                progressDialog.dismiss()
                showErrorDialog()
            }
        }
        holder.menu.setOnClickListener {
            showPopUpMenu(holder.menu, pdfList[position], position)
        }
    }

    fun showErrorDialog(){
        val alertDialog: AlertDialog = AlertDialog.Builder(context).create() //Read Update
        alertDialog.setTitle("Can't Display")
        alertDialog.setMessage("Sorry, this pdf can't be displayed.")
        alertDialog.setButton("OK",
            DialogInterface.OnClickListener { dialog, _ ->
                dialog.dismiss()
            })
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun showPopUpMenu(view: View, pdf: Pdf, int: Int) {
        val popupMenu = PopupMenu(context, view)
        popupMenu.inflate(R.menu.edit)
        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->
            when (item!!.itemId) {
                R.id.action_edit -> {
                    val bundle = Bundle()
                    bundle.putSerializable(CollectionName.bCourse, course)
                    bundle.putSerializable(CollectionName.bContent, content)
                    bundle.putSerializable(CollectionName.bPdf, pdf)
                    Navigation.findNavController(view).navigate(
                        R.id.action_pdfFragment_to_editPdfFragment,
                        bundle
                    )
                }
                R.id.action_delete -> {
                    val progressDialog = ProgressDialog(context)
                    progressDialog.setTitle("Deleting")
                    progressDialog.setMessage("Loading...")
                    progressDialog.show()
                    progressDialog.setCancelable(false)
                    content.id?.let { contentID ->
                        course.id?.let { courseID ->
                            pdf.id?.let { pdfID ->
                                FireStore.instance().collection(CollectionName.pdfByContent)
                                    .document(
                                        courseID
                                    ).collection(contentID).document(pdfID)
                                    .delete()
                                    .addOnSuccessListener {
                                        pdfList.removeAt(int)
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




