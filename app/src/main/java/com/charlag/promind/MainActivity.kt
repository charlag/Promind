package com.charlag.promind

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.charlag.promind.hints_screen.DaggerHintsComponent
import com.charlag.promind.hints_screen.HintsScreenModule
import com.charlag.promind.hints_screen.HintsScreenViewModel
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject lateinit var viewModel: HintsScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ActivityCompat.checkSelfPermission(this,
                ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(ACCESS_COARSE_LOCATION),
                    1)
        } else {
            getHints()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissions.asSequence()
        permissions.indices
                .filter {
                    permissions[it] == ACCESS_COARSE_LOCATION &&
                            grantResults[it] == PackageManager.PERMISSION_GRANTED
                }
                .forEach { getHints() }
    }

    private fun getHints() {
        // Silence permission warning
        // Android linter is too stupid to silence the warning even if
        // I checked it and I didn't find another way.
        if (ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        val hintsTextView = findViewById(R.id.tv_hints) as TextView

        DaggerHintsComponent.builder()
                .appComponent(App.graph)
                .hintsScreenModule(HintsScreenModule())
                .build()
                .inject(this)

        viewModel.hints.subscribe { hints ->
            hintsTextView.text = hints.foldRight(StringBuilder()) { (title), sb ->
                sb.append(title)
                sb.append('\n')
            }
                    .toString()
        }
    }
}
