package com.charlag.promind.core.repository

import com.charlag.promind.core.AssistantContext
import com.charlag.promind.core.Model
import com.charlag.promind.core.UserHint
import com.charlag.promind.core.app_data.AppDataProvider
import com.charlag.promind.core.context_data.DateProvider
import com.charlag.promind.core.context_data.LocationProvider
import com.charlag.promind.core.data.models.Action
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
                          val dateProvider: DateProvider,
                          val appDataProvider: AppDataProvider) : HintsRepository {
    override val hints: Observable<List<UserHint>> = locationProvider.currentLocation()
            .map { it.toOptional() }
            .onErrorReturn { Optional.empty() }
            .startWith(Optional.empty<Location>())
            .switchMap { location ->
                val context = AssistantContext(location.orNull(), dateProvider.date)
                model.getHintsForContext(context)
            }
            .map { hints ->
                hints.map { hint ->
                    if (hint.title == null && hint.action is Action.OpenMainAction) {
                        val appData = appDataProvider.getAppData(hint.action.packageName)
                        hint.copy(title = appData.name)
                    } else {
                        hint
                    }
                }
            }
}