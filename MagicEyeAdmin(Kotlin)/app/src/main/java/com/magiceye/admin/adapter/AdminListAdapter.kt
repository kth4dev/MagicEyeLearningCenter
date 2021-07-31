package com.magiceye.admin.adapter


import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.magiceye.admin.helper.CollectionName
import com.magiceye.admin.helper.FireStore
import com.magiceye.admin.R
import com.magiceye.admin.helper.MSharePreference
import com.magiceye.admin.model.Admin


class AdminListAdapter(private var adminList: ArrayList<Admin>, private val context: Context) :
    RecyclerView.Adapter<AdminListAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_admin, parent, false)
        )
    }


    override fun getItemCount(): Int {
        return adminList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.name.text = adminList[position].name
        holder.phone.text = adminList[position].phone

        if (adminList[position].admin!!) {
            holder.indicator.setImageDrawable(context.resources.getDrawable(R.drawable.ic_admin))
        } else {
            holder.indicator.setImageDrawable(context.resources.getDrawable(R.drawable.ic_teacher))
        }
        if(MSharePreference.isAdmin(context)){
            holder.menu.visibility=View.VISIBLE
            holder.menu.setOnClickListener{
                val message = "Are you sure you want to delete?"
                val alertDialogBuilder = AlertDialog.Builder(context)
                alertDialogBuilder.setTitle("Delete")
                alertDialogBuilder.setMessage(message)
                alertDialogBuilder.setPositiveButton(android.R.string.yes) { _, _ ->
                    delete(position)
                }
                alertDialogBuilder.setNegativeButton(android.R.string.no) { _, _ ->

                }
                alertDialogBuilder.create()
                alertDialogBuilder.show()
            }
        }else{
            holder.menu.visibility=View.GONE
        }



    }


    private fun delete(int: Int) {

                    adminList[int].id?.let {
                        FireStore.instance().collection(CollectionName.admin).document(it)
                            .delete()
                            .addOnSuccessListener {
                                adminList.removeAt(int)
                                notifyDataSetChanged()
                                Toast.makeText(context, "Deleted!", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(
                                    context,
                                    "Error deleting document$e",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }}

    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.admin_name)
        val phone: TextView = itemView.findViewById(R.id.admin_phone)
        val indicator: ImageView = itemView.findViewById(R.id.iv_adminRegistration_indicator)
        val menu: ImageView = itemView.findViewById(R.id.iv_admin_menu)
    }

}




