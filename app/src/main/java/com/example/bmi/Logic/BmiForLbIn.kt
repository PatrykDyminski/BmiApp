package com.example.bmi.Logic

class BmiForLbIn(var mass: Int, var height: Int) : Bmi {

    override fun countBmi(): Double {
        val bmi: Double = ((mass * 1.0) / (height * height)) * 703
        return bmi
    }
}