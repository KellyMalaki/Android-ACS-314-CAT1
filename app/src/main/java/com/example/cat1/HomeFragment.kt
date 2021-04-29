package com.example.cat1

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.cat1.adapters.BookTicketsAdapter
import com.example.cat1.adapters.DateAdapter
import com.example.cat1.adapters.HomeAdapter
import com.example.cat1.databinding.HomeFragmentBinding
import org.json.JSONArray

class HomeFragment : Fragment() {
    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding!!
    lateinit var recycleView: RecyclerView
    lateinit var homeAdapter: HomeAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        //code starts here

        Log.d("find", "Am in home fragment")
        var resultstring = arguments?.get("resultstring").toString()
        Log.d("try", "resultstring is = "+ resultstring)
        var phonenumber = arguments?.get("phonenumber").toString()
        Log.d("try", "phonenumber is = "+ phonenumber)

        homeAdapter = HomeAdapter(getourtickets(resultstring), phonenumber)
        recycleView = binding.homerecyclerView
        recycleView.adapter = homeAdapter
        val dividerdecoration = DividerItemDecoration(
            container?.context,
            DividerItemDecoration.VERTICAL
        )
        recycleView.addItemDecoration(dividerdecoration)

        //code stops here
        return view
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun getourtickets(thestring: String): ArrayList<Array<String>>{
        val alldata= arrayListOf<Array<String>>()
        val jsonArray = JSONArray(thestring)
        if (jsonArray.toString() == "[\"0\"]"){
            Log.d("alldata", "All data is "+alldata)
           //tell user to book trains
            binding.moreheaderdetails.text = "You don't have any trains yet. Book to view them"
        }else{
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val ticketnumber = jsonObject.optString("ticketnumber")
            val source = jsonObject.optString("source")
            val destination = jsonObject.optString("destination")
            val bookdayandtime = jsonObject.optString("traindeparttime")
            val traveltimeanddate = jsonObject.optString("ticketdate")

           // val separatedtimeanddate= traveltimeanddate.split(" ")
           // val traveldate = separatedtimeanddate[0]
           // val traveltime = separatedtimeanddate[1]
                val temparray = arrayOf(ticketnumber, source, destination, bookdayandtime, traveltimeanddate)
                alldata.add(temparray)
        }
        }
        return alldata
    }
}