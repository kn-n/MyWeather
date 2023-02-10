package ru.kn_n.myweather.di

import android.content.Context
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import ru.kn_n.myweather.data.api.APIPlace
import ru.kn_n.myweather.data.api.APIWeather
import ru.kn_n.myweather.data.api.RetrofitClientPlace
import ru.kn_n.myweather.data.api.RetrofitClientWeather
import ru.kn_n.myweather.presentation.model.LocationCache
import toothpick.ktp.binding.bind
import toothpick.ktp.binding.module

fun appModule(context: Context) = module {
    //Global
    bind(Context::class.java).toInstance(context)

    //Navigation
    val cicerone = Cicerone.create()
    bind(Router::class.java).toInstance(cicerone.router)
    bind(NavigatorHolder::class.java).toInstance(cicerone.getNavigatorHolder())

    bind(APIWeather::class.java).toProvider(RetrofitClientWeather::class.java)
    bind(APIPlace::class.java).toProvider(RetrofitClientPlace::class.java)

    bind(LocationCache::class.java).singleton()
}
