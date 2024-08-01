package com.zzang.chongdae.presentation.view.write

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.zzang.chongdae.data.remote.api.NetworkManager
import com.zzang.chongdae.data.remote.source.impl.OfferingWriteDataSourceImpl
import com.zzang.chongdae.data.repository.remote.OfferingWriteRepositoryImpl
import com.zzang.chongdae.databinding.ActivityOfferingWriteBinding

class OfferingWriteActivity : AppCompatActivity() {
    private var _binding: ActivityOfferingWriteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OfferingWriteViewModel by viewModels {
        OfferingWriteViewModel.getFactory(
            OfferingWriteRepositoryImpl(
                OfferingWriteDataSourceImpl(
                    NetworkManager.offeringsService(),
                ),
            ),
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
    }

    private fun initBinding() {
        _binding = ActivityOfferingWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun startActivity(context: Context) =
            Intent(context, OfferingWriteActivity::class.java).run {
                context.startActivity(this)
            }
    }
}
