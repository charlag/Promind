package com.charlag.promind.new_hint

import io.reactivex.Observable
import io.reactivex.Single

/**
 * Created by charlag on 10/04/2017.
 */

class NewHintContract private constructor() {
    interface Presenter {
        val appsList: Single<List<AppViewModel>>
    }

    interface View {
        val titleText: Observable<String>
        val fromTimeText: Observable<String>
        val toTimeText: Observable<String>
        val appSelected: Observable<Int>
        val addPressed: Observable<Any>
    }
}