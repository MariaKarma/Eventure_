package com.example.ev.utils

import androidx.multidex.BuildConfig
import androidx.multidex.MultiDexApplication
import androidx.room.Room
import com.example.ev.data.Country
import com.example.ev.database.AppDatabase
import com.example.ev.database.Dao
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import java.io.IOException
import java.nio.charset.Charset

class AppController : MultiDexApplication() {


    companion object {
        lateinit var instance: AppController
            private set

        lateinit var list: List<Country>
            private set

        lateinit var DB_DAO : Dao

        lateinit var DB: AppDatabase

        private fun loadJSONFromAsset(): String {

            val json: String?
            try {
                val inputStream = instance.assets.open("countries.json")
                val size = inputStream.available()
                val buffer = ByteArray(size)
                val charset: Charset = Charsets.UTF_8
                inputStream.read(buffer)
                inputStream.close()
                json = String(buffer, charset)
            } catch (ex: IOException) {
                ex.printStackTrace()
                return ""
            }
            return json

        }

        fun filterList(query: String): ArrayList<Country> {
            val list = list
            val filteredList = ArrayList<Country>()
            for (region in list) {

                if (query.isEmpty()) {
                    filteredList.add(region)
                } else {
                    if (region.name.lowercase().contains(query, ignoreCase = true)) {
                        filteredList.add(region)
                    }
                }
            }
            return filteredList
        }

    }



    override fun onCreate() {
        super.onCreate()
        instance = this
        DB = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "app_database").build()
        DB_DAO = AppDatabase.getDatabase(this).Dao()
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)
        val typeToken = object : com.google.gson.reflect.TypeToken<List<Country>>() {}.type
        list = Gson().fromJson(loadJSONFromAsset(), typeToken)
    }

    fun getCountryByCountryCode(countryCode: String): Country? {
        for (country in list) {
            if (country.code == countryCode || country.dialcode == countryCode) {
                return country
            }
        }
        return null
    }


}