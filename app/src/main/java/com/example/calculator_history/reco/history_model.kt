package com.example.calculator_history.reco

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.calculator_history.R

class MainAdapter(private var items: List<MainList>,private val onItemClick: (MainList) -> Unit) : RecyclerView.Adapter<MainAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val date: TextView = view.findViewById(R.id.date)
        val list: TextView = view.findViewById(R.id.list)
        val value: TextView = view.findViewById(R.id.value_history)
        val result: TextView = view.findViewById(R.id.result_history)
        val reco: LinearLayout = view.findViewById(R.id.add_reco)
    }

    fun updateList(newList: List<MainList>) {
        items = newList // Update to a new List
        notifyDataSetChanged() // Notify adapter about data change
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.add_reco, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = items[position]
        holder.date.text = item.date // Use property access syntax
        holder.list.text = item.list
        holder.value.text = item.value
        holder.result.text = item.result
        holder.reco.setOnClickListener(){
            onItemClick(item)
        }

    }

    override fun getItemCount(): Int {
        return items.size // Return the size of the list
    }
}
