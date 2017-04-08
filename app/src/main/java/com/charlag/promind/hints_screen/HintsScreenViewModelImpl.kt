package com.charlag.promind.hints_screen

import com.charlag.promind.HintViewModel
import com.charlag.promind.core.AssistantContext
import com.charlag.promind.core.Model
import com.charlag.promind.core.UserHint
import com.charlag.promind.core.context_data.DateProvider
import io.reactivex.Observable

/**
 * Created by charlag on 27/03/2017.
 */

class HintsScreenViewModelImpl(private val model: Model,
                               private val locationProvider: com.charlag.promind.LocationProvider,
                               private val dateProvider: DateProvider) : HintsScreenViewModel {
    override val hints: Observable<List<HintViewModel>>
        get() {
            // TODO: use location
            val context = AssistantContext(null, dateProvider.date)
            return model.getHintsForContext(context).map { hints ->
                hints.map(this::hintToViewModel)
            }
        }

    private fun hintToViewModel(hint: UserHint): HintViewModel = HintViewModel(hint.title)
}