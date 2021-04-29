package com.example.cat1


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.example.cat1.databinding.ActivityMainBinding
import com.example.cat1.models.Confirmdialog
import com.example.cat1.models.LoadingDialog
import com.google.android.material.navigation.NavigationView
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var name: String
    private lateinit var phone: String
    private lateinit var email: String
   // lateinit var theresult: String
    val alldata= arrayListOf<Array<String>>()
    val bookfragmentwithdata = BookFragment()
    val homefragmentwithdata = HomeFragment()
    lateinit var headername: TextView
    lateinit var headerphone: TextView
    lateinit var headeremail: TextView
    lateinit var allthree: ArrayList<String>
    lateinit var get_all_tickets_online: MyThirdAsyncTask
    private val SHARED_PREFS = "sharedPrefs"
    lateinit private var loginstatus: String
    lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        lateinit var header1: View
        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        loginstatus =sharedPreferences.getString("phone","x").toString()
        val loadingDialog= LoadingDialog(this)
        loadingDialog.startloadingdialog()
        if(loginstatus == "x"){
            name = "name"
            phone = "phone"
            email = "email"
            tologin()
        }else{
            name = sharedPreferences.getString("name","x").toString()
            phone = loginstatus
            email = sharedPreferences.getString("email","x").toString()
            get_all_tickets_online = MyThirdAsyncTask(phone)
            get_all_tickets_online.execute()
        }


        binding.navView.setNavigationItemSelectedListener(this)

        setSupportActionBar(binding.toolbar)
        var toggle: ActionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
         header1 = binding.navView.getHeaderView(0)
         headername = header1.findViewById<TextView>(R.id.headername)
         headerphone = header1.findViewById<TextView>(R.id.headerphone)
         headeremail = header1.findViewById<TextView>(R.id.headeremail)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
         headername!!.text = name
        headerphone!!.text = phone
        headeremail!!.text = email
        //allthree = arrayOf(name, phone, email)
        allthree = arrayListOf(name, phone, email)

        val bundlehome = Bundle()
        bundlehome.putString("phonenumber", phone)
        Log.d("try", "Am here")
        if(loginstatus != "x"){
        if(get_all_tickets_online.stopasynctask == true){
            Log.d("try", "Finished is true")
            bundlehome.putString("resultstring", get_all_tickets_online.resulte)
        }else{
            Log.d("try", "Finished is not true")
            while (get_all_tickets_online.stopasynctask == false){
                Log.d("try", "Stuck here")
            }
            if(get_all_tickets_online.stopasynctask == true){
                Log.d("try", "Escaped from while loop")
                bundlehome.putString("resultstring", get_all_tickets_online.resulte)
                get_all_tickets_online.cancel(true)
            }
        }
            homefragmentwithdata.setArguments(bundlehome)
        }
        loadingDialog.dismissdialog()


        supportActionBar?.title = "Home"
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, homefragmentwithdata, "FRAGMENT_HOME_DATA").commit()

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                supportActionBar?.title = "Home"
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, homefragmentwithdata, "FRAGMENT_HOME_DATA").commit()
            }
            R.id.nav_book -> {
                //temp code
                val bundle = Bundle()
                bundle.putString("resultstring", get_all_tickets_online.resultd)
               // bundle.putStringArray("allthree", allthree)
                bundle.putStringArrayList("allthree", allthree)
                bookfragmentwithdata.setArguments(bundle)
                //stop here
                supportActionBar?.title = "Book Ticket"
                supportFragmentManager.beginTransaction().replace(
                    R.id.fragment_container,
                    bookfragmentwithdata, "FRAGMENT_CHOOSE_DATE"
                ).commit()
            }
            R.id.nav_my_profile -> {

                supportActionBar?.title = "My Profile"
                supportFragmentManager.beginTransaction().replace(
                    R.id.fragment_container,
                    MyProfileFragment()
                ).commit()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true;
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)){
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        var inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_settings -> {
                Toast.makeText(applicationContext, "You clicked on Settings", Toast.LENGTH_SHORT)
                    .show()
                return true
            }
            R.id.menu_about -> {
                supportActionBar?.title = "About"
                supportFragmentManager.beginTransaction().replace(
                    R.id.fragment_container,
                    AboutFragment()
                ).commit()
                return true
            }
            R.id.menu_logout -> {
                val confirmdialog = Confirmdialog(this, "logout", sharedPreferences.edit())
                confirmdialog.startdialog()
                return true
            }
            R.id.menu_delacc -> {
                val loadingDialog= LoadingDialog(this)
                val deleteaccount = DeleteAsyncTask(sharedPreferences.edit(), loadingDialog, this, phone, this)
                val confirmdialog = Confirmdialog(deleteaccount, this, "deleteaccount", sharedPreferences.edit())
                confirmdialog.startdialog()
                return true
            }
            R.id.menu_Quit -> {
                val confirmdialog = Confirmdialog(this, "quit")
                confirmdialog.startdialog()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
   /* fun FillingUserData(){
       // var bundle: Bundle?= intent.extras

        /*var headername = findViewById<TextView>(R.id.headername)
        var headerphone = findViewById<TextView>(R.id.headerphone)
        var headeremail = findViewById<TextView>(R.id.headeremail) */

       // headername.text = bundle!!.getString("name")
       // headerphone.text = bundle!!.getString("phone")
       // headeremail.text = bundle!!.getString("email")

       // headername.text = "One"
      //  headerphone.text ="Two"
        //headeremail.text ="Three"
   // (findViewById<TextView>(R.id.headerphone)).text = "One"
     //   (findViewById<TextView>(R.id.headerphone)).text = "Two"
      //  (findViewById<TextView>(R.id.headerphone)).text = "Three"

    }*/
   companion object {
       class MyThirdAsyncTask internal constructor(thephonenumber: String) :
           AsyncTask<String, String, String>() {
           val thephonenumber = thephonenumber
           lateinit var con: HttpURLConnection
           lateinit var resultc: String
           lateinit var resultd: String
           lateinit var resulte: String
           var stopasynctask = false

           // lateinit var alldata: ArrayList<Array<String>>
           val alldata = arrayListOf<Array<String>>()
           val builder = Uri.Builder()
           /*override fun onPreExecute() {
               super.onPreExecute()

               //  builder.appendQueryParameter("key", "oooo")
           } */
           override fun onPreExecute() {
               super.onPreExecute()
           builder.appendQueryParameter("phone", thephonenumber)
       }
           override fun doInBackground(vararg params: String?): String? {
               try {
                     var query = builder.build().encodedQuery
                   var url = "https://cadential-collar.000webhostapp.com/Sgrticket/myticketsandbooktickets.php"
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
                   Log.e("Fail 1", e.toString())
               }
               try {
                   resultc = con.inputStream.bufferedReader().readText()
                   Log.e("try","Result c is "+ resultc)
                   var spliter =resultc.split("][")
                   val a = ']'
                   val b = '['
                   val c = spliter[0]
                   val d = spliter[1]
                    resultd = "$c$a"
                   resulte = "$b$d"

                   //val sb = StringBuilder()
                  // sb.append(a).append(b)
                   //cut from here
                   Log.d("value", "Result c is (all of it)"+ resultc)
                   Log.d("value", "Result d is (cropped one)"+resultd)
                   Log.d("value", "Result e is (cropped one)"+resulte)
                   stopasynctask =true
                       Log.d("try", "It is cancelled. No one is supposed to see this 241")

               } catch (e: java.lang.Exception) {
                   Log.e("Fail 2", e.toString())
               }
               return null
           }



       }

   }
        class DeleteAsyncTask internal constructor(editor: SharedPreferences.Editor, loadingDialog: LoadingDialog, context: Context, phonenumber: String, activity: Activity) :
            AsyncTask<String, String, String>() {
            lateinit var con: HttpURLConnection
            lateinit var resulta: String
            val builder = Uri.Builder()
            private val cont: Context = context
            private val thephonenumber: String = phonenumber
            private val activity = activity
            private val loadingDialog = loadingDialog
            private val editor = editor
            override fun onPreExecute() {
                super.onPreExecute()
                loadingDialog.startloadingdialog()
                builder.appendQueryParameter("phone", thephonenumber)
            }

            override fun doInBackground(vararg params: String?): String? {
                try {
                    var query = builder.build().encodedQuery
                    var url = "https://cadential-collar.000webhostapp.com/Sgrticket/delete.php"
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
                    Log.e("Fail 1", e.toString())
                }
                try {
                    resulta = con.inputStream.bufferedReader().readText()
                } catch (e: java.lang.Exception) {
                    Log.e("Fail 2", e.toString())
                }

                return null
            }

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)

                var json_data = JSONObject(resulta)
                val code: Int = json_data.getInt("code")
                if (code == 1) {

                    editor.clear().commit()

                   /* val toLogin = Intent(cont, LoginActivity::class.java)
                    toLogin.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                    cont.startActivity(toLogin)
                    activity.finish()
                    */
                    (activity as MainActivity?)!!.tologin()
                }
                //  loadingDialog.dismissdialog()
            }
        }
    fun reloadactivity(name: String, phone: String, email: String){
        val toMain = Intent(applicationContext, MainActivity::class.java)
        toMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        toMain.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
       // toMain.putExtra("name", name)
       // toMain.putExtra("phone", phone)
      //  toMain.putExtra("email", email)
        startActivity(toMain)
        this.finish()
    }

    fun tologin(){
        val toLogin = Intent(applicationContext, LoginActivity::class.java)
        toLogin.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
        startActivity(toLogin)
        this.finish()
    }




}