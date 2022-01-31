package com.egs.ansroid.samples

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.egs.ansroid.samples.lesson7.Lesson7
import com.egs.ansroid.samples.lesson8.Lesson8
import com.egs.ansroid.samples.lesson9.Lesson9

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // *************  LESSON 1 ************************
        //Lesson1().makeRequest1()
        //Lesson1().makeRequest2()
        //Lesson1().makeRequest3()
        //Lesson1().makeRequest4()
        //Lesson1().makeRequest5()

        //************** LESSON 2 **************************
        //Lesson2().makeRequest1()
        //Lesson2().makeRequest2("111")

        //************** LESSON 3 **************************
        //Lesson3().makeRequest1()

        //************** LESSON 4 **************************
        //Lesson4().makeRequest5()

        //************** LESSON 7 **************************
        //Lesson7().getMessage()
        //Lesson7().customOperator()
        //Lesson7().exceptionExperiment1()
        //Lesson7().exceptionExperiment2()
        //Lesson7().exceptionExperiment3()
        //Lesson7().exceptionExperiment3()
        //Lesson7().exceptionExperiment3()
        //Lesson7().catchOperator()


        //************** LESSON 8 **************************
//        Lesson8().getMessageFlow()

        //************** LESSON 9 **************************

        //Lesson9().flowTest()
//        Lesson9().switchingScope()
//        Lesson9().violatesTest()
        Lesson9().zipTest()

    }
}