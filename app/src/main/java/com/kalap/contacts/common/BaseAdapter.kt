package com.kalap.contacts.common

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlin.reflect.KClass

class BaseAdapter<VH: BaseViewHolder<T>, T>(private val kClass: KClass<VH>, private val listener: BaseListener<T>? = null) : RecyclerView.Adapter<VH>() {

    private val data = arrayListOf<T>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val holder = kClass.java.getConstructor(ViewGroup::class.java).newInstance(parent)
        holder.listener = listener
        return holder
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bindTo(data[position])
    override fun getItemCount() = data.size

    fun updateItems(newData: List<T>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }
}