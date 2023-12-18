package com.wkhalil.eshop.fragments.shopping

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.wkhalil.eshop.R
import com.wkhalil.eshop.adapters.HomeViewpagerAdapter
import com.wkhalil.eshop.databinding.FragmentHomeBinding
import com.wkhalil.eshop.fragments.categories.AccessoryFragment
import com.wkhalil.eshop.fragments.categories.ChairFragment
import com.wkhalil.eshop.fragments.categories.CupboardFragment
import com.wkhalil.eshop.fragments.categories.FurnitureFragment
import com.wkhalil.eshop.fragments.categories.MainCategoryFragment
import com.wkhalil.eshop.fragments.categories.TableFragment
import com.google.android.material.tabs.TabLayoutMediator
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

class HomeFragment: Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val categoriesFragments = arrayListOf<Fragment>(
            MainCategoryFragment(),
            ChairFragment(),
            CupboardFragment(),
            TableFragment(),
            AccessoryFragment(),
            FurnitureFragment()
        )

        val tabTitles = mutableMapOf(
            "Main" to R.drawable.ic_home,
            "Chair" to R.drawable.ic_chair,
            "Cupboard" to R.drawable.ic_cupboard,
            "Table" to R.drawable.ic_table,
            "Accessory" to R.drawable.ic_accessories,
            "Furniture" to R.drawable.ic_furniture
        )
        val titles = ArrayList(tabTitles.keys)

        val viewpagerAdapter =  HomeViewpagerAdapter(categoriesFragments, childFragmentManager, lifecycle)
        binding.vpHome.adapter = viewpagerAdapter
        TabLayoutMediator(binding.tlHome, binding.vpHome) { tab, position ->
            //tab.text = titles[position]
            when(position){
                0 -> tab.text = "Main"
                1 -> tab.text = "Chair"
                2 -> tab.text = "Cupboard"
                3 -> tab.text = "Table"
                4 -> tab.text = "Accessory"
                5 -> tab.text = "Furniture"

            }
        }.attach()

        tabTitles.values.forEachIndexed { index, imageResId ->
            val textView = LayoutInflater.from(requireContext()).inflate(R.layout.tab_title, null) as TextView
            textView.setCompoundDrawablesWithIntrinsicBounds(imageResId,0,0,0)
            textView.compoundDrawablePadding = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 10f, resources.displayMetrics
            ).roundToInt()
            binding.tlHome.getTabAt(index)?.customView = textView
        }

        binding.vpHome.isUserInputEnabled = false
    }
}