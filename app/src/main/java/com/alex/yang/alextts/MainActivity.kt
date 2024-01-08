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
//            val result: Int
//            val locale = Locale.getDefault()
            // 設置語言，如果語言不可用，則使用默認語言
//            result = textToSpeech?.setLanguage(locale) ?: TextToSpeech.LANG_MISSING_DATA
//            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
//                Log.e("TextToSpeech", "Language is not available.")
//            }
            // 設置語言為『 英文 』
            val englishResult = textToSpeech?.setLanguage(Locale.ENGLISH)
            if (englishResult == TextToSpeech.LANG_MISSING_DATA || englishResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TextToSpeech", "English language is not available.")
            }
            // 設置語言為『 繁體中文（台灣）』
            val chineseResult = textToSpeech?.setLanguage(Locale("zh", "TW"))
            if (chineseResult == TextToSpeech.LANG_MISSING_DATA || chineseResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TextToSpeech", "未支援繁體中文（台灣）！")
            }
        } else {
            Log.e("TextToSpeech", "Initialization failed.")
        }
    }

    private val ttsData = listOf(
        "1. 南韓一年一度音樂盛會「金唱片獎（Golden Disc Awards）」被譽為韓國葛萊美獎，今（6）日來到第38屆",
        "2. 中國46歲人氣演員黃曉明與前妻Angelababy（楊穎），2022年1月宣布離婚震撼演藝圈，他隨後和企業家網紅葉珂爆出新戀情",
        "3. 今年金唱片獎主持人為ASTRO成員車銀優跟成詩京，出席的藝人包含：SEVENTEEN、NewJeans、少女時代Tiffany Young（蒂芬妮）等"
    )
    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpTTS()
        initViews()
    }

    private fun initViews() {
        findViewById<TextView>(R.id.tv_content_1).text = ttsData[0]
        findViewById<TextView>(R.id.tv_content_2).text = ttsData[1]
        findViewById<TextView>(R.id.tv_content_3).text = ttsData[2]

        // 啟用TTS
        findViewById<View>(R.id.btn_tts_play).setOnClickListener(View.OnClickListener {
            for(index in currentIndex until ttsData.size){
                textToSpeech?.speak(ttsData[index], TextToSpeech.QUEUE_ADD, null, index.toString())
            }

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
                override fun onStart(utteranceId: String) {
                    Log.d("TTS", "onStart: $utteranceId")
                    currentIndex = utteranceId.toInt()
                }

                override fun onDone(utteranceId: String) {
                    Log.d("TTS", "onDone: $utteranceId")
                }

                override fun onError(utteranceId: String) {
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