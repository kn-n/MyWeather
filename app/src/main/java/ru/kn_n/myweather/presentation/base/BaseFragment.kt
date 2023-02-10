package ru.kn_n.myweather.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import ru.kn_n.myweather.utils.Resource
import ru.kn_n.myweather.utils.Status

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<VB: ViewBinding>(
    private val inflate: Inflate<VB>
): Fragment() {

    private var _binding: VB? = null
    val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = inflate.invoke(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun <T> showRequestResult(
        resource: Resource<T>,
        doOnSuccess: () -> Unit,
        doOnError: () -> Unit,
        doOnLoading: () -> Unit
    ){
        when(resource.status) {
            Status.LOADING -> doOnLoading()
            Status.SUCCESS -> doOnSuccess()
            Status.ERROR -> doOnError()
        }
    }
}