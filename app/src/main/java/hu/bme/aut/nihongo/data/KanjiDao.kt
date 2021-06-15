package hu.bme.aut.nihongo.data

import androidx.room.*

@Dao
interface KanjiDao {
    @Query("SELECT * FROM kanijtable")
    fun getAll(): List<KanjiItem>

    @Insert
    fun insert(kanjiItems: KanjiItem): Long

    @Update
    fun update(kanjiItem: KanjiItem)

    @Delete
    fun deleteItem(kanjiItem: KanjiItem)
}