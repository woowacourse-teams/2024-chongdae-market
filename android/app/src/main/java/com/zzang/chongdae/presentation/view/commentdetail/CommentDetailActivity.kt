package com.zzang.chongdae.presentation.view.commentdetail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.zzang.chongdae.databinding.ActivityCommentDetailBinding

class CommentDetailActivity : AppCompatActivity() {
    private var _binding: ActivityCommentDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
    }

    private fun initBinding() {
        _binding = ActivityCommentDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
