package com.example.playlistmaker.ui.media.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.LikedTracksFragmentBinding
import com.example.playlistmaker.ui.media.view_model.TracksFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class TracksFragment : Fragment() {

    private val viewModel by viewModel<TracksFragmentViewModel>()

    private lateinit var binding: LikedTracksFragmentBinding

    override fun onCreateView(inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LikedTracksFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        fun newInstanse() = TracksFragment()
    }
}