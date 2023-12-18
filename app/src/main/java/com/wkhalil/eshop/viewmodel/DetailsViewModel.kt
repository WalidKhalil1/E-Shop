package com.wkhalil.eshop.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wkhalil.eshop.data.CartProduct
import com.wkhalil.eshop.firebase.FirebaseCommon
import com.wkhalil.eshop.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val firebaseCommon: FirebaseCommon
): ViewModel() {
    private val _addToCard = MutableStateFlow<Resource<CartProduct>>(Resource.Unspecified())
    val addToCard = _addToCard.asStateFlow()

    fun addUpdateProductInCart(cartProduct: CartProduct) {
        viewModelScope.launch {
            _addToCard.emit(
                Resource.Loading()
            )
        }
        firestore.collection("user")
            .document(auth.uid!!)
            .collection("cart")
            .whereEqualTo("product.id", cartProduct.product.id)
            .get()
            .addOnSuccessListener {
                it.documents.let {
                    if (it.isEmpty()) {
                        addNewProduct(cartProduct)

                    } else {
                        val product = it.first().toObject(CartProduct::class.java)
                        if (product == cartProduct) {
                            val documentId = it.first().id
                            increaseQuantity(documentId, cartProduct)
                        } else {
                            addNewProduct(cartProduct)
                        }
                    }
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _addToCard.emit(
                        Resource.Error(it.message.toString())
                    )
                }
            }
    }

    private fun addNewProduct(cartProduct: CartProduct){
        firebaseCommon.addProductToCart(cartProduct){ addedProduct, e ->
           viewModelScope.launch {
               if(e == null)
                   _addToCard.emit(Resource.Success(addedProduct!!))
               else
                   _addToCard.emit(Resource.Error(e.message.toString()))
           }
        }
    }

    private fun  increaseQuantity(documentId: String, cartProduct: CartProduct){
        firebaseCommon.increaseQuantity(documentId){ _, e ->
            viewModelScope.launch {
                if(e == null)
                    _addToCard.emit(Resource.Success(cartProduct))
                else
                    _addToCard.emit(Resource.Error(e.message.toString()))
            }
        }
    }
}