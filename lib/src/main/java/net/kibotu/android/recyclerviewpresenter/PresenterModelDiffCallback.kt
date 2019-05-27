package net.kibotu.android.recyclerviewpresenter

import androidx.recyclerview.widget.DiffUtil

open class PresenterModelDiffCallback(var newItems: List<PresenterModel<*>>, var oldItems: List<PresenterModel<*>>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldItems.size

    override fun getNewListSize(): Int = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = oldItems[oldItemPosition].uuid == newItems[newItemPosition].uuid

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = oldItems[oldItemPosition].model == (newItems[newItemPosition]).model

    //you can return particular field for changed item.
    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? = super.getChangePayload(oldItemPosition, newItemPosition)
}