package com.widgetfy.com.util

import android.content.Context
import android.content.SharedPreferences

object WidgetPrefs {
    private const val PREFS = "widget_prefs"
    fun prefs(context: Context): SharedPreferences =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    fun getNotes(context: Context): List<String> {
        val raw = prefs(context).getString("notes", "Buy groceries|Meeting at 4 PM|Call mom ❤️") ?: ""
        return raw.split("|").filter { it.isNotBlank() }
    }

    fun saveNotes(context: Context, notes: List<String>) {
        prefs(context).edit().putString("notes", notes.joinToString("|")).apply()
    }

    fun getSteps(context: Context): Int {
        return prefs(context).getInt("steps", (2000..8000).random())
    }

    fun saveSteps(context: Context, steps: Int) {
        prefs(context).edit().putInt("steps", steps).apply()
    }
}
