package com.egs.ansroid.samples.lesson4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.egs.ansroid.samples.R
import com.egs.ansroid.samples.lesson4.ui.main.ChannelTestFragment

class ChannelTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.channel_test_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ChannelTestFragment.newInstance())
                .commitNow()
        }
    }
}