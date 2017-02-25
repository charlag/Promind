package com.charlag.promind

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.charlag.promind.core.AssistantContext
import com.charlag.promind.core.ModelImpl
import java.util.Date

class MainActivity : AppCompatActivity() {

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

        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val lastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        val assistContext = AssistantContext(lastLocation, Date())
        val model = ModelImpl()
        val hints = model.getHintsForContext(assistContext)
        hintsTextView.text = hints.map { it.title }.joinToString(separator = "\n")
        hintsTextView.setOnClickListener {
            hints.firstOrNull()?.let { hint ->
                val intent = hint.action.makeIntent(this)
                startActivity(intent)
            }
        }
    }
}
