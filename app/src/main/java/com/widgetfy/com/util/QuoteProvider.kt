package com.widgetfy.com.util

object QuoteProvider {
    val quotes = listOf(
        "Believe you can and you're halfway there." to "Theodore Roosevelt",
        "Dream big and dare to fail." to "Norman Vaughan",
        "Keep going. Everything you need will come." to "Unknown",
        "Success is not final, failure is not fatal." to "Winston Churchill",
        "The future belongs to those who believe." to "Eleanor Roosevelt",
        "Do what you can, with what you have." to "Theodore Roosevelt",
        "Stay hungry, stay foolish." to "Steve Jobs",
        "Act as if what you do makes a difference." to "William James",
        "Quality is not an act, it is a habit." to "Aristotle",
        "Life is what happens when you're busy making plans." to "John Lennon"
    )
    fun random(): Pair<String,String> = quotes.random()
    fun ofDay(): Pair<String,String> {
        val day = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_YEAR)
        return quotes[day % quotes.size]
    }
}
