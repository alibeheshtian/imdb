package com.example.yaran.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.kodein.di.Copy
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.android.retainedKodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton


abstract class BaseActivity(open val layout: Int) : AppCompatActivity(), KodeinAware {

    private val _parentKodein by kodein()

    override val kodein: Kodein by retainedKodein {
        extend(_parentKodein, copy = Copy.All)
        bind<AppCompatActivity>("ActivityContext") with singleton { this@BaseActivity }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout)
        viewIsReady(savedInstanceState)
    }

    abstract fun viewIsReady(savedInstanceState: Bundle?)

}