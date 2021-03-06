package com.charlag.promind.ui.component.hints_screen

import com.charlag.promind.util.Empty
import io.reactivex.Observable

/**
 * Created by charlag on 17/04/2017.
 */
object HintsScreenContract {
    interface Presenter : com.charlag.promind.ui.lib.base.Presenter<View> {
        val hints: Observable<List<HintViewModel>>
        val requestLocationPermission: Observable<Empty>
        val requestUsagePermission: Observable<Empty>
        val refreshing: Observable<Boolean>
        val showUsagePermissionInfo: Observable<Boolean>
    }

    interface View {
        val isLocationPermissionGranted: Boolean
        val permissionsGranted: Observable<List<String>>
        val hintSelected: Observable<Int>
        val usagePermissionGranted: Observable<Boolean>
        val refreshed: Observable<Empty>
        val requestUsagePermissionClicked: Observable<Empty>
        val addNewHintPressed: Observable<Empty>
    }
}