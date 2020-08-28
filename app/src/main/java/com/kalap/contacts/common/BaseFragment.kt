package com.kalap.contacts.common

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.KClass

abstract class BaseFragment<VM: ViewModel, VH: BaseViewHolder<T>, T>(
        @LayoutRes contentLayoutId: Int,
        viewModelClass: KClass<VM>,
        viewHolderClass: KClass<VH>
) : Fragment(contentLayoutId) {

    protected val viewModel by lazy {
        ViewModelProvider(this).get(viewModelClass.java)
    }

    protected val adapter by lazy {
        BaseAdapter(viewHolderClass)
    }
}