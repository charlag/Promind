package com.charlag.promind

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.charlag.promind.hints_screen.DaggerHintsComponent
import com.charlag.promind.hints_screen.HintsScreenContract
import com.charlag.promind.hints_screen.HintsScreenModule
import com.charlag.promind.new_hint.NewHintActivity
import com.charlag.promind.util.view.findView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HintsScreenContract.View {

    @Inject lateinit var presenter: HintsScreenContract.Presenter

    lateinit var hintsListView: RecyclerView

    val adapter = HintsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        hintsListView = findView(R.id.list_hints)
        hintsListView.layoutManager = LinearLayoutManager(this)
        hintsListView.adapter = adapter

        if (ActivityCompat.checkSelfPermission(this,
                ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(ACCESS_COARSE_LOCATION),
                    1)
        } else {
            getHints()
        }

        val addButton = findViewById(R.id.btn_add) as Button
        addButton.setOnClickListener {
            startActivity(Intent(this, NewHintActivity::class.java))
        }

        // TODO: add check for permission
//        startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissions.asSequence()
        permissions.indices
                .filter {
                    permissions[it] == ACCESS_COARSE_LOCATION &&
                            grantResults[it] == PackageManager.PERMISSION_GRANTED
                }
                .forEach { getHints() }
    }

    override val hintSelected: Observable<Int> = adapter.clicks

    private fun getHints() {
        // Silence permission warning
        // Android linter is too stupid to silence the warning even if
        // I checked it and I didn't find another way.
        if (ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        DaggerHintsComponent.builder()
                .appComponent(App.graph)
                .hintsScreenModule(HintsScreenModule(this))
                .build()
                .inject(this)

        presenter.hints.subscribe { hints ->
            adapter.items.clear()
            adapter.items.addAll(hints)
            adapter.notifyDataSetChanged()
        }
    }

    class HintsAdapter : RecyclerView.Adapter<HintsAdapter.ViewHolder>() {

        val clicks: PublishSubject<Int> = PublishSubject.create()
        val items: MutableList<HintViewModel> = mutableListOf()

        override fun getItemCount(): Int = items.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val vm = items[position]
            holder.title.text = vm.title
            holder.image.setImageDrawable(vm.icon)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.view_hint, parent, false)
            return ViewHolder(view, clicks)
        }

        class ViewHolder(itemView: View, val clicks: PublishSubject<Int>) :
                RecyclerView.ViewHolder(itemView), View.OnClickListener {
            val image: ImageView = itemView.findView(R.id.iv_hint_icon)
            val title: TextView = itemView.findView(R.id.tv_hint_title)

            override fun onClick(v: View) {
                clicks.onNext(adapterPosition)
            }
        }
    }
}
