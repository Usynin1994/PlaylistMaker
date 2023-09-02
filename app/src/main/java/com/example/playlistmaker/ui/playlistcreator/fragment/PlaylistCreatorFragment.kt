package com.example.playlistmaker.ui.playlistcreator.fragment

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.example.playlistmaker.ui.playlistcreator.viewmodel.PlaylistCreatorViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistCreatorFragment : Fragment() {

    private val viewModel by viewModel<PlaylistCreatorViewModel>()

    private var _binding: FragmentCreatePlaylistBinding? = null
    private val binding get() = _binding!!

    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    binding.playlistImage.setImageURI(uri)
                    imageUri = uri
                }
            }

        val confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(requireContext().getString(R.string.cancel_creating_of_playlist))
            .setMessage(requireContext().getString(R.string.all_unsaved_data_will_be_lost))
            .setNeutralButton(requireContext().getString(R.string.cancel)) { _, _ -> }
            .setPositiveButton(requireContext().getString(R.string.finish)) { _, _ ->
                findNavController().navigateUp()
            }

        binding.playlistImage.setOnClickListener{
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.editTextPlaylistTitle.doOnTextChanged { text, _, _, _ ->
            if (text != null) {
                if (text.isNotEmpty()) {
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

        binding.buttonCreatePlaylist.setOnClickListener {
            viewModel.insertPlaylist(binding.editTextPlaylistTitle.text.toString(),
                imageUri,
                binding.editTextPlaylistDescription.text.toString()
                )
            imageUri?.let {viewModel.saveToPrivateStorage(it)}

            Toast.makeText(
                requireContext(),
                getString(R.string.playlist_created, binding.editTextPlaylistTitle.text.toString()),
                Toast.LENGTH_LONG
            ).show()

            findNavController().navigateUp()
        }

        binding.goBack.setOnClickListener {
            if (imageUri != null ||
                !binding.editTextPlaylistTitle.text.isNullOrEmpty() ||
                !binding.editTextPlaylistDescription.text.isNullOrEmpty()) {
                confirmDialog.show()
            } else {
                findNavController().navigateUp()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (imageUri != null ||
                    !binding.editTextPlaylistTitle.text.isNullOrEmpty() ||
                    !binding.editTextPlaylistDescription.text.isNullOrEmpty()) {
                    confirmDialog.show()
                } else {
                    findNavController().navigateUp()
                }
            }
        })
    }
}