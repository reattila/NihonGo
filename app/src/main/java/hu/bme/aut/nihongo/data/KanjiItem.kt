package hu.bme.aut.nihongo.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "kanijtable")
data class KanjiItem(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) val id: Long?,
    @ColumnInfo(name = "kanji") val kanji: String,
    @ColumnInfo(name = "hiragana") val hiragana: String,
    @ColumnInfo(name = "meaning") val meaning: String
) { }