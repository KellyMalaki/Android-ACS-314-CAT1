package com.example.cat1.adapters

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.example.cat1.BookFragment
import com.example.cat1.MainActivity
import com.example.cat1.R
import kotlin.coroutines.coroutineContext

class DateAdapter: RecyclerView.Adapter<DateAdapter.ViewHolder>  {
   lateinit var thedate: Array<String>
   lateinit var thestring: String
    lateinit var allthree: Array<String?>

    constructor(thedate: Array<String>, thestring: String, allthree: Array<String?>){
        this.thedate = thedate
        this.thestring = thestring
        this.allthree = allthree
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        var view = layoutInflater.inflate(R.layout.ticket_date_row, parent, false)
        var viewHolder = ViewHolder(view, parent.context, thestring, allthree)
        return  viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.date.text = thedate[position].toString()
    }

    override fun getItemCount(): Int {
        return thedate.size
    }

    class ViewHolder : RecyclerView.ViewHolder, View.OnClickListener {
        lateinit var date: TextView
        lateinit var context: Context
        lateinit var thestring: String
        lateinit var allthree1: Array<String?>

        constructor(@NonNull itemView: View, context: Context, thestring: String, allthree1: Array<String?>) : super(itemView) {
            date = itemView.findViewById(R.id.the_date)
            this.context = context
            this.thestring = thestring
            this.allthree1 = allthree1
           itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            Log.d("find", "you pressed the button with "+ date.text.toString())

            /* the old one with the error is below
            val fragmentwithdate = BookFragment()
            val bundle = Bundle()
            bundle.putString("ourdate", date.text.toString())
            bundle.putString("resultstring", thestring)
            val allthree: ArrayList<String?> = arrayListOf(allthree1[0], allthree1[1], allthree1[2])
            bundle.putStringArrayList("allthree", allthree)
            fragmentwithdate.setArguments(bundle)
            val thesolution = ( context as AppCompatActivity).supportFragmentManager
            val currentFragment: Fragment? = thesolution.findFragmentByTag("FRAGMENT_CHOOSE_DATE")
            val fragmentTransaction: FragmentTransaction = thesolution.beginTransaction().replace(
                R.id.fragment_container,
                fragmentwithdate//fragment with the date passed on it
            )
           fragmentTransaction.detach(currentFragment!!)
           fragmentTransaction.attach(currentFragment)
            fragmentTransaction.commit()

             */

            //This is the new one
           // val fragmentwithdate = BookFragment()
            val bundle = Bundle()
            bundle.putString("ourdate", date.text.toString())
            bundle.putString("resultstring", thestring)
            val allthree: ArrayList<String?> = arrayListOf(allthree1[0], allthree1[1], allthree1[2])
            bundle.putStringArrayList("allthree", allthree)
            val thesolution = ( context as AppCompatActivity).supportFragmentManager
            val currentFragment: Fragment? = thesolution.findFragmentByTag("FRAGMENT_CHOOSE_DATE")
            currentFragment?.setArguments(bundle)
            val fragmentTransaction: FragmentTransaction = thesolution.beginTransaction().replace(
                R.id.fragment_container,
                currentFragment!!
            )
            fragmentTransaction.detach(currentFragment!!)
            fragmentTransaction.attach(currentFragment)
            fragmentTransaction.commit()


            //stop here
            /* val currentFragment: Fragment = getFragmentManager().findFragmentByTag("YourFragmentTag")
             val fragmentTransaction: FragmentTransaction = getFragmentManager().beginTransaction()
             fragmentTransaction.detach(currentFragment)
             fragmentTransaction.attach(currentFragment)
             fragmentTransaction.commit()

             var frg: Fragment? = null
             frg = getSupportFragmentManager().findFragmentByTag("Your_Fragment_TAG")
             val ft: FragmentTransaction = getSupportFragmentManager().beginTransaction()
             ft.detach(frg)
             ft.attach(frg)
             ft.commit()*/
          //  (activity as AppCompatActivity).supportFragmentManager




            /*the function with inline shit

            fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
               Log.d("find", "I am in the function")
               val fragmentTransaction = beginTransaction().replace(
                   R.id.fragment_container,
                   fragmentwithdate//fragment with the date passed on it
               )
               var frg: Fragment? = findFragmentByTag("FRAGMENT_CHOOSE_DATE")
               fragmentTransaction.func()
               fragmentTransaction.detach(frg!!)
               fragmentTransaction.attach(frg)
               fragmentTransaction.commit()
           }

             */

        }
    }

}
