package com.app.integrathlete.loader

import android.content.Context
import com.app.integrathlete.model.Supplement
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object JsonHelper {
    fun loadSupplements(context: Context): List<Supplement> {
        val json = context.assets.open("supplements.json")
            .bufferedReader()
            .use { it.readText() }

        val type = object : TypeToken<List<Supplement>>() {}.type
        return Gson().fromJson(json, type)
    }
}
