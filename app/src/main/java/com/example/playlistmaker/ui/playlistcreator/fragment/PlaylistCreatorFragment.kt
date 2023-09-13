package com.example.playlistmaker.ui.playlistcreator.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class PlaylistCreatorFragment : PlaylistBaseFragment() {

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val confirmDialog = MaterialAlertDialogBuilder(requireContext(), R.style.MyDialogTheme)
            .setTitle(requireContext().getString(R.string.cancel_creating_of_playlist))
            .setMessage(requireContext().getString(R.string.all_unsaved_data_will_be_lost))
            .setNegativeButton(requireContext().getString(R.string.cancel)) { _, _ -> }
            .setPositiveButton(requireContext().getString(R.string.finish)) { _, _ ->
                findNavController().navigateUp()
            }

        binding.playlistImage.setOnClickListener{
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.editTextPlaylistTitle.addTextChangedListener(textWatcher)

        binding.buttonCreatePlaylist.setOnClickListener {
            viewModel.insertPlaylist(binding.editTextPlaylistTitle.text.toString(),
                imageUri.toString(),
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

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object: OnBackPressedCallback(true) {
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