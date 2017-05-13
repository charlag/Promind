package com.charlag.promind.new_hint

import com.charlag.promind.util.Empty
import io.reactivex.Observable
import io.reactivex.Single
import java.sql.Time

/**
 * Created by charlag on 10/04/2017.
 */

class NewHintContract private constructor() {
    interface Presenter {
        val appsList: Single<List<AppViewModel>>
        val showFromTimePicker: Observable<Empty>
        val showToTimePicker: Observable<Empty>
    }

    interface View {
        val titleText: Observable<String>
        val appSelected: Observable<Int>
        val fromTimePressed: Observable<Empty>
        val toTimePressed: Observable<Empty>
        val fromTimePicked: Observable<Time>
        val toTimePicked: Observable<Time>
        val addPressed: Observable<Any>
    }

    data class Time(val hours: Int, val minutes: Int)
}