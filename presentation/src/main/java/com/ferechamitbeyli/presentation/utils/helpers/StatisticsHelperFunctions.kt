package com.ferechamitbeyli.presentation.utils.helpers

import java.util.*

object StatisticsHelperFunctions {

    fun getTimestampOfStartAndEndDatesForWeek(destinedWeek: Int = 0): Array<Long> {
        //initialize Calendar
        val calendar = Calendar.getInstance()

        //get current week number
        val currentWeekNumber = calendar.get(Calendar.WEEK_OF_YEAR)

        //decrement week number by 1 for the previous week
        //increment week number by 1 for the next week
        calendar[Calendar.WEEK_OF_YEAR] = currentWeekNumber + destinedWeek

        //set first day of week
        calendar[Calendar.DAY_OF_WEEK] = Calendar.MONDAY

        //get start date of previous week
        val startDate = calendar.timeInMillis

        calendar[Calendar.WEEK_OF_YEAR] = currentWeekNumber + destinedWeek + 1

        //set last day of week
        calendar[Calendar.DAY_OF_WEEK] = Calendar.SUNDAY

        //get end date of previous week
        val endDate = calendar.timeInMillis

        return arrayOf(startDate, endDate)
    }

    fun getTimestampOfStartAndEndDatesForMonth(destinedWeek: Int = 0): Array<Long> {
        //initialize Calendar
        val calendar = Calendar.getInstance()

        //get current week number
        val currentWeekNumber = calendar.get(Calendar.WEEK_OF_YEAR)

        //decrement week number by 1 for the previous week
        //increment week number by 1 for the next week
        calendar[Calendar.WEEK_OF_YEAR] = currentWeekNumber + destinedWeek + 1

        //set last day of week
        calendar[Calendar.DAY_OF_WEEK] = Calendar.SUNDAY

        //get start date of previous week
        val endDate = calendar.timeInMillis

        calendar[Calendar.WEEK_OF_YEAR] = currentWeekNumber + destinedWeek - 4

        //set last day of week
        calendar[Calendar.DAY_OF_WEEK] = Calendar.MONDAY

        //get end date of previous week
        val startDate = calendar.timeInMillis

        return arrayOf(startDate, endDate)
    }

    fun getTimestampOfToday(): Array<Long> {
        //initialize Calendar
        val calendar = Calendar.getInstance()

        //get current day number
        val currentDayNumber = calendar.get(Calendar.DAY_OF_YEAR)

        calendar[Calendar.DAY_OF_YEAR] = currentDayNumber
        calendar[Calendar.HOUR_OF_DAY] = 0 //set hours to zero
        calendar[Calendar.MINUTE] = 0 // set minutes to zero
        calendar[Calendar.SECOND] = 0 //set seconds to zero

        val startDate = calendar.timeInMillis

        calendar[Calendar.HOUR_OF_DAY] = 23 //set hours to zero
        calendar[Calendar.MINUTE] = 59 // set minutes to zero
        calendar[Calendar.SECOND] = 59 //set seconds to zero

        //get end date of previous week
        val endDate = calendar.timeInMillis

        return arrayOf(startDate, endDate)
    }
}