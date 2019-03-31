package com.example.bmi

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bmi.Logic.DataItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_history.*

class HistoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val myDataset = getHistory()

        viewManager = LinearLayoutManager(this)
        viewAdapter = HistoryAdapter(this,myDataset)

        recyclerView = history_recycler_view.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }

    }

    private fun getHistory() : List<DataItem> {

        val prefs = this.getSharedPreferences(MainActivity.PREFS_KEY, Context.MODE_PRIVATE)

        val jsonHistory = prefs.getString(MainActivity.HIST_KEY, "[]")
        class Token : TypeToken<List<DataItem>>()
        return Gson().fromJson<List<DataItem>>(jsonHistory, Token().type)

    }
}
