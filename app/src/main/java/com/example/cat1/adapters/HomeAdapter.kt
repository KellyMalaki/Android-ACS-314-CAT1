package com.example.cat1.adapters

import android.content.Context
import android.net.Uri
import android.os.AsyncTask
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.cat1.HomeFragment
import com.example.cat1.R
import org.w3c.dom.Text
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class HomeAdapter: RecyclerView.Adapter<HomeAdapter.ViewHolder>  {
    var thearray = arrayListOf<Array<String>>()
    lateinit var phonenumber: String

    constructor(thearray: ArrayList<Array<String>>, phonenumber: String){
        this.thearray = thearray
        this.phonenumber = phonenumber
    }

//save the context recievied via constructor in a local variable

    //save the context recievied via constructor in a local variable
    /*constructor(context: Context) {
        this.context = context
    }

     */


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        var view = layoutInflater.inflate(R.layout.home_display_row, parent, false)
        var viewHolder = ViewHolder(view, phonenumber)
        return  viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.source.text = thearray[position][1].toString()
        holder.destination.text = thearray[position][2].toString()
        holder.date.text = thearray[position][3].toString()
       holder.bookeddate.text = thearray[position][4].toString()
       holder.ticketid.text = thearray[position][0].toString()
    }

    override fun getItemCount(): Int {
        return thearray.size
    }

    class ViewHolder : RecyclerView.ViewHolder{
        lateinit var bookeddate: TextView
        lateinit var ticketid: TextView
        lateinit var source: TextView
        lateinit var destination: TextView
        lateinit var date: TextView
        lateinit var context: Context
        lateinit var phonenumber: String

        constructor(@NonNull itemView: View, phonenumber: String) : super(itemView) {
           // this.context = context
            this.phonenumber = phonenumber
            source = itemView.findViewById(R.id.cbsource)
            destination = itemView.findViewById(R.id.cbdestination)
            date = itemView.findViewById(R.id.cbdate)
            bookeddate = itemView.findViewById(R.id.bookingdate)
            ticketid = itemView.findViewById(R.id.ticketid)
        }


    }

    companion object {
        class BookThatTicket internal constructor(context: Context, ticketnumber: String, phonenumber: String) :
            AsyncTask<String, String, String>() {

            lateinit var con: HttpURLConnection
            lateinit var resulta: String
            val builder = Uri.Builder()
            private val cont: Context = context
            private  val ticketnumber: String = ticketnumber
            private val thephonenumber: String = phonenumber
            override fun onPreExecute() {
                super.onPreExecute()

                // val progressBar = ProgressBar(cont)
                //  progressBar.isIndeterminate = true
                //  progressBar.visibility = View.VISIBLE
                builder.appendQueryParameter("ticketnumber", ticketnumber)
                builder.appendQueryParameter("phone", thephonenumber)
            }

            override fun doInBackground(vararg params: String?): String? {
                try {
                    var query = builder.build().encodedQuery
                    var url = "https://cadential-collar.000webhostapp.com/Sgrticket/bookaticket.php"

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
                    Log.e("data", resulta)
                } catch (e: java.lang.Exception) {
                    Log.e("Fail 2", e.toString())
                }

                return null
            }

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)


                val code: Int = resulta.toInt()

                Log.d("try", "The code is = " + code.toString())
                //Log.d("data", "The name is = " + name)
                //Log.e("data is...", code.toString())
                if (code == 1) {
                    Log.d("try", "It returned one as the code meaning it added to database")
                    //Go to Home fragment
                    val thesolution = ( cont as AppCompatActivity).supportFragmentManager
                    thesolution.beginTransaction().replace(
                        R.id.fragment_container,
                        HomeFragment()
                    ).commit()
                }
            }
        }

    }
}