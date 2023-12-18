package com.wkhalil.eshop.util

import android.view.View
import androidx.fragment.app.Fragment
import com.wkhalil.eshop.R
import com.wkhalil.eshop.activities.ShoppingActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

fun Fragment.hideBNV(){
    val bnv = (activity as ShoppingActivity).findViewById<BottomNavigationView>(R.id.bnv_shopping)
    bnv.visibility = View.GONE
}

fun Fragment.showBNV(){
    val bnv = (activity as ShoppingActivity).findViewById<BottomNavigationView>(R.id.bnv_shopping)
    bnv.visibility = View.VISIBLE
}