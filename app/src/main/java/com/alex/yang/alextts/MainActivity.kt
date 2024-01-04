package com.alex.yang.alextts

import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private var textToSpeech: TextToSpeech? = null
    private val textOnInitListener = TextToSpeech.OnInitListener { status ->
        if (status == TextToSpeech.SUCCESS) {
            val result: Int
            val locale = Locale.getDefault()
            // 設置語言，如果語言不可用，則使用默認語言
            result = textToSpeech?.setLanguage(locale) ?: TextToSpeech.LANG_MISSING_DATA
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TextToSpeech", "Language is not available.")
            }
        } else {
            Log.e("TextToSpeech", "Initialization failed.")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpTTS()
        initViews()
    }

    private fun initViews() {
        val tvContent = findViewById<TextView>(R.id.tv_content)
        // 啟用TTS
        findViewById<View>(R.id.btn_tts_play).setOnClickListener(View.OnClickListener {
            val utteranceId = "uniqueId" // 設置一個獨特的ID
            textToSpeech?.speak(tvContent.text, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
        })
        // 關閉TTS
        findViewById<View>(R.id.btn_tts_stop).setOnClickListener(View.OnClickListener {
            textToSpeech?.stop()
        })
    }

    private fun setUpTTS() {
        textToSpeech = TextToSpeech(this, textOnInitListener)
        // 設置語音合成進度監聽器
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    // 語音開始合成時的回調
                }

                override fun onDone(utteranceId: String?) {
                    if (utteranceId == "uniqueId") {
                        Log.d("TTS", "TTS playback completed")
                    }
                }

                override fun onError(utteranceId: String?) {
                    // 語音合成錯誤時的回調
                }
            })
        }
    }

    override fun onDestroy() {
        // 釋放TextToSpeech資源
        if (textToSpeech != null) {
            textToSpeech?.stop()
            textToSpeech?.shutdown()
        }
        super.onDestroy()
    }
}