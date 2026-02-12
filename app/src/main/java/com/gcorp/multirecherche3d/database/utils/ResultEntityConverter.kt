package com.gcorp.multirecherche3d.database.utils

import androidx.room.TypeConverter
import com.gcorp.multirecherche3d.database.entity.ResultEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

internal class ResultEntityConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromResultEntityList(value: List<ResultEntity>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toResultEntityList(value: String): List<ResultEntity> {
        val listType = object : TypeToken<List<ResultEntity>>() {}.type
        return gson.fromJson(value, listType)
    }
}