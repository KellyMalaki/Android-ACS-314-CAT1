package com.example.cat1.models

import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater
import android.widget.TextView
import com.example.cat1.R

class ErrorDialog {
    lateinit var dialog: AlertDialog
    lateinit var activity: Activity
    lateinit var thetask: String
    constructor(activity: Activity, thetask: String){
        this.activity = activity
        this.thetask = thetask
    }
    fun displayerror(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        val inflater: LayoutInflater = activity.layoutInflater
        val viewadded = inflater.inflate(R.layout.error_dialog, null)
        builder.setView(viewadded)
        builder.setCancelable(true)
        val themessage = viewadded.findViewById<TextView>(R.id.theerror)
        if(thetask == "emptyfield"){
            themessage.text = "Not all fields have been filled"
        }else{
            if(thetask == "name"){
                themessage.text = "Name input is too short. It should be three characters and above"
            }else{
            if(thetask=="email"){
                themessage.text = "The email inserted is not correct"
            }else{
                if (thetask == "phone") {
                    themessage.text = "Insert the correct phone number"
                }else{
                    if (thetask == "password") {
                        themessage.text = "Password too short"
                    }else{
                        if (thetask == "internet"){
                            themessage.text = "An error occurred with the Internet"
                        }else{
                            if (thetask == "server"){
                                themessage.text = "A server side error occurred"
                            }else{
                                if (thetask == "nologin"){
                                    themessage.text = "Incorrect Username or password Inserted. Check and try again"
                                }
                            }
                        }
                    }
                }
        }
        }
        }
        dialog = builder.create()
        dialog.show()
    }
}