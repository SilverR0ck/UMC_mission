package com.example.flo

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.flo.databinding.FragmentDetailBinding
import com.example.flo.databinding.FragmentSongBinding


class SongFragment : Fragment() {
    lateinit var binding : FragmentSongBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSongBinding.inflate(inflater, container, false)

        binding.songMixoffTg.setOnClickListener {
            setMixStatus(true)
        }
        binding.songMixonTg.setOnClickListener {
            setMixStatus(false)
        }






        return binding.root
    }

    private fun setMixStatus(isMixing: Boolean){
        if(isMixing){ // Play 버튼이 눌렸을 때, Play이미지를 보이게 하고, Pause이미지는 뷰와 공간을 없애도록 GONE사용
            binding.songMixoffTg.visibility = View.GONE
            binding.songMixonTg.visibility = View.VISIBLE
        }
        else{
            binding.songMixonTg.visibility = View.GONE
            binding.songMixoffTg.visibility = View.VISIBLE

        }
    }

}