package com.example.cat1.models

import android.app.Activity
import android.app.AlertDialog
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.example.cat1.MainActivity
import com.example.cat1.R
import kotlin.coroutines.coroutineContext
import kotlin.system.exitProcess

class Confirmdialog {
    lateinit var dialog: AlertDialog
     private var thetask: String
     private var activity: Activity
    private var editor: SharedPreferences.Editor?
    private var deleteaccount: MainActivity.DeleteAsyncTask?

    constructor(activity: Activity, thetask: String){
        this.activity = activity
        this.thetask = thetask
        deleteaccount = null
        editor = null
    }
    constructor(activity: Activity,thetask: String,editor: SharedPreferences.Editor){
        this.activity = activity
        this.thetask = thetask
        this.editor = editor
        deleteaccount = null
    }
    constructor(deleteaccount: MainActivity.DeleteAsyncTask, activity: Activity, thetask: String, editor: SharedPreferences.Editor){
        this.editor = editor
        this.activity = activity
        this.thetask = thetask
        this.deleteaccount = deleteaccount
    }

    fun startdialog(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        val inflater: LayoutInflater = activity.layoutInflater
        val viewadded = inflater.inflate(R.layout.confirmdialog, null)
        builder.setView(viewadded)
        builder.setCancelable(true)
       // val theviewer = inflater.inflate(R.layout.confirmdialog, null)
        val nobutton = viewadded.findViewById<Button>(R.id.no)
        val yesbutton = viewadded.findViewById<Button>(R.id.yes)
        val themessage = viewadded.findViewById<TextView>(R.id.textView2)
        if(thetask == "quit"){
            themessage.text = "Are you sure you want to quit the app?"
        }else{
            if(thetask == "logout") {
                themessage.text = "Are you sure you want to logout from your account?"
            }else{
            if(thetask =="deleteaccount"){
               themessage.text = "Are you sure you want to delete your account? This action is not reversible"
            }
            }
            }
        dialog = builder.create()
        dialog.show()
        nobutton.setOnClickListener {
            Log.d("try", "He pressed no")
            dialog.dismiss()
        }

        yesbutton.setOnClickListener {
            Log.d("try", "He pressed yes")
            if (thetask == "quit"){
            activity.finish()
            exitProcess(0)
            }else{
            if (thetask == "logout"){
                editor?.clear()?.commit()
                (activity as MainActivity?)!!.tologin()
            }else{
                if (thetask == "deleteaccount"){
                    deleteaccount?.execute()
                }
            }}
        }
    }
}