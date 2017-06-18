package com.charlag.promind.ui.frame

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import com.charlag.promind.R
import com.charlag.promind.ui.component.hints_screen.HintsScreenFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.frame, HintsScreenFragment())
                    .commit()
        }
    }
}
