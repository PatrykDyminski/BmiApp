package com.example.bmi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bmi.Logic.DataItem

class HistoryAdapter(private val history: List<DataItem>): RecyclerView.Adapter<HistoryAdapter.DataHolder>() {

    override fun getItemCount(): Int {
        return history.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cell = layoutInflater.inflate(R.layout.data_view, parent,false)

        return DataHolder(cell)
    }

    override fun onBindViewHolder(holder: DataHolder, position: Int) {

        holder.result.text = history[position].bmi
        holder.category.text = history[position].category
        holder.mass.text = "mass: " + history[position].mass
        holder.height.text = "height: " + history[position].height
        holder.date.text = history[position].date

    }

    class DataHolder(v: View):RecyclerView.ViewHolder(v){

        val height: TextView = itemView.findViewById(R.id.height)
        val mass: TextView = itemView.findViewById(R.id.mass)
        val date: TextView = itemView.findViewById(R.id.date)
        val result: TextView = itemView.findViewById(R.id.result)
        val category: TextView = itemView.findViewById(R.id.category)

    }
}