package com.example.cat1.adapters
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.cat1.MainActivity
import com.example.cat1.R
import com.example.cat1.models.LoadingDialog
import org.w3c.dom.Text
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class BookTicketsAdapter: RecyclerView.Adapter<BookTicketsAdapter.ViewHolder> {
    var thearray = arrayListOf<Array<String>>()
    lateinit var allthree: Array<String?>
    var activity: FragmentActivity? = null

    constructor(thearray: ArrayList<Array<String>>, allthree: Array<String?>, activity: FragmentActivity?){
        this.thearray = thearray
        this.allthree = allthree
        this.activity = activity
    }

//save the context recievied via constructor in a local variable

    //save the context recievied via constructor in a local variable
    /*constructor(context: Context) {
        this.context = context
    }

     */


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        var view = layoutInflater.inflate(R.layout.book_tickets_row, parent, false)
        var viewHolder = ViewHolder(view, parent.context, allthree, activity )


        //hold this
        //LayoutInflater.from(parent.context).inflate(R.layout.nav_header, parent)
        var theview = layoutInflater.inflate(R.layout.nav_header, parent, false)
        val thename = (theview.findViewById<TextView>(R.id.headername)).text
        val thephone = (theview.findViewById<TextView>(R.id.headerphone)).text
        val theemail = (theview.findViewById<TextView>(R.id.headeremail)).text


        Log.d("try", "The values we taking from navview are" + thename + theemail + thephone)

        return  viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.source.text = thearray[position][0].toString()
        holder.destination.text = thearray[position][1].toString()
        holder.date.text = thearray[position][2].toString()
        holder.ticketid = thearray[position][3]
        holder.availableseats.text = thearray[position][4].toString()
    }

    override fun getItemCount(): Int {
        return thearray.size
    }

    class ViewHolder : RecyclerView.ViewHolder, View.OnClickListener  {
        lateinit var ticketid: String
        lateinit var source: TextView
        lateinit var destination: TextView
        lateinit var date: TextView
        lateinit var availableseats: TextView
        lateinit var context: Context
        lateinit var allthree: Array<String?>
        var activity:FragmentActivity? = null

        constructor(@NonNull itemView: View, context: Context, allthree: Array<String?>, activity: FragmentActivity?) : super(itemView) {
            this.activity = activity
            this.context = context
            this.allthree = allthree
            source = itemView.findViewById(R.id.cbsource)
            destination = itemView.findViewById(R.id.cbdestination)
            date = itemView.findViewById(R.id.cbdate)
            availableseats = itemView.findViewById(R.id.availableseats)
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            //Book here
            val loadingDialog= LoadingDialog(activity!!)
            loadingDialog.startloadingdialog()
            val bookit = BookThatTicket(loadingDialog,context, ticketid, allthree, activity)
            bookit.execute()
        }


    }

    companion object {
        class BookThatTicket internal constructor(
            loadingDialog: LoadingDialog,
            context: Context,
            ticketnumber: String,
            allthree: Array<String?>,
            activity: FragmentActivity?
        ) :
            AsyncTask<String, String, String>() {

            lateinit var con: HttpURLConnection
            lateinit var resulta: String
            val builder = Uri.Builder()
            private val cont: Context = context
            private  val ticketnumber: String = ticketnumber
            private val allthree: Array<String?> = allthree
            private val activity: FragmentActivity? = activity
            private val loadingDialog = loadingDialog
            override fun onPreExecute() {
                super.onPreExecute()

               // val progressBar = ProgressBar(cont)
              //  progressBar.isIndeterminate = true
              //  progressBar.visibility = View.VISIBLE
                builder.appendQueryParameter("ticketnumber", ticketnumber)
                builder.appendQueryParameter("phone", allthree[1])
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
                    //Use the same instance of the main activity
                    (activity as MainActivity?)!!.reloadactivity(allthree[0]!!, allthree[1]!!, allthree[2]!!)
                    /*
                    This is on hold
                    val refresher = MainActivity.Companion.MyThirdAsyncTask(thephonenumber)
                    refresher.execute()
                    Thread.sleep(8000)

                    //From here
                    val homefragmentwithdata = HomeFragment()
                    val thesolution = ( cont as AppCompatActivity).supportFragmentManager
                    val bundlebackhome = Bundle()
                    bundlebackhome.putString("resultstring", refresher.resulte)
                    bundlebackhome.putString("phonenumber", thephonenumber)
                    homefragmentwithdata.setArguments(bundlebackhome)


                    thesolution.beginTransaction().replace(R.id.fragment_container, homefragmentwithdata, "FRAGMENT_HOME_DATA").commit()

                    we putting this on hold
                    */


                    //Till here
                }
                //loadingDialog.dismissdialog()
            }
        }

    }

}
