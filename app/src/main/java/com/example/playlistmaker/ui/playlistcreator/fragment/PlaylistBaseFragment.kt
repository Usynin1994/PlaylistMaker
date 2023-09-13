package com.example.playlistmaker.ui.playlistcreator.fragment

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.example.playlistmaker.ui.playlistcreator.viewmodel.PlaylistCreatorViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

abstract class PlaylistBaseFragment : Fragment() {

    val viewModel by viewModel<PlaylistCreatorViewModel>()

    var _binding: FragmentCreatePlaylistBinding? = null
    val binding get() = _binding!!

    var imageUri: Uri? = null

    val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                binding.playlistImage.setImageURI(uri)
                imageUri = uri
            }
        }

    val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(p0: Editable?) {
            if (p0 != null) {
                if (p0.isNotEmpty()) {
                    binding.buttonCreatePlaylist.isEnabled = true
                    context?.let {
                        ContextCompat.getColor(
                            it,
                            R.color.blue_main)
                    }?.let {
                        binding.buttonCreatePlaylist.setBackgroundColor(
                            it
                        )
                    }
                } else {
                    binding.buttonCreatePlaylist.isEnabled = false
                    context?.let {
                        ContextCompat.getColor(
                            it,
                            R.color.grey_main)
                    }?.let {
                        binding.buttonCreatePlaylist.setBackgroundColor(
                            it
                        )
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}