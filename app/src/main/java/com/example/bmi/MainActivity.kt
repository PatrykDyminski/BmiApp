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
import android.content.SharedPreferences
import android.R.id.edit
import android.content.SharedPreferences.Editor
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class MainActivity : AppCompatActivity() {

    private var unitsSwitched: Boolean = false

    var historyList = arrayListOf<DataItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        countBtn.setOnClickListener{ onCountClicked() }
        infoBtn.setOnClickListener{ onInfoClicked() }
    }

    fun saveData(){

        val prefs = getPreferences(MODE_PRIVATE)
        val editor = prefs.edit()

        val jsonHistory = Gson().toJson(historyList)
        editor.remove(getString(R.string.prefs_history_key)).commit()
        editor.putString(getString(R.string.prefs_history_key),jsonHistory)
        editor.commit()
    }

    fun loadData(){

        val prefs = getPreferences(MODE_PRIVATE)

        val jsonHistory = prefs!!.getString(getString(R.string.prefs_history_key),"")
        val type = object : TypeToken<ArrayList<DataItem>>(){}.type

        historyList = Gson().fromJson<ArrayList<DataItem>>(
            jsonHistory,
            type
        )

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

    private fun onHistoryPressed() {

        //val intent = Intent(this,)

        //saveData()
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

            var result: Double = 0.0

            if(unitsSwitched){
                val bmiLb = BmiForLbIn(mass,height)
                result = bmiLb.countBmi()
            }else{
                val bmiKg = BmiForKgCm(mass,height)
                result = bmiKg.countBmi()
            }

            val df = "%.2f".format(result)
            result_label.text = df

            changeColorAndCategory(result)

        }else{
            result_label.text = "Podaj poprawne dane!"
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

    private fun changeColorAndCategory(bmi: Double){

        when(bmi){
            in 0.0 ..18.5 -> {
                result_label.setTextColor(ContextCompat.getColor(this, R.color.pompeianRose))
                category_label.text = getString(R.string.underweight)
                category_label.setTextColor(ContextCompat.getColor(this, R.color.pompeianRose))
            }
            in 18.5..25.00 -> {
                result_label.setTextColor(ContextCompat.getColor(this, R.color.verdigris))
                category_label.text = getString(R.string.healthy)
                category_label.setTextColor(ContextCompat.getColor(this, R.color.verdigris))
            }
            in 25.0..30.00 -> {
                result_label.setTextColor(ContextCompat.getColor(this, R.color.lapisLazuli))
                category_label.text = getString(R.string.overweight)
                category_label.setTextColor(ContextCompat.getColor(this, R.color.lapisLazuli))
            }
            in 30.00..500.00 -> {
                result_label.setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
                category_label.text = getString(R.string.obesity)
                category_label.setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
            }
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
