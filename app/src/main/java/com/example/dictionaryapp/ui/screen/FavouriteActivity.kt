package com.example.dictionaryapp.ui.screen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dictionaryapp.R
import com.example.dictionaryapp.presenter.WordViewModel
import com.example.dictionaryapp.presenter.impl.WordViewModelImpl
import com.example.dictionaryapp.ui.adapter.WordAdapter
import com.example.dictionaryapp.ui.dialog.WordInfoDialog

class FavouriteActivity : AppCompatActivity() {

    private lateinit var listWords: RecyclerView

    private lateinit var backButton: ImageView

    private val adapter: WordAdapter by lazy { WordAdapter() }

    private val viewModel: WordViewModel by viewModels<WordViewModelImpl>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite)

        listWords = findViewById(R.id.list_favorite)

        backButton = findViewById(R.id.btnBack)
        backButton.setOnClickListener {
            onBackPressed()
        }
        listWords.layoutManager = LinearLayoutManager(this)
        adapter.setItemClickListener { viewModel.showInfo(it) }
        adapter.setFavouriteClickListener { data ->
            viewModel.changeFavorite(data)
            viewModel.showFavourites()
        }

        listWords.adapter = adapter
        viewModel.showFavourites()

        viewModel.showWordInfoLiveData.observe(this) {
            val dialog = WordInfoDialog()
            dialog.setFavouriteClickListener {
                viewModel.changeFavorite(it)
                viewModel.showFavourites()

            }
            val bundle = Bundle()
            bundle.putSerializable("data", it)
            dialog.arguments = bundle
            dialog.show(supportFragmentManager, "Info")
        }
        viewModel.favouritesLiveData.observe(this) {
            adapter.submitCursor(it)
        }
        viewModel.updateItemLiveData.observe(this) {
            adapter.notifyItemChanged(it)
        }

        viewModel.favouritesLiveData.observe(this) {
            adapter.submitCursor(it)
            adapter.notifyDataSetChanged()
        }

    }
}