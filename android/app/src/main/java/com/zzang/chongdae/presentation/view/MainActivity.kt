package com.zzang.chongdae.presentation.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zzang.chongdae.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
