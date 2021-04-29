package com.example.cat1

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.cat1.adapters.BookTicketsAdapter
import com.example.cat1.adapters.DateAdapter
import com.example.cat1.databinding.BookFragmentBinding
import org.json.JSONArray
import org.json.JSONObject


class BookFragment : Fragment() {
    private var _binding: BookFragmentBinding? = null
    private val binding get() = _binding!!
    lateinit var recycleView: RecyclerView
    //lateinit var bookticketsAdapter: BookTicketsAdapter

    lateinit var dateAdapter: DateAdapter
    override fun onCreateView(
       inflater: LayoutInflater,
       container: ViewGroup?,
       savedInstanceState: Bundle?
   ): View? {
        _binding = BookFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        //Where code starts
        Log.d("try","I run book fragment")
        Log.d("find", "Am in book fragment")
        var resultstring = arguments?.get("resultstring").toString()
        Log.d("try", "resultstring is = "+ resultstring)
        var ourdate = arguments?.get("ourdate").toString()
        Log.d("try", "ourdate is = "+ ourdate)
        //var allthree: Array<String> = arguments?.getStringArrayList("allthree").toArray()

        var allthree1 = arguments?.getStringArrayList("allthree")
        val fi = allthree1?.get(0)
        val se = allthree1?.get(1)
        val th = allthree1?.get(2)
        Log.d("try", "the first is = "+ (allthree1?.get(0)))
        val allthree = arrayOf(fi, se, th)
        Log.d("try", "the array is = "+ allthree)
        if (ourdate[0] != '2'){
            Log.d("try","I don't have a date")
            dateAdapter = DateAdapter(getdate(resultstring), resultstring, allthree)
            recycleView = binding.recyclerView
            recycleView.adapter = dateAdapter
            val dividerdecoration = DividerItemDecoration(
                container?.context,
                DividerItemDecoration.VERTICAL
            )
            recycleView.addItemDecoration(dividerdecoration)
        }else {
            Log.d("try","I have a date")
            val thenewstring: TextView = binding.theHeaderId
            thenewstring.text = "Trains available for the date selected"
              var bookticketsAdapter = BookTicketsAdapter(getdayarray(resultstring, ourdate), allthree, activity)
                recycleView = binding.recyclerView
                recycleView.adapter = bookticketsAdapter
                val dividerdecoration = DividerItemDecoration(
                    container?.context,
                    DividerItemDecoration.VERTICAL
                )
                recycleView.addItemDecoration(dividerdecoration)



        }


        //bookticketsAdapter = BookTicketsAdapter(getarray(resultstring))
       // bookticketsAdapter = DateAdapter(getarray(resultstring))
        return view
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun getdayarray(thestring: String, theday: String): ArrayList<Array<String>> {
        val alldata= arrayListOf<Array<String>>()
        val jsonArray = JSONArray(thestring)
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val source = jsonObject.optString("source")
            val destination = jsonObject.optString("destination")
            val traveltimeanddate = jsonObject.optString("traveltime")
            val ticketnumber = jsonObject.optString("ticketnumber")
            val availableseats = jsonObject.optString("availableseats")

            val separatedtimeanddate= traveltimeanddate.split(" ")
            val traveldate = separatedtimeanddate[0]
            val traveltime = separatedtimeanddate[1]
            if(traveldate == theday){
            val temparray = arrayOf(source, destination, traveltime, ticketnumber, availableseats)
            alldata.add(temparray)
            }
        }
        return alldata
    }

    fun getdate(thestring: String): Array<String> {
        Log.d("try", "The result string is "+thestring.toString())
        //val singlearray = onearray.getString(0)
       // val singlearray =onearray
       // lateinit var thedate:Array<String>
        val thedate = ArrayList<String>()
        val jsonArray = JSONArray(thestring)
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val traveltimeanddate = jsonObject.optString("traveltime")
            val separatedtimeanddate= traveltimeanddate.split(" ")
            val traveldate = separatedtimeanddate[0]
          //  val temparray = arrayOf(traveldate)
            thedate.add(traveldate)
        }
       var filteredone = thedate.toSet().toTypedArray()
        return filteredone
    }

}
