package com.charlag.promind.ui.component.hints_screen

import android.Manifest
import android.app.AppOpsManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.charlag.promind.R
import com.charlag.promind.ui.component.new_hint.NewHintActivity
import com.charlag.promind.util.Empty
import com.charlag.promind.util.appComponent
import com.charlag.promind.util.rx.addTo
import com.charlag.promind.util.view.findView
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HintsScreenContract.View {

    @Inject lateinit var presenter: HintsScreenContract.Presenter

    lateinit var hintsListView: RecyclerView
    lateinit var swipeRefresh: SwipeRefreshLayout
    lateinit var usagePermissionTextView: TextView

    val adapter = HintsAdapter()
    lateinit var appOpsManager: AppOpsManager
    override val usagePermissionGranted: BehaviorSubject<Boolean> = BehaviorSubject.create()
    override val refreshed: BehaviorSubject<Empty> = BehaviorSubject.create()
    override val requestUsagePermissionClicked: BehaviorSubject<Empty> = BehaviorSubject.create()

    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        hintsListView = findView(R.id.list_hints)
        swipeRefresh = findView(R.id.swipe_refresh)
        usagePermissionTextView = findView(R.id.tv_usage)
        hintsListView.layoutManager = LinearLayoutManager(this)
        hintsListView.adapter = adapter

        appOpsManager = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager

        val addButton = findViewById(R.id.btn_add) as FloatingActionButton
        addButton.setOnClickListener {
            startActivity(Intent(this, NewHintActivity::class.java))
        }

        DaggerHintsComponent.builder()
                .appComponent(appComponent)
                .hintsScreenModule(HintsScreenModule())
                .build()
                .inject(this)

        presenter.attachView(this)

        presenter.hints.subscribe { hints ->
            adapter.items.clear()
            adapter.items.addAll(hints)
            adapter.notifyDataSetChanged()
        }.addTo(disposable)

        presenter.requestUsagePermission.subscribe {
            startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        }.addTo(disposable)

        swipeRefresh.setOnRefreshListener { refreshed.onNext(Empty) }
        presenter.refreshing.subscribe { swipeRefresh.isRefreshing = it }.addTo(disposable)

        usagePermissionTextView.setOnClickListener { requestUsagePermissionClicked.onNext(Empty) }
        presenter.showUsagePermissionInfo.subscribe {
            usagePermissionTextView.visibility = if (it) VISIBLE else GONE
        }.addTo(disposable)
    }

    override fun onStart() {
        super.onStart()
        val hasUsagePermission = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), packageName) == AppOpsManager.MODE_ALLOWED
        usagePermissionGranted.onNext(hasUsagePermission)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val granted = grantResults.filter { it == PackageManager.PERMISSION_GRANTED }
                .map { permissions[it] }
        permissionsGranted.onNext(granted)
    }

    override val hintSelected: Observable<Int> = adapter.clicks

    override val isLocationPermissionGranted: Boolean
        get() = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    override val permissionsGranted: BehaviorSubject<List<String>> = BehaviorSubject.create()

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
        presenter.detachView()
    }

    class HintsAdapter : RecyclerView.Adapter<HintsAdapter.ViewHolder>() {

        val clicks: PublishSubject<Int> = PublishSubject.create()
        val items: MutableList<HintViewModel> = mutableListOf()

        override fun getItemCount(): Int = items.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val vm = items[position]
            holder.title.text = vm.title
            holder.image.setImageBitmap(vm.icon)
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

            init {
                itemView.setOnClickListener(this)
            }

            override fun onClick(v: View) {
                clicks.onNext(adapterPosition)
            }
        }
    }
}
