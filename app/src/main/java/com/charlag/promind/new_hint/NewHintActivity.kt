package com.charlag.promind.new_hint

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import com.charlag.promind.App
import com.charlag.promind.R
import com.charlag.promind.util.rx.rxClicks
import com.charlag.promind.util.rx.rxItemCeleted
import com.charlag.promind.util.rx.rxText
import com.charlag.promind.util.view.findView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class NewHintActivity : AppCompatActivity(), NewHintContract.View {

    @Inject lateinit var presenter: NewHintContract.Presenter

    private lateinit var titleField: EditText
    private lateinit var typePicker: Spinner
    private lateinit var appPicker: Spinner
    private lateinit var timeFromField: EditText
    private lateinit var timeToField: EditText
    private lateinit var addButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_condition)
        titleField = findView(R.id.et_title)
        typePicker = findView(R.id.picker_type)
        appPicker = findView(R.id.picker_app)
        timeFromField = findView(R.id.et_time_from)
        timeToField = findView(R.id.et_time_to)
        addButton = findView(R.id.btn_add)

        DaggerNewHintComponent.builder()
                .appComponent(App.graph)
                .newHintPresenterModule(NewHintPresenterModule(this))
                .build()
                .inject(this)

        val appsAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1)
        appPicker.adapter = appsAdapter

        presenter.appsList
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { apps ->
                    appsAdapter.addAll(apps.map(AppViewModel::name))
                }
    }

    override val titleText: Observable<String>
        get() = titleField.rxText.map(CharSequence::toString)
    override val fromTimeText: Observable<String>
        get() = timeFromField.rxText.map(CharSequence::toString)
    override val toTimeText: Observable<String>
        get() = timeToField.rxText.map(CharSequence::toString)
    override val appSelected: Observable<Int>
        get() = appPicker.rxItemCeleted
    override val addPressed: Observable<Any>
        get() = addButton.rxClicks
}
