package com.magiceye.admin.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.magiceye.admin.helper.CollectionName
import com.magiceye.admin.helper.FireStore
import com.magiceye.admin.R
import com.magiceye.admin.model.Course
import com.magiceye.admin.model.Student


class StudentListAdapter(private val course: Course,private val context: Context) :
    RecyclerView.Adapter<StudentListAdapter.MyViewHolder>() {
    var studentList = ArrayList<Student>()

    fun clear(){
        this.studentList.clear()
        notifyDataSetChanged()
    }

    fun setData(list: ArrayList<Student>) {
        this.studentList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_student, parent, false)
        )
    }


    override fun getItemCount(): Int {
        return studentList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val student = studentList[position]
        holder.setText(student)
        holder.delete.setOnClickListener{
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
    }
    fun delete(position: Int){

        FireStore.instance().collection(CollectionName.dataByCourse).document(course.id!!).collection(
            CollectionName.student).document(studentList[position].phone!!)
            .delete()
            .addOnSuccessListener {
                FireStore.instance().collection(CollectionName.courseByStudent).document(studentList[position].phone!!).collection(
                    CollectionName.course).document(course.id!!)
                    .delete()
                    .addOnSuccessListener {
                        studentList.removeAt(position)
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
            .addOnFailureListener { e ->
                Toast.makeText(
                    context,
                    "Error deleting document$e",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }




    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.student_name)
        val phone: TextView = itemView.findViewById(R.id.student_phone)
        val delete: ImageView = itemView.findViewById(R.id.iv_student_menu)
        fun setText(student: Student) {
            name.text = student.name
            phone.text = student.phone

        }

    }



}




