package com.example.dictionaryapp.ui.screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dictionaryapp.R
import com.example.dictionaryapp.presenter.WordViewModel
import com.example.dictionaryapp.presenter.impl.WordViewModelImpl
import com.example.dictionaryapp.ui.adapter.WordAdapter
import com.example.dictionaryapp.ui.dialog.AboutDialog
import com.example.dictionaryapp.ui.dialog.WordInfoDialog
import java.util.Locale

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private lateinit var listWords: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var emptyPlaceholder: View
    private var buttonSpeak: ImageView? = null
    private var tts: TextToSpeech? = null

    private val adapter: WordAdapter by lazy { WordAdapter() }

    private val viewModel: WordViewModel by viewModels<WordViewModelImpl>()

    private val handler: Handler by lazy { Handler(Looper.getMainLooper()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<ImageView>(R.id.popmenu)
        button.setOnClickListener {
            val popupMenu: PopupMenu = PopupMenu(this, button)
            popupMenu.menuInflater.inflate(R.menu.menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.favourites -> {
                        startActivity(Intent(this, FavouriteActivity::class.java))
                    }
                    R.id.share -> {
                        val intentInvite = Intent(Intent.ACTION_SEND)
                        intentInvite.type = "text/plain"
                        val body = "https://play.google.com"
                        val subject = "Your Subject"
                        intentInvite.putExtra(Intent.EXTRA_SUBJECT, subject)
                        intentInvite.putExtra(Intent.EXTRA_TEXT, body)
                        startActivity(Intent.createChooser(intentInvite, "Share using"))
                    }
                    R.id.about -> {
                        val dialog = AboutDialog(this@MainActivity)
                        dialog.show()
                    }
                }
                true
            })
            popupMenu.show()
        }

        tts = TextToSpeech(this, this)
        buttonSpeak = findViewById(R.id.btnVoice)

        listWords = findViewById(R.id.listWords)
        searchView = findViewById(R.id.searchView)
        emptyPlaceholder = findViewById(R.id.emptyPlaceholder)

        listWords.layoutManager = LinearLayoutManager(this)

        adapter.setItemClickListener { viewModel.showInfo(it) }

        adapter.setFavouriteClickListener { data ->
            viewModel.changeFavorite(data)
        }

        adapter.setVoiceClickListener { data ->
            speakOut(data.english)
        }

        listWords.adapter = adapter


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                handler.postDelayed({
                    viewModel.filter(newText)
                }, 300)
                return true
            }

        })
        viewModel.showWordInfoLiveData.observe(this) {
            val dialog = WordInfoDialog()
            dialog.setFavouriteClickListener {
                viewModel.changeFavorite(it)
            }
            val bundle = Bundle()
            bundle.putSerializable("data", it)
            dialog.arguments = bundle
            dialog.show(supportFragmentManager, "Info")
        }

        viewModel.cursorLiveData.observe(this) {
            if (it.count == 0) {
                emptyPlaceholder.visibility = View.VISIBLE
            } else {
                emptyPlaceholder.visibility = View.GONE
            }
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

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts!!.setLanguage(Locale.ENGLISH)
        }
    }

    private fun speakOut(text: String) {
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }
    public override fun onDestroy() {
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }
}