package com.alexander.base.data.local

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import java.util.*
import kotlin.reflect.KClass

class SharedPreferences constructor(
        private val gson: Gson,
        private val context: Context,
        private val sharedPreferencesName: String
) : ISharedPreferences {

    companion object {
        const val CURRENT_LANGUAGE = "current_language"
    }

    private val mPrefs: SharedPreferences = context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)

    @setparam:ApplicationLanguage
    @get:ApplicationLanguage
    override var language: String
        get() {
            val currentLanguage = getString(CURRENT_LANGUAGE)
            return currentLanguage
                    ?: if (Locale.getDefault().language.equals(ApplicationLanguage.ARABIC, true)) {
                        ApplicationLanguage.ARABIC
                    } else {
                        ApplicationLanguage.ENGLISH
                    }
        }
        set(value) {
            putString(CURRENT_LANGUAGE, value)
        }

    override fun putString(key: String, value: String?) {
        mPrefs.edit().putString(key, value).apply()
    }

    override fun getString(key: String): String? {
        return mPrefs.getString(key, null)
    }

    override fun putBoolean(key: String, value: Boolean) {
        mPrefs.edit().putBoolean(key, value).apply()
    }

    override fun getBoolean(key: String): Boolean {
        return mPrefs.getBoolean(key, false)
    }

    override fun putInt(key: String, value: Int) {
        mPrefs.edit().putInt(key, value).apply()
    }

    override fun getInt(key: String, defaultValue: Int): Int {
        return mPrefs.getInt(key, defaultValue)
    }

    override fun <T> putObject(key: String, value: T) {
        mPrefs.edit().putString(key, gson.toJson(value)).apply()
    }

    override fun <T : Any> getObject(key: String, type: KClass<T>): T? {
        val s = mPrefs.getString(key, null) ?: return null
        return gson.fromJson(s, type.java)
    }

    override fun clearData() {
        val currentLanguage = getString(CURRENT_LANGUAGE)
        mPrefs.edit().clear().apply()
        putString(CURRENT_LANGUAGE, currentLanguage)
    }
}
