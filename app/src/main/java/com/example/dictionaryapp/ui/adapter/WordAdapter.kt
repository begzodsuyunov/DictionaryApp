package com.example.dictionaryapp.ui.adapter

import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dictionaryapp.R
import com.example.dictionaryapp.data.room.WordData
import java.util.Currency

class WordAdapter : RecyclerView.Adapter<WordAdapter.WordItemViewHolder>() {
    private var cursor: Cursor? = null
    private var itemClickListener: ((WordData) -> Unit)? = null
    private var favouriteClickListener: ((WordData) -> Unit)? = null
    private var voiceClickListener: ((WordData) -> Unit)? = null

    fun setItemClickListener(block: (WordData) -> Unit) {
        itemClickListener = block
    }

    fun setFavouriteClickListener(block: (WordData) -> Unit) {
        favouriteClickListener = block
    }

    fun setVoiceClickListener(block: (WordData) -> Unit) {
        voiceClickListener = block
    }

    fun submitCursor(cursor: Cursor) {
        this.cursor = cursor
        notifyDataSetChanged()
    }

    inner class WordItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val txtWord: TextView = view.findViewById(R.id.txtWord)
        private val btnStar: ImageButton = view.findViewById(R.id.btnStar)
        private val btnVoice: ImageButton = view.findViewById(R.id.btnVoice)

        fun bind() {
            cursor!!.moveToPosition(adapterPosition)
            val data = cursor!!.getWordData()

            txtWord.text = data.english
            if (data.isFavorite == 0) {
                btnStar.setImageResource(R.drawable.start_outlined)
            } else {
                btnStar.setImageResource(R.drawable.star_filled)
            }
            btnVoice.setOnClickListener {
                voiceClickListener?.invoke(data)
            }
        }

        init {
            view.setOnClickListener {
                cursor!!.moveToPosition(adapterPosition)
                val data = cursor!!.getWordData()
                itemClickListener?.invoke(data)
            }
            btnStar.setOnClickListener {
                cursor!!.moveToPosition(adapterPosition)
                val data = cursor!!.getWordData()
                favouriteClickListener?.invoke(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = WordItemViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_word, parent, false)
    )

    override fun onBindViewHolder(holder: WordItemViewHolder, position: Int) = holder.bind()

    override fun getItemCount(): Int {
        val count = cursor?.count ?: 0
        return count
    }
}


fun Cursor.getWordData(): WordData {
    return WordData(
        getLong(0),
        getString(1),
        getString(2),
        getString(3),
        getString(4),
        getString(5),
        getInt(6)
    )

}
