package com.zzang.chongdae.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.zzang.chongdae.R

class CommentRoomFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_comment_room, container, false)
    }

    /*companion object {
        @JvmStatic
        fun newInstance(
            param1: String,
            param2: String,
        ) = CommentRoomFragment().apply {
            arguments =
                Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
        }
    }*/
}
