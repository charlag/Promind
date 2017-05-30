package com.charlag.promind.core.context_data

import android.content.Context
import android.location.Criteria
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import com.charlag.promind.LocationProvider
import com.charlag.promind.core.data.Location
import io.reactivex.Observable

/**
 * Created by charlag on 08/04/2017.
 */

class LocationProviderImpl(context: Context) : LocationProvider {
    val locationManger = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    override fun currentLocation(): Observable<Location> =
            Observable.create { subscriber ->
                val providerName = locationManger.getBestProvider(Criteria(), true)
                locationManger.requestLocationUpdates(providerName, 0L, 0f, object : LocationListener {
                    override fun onLocationChanged(location: android.location.Location?) {
                        if (location != null) {
                            val latLon = Location(location.latitude, location.longitude)
                            subscriber.onNext(latLon)
                        }
                    }

                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

                    override fun onProviderEnabled(provider: String?) {}

                    override fun onProviderDisabled(provider: String?) {
                        subscriber.onComplete()
                    }
                })
            }

}