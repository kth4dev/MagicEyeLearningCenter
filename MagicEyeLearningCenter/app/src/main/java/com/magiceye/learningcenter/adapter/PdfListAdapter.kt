package com.magiceye.learningcenter.adapter



import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.htetznaing.xgetter.Model.XModel
import com.htetznaing.xgetter.XGetter
import com.magiceye.learningcenter.R
import com.magiceye.learningcenter.model.Pdf


class PdfListAdapter(
    private var pdfList: ArrayList<Pdf>,
    private val context: Context
) :
    RecyclerView.Adapter<PdfListAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false)
        )
    }


    override fun getItemCount(): Int {
        return pdfList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.name.text = pdfList[position].name
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



    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.link_name)
    }

}




