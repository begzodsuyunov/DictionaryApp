package com.example.dictionaryapp.presenter

import android.database.Cursor
import androidx.lifecycle.LiveData
import com.example.dictionaryapp.data.room.WordData

interface WordViewModel {

    val cursorLiveData: LiveData<Cursor>
    val showWordInfoLiveData: LiveData<WordData>
    val favouritesLiveData: LiveData<Cursor>
    val updateItemLiveData: LiveData<Int>

    fun showInfo(wordData: WordData)
    fun changeFavorite(wordData: WordData)
    fun filter(query: String)
    fun showFavourites()
    fun showAll()
}