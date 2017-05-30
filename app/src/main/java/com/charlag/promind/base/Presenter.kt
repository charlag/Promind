package com.charlag.promind.base

/**
 * Created by charlag on 28/05/2017.
 */

interface Presenter<in View> {
    fun attachView(v: View)
    fun detachView()
}