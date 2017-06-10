package com.charlag.promind.core.repository

import com.charlag.promind.core.AssistantContext
import com.charlag.promind.core.Model
import com.charlag.promind.core.UserHint
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
                          val locationProvider: LocationProvider,
                          val dateProvider: DateProvider) : HintsRepository {
    override val hints: Observable<List<UserHint>> = locationProvider.currentLocation()
            .map { it.toOptional() }
            .onErrorReturn { Optional.empty() }
            .startWith(Optional.empty<Location>())
            .switchMap { location ->
                val context = AssistantContext(location.orNull(), dateProvider.date)
                model.getHintsForContext(context)
            }

}