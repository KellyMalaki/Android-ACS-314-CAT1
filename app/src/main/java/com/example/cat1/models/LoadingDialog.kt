package com.example.cat1.models

import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater
import com.example.cat1.R

class LoadingDialog {
    lateinit var dialog: AlertDialog
    lateinit var activity: Activity
    constructor(activity: Activity){
        this.activity = activity
    }
    fun startloadingdialog(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        val inflater: LayoutInflater = activity.layoutInflater
        builder.setView(inflater.inflate(R.layout.loading_dialog, null))
        builder.setCancelable(false)
        dialog = builder.create()
        dialog.show()
    }

    fun dismissdialog(){
        dialog.dismiss()
    }
}
