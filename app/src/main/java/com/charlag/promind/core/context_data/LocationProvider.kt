package com.charlag.promind.core.context_data

import com.charlag.promind.core.data.models.Location
import io.reactivex.Observable

/**
 * Created by charlag on 27/03/2017.
 */
interface LocationProvider {
    fun currentLocation(): Observable<Location>
}