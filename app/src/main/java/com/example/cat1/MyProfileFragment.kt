package com.example.cat1

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.cat1.databinding.MyProfileFragmentBinding
import com.example.cat1.models.ErrorDialog
import com.example.cat1.models.LoadingDialog
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class MyProfileFragment : Fragment() {
    private var _binding: MyProfileFragmentBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MyProfileFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        //code starts here
        val sharedPreferences = activity?.getSharedPreferences(
            "sharedPrefs",
            AppCompatActivity.MODE_PRIVATE
        )
        binding.headname.text =sharedPreferences?.getString("name", "x").toString()
        binding.txtname.setText(sharedPreferences?.getString("name", "x").toString())
        binding.txtphonenumber.setText(sharedPreferences?.getString("phone", "x").toString())
        binding.txtemail.setText(sharedPreferences?.getString("email", "x").toString())


        binding.btnupdate.setOnClickListener {
            val regex = Regex("^[+]?[0-9]{10,13}$")
            if((binding.txtname.text.toString() != sharedPreferences?.getString("name", "x").toString()) || (binding.txtphonenumber.text.toString() != sharedPreferences?.getString("phone", "x").toString()) || (binding.txtemail.text.toString() != sharedPreferences?.getString("email", "x").toString())){
            if(!(binding.txtname.text.toString().isNullOrEmpty()) && !(binding.txtphonenumber.text.toString().isNullOrEmpty())  && !(binding.txtemail.text.toString().isNullOrEmpty())){
                if(binding.txtname.text.toString().count() >= 3){
                    if(android.util.Patterns.EMAIL_ADDRESS.matcher(binding.txtemail.text.toString()).matches()){
                        if(regex.containsMatchIn(input = binding.txtphonenumber.text.toString())){
                            //change it here it is ok
                            //check if the number exists on another account
                            //first change the databese online
                            //then change sharedpreference
                            //then restart activity
                            val loadingDialog= LoadingDialog((activity as MainActivity?)!!)
                            loadingDialog.startloadingdialog()
                            //val sharedPreferences: SharedPreferences = getSharedPreferences(SHARED_PREFS, AppCompatActivity.MODE_PRIVATE)
                            val editor = sharedPreferences?.edit()
                            val update = MyAsyncTask((activity as MainActivity?)!!, editor!!, loadingDialog, binding.txtname.text.toString(), binding.txtphonenumber.text.toString(), binding.txtemail.text.toString(), sharedPreferences?.getString("phone", "x").toString())
                            update.execute()
                            Log.d("try", "Its ok I will do it")


                        }else{
                            //Phone number not correct
                            val errorDialog = ErrorDialog((activity as MainActivity?)!!, "phone")
                            errorDialog.displayerror()
                        }
                    }else{
                        //Email not correct
                        val errorDialog = ErrorDialog((activity as MainActivity?)!!, "email")
                        errorDialog.displayerror()
                    }}else{
                    //Name too short
                    val errorDialog = ErrorDialog((activity as MainActivity?)!!, "name")
                    errorDialog.displayerror()
                }
            }else{
                //Some field is left empty
                val errorDialog = ErrorDialog((activity as MainActivity?)!!, "emptyfield")
                errorDialog.displayerror()
        }
            }else{
                //No Changes made
                val errorDialog = ErrorDialog((activity as MainActivity?)!!, "nochanges")
                errorDialog.displayerror()
            }
        }

        //code stops here
        return view
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        class MyAsyncTask internal constructor(activity: Activity, editor: SharedPreferences.Editor, loadingDialog: LoadingDialog, name: String, phonenumber: String, email: String, lastphone: String) :
            AsyncTask<String, String, String>() {
            private var activity: Activity = activity
            lateinit var con: HttpURLConnection
            lateinit var resulta: String
            val builder = Uri.Builder()
            private  val thename: String = name
            private val thephonenumber: String = phonenumber
            private val theemail: String = email
            private val loadingDialog = loadingDialog
            private val editor = editor
            private val lastphone = lastphone
            override fun onPreExecute() {
                super.onPreExecute()

                builder.appendQueryParameter("name", thename)
                builder.appendQueryParameter("phone", thephonenumber)
                builder.appendQueryParameter("email", theemail)
                builder.appendQueryParameter("lastphone", lastphone)
            }

            override fun doInBackground(vararg params: String?): String? {
                try {
                    var query = builder.build().encodedQuery
                    var url = "https://cadential-collar.000webhostapp.com/Sgrticket/update.php"

                    val obj = URL(url)
                    con = obj.openConnection() as HttpURLConnection
                    con.setRequestMethod("POST")
                    con.setRequestProperty(
                        "User-Agent",
                        "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)"
                    )
                    con.setRequestProperty("Accept-Language", "UTF-8")
                    con.setDoOutput(true)
                    val outputStreamWriter = OutputStreamWriter(con.getOutputStream())
                    outputStreamWriter.write(query)
                    outputStreamWriter.flush()
                    Log.e("pass 1", "connection success ")
                } catch (e: Exception) {
                    //Log.e("Fail 1", e.toString())
                    val errorDialog = ErrorDialog(activity , "internet")
                    errorDialog.displayerror()
                    loadingDialog.dismissdialog()
                    this.cancel(true)
                }
                try {
                    resulta = con.inputStream.bufferedReader().readText()
                    Log.e("data", resulta)
                } catch (e: java.lang.Exception) {
                    val errorDialog = ErrorDialog(activity , "server")
                    errorDialog.displayerror()
                    //Log.e("Fail 2", e.toString())
                    loadingDialog.dismissdialog()
                    this.cancel(true)
                }
                return null
            }



            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)

                var json_data = JSONObject(resulta)
                val code: Int = json_data.getInt("code")
//To be implemented later  val name: String = json_data.getString("name")
                Log.d("data", "The code is = " + code.toString())
                //Log.d("data", "The name is = " + name)
                //Log.e("data is...", code.toString())
                if (code == 1) {

                    editor.putString("name", thename)
                    editor.putString("phone", thephonenumber)
                    editor.putString("email", theemail)
                    editor.apply()

                    (activity as MainActivity?)!!.reloadactivity(thename, thephonenumber, theemail)
                }else if (code == 3){
                    val errorDialog = ErrorDialog(activity , "phoneused")
                    errorDialog.displayerror()
                    loadingDialog.dismissdialog()
                    this.cancel(true)
                }
                //  loadingDialog.dismissdialog()
            }
        }

    }
}