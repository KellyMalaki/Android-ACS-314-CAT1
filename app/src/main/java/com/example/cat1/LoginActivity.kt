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
import com.example.cat1.models.ErrorDialog
import com.example.cat1.models.LoadingDialog
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val SHARED_PREFS = "sharedPrefs"
    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.title = "Log In"
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnLoginLogin.setOnClickListener {
            val regex = Regex("^[+]?[0-9]{10,13}$")
            if(!(binding.txtloginphonenumber.text.toString().isNullOrEmpty()) && !(binding.txtloginpassword.text.toString().isNullOrEmpty())){
            if(regex.containsMatchIn(input = binding.txtloginphonenumber.text.toString())){
                if(binding.txtloginpassword.text.toString().count() >= 6){
            val loadingDialog= LoadingDialog(this)
            loadingDialog.startloadingdialog()
            val sharedPreferences: SharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            val login = MyAsyncTask(this, editor, loadingDialog, this, binding.txtloginphonenumber.text.toString(), binding.txtloginpassword.text.toString())
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
                //Some field is left empty
                val errorDialog = ErrorDialog(this, "emptyfield")
                errorDialog.displayerror()

            }
        }


        binding.btnLoginSignUp.setOnClickListener {
            val enter_signup = Intent(applicationContext, SignUpActivity::class.java)
            startActivity(enter_signup)
        }
    }

    companion object {
        class MyAsyncTask internal constructor(activity: Activity,editor: SharedPreferences.Editor, loadingDialog: LoadingDialog, context: Context, phonenumber: String, password: String) :
            AsyncTask<String, String, String>() {
            private var activity: Activity= activity
            lateinit var con: HttpURLConnection
            lateinit var resulta: String
            val builder = Uri.Builder()
            private val cont: Context = context
            private val thephonenumber: String = phonenumber
            private val thepassword: String = password
            private val loadingDialog = loadingDialog
            private val editor = editor
            override fun onPreExecute() {
                super.onPreExecute()
                builder.appendQueryParameter("phone", thephonenumber)
              builder.appendQueryParameter("password", thepassword)
            }

            override fun doInBackground(vararg params: String?): String? {
                try {
                    var query = builder.build().encodedQuery
                    var url = "https://cadential-collar.000webhostapp.com/Sgrticket/login.php"
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
                    val errorDialog = ErrorDialog(activity , "internet")
                    errorDialog.displayerror()
                    //Log.e("Fail 1", e.toString())
                    loadingDialog.dismissdialog()
                    this.cancel(true)
                }
                try {
                    resulta = con.inputStream.bufferedReader().readText()
                } catch (e: java.lang.Exception) {
                    val errorDialog = ErrorDialog(activity , "server")
                    //errorDialog.displayerror()
                    //Log.e("Fail 2", e.toString())
                    loadingDialog.dismissdialog()
                    this.cancel(true)
                }

                return null
            }

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
               // progressBar.visibility = View.GONE

                var json_data = JSONObject(resulta)
                val code: Int = json_data.getInt("code")
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
                   // toMain.putExtra("name", name)
                   // toMain.putExtra("phone", phone)
                   // toMain.putExtra("email", email)
                    cont.startActivity(toMain)
                }else{
                    val errorDialog = ErrorDialog(activity , "nologin")
                    errorDialog.displayerror()
                    loadingDialog.dismissdialog()
                    this.cancel(true)
                }
              //  loadingDialog.dismissdialog()
            }
        }

    }
}
