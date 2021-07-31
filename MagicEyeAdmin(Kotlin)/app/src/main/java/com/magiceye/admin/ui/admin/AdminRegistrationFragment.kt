package com.magiceye.admin.ui.admin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.magiceye.admin.helper.CollectionName
import com.magiceye.admin.helper.FireStore
import com.magiceye.admin.R
import com.magiceye.admin.model.Admin
import kotlinx.android.synthetic.main.fragment_admin_registration.*


class AdminRegistrationFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_admin_registration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_admin_register.setOnClickListener {
            if (et_adminRegistration_name.text.toString() != "" && et_adminRegistration_phone.text.toString() != "" && rg_admin.checkedRadioButtonId != -1) {
                adminRegistration()
            } else {
                Toast.makeText(context, "Please Fill Completely!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun adminRegistration() {
        btn_admin_register.isEnabled=false
        val name=et_adminRegistration_name.text.toString()
        val ph=et_adminRegistration_phone.text.toString()
        val isAdmin= rb_admin.isChecked
        val admin = Admin(name ,ph, isAdmin)


        FireStore.instance().collection(CollectionName.admin)
            .add(admin)
            .addOnSuccessListener { documentReference ->
                btn_admin_register.isEnabled=false
                et_adminRegistration_name.setText("")
                et_adminRegistration_phone.setText("")
                if(isAdmin){
                    rb_admin.isChecked=false
                }else{
                    rb_teacher.isChecked=false
                }
              }
            .addOnFailureListener { e ->
                Log.w("Kyawthiha", "Error adding document", e)
            }

    }
}