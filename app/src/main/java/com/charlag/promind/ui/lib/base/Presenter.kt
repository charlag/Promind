package com.charlag.promind.ui.lib.base

/**
 * Created by charlag on 28/05/2017.
 */

interface Presenter<in View> {
    fun attachView(view: View)
    fun detachView()
}