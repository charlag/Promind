package com.charlag.promind.core.repository

import android.util.Log
import com.charlag.promind.core.data.models.AssistantContext
import com.charlag.promind.core.model.Model
import com.charlag.promind.core.data.models.UserHint
import com.charlag.promind.core.context_data.DateProvider
import com.charlag.promind.core.context_data.LocationProvider
import com.charlag.promind.core.data.models.Location
import com.stepango.koptional.Optional
import com.stepango.koptional.orNull
import com.stepango.koptional.toOptional
import io.reactivex.Observable

/**
 * Created by charlag on 10/06/2017.
 */

class HintsRepositoryImpl(val model: Model,
                          locationProvider: LocationProvider,
                          val dateProvider: DateProvider) : HintsRepository {
    companion object {
        private val TAG = HintsRepositoryImpl::class.java.simpleName
    }
    override val hints: Observable<List<UserHint>> = locationProvider.currentLocation()
            .map { it.toOptional() }
            .doOnError { Log.e(TAG, "Error while observing locaiton", it) }
            .onErrorReturn { Optional.empty() }
            .startWith(Optional.empty<Location>())
            .switchMap { location ->
                val context = AssistantContext(location.orNull(), dateProvider.currentDate())
                model.getHintsForContext(context)
            }
}