package com.gcorp.multirecherche3d.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gcorp.multirecherche3d.database.dao.QueryDao
import com.gcorp.multirecherche3d.database.entity.SearchQueryEntity
import com.gcorp.multirecherche3d.database.utils.ResultEntityConverter

@Database(
    entities = [SearchQueryEntity::class],
    version = 1
)
@TypeConverters(ResultEntityConverter::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun queryDao(): QueryDao
}