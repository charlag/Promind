package com.charlag.promind.hints_screen

import com.charlag.promind.HintViewModel
import io.reactivex.Observable

/**
 * Created by charlag on 27/03/2017.
 */

interface HintsScreenViewModel {
    val hints: Observable<List<HintViewModel>>
}