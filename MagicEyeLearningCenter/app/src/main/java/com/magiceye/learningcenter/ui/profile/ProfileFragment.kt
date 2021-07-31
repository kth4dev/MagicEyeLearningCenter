package com.magiceye.learningcenter.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.magiceye.learningcenter.R
import com.magiceye.learningcenter.helper.CollectionName
import com.magiceye.learningcenter.helper.MSharePreference
import com.magiceye.learningcenter.ui.login.Login
import kotlinx.android.synthetic.main.fragment_notifications.*

class ProfileFragment : Fragment() {
   override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_notifications, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_name.text= MSharePreference.getSavingString(view.context, CollectionName.sName)
        tv_ph.text= MSharePreference.getSavingString(view.context, CollectionName.sPhone)
        btn_logout.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(activity, Login::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }
}