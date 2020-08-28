package com.kalap.contacts.common

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlin.reflect.KClass

class BaseAdapter<VH: BaseViewHolder<T>, T>(private val kClass: KClass<VH>) : RecyclerView.Adapter<VH>() {

    private val data = arrayListOf<T>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return kClass.java.getConstructor(ViewGroup::class.java).newInstance(parent)
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bindTo(data[position])
    override fun getItemCount() = data.size

    fun updateItems(newData: List<T>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }
}