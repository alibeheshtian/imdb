package com.example.yaran.base

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.yaran.R
import com.example.yaran.di.IRxSchedulers
import com.example.yaran.webService.ApiService
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext
import retrofit2.HttpException
import java.net.UnknownHostException

abstract class BaseViewModel(protected val context: Context) : ViewModel(), LifecycleObserver,
    KodeinAware {

    override val kodein by kodein(context)
    override val kodeinContext = kcontext(context)
    private val schedulers: IRxSchedulers by instance()
    private var compositeDisposable: CompositeDisposable? = null


    protected val apiService: ApiService by instance()

    val loading: MutableLiveData<Boolean> = MutableLiveData()


    override fun onCleared() {
        super.onCleared()
        compositeDisposable?.let {
            it.dispose()
            it.clear()
            compositeDisposable = null
        }
    }

    private fun addDisposable(disposable: Disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = CompositeDisposable()
        }
        compositeDisposable!!.add(disposable)
    }

    protected fun <T> callService(
        singleApi: Single<T>,
        onSuccess: (T) -> Unit,
        onFinish: (() -> Unit)? = null,
        onError: ((HttpException) -> Unit)? = null
    ) {

        if (onFinish == null) {
            loading.postValue(true)
        }
        addDisposable(
            singleApi.subscribeOn(schedulers.io())
                .observeOn(schedulers.main())
                .doFinally {
                    if (onFinish == null) {
                        loading.postValue(false)
                    } else {
                        onFinish()
                    }
                }
                .subscribe(
                    {
                        onSuccess(it)
                    },
                    { exception ->
                        when (exception) {
                            is HttpException ->
                                onError?.invoke(exception)
                            is UnknownHostException -> offlineDialog(
                                singleApi,
                                onSuccess,
                                onFinish,
                                onError
                            )
                        }
                    })
        )
    }

    private fun <T> offlineDialog(
        singleApi: Single<T>,
        onSuccess: (T) -> Unit,
        onFinish: (() -> Unit)? = null,
        onError: ((HttpException) -> Unit)? = null
    ) {

        val builder = AlertDialog.Builder(context)

        builder.setCancelable(false).setMessage(R.string.check_your_internet)
            .setNegativeButton(R.string.retry) { _, _ ->
                callService(
                    singleApi,
                    onSuccess,
                    onFinish,
                    onError
                )
            }


    }


}
