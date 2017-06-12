package com.charlag.promind.ui.component.new_hint

import com.charlag.promind.util.Empty
import io.reactivex.Observable
import io.reactivex.Single
import java.util.*

/**
 * Created by charlag on 10/04/2017.
 */

class NewHintContract private constructor() {
    interface Presenter : com.charlag.promind.base.Presenter<View> {
        val appsList: Observable<List<AppViewModel>>
        val showFromTimePicker: Observable<Empty>
        val showToTimePicker: Observable<Empty>
        val showDatePicker: Observable<Empty>
    }

    interface View {
        val titleText: Observable<String>
        val appSelected: Observable<Int>
        val fromTimePressed: Observable<Empty>
        val toTimePressed: Observable<Empty>
        val fromTimePicked: Observable<Time>
        val toTimePicked: Observable<Time>
        val datePressed: Observable<Empty>
        val datePicked: Observable<Date>
        val addPressed: Observable<Empty>
    }

    data class Time(val hours: Int, val minutes: Int)
}