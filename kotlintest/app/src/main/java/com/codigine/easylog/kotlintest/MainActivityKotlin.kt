package com.codigine.easylog.kotlintest

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.codigine.easylog.EasyLog
import com.codigine.easylog.Logger

class MainActivityKotlin : AppCompatActivity() {
    @EasyLog(tag="kotlin activity")
    lateinit var log: Logger

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val testJava = TestJava()
        val testKotlin = TestKotlin();

        log.i("Hello from kotlin example")
        testJava.log.i("Hello from java class")
        testKotlin.log.i("Hello from Kotlin class")
    }
}
