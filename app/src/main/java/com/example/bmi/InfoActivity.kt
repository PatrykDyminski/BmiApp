package com.example.bmi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_info.*

class InfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        val bundle = intent.extras

        bmiText.text = bundle.getString(getString(R.string.result_key))
        categoryText.text = bundle.getString(getString(R.string.category_key))

        setInfo(bundle.getString(getString(R.string.category_key)))

    }

    private fun setInfo(category:String){

        textWynik.text = when(category){
            getString(R.string.healthy) -> "healthy dhuaihiuahfioasjfas"
            getString(R.string.underweight) -> "Under sfafasfasfasfas"
            getString(R.string.overweight) -> "overve dsffaf"
            getString(R.string.obesity) -> "obese"

            else -> "No niee podałeś poprawnych danych albo poza skalą XD"
        }

    }
}
