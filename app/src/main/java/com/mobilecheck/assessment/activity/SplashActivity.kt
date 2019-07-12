package com.mobilecheck.assessment.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.mobilecheck.assessment.R

class SplashActivity : AppCompatActivity() {
    private val SPLASH_TIMEOUT = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Run.after(SPLASH_TIMEOUT.toLong()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    class Run {
        companion object {
            fun after(delay: Long, process: () -> Unit) {
                Handler().postDelayed({
                    process()
                }, delay)
            }
        }
    }
}
