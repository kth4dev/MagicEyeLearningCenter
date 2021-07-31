package com.magiceye.admin.ui.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.magiceye.admin.helper.CollectionName
import com.magiceye.admin.helper.MSharePreference
import com.magiceye.admin.R
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(MSharePreference.isAdmin(view.context)){
            iv_admin.setImageDrawable(view.context.resources.getDrawable(R.drawable.ic_admin))
            tv_admin.text="Admin"
        }else{
            iv_admin.setImageDrawable(view.context.resources.getDrawable(R.drawable.ic_teacher))
            tv_admin.text="Teacher"
        }
        tv_name.text= MSharePreference.getSavingString(view.context, CollectionName.sName)
        tv_ph.text= MSharePreference.getSavingString(view.context, CollectionName.sPhone)
    }
}