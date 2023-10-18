package com.example.dictionaryapp.ui.dialog

import android.app.AlertDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import com.example.dictionaryapp.R

class AboutDialog (context: Context) : AlertDialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_about)

        findViewById<AppCompatButton>(R.id.btn_ok).setOnClickListener { dismiss() }
        setCancelable(false)
    }
}