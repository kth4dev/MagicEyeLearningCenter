package com.magiceye.admin.ui.course

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.magiceye.admin.R


class FetchingUrlDialog(context: Context) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_fetching_url)

    }
}