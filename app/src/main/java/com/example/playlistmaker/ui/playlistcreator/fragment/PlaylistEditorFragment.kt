package com.example.playlistmaker.ui.playlistcreator.fragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.model.Playlist
import java.io.Serializable

class PlaylistEditorFragment : PlaylistBaseFragment() {

    private var playlist: Playlist? = null

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editTextPlaylistTitle.addTextChangedListener(textWatcher)

        playlist = requireArguments().getSerializableExtra(EDIT_PLAYLIST, Playlist::class.java)
        viewModel.getImageFile(playlist?.image?.toUri()?.lastPathSegment)

        playlist?.name?.let { binding.editTextPlaylistTitle.setText(it) }
        playlist?.description?.let { binding.editTextPlaylistDescription.setText(it) }

        viewModel.image.observe(viewLifecycleOwner) {
            if (playlist?.image == "null") {
                binding.playlistImage.setImageResource(R.drawable.placeholder)
            } else {
                binding.playlistImage.setImageURI(it)
            }
        }

        binding.buttonCreatePlaylist.isEnabled = true
        context?.let {
            ContextCompat.getColor(
                it,
                R.color.blue_main
            )
        }?.let {
            binding.buttonCreatePlaylist.setBackgroundColor(
                it
            )
        }

        binding.headerText.text = requireContext().getString(R.string.edit)
        binding.buttonCreatePlaylist.text = requireContext().getString(R.string.save)

        binding.playlistImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.buttonCreatePlaylist.setOnClickListener {
            viewModel.updatePlaylist(
                id = playlist!!.id,
                image = if (imageUri != null) imageUri.toString() else playlist!!.image,
                name = binding.editTextPlaylistTitle.text.toString(),
                description = binding.editTextPlaylistDescription.text.toString(),
                tracks = playlist!!.tracks
            )
            binding.buttonCreatePlaylist.isEnabled = false
            if (imageUri.toString() != playlist!!.image) {
                 viewModel.saveToPrivateStorage(imageUri!!, onComplete)
            } else {
                findNavController().navigateUp()
            }
        }

        binding.goBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun <T : Serializable?> Bundle.getSerializableExtra(key: String, m_class: Class<T>): T {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            this.getSerializable(key, m_class)!!
        else
            this.getSerializable(key) as T
    }

    companion object {
        const val EDIT_PLAYLIST = "EDIT_PLAYLIST"
        fun createArgs(playlist: Playlist): Bundle {
            return bundleOf(EDIT_PLAYLIST to playlist)
        }
    }
}