package com.charlag.promind.util.rx

import io.reactivex.Observable

/**
 * Created by charlag on 28/05/2017.
 */

fun <T> Observable<T>.onErrorComplete(): Observable<T> = this.onErrorResumeNext(Observable.empty())