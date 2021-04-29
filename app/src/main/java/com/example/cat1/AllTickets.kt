package com.example.cat1


import android.net.Uri
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.JsonReader
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.cat1.adapters.AllTicketsAdapter
import com.example.cat1.databinding.ActivityAllTicketsBinding
import com.example.cat1.models.Ticket
import org.json.JSONArray
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class AllTickets : AppCompatActivity() {
    lateinit var AllTicketsAdapter: AllTicketsAdapter
    lateinit var recycleView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = ActivityAllTicketsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        //Ignore this
        //below pass the ticket with how it gets all data
        val one = MySecondAsyncTask()
        one.execute()
        Thread.sleep(1000)
        AllTicketsAdapter = AllTicketsAdapter(one.alldata)
            recycleView = binding.recyclerViewAlltickets
       recycleView.adapter = AllTicketsAdapter
        val dividerdecoration = DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
        recycleView.addItemDecoration(dividerdecoration)


        //To here




    }

    companion object {
        class MySecondAsyncTask internal constructor() :
            AsyncTask<String, String, String>() {
            lateinit var con: HttpURLConnection
            lateinit var resultb: String
          // lateinit var alldata: ArrayList<Array<String>>
             val alldata= arrayListOf<Array<String>>()
            val builder = Uri.Builder()
            /*override fun onPreExecute() {
                super.onPreExecute()

                //  builder.appendQueryParameter("key", "oooo")
            } */

            override fun doInBackground(vararg params: String?): String? {
                try {
                  //  var query = builder.build().encodedQuery
                    var url = "https://cadential-collar.000webhostapp.com/Sgrticket/allticketsscript.php"
                    val obj = URL(url)
                    con = obj.openConnection() as HttpURLConnection
                    con.setRequestMethod("GET")
                    con.setRequestProperty(
                        "User-Agent",
                        "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)"
                    )
                    con.setRequestProperty("Accept-Language", "UTF-8")
                    con.setDoOutput(true)
                    val outputStreamWriter = OutputStreamWriter(con.getOutputStream())
                   // outputStreamWriter.write(query)
                    outputStreamWriter.flush()
                    Log.e("pass 1", "connection success ")
                } catch (e: Exception) {
                    Log.e("Fail 1", e.toString())
                }
                Log.d("data", "Am at AllTickets between two trys")
                try {
                    resultb = con.inputStream.bufferedReader().readText()
                    Log.e("data", resultb)
                } catch (e: java.lang.Exception) {
                    Log.e("Fail 2", e.toString())
                }
                return null
            }

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)

              //  var json_data = JSONArray(resultb)[0]
                //STop here



                val jsonArray = JSONArray(resultb)
                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val name = jsonObject.optString("name")
                    val source = jsonObject.optString("source")
                    val phone = jsonObject.optString("phone")
                    val destination = jsonObject.optString("destination")
                    val ticketno = jsonObject.optString("ticketnumber")
                    val date = jsonObject.optString("tdate&time")
                    val temparray = arrayOf(name, phone, source, destination, ticketno, date)
                    Log.d("data", "One is " + name)
                    alldata.add(temparray)
                }

                //Till here

            }
        }

    }
}