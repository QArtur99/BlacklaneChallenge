package com.qartf.blacklanechallenge.util

import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.lang.reflect.Type

object MoshiConverter {

    val moshi = Moshi.Builder().apply {
        add(KotlinJsonAdapterFactory())
    }.build()

    inline fun <reified T> convertToStr(item: T): String {
        val jsonAdapter = moshi.adapter<T>(T::class.java)
        return jsonAdapter.toJson(item)
    }

    inline fun <reified T> convertFromStr(item: String): T {
        val jsonAdapter = moshi.adapter<T>(T::class.java)
        return jsonAdapter.fromJson(item)!!
    }

    fun <T> convertListToStr(clazz: Class<T>, item: List<T>): String {
        val type: Type = Types.newParameterizedType(List::class.java, clazz)
        val jsonAdapter = moshi.adapter<List<T>>(type)
        return jsonAdapter.toJson(item)!!
    }

    fun <T> convertListFromStr(clazz: Class<T>, item: String): List<T> {
        val type: Type = Types.newParameterizedType(List::class.java, clazz)
        val jsonAdapter = moshi.adapter<List<T>>(type)
        return jsonAdapter.fromJson(item)!!
    }
}