package ru.kn_n.myweather.di

import android.app.Application
import android.content.Context
import ru.kn_n.myweather.data.repositories.MainRepository
import toothpick.config.Module

fun appModule(context: Context) = Module().apply {
    //Global
    bind(Context::class.java).toInstance(context)
}
