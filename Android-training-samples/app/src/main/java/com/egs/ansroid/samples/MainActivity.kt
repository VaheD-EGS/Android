package com.egs.ansroid.samples

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.egs.ansroid.samples.lesson1.Lesson1

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // *************  LESSON 1 ************************
        //Lesson1().makeRequest1()
        //Lesson1().makeRequest2()
        //Lesson1().makeRequest3()
        Lesson1().makeRequest4()
        //Lesson1().makeRequest5()

        //************** LESSON 2 **************************
        //Lesson2().makeRequest1()
        //Lesson2().makeRequest2("111")
    }
}