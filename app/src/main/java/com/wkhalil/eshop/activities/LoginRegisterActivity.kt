package com.wkhalil.eshop.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wkhalil.eshop.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginRegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_register)
    }
}