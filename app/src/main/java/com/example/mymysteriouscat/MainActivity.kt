package com.example.mymysteriouscat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val funFactButton: Button? = findViewById(R.id.funFactButton)
        funFactButton?.setOnClickListener{
            run("https://catfact.ninja/fact")
        }
    }

    fun run(url: String) {
        val request = Request.Builder()
            .url(url)
            .build()

        val funFactTextView: TextView = findViewById(R.id.funFactTextView)

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) = funFactTextView.setText(response.body()?.string())
        })
    }
}