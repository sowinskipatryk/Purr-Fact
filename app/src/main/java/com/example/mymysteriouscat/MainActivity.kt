package com.example.mymysteriouscat

import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONObject
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        val funFactButton: ImageButton = findViewById(R.id.funFactButton)
        funFactButton.setOnClickListener{
            run("https://catfact.ninja/fact")
        }
        funFactButton.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(arg0: View?, arg1: MotionEvent): Boolean {
                if (arg1.action == MotionEvent.ACTION_DOWN)
                {
                    funFactButton.setImageResource(R.drawable.button_paw_clicked)
                } else {
                    funFactButton.setImageResource(R.drawable.button_paw)
                    run("https://catfact.ninja/fact")
                }
                return true
            }
        })
    }

    fun run(url: String) {
        val request = Request.Builder()
            .url(url)
            .build()

        val funFactTextView: TextView = findViewById(R.id.funFactTextView)

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response){
                Handler(Looper.getMainLooper()).post(Runnable {
                    val jsonString = response.body()?.string()
                    val jsonObject = JSONObject(jsonString)
                    val data = jsonObject.getString("fact")
                    funFactTextView.setText(data)
                })
            }
        })
    }
}