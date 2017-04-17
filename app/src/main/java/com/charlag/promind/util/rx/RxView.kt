package com.charlag.promind.util.rx

import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable

/**
 * Created by charlag on 10/04/2017.
 */

val TextView.rxText: Observable<CharSequence>
    get() = RxTextView.textChanges(this)

val View.rxClicks: Observable<Any>
    get() = RxView.clicks(this)

val Spinner.rxItemCeleted: Observable<Int>
    get() = Observable.create { subscriber ->
        this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int,
                                        id: Long) {
                subscriber.onNext(position)
            }

        }
    }