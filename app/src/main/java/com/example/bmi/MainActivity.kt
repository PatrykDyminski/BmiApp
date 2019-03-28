package com.example.bmi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.content.ContextCompat
import com.example.bmi.Logic.BmiForKgCm
import com.example.bmi.Logic.BmiForLbIn
import com.example.bmi.Logic.DataItem
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private var unitsSwitched: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        countBtn.setOnClickListener{ onCountClicked() }
        infoBtn.setOnClickListener{ onInfoClicked() }
    }

    private fun saveData(elem:DataItem){

        val prefs = this.getSharedPreferences("com.example.bmi.prefs", Context.MODE_PRIVATE)

        val t = getHistory()
        val historyList = listOf(elem) + t.take(9)

        val jsonHistory = Gson().toJson(historyList)
        with(prefs.edit()) {
            remove("history")
            putString("history", jsonHistory)
            apply()
        }

    }

    private fun getHistory() : List<DataItem> {

        val prefs = this.getSharedPreferences("com.example.bmi.prefs", Context.MODE_PRIVATE)

        val jsonHistory = prefs.getString("history", "[]")
        class Token : TypeToken<List<DataItem>>()
        return Gson().fromJson<List<DataItem>>(jsonHistory, Token().type)

    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putBoolean(getString(R.string.imperial),unitsSwitched)
        outState?.putString(getString(R.string.result_key),result_label.text.toString())
        outState?.putString(getString(R.string.category_key),category_label.text.toString())

        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        unitsSwitched = savedInstanceState!!.getBoolean(getString(R.string.imperial))
        result_label.text = savedInstanceState!!.getString(getString(R.string.result_key))
        val cat = savedInstanceState!!.getString(getString(R.string.category_key))
        category_label.text = cat
        setColors(cat)

        if(unitsSwitched){
            mass_label.text = getString(R.string.mass_lb)
            height_label.text = getString(R.string.height_in)
            invalidateOptionsMenu()
        }else{
            mass_label.text = getString(R.string.mass_kg)
            height_label.text = getString(R.string.height_cm)
        }

        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.dot_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.change_units)?.isChecked = unitsSwitched
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.about -> {
                onAboutPressed()
                true
            }
            R.id.change_units -> {
                unitsSwitched =! unitsSwitched
                item.isChecked = unitsSwitched
                changeUnitsPressed()
                true
            }
            R.id.history->{
                onHistoryPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onHistoryPressed(): Boolean {
        val intent = Intent(this,HistoryActivity::class.java)
        startActivity(intent)
        return true
    }

    private fun changeUnitsPressed() {

        if(unitsSwitched){
            mass_label.text = getString(R.string.mass_lb)
            height_label.text = getString(R.string.height_in)

        }else{
            mass_label.text = getString(R.string.mass_kg)
            height_label.text = getString(R.string.height_cm)
        }

        mass_edit.text.clear()
        height_edit.text.clear()
        result_label.text = getString(R.string.your_bmi)
        result_label.setTextColor(ContextCompat.getColor(this, R.color.grejXD))
        category_label.text = ""

    }

    private fun onAboutPressed(): Boolean {
        val intent = Intent(this,AboutActivity::class.java)
        startActivity(intent)
        return true
    }


    private fun onCountClicked() {

        if(!(mass_edit.text.isBlank() || height_edit.text.isBlank()||mass_edit.text.toString().toInt()==0||height_edit.text.toString().toInt()==0)){

            val mass = mass_edit.text.toString().toInt()
            val height = height_edit.text.toString().toInt()

            var result = 0.0

            if(unitsSwitched){
                val bmiLb = BmiForLbIn(mass,height)
                result = bmiLb.countBmi()
            }else{
                val bmiKg = BmiForKgCm(mass,height)
                result = bmiKg.countBmi()
            }

            val df = "%.2f".format(result)
            result_label.text = df
            var catego = getCategory(result)
            setColors(catego)
            category_label.text = catego
            val date = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
            val historyObject = DataItem(df,mass_edit.text.toString(),height_edit.text.toString(),getCategory(result),date)
            saveData(historyObject)

        }else{
            result_label.text = getString(R.string.podaj_dane)
            category_label.text = ""
        }
    }

    private fun onInfoClicked(): Boolean {
        val intent = Intent(this,InfoActivity::class.java)
        val bundle = Bundle()

        bundle.putString(getString(R.string.result_key),result_label.text.toString())
        bundle.putString(getString(R.string.category_key),category_label.text.toString())
        intent.putExtras(bundle)
        startActivity(intent)
        return true
    }

    private fun getCategory(bmi:Double):String{
        return when(bmi){
            in 0.0 ..18.5 -> { getString(R.string.underweight) }
            in 18.5..25.00 -> { getString(R.string.healthy) }
            in 25.0..30.00 -> { getString(R.string.overweight)  }
            in 30.00..50000.00 -> { getString(R.string.obesity) }
            else -> "hmm"
        }

    }

    private fun setColors(category: String){
        when(category){
            getString(R.string.underweight) -> {
                category_label.setTextColor(ContextCompat.getColor(this, R.color.pompeianRose))
                result_label.setTextColor(ContextCompat.getColor(this, R.color.pompeianRose))
            }
            getString(R.string.healthy) -> {
                category_label.setTextColor(ContextCompat.getColor(this, R.color.verdigris))
                result_label.setTextColor(ContextCompat.getColor(this, R.color.verdigris))
            }
            getString(R.string.overweight) -> {
                category_label.setTextColor(ContextCompat.getColor(this, R.color.lapisLazuli))
                result_label.setTextColor(ContextCompat.getColor(this, R.color.lapisLazuli))
            }
            getString(R.string.obesity) -> {
                category_label.setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
                result_label.setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
            }
        }
    }
}
