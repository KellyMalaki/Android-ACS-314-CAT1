package com.example.cat1.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cat1.R
import com.example.cat1.models.Ticket
import kotlin.coroutines.coroutineContext

class AllTicketsAdapter(val thearray: ArrayList<Array<String>>) : RecyclerView.Adapter<AllTicketsAdapter.ViewHolder>() {

    class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val txtpassengerName: TextView = view.findViewById(R.id.cname)
        val txtSource: TextView = view.findViewById(R.id.source)
        val txtdestination: TextView = view.findViewById(R.id.destination)
        val txtticketno: TextView = view.findViewById(R.id.ticketno)
        val txtdate: TextView = view.findViewById(R.id.date)
        val txtphone: TextView = view.findViewById(R.id.cphone)


    }

    override fun onCreateViewHolder(
       parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        var view = layoutInflater.inflate(R.layout.all_tickets_row, parent, false)
        var viewHolder = ViewHolder(view)
        return  viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtpassengerName.text = thearray[position][0].toString()
        holder.txtSource.text = thearray[position][1].toString()
        holder.txtphone.text = thearray[position][2].toString()
        holder.txtdestination.text = thearray[position][3].toString()
        holder.txtticketno.text = thearray[position][4].toString()
        holder.txtdate.text = thearray[position][5].toString()
    }

    override fun getItemCount(): Int {
        Log.d("one", "We are returning " +thearray.size)
        return  thearray.size
    }

}