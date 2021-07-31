package com.magiceye.admin.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.magiceye.admin.R
import com.magiceye.admin.adapter.AdminListAdapter
import com.magiceye.admin.helper.MSharePreference
import com.magiceye.admin.model.Admin
import kotlinx.android.synthetic.main.fragment_admin.*

class AdminFragment : Fragment() {

    private lateinit var adminViewModel: AdminViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        adminViewModel = ViewModelProviders.of(this).get(AdminViewModel::class.java)
        adminViewModel.getData()
        return inflater.inflate(R.layout.fragment_admin, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(MSharePreference.isAdmin(context)){
            btn_add_admin.visibility=View.VISIBLE
            btn_add_admin.setOnClickListener {
                Navigation.findNavController(it)
                    .navigate(R.id.action_nav_admin_to_adminRegistrationFragment)
            }
        }else{
        btn_add_admin.visibility=View.GONE
        }

        adminViewModel.adminList.observe(viewLifecycleOwner, Observer { k ->
            setUpAdminList(view, k)
        })

    }
    private fun setUpAdminList(view: View, list: ArrayList<Admin>) {
        pb_admin.visibility=View.GONE
        val adminListAdapter = AdminListAdapter(list, view.context)
        rv_admin.adapter = adminListAdapter
        rv_admin.layoutManager = LinearLayoutManager(context)
    }

}