package net.kibotu.android.recyclerviewpresenter.app

import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.exozet.android.core.extensions.logv
import com.exozet.android.core.extensions.resInt
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import kotlinx.android.synthetic.main.activity_pagination.*
import kotlinx.android.synthetic.main.photo_presenter_item.view.*
import net.kibotu.android.recyclerviewpresenter.v2.RecyclerViewHolder


/**
 * https://guides.codepath.com/android/Paging-Library-Guide
 */
class PaginationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_pagination)

        val adapter = FakePageListAdapter()
        list.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        list.adapter = adapter.decorateWithAlphaScaleAdapter()

        val config = PagedList.Config.Builder()
            .setPageSize(30)
            .setPrefetchDistance(3)
            .setEnablePlaceholders(false)
            .build()

        // http://androidkt.com/paging-library-datasource/
        val pageKeyedDataSourceFactory = SimplePageKeyedDataSource.Factory()
        val pageKeyedDataSource = LivePagedListBuilder<Int, ViewModel<String>>(pageKeyedDataSourceFactory, config).build()
        val positionalDataSourceFactory = SimplePositionalDataSource.Factory()
        val positionalDataSource = LivePagedListBuilder<Int, ViewModel<String>>(positionalDataSourceFactory, config).build()
        val itemKeyedDataSourceFactory = SimpleItemKeyedDataSource.Factory()
        val itemKeyedDataSource = LivePagedListBuilder<String, ViewModel<String>>(itemKeyedDataSourceFactory, config).build()

        positionalDataSource.observe(this) {
            logv("positionalDataSource data: ${it.size}")
            adapter.submitList(it)
            swipe_refresh.isRefreshing = false
        }

        swipe_refresh.setOnRefreshListener {
            pageKeyedDataSourceFactory.dataSource.value?.invalidate()
//            positionalDataSourceFactory.dataSource.invalidate()
//            itemKeyedDataSourceFactory.dataSource.invalidate()
        }
    }

    class FakePageListAdapter : PagedListAdapter<ViewModel<String>, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            RecyclerViewHolder(R.layout.photo_presenter_item, parent)

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            with(holder.itemView) {

                val uri = Uri.parse(getItem(position)!!.t)
                val width = uri.pathSegments.takeLast(2).first().toInt()
                val height = uri.pathSegments.last().toInt()

                photo.minimumWidth = width
                photo.minimumHeight = height

                GlideApp.with(this.context.applicationContext)
                    .load(uri)
                    .transition(withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(photo)
                    .clearOnDetach()
            }
        }
    }

    companion object {

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ViewModel<String>>() {

            override fun areItemsTheSame(oldItem: ViewModel<String>, newItem: ViewModel<String>): Boolean = oldItem.uuid == newItem.uuid

            override fun areContentsTheSame(oldItem: ViewModel<String>, newItem: ViewModel<String>): Boolean = oldItem == newItem
        }

        inline fun <VH : RecyclerView.ViewHolder> RecyclerView.Adapter<VH>.decorateWithAlphaScaleAdapter(): ScaleInAnimationAdapter {
            val alphaAdapter = AlphaInAnimationAdapter(this, 0f).apply {
                setFirstOnly(false)
                setDuration(android.R.integer.config_mediumAnimTime.resInt)
            }
            return ScaleInAnimationAdapter(alphaAdapter, 1.5f).apply {
                setFirstOnly(false)
                setDuration(android.R.integer.config_shortAnimTime.resInt)
            }
        }
    }
}