package com.mabrouk.dalilmuslim

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.mabrouk.dalilmuslim.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import np.com.susanthapa.curved_bottom_navigation.CbnMenuItem

@AndroidEntryPoint
class MainActivity : LocalizationActivity() {
    lateinit var viewBinding : ActivityMainBinding
    lateinit var navController: NavController
    private val menuItems = arrayOf(
        CbnMenuItem(R.drawable.ic_hadec, R.drawable.avd_hadec, R.id.quranFragment),
        CbnMenuItem(R.drawable.ic_azqar, R.drawable.avd_azqar, R.id.quranFragment),
        CbnMenuItem(R.drawable.ic_quran, R.drawable.avd_quran, R.id.quranFragment),
        CbnMenuItem(R.drawable.ic_story, R.drawable.avd_story, R.id.quranFragment),
        CbnMenuItem(R.drawable.ic_inheritance, R.drawable.avd_inhertince, R.id.quranFragment)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val navHostFragment: Fragment? =
            supportFragmentManager.findFragmentById(R.id.main_nav)
        navHostFragment?.findNavController()?.also { navController = it }

        viewBinding.navView.setMenuItems(menuItems, 2)
        //viewBinding.navView.setupWithNavController(navController)
    }
}