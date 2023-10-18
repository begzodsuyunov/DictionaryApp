package com.example.dictionaryapp.data.room.dao

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.dictionaryapp.data.room.WordData

@Dao
interface WordDao {
    @Insert
    fun insert(wordData: WordData)

    @Delete
    fun delete(wordData: WordData)

    @Update
    fun update(wordData: WordData)

    @Query("SELECT * FROM dictionary")
    fun getEngUzCursor(): Cursor

    @Query("SELECT * FROM dictionary WHERE is_favourite=1")
    fun getFavorite(): Cursor

    @Query("SELECT * FROM dictionary ORDER BY uzbek")
    fun getUzEngCursor(): Cursor

    @Query("SELECT * FROM dictionary WHERE english LIKE :query || '%'")
    fun getFilteredCursor(query: String): Cursor

}