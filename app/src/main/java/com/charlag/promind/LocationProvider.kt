package com.charlag.promind

import com.charlag.promind.core.data.Location
import io.reactivex.Observable

/**
 * Created by charlag on 27/03/2017.
 */
interface LocationProvider {
    fun currentLocation(): Observable<Location>
}