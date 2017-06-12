package com.charlag.promind.util

import android.content.Context
import com.charlag.promind.app.App
import com.charlag.promind.app.AppComponent

/**
 * Created by charlag on 12/06/2017.
 */

val Context.app: App
    get() = this.applicationContext as App

val Context.appComponent: AppComponent
    get() = this.app.graph