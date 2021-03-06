package net.kibotu.android.recyclerviewpresenter.app.pagination

import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import kotlinx.android.synthetic.main.photo_presenter_item.view.*
import net.kibotu.android.recyclerviewpresenter.Adapter
import net.kibotu.android.recyclerviewpresenter.Presenter
import net.kibotu.android.recyclerviewpresenter.PresenterModel
import net.kibotu.android.recyclerviewpresenter.app.R
import net.kibotu.android.recyclerviewpresenter.app.misc.GlideApp

class PhotoPresenter : Presenter<String>() {

    override val layout = R.layout.photo_presenter_item

    override fun bindViewHolder(viewHolder: RecyclerView.ViewHolder, item: PresenterModel<String>, position: Int, payloads: MutableList<Any>?, adapter: Adapter): Unit = with(viewHolder.itemView) {

        val uri = Uri.parse(item.model)
        val width = uri.pathSegments.takeLast(2).first().toInt()
        val height = uri.pathSegments.last().toInt()

        photo.minimumWidth = width
        photo.minimumHeight = height

        GlideApp.with(this.context.applicationContext)
            .load(uri)
            .transition(withCrossFade())
            .into(photo)
            .clearOnDetach()
    }
}