package ru.kn_n.myweather.presentation

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.androidx.AppNavigator
import ru.kn_n.myweather.R
import ru.kn_n.myweather.di.Scopes
import ru.kn_n.myweather.utils.Constants
import ru.kn_n.myweather.utils.haveLocationPermissions
import toothpick.Toothpick
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: MainViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    private val navigator = AppNavigator(this, R.id.container)

    override fun onCreate(savedInstanceState: Bundle?) {
        Toothpick.inject(this@MainActivity, Toothpick.openScope(Scopes.APP_SCOPE))
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViewModel()

//        viewModel.checkLocationPermissions(this)
        if (!haveLocationPermissions(this)){
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                Constants.PERMISSION_ID
            )
        }
    }

    private fun setupViewModel() {
        viewModelFactory = Toothpick.openScope(Scopes.APP_SCOPE).getInstance(ViewModelFactory::class.java)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        viewModel.onRequestPermissionsResult(requestCode, grantResults)
    }

    override fun onResume() {
        super.onResume()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }
}