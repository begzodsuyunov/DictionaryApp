package com.example.dictionaryapp.Repository

import android.database.Cursor
import com.example.dictionaryapp.data.room.WordData

interface WordRepository {

    fun getWordCursor(): Cursor
    fun getFilteredWordsCursor(query: String): Cursor
    fun update(wordData: WordData)
    fun getFavourites(): Cursor
}