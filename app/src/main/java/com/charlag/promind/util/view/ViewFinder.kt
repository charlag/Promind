package com.charlag.promind.util.view

import android.app.Activity
import android.support.annotation.IdRes
import android.view.View

/**
 * Created by charlag on 08/04/2017.
 */

inline fun <reified T: View> Activity.findView(@IdRes id: Int) = this.findViewById(id) as T
inline fun <reified T: View> View.findView(@IdRes id: Int) = this.findViewById(id) as T