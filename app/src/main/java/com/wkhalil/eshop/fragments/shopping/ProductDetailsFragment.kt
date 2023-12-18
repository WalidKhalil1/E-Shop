package com.wkhalil.eshop.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.wkhalil.eshop.R
import com.wkhalil.eshop.adapters.ColorsAdapter
import com.wkhalil.eshop.adapters.SizesAdapter
import com.wkhalil.eshop.adapters.ViewPager2Images
import com.wkhalil.eshop.data.CartProduct
import com.wkhalil.eshop.databinding.FragmentProductDetailsBinding
import com.wkhalil.eshop.util.Resource
import com.wkhalil.eshop.util.hideBNV
import com.wkhalil.eshop.viewmodel.DetailsViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ProductDetailsFragment: Fragment() {
    private val args by navArgs<ProductDetailsFragmentArgs>()
    private lateinit var binding: FragmentProductDetailsBinding
    private val viewPagerAdapter by lazy {ViewPager2Images()}
    private val sizeAdapter by lazy{SizesAdapter(requireContext())}
    private val colorsAdapter by lazy{ColorsAdapter()}
    private var selectedColor: Int? = null
    private var selectedSize: String? = null
    private val viewModel by viewModels<DetailsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        hideBNV()

        binding = FragmentProductDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val product = args.product
        setupSizesRv()
        setupColorsRv()
        setupViewPager()

        binding.cvClose.setOnClickListener {
            findNavController().navigateUp()
        }

        sizeAdapter.onItemClick = {
            selectedSize = it
        }

        colorsAdapter.onItemClick = {
            selectedColor = it
        }

        binding.btnProductDetailsCart.setOnClickListener {
            viewModel.addUpdateProductInCart(CartProduct(product, 1, selectedColor, selectedSize))
        }

        lifecycleScope.launchWhenStarted {
            viewModel.addToCard.collectLatest {
                when(it){
                    is Resource.Loading ->{
                        binding.btnProductDetailsCart.startAnimation()
                    }
                    is Resource.Success ->{
                        binding.btnProductDetailsCart.revertAnimation()
                        binding.btnProductDetailsCart.setBackgroundColor(resources.getColor(R.color.black))
                    }
                    is Resource.Error -> {
                        binding.btnProductDetailsCart.revertAnimation()
                        Snackbar.make(requireView(), it.message.toString(), Snackbar.LENGTH_SHORT).show()

                    }
                    else -> Unit
                }
            }
        }

        binding.apply {
            tvProductDetailsName.text = product.name
            tvProductDetailsPrice.text = "$ ${product.price}"
            tvProductDetailsDescription.text = product.description

            if(product.colors.isNullOrEmpty())
                tvProductDetailsColors.visibility = View.INVISIBLE
            if(product.sizes.isNullOrEmpty())
                tvProductDetailsSizes.visibility = View.INVISIBLE
        }

        viewPagerAdapter.differ.submitList(product.images)
        product.colors?.let{ colorsAdapter.differ.submitList(it)}
        product.sizes?.let{ sizeAdapter.differ.submitList(it)}

    }

    private fun setupViewPager() {
        binding.apply {
            vpProductDetails.adapter = viewPagerAdapter
        }
    }

    private fun setupColorsRv() {
        binding.rvProductDetailsColors.apply {
            adapter = colorsAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setupSizesRv() {
        binding.rvProductDetailsSizes.apply {
            adapter = sizeAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

}