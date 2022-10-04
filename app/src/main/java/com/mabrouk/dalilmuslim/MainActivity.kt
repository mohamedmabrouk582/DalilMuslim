package com.mabrouk.dalilmuslim

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.mabrouk.core.network.loadLibrary
import com.mabrouk.dalilmuslim.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : LocalizationActivity() {
    lateinit var viewBinding : ActivityMainBinding
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        loadLibrary()
        val navHostFragment: Fragment? =
            supportFragmentManager.findFragmentById(R.id.main_nav)
        navHostFragment?.findNavController()?.also { navController = it }
        viewBinding.navView.setupWithNavController(navController)
    }
}