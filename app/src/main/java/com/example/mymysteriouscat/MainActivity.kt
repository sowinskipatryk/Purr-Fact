package com.example.mymysteriouscat

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

        funFactButton.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(arg0: View?, arg1: MotionEvent): Boolean {
                if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
                    funFactButton.setImageResource(R.drawable.button_paw_clicked)
                } else if (arg1.getAction() == MotionEvent.ACTION_UP) {
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
            override fun onFailure(call: Call, e: IOException) {
                funFactTextView.setText(getString(R.string.try_again_later))
            }
            override fun onResponse(call: Call, response: Response){
                Handler(Looper.getMainLooper()).post(Runnable {
                    val jsonString = response.body()?.string()
                    if (response.code() != 200)
                    {
                        funFactTextView.setText(getString(R.string.try_again_later))
                    }
                    else {
                        val jsonObject = JSONObject(jsonString)
                        val data = jsonObject.getString("fact")
                        val length = jsonObject.getString("length")
                        if (length.toInt() <= 250) {
                            funFactTextView.setText(data)
                        } else {
                            run(url)
                        }
                    }
                })
            }
        })
    }
}