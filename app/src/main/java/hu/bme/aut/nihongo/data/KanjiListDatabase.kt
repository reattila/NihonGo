package hu.bme.aut.nihongo.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [KanjiItem::class], version = 1)
abstract class KanjiListDatabase : RoomDatabase() {
    abstract fun kanjiItemDao(): KanjiDao
}