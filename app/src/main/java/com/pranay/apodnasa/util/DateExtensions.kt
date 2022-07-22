package com.pranay.apodnasa.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * give two date string from given days different to current date
 */
fun getTwoDates(format:String="yyyy-MM-dd",beforeDays:Long=7):Pair<String,String>?{
    return try {
        val formatter = DateTimeFormatter.ofPattern(format)
        val firstDate = formatter.format(LocalDate.now())
        val secondDate = formatter.format(LocalDate.now().minusDays(beforeDays))
        Pair(secondDate,firstDate)
    }catch (ex:Exception){
        null
    }
}