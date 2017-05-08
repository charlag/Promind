package com.charlag.promind.hints_screen

import com.charlag.promind.HintViewModel
import io.reactivex.Observable

/**
 * Created by charlag on 17/04/2017.
 */
class HintsScreenContract private constructor() {
    interface Presenter {
        val hints: Observable<List<HintViewModel>>
    }

    interface View {
        val hintSelected: Observable<Int>
    }
}