package com.example.cat1

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import com.example.cat1.databinding.ActivityLoginBinding
import com.example.cat1.databinding.ActivityMainBinding
import com.example.cat1.databinding.ActivitySignUpBinding
import com.example.cat1.models.ErrorDialog
import com.example.cat1.models.LoadingDialog
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class SignUpActivity : AppCompatActivity() {
    private val SHARED_PREFS = "sharedPrefs"
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.title = "Sign Up"
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnsignuplogin.setOnClickListener {
            val enterlogin = Intent (applicationContext, LoginActivity::class.java)
            startActivity(enterlogin)
        }

        binding.btnsignupsignup.setOnClickListener {

           // if ((binding.txtname.text.toString() != null) and ((binding.txtphonenumber.text.toString()).length >= 10) and (binding.txtpassword.text.toString() != null)) {
            val regex = Regex("^[+]?[0-9]{10,13}$")
            if(!(binding.txtname.text.toString().isNullOrEmpty()) && !(binding.txtphonenumber.text.toString().isNullOrEmpty()) &&!(binding.txtpassword.text.toString().isNullOrEmpty()) && !(binding.txtemail.text.toString().isNullOrEmpty())){
                if(binding.txtname.text.toString().count() >= 3){
                    if(android.util.Patterns.EMAIL_ADDRESS.matcher(binding.txtemail.text.toString()).matches()){
                if(regex.containsMatchIn(input = binding.txtphonenumber.text.toString())){
                    if(binding.txtpassword.text.toString().count() >= 6){
                val loadingDialog= LoadingDialog(this)
                loadingDialog.startloadingdialog()
                val sharedPreferences: SharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                val login = MyAsyncTask(this,editor,loadingDialog,this, binding.txtname.text.toString(), binding.txtphonenumber.text.toString(),binding.txtemail.text.toString(), binding.txtpassword.text.toString())
                login.execute()
                    }else{
                        //Password too short
                        val errorDialog = ErrorDialog(this, "password")
                        errorDialog.displayerror()
                    }
                }else{
                    //Phone number not correct
                    val errorDialog = ErrorDialog(this, "phone")
                    errorDialog.displayerror()
                }
                }else{
                        //Email not correct
                        val errorDialog = ErrorDialog(this, "email")
                        errorDialog.displayerror()
                }}else{
                    //Name too short
                    val errorDialog = ErrorDialog(this, "name")
                    errorDialog.displayerror()
                }
            }else{
                //Some field is left empty
                val errorDialog = ErrorDialog(this, "emptyfield")
                errorDialog.displayerror()

            }
            //}
        }
    }


    companion object {
        class MyAsyncTask internal constructor(activity: Activity, editor: SharedPreferences.Editor, loadingDialog: LoadingDialog, context: Context, name: String, phonenumber: String, email: String, password: String) :
            AsyncTask<String, String, String>() {
            private var activity: Activity= activity
            lateinit var con: HttpURLConnection
            lateinit var resulta: String
            val builder = Uri.Builder()
            private val cont: Context = context
            private  val thename: String = name
            private val thephonenumber: String = phonenumber
            private val theemail: String = email
            private val thepassword: String = password
            private val loadingDialog = loadingDialog
            private val editor = editor
            override fun onPreExecute() {
                super.onPreExecute()
                builder.appendQueryParameter("name", thename)
                builder.appendQueryParameter("phone", thephonenumber)
                builder.appendQueryParameter("email", theemail)
                builder.appendQueryParameter("password", thepassword)

            }

            override fun doInBackground(vararg params: String?): String? {
                try {
                    var query = builder.build().encodedQuery
                    var url = "https://cadential-collar.000webhostapp.com/Sgrticket/signin.php"

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


                /*fun postdata(){
                    //fun onPostExecute(result: String?) {
                    //super.onPostExecute(result)

                    var json_data = JSONObject(resulta)
                    val code: Int = json_data.getInt("code")
                    Log.d("data", "The code is = " + code.toString())
                    //Log.e("data is...", code.toString())
                    if (code == 1) {
//                        val com: JSONArray = json_data.getJSONArray("userdetails")
//                        val comObject = com[0] as JSONObject
//                        Log.e("data",""+comObject.optString("fname"))
                        val toMain = Intent(cont, MainActivity::class.java)
                        cont.startActivity(toMain)
                    }
                }*/

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
                    val name: String = json_data.getString("name")
                    val phone: String = json_data.getString("phone")
                    val email: String = json_data.getString("email")
                    editor.putString("name", name)
                    editor.putString("phone", phone)
                    editor.putString("email", email)
                    editor.apply()

                    val toMain = Intent(cont, MainActivity::class.java)
                    //val toMain = Intent(cont, MainActivity::class.java)
                  //  toMain.putExtra("name", name)
                   // toMain.putExtra("phone", phone)
                   // toMain.putExtra("email", email)
                    cont.startActivity(toMain)
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
