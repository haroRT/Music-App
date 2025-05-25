package com.example.haonv.ui.playlist.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.haonv.databinding.DialogAddPlaylistBinding
import com.example.haonv.databinding.DialogAddSongToPlaylistBinding

class DialogRenamePlaylist : DialogFragment() {
    private var _binding: DialogAddPlaylistBinding? = null
    private val binding get() = _binding!!
    private var listener: OnClickListener? = null

    fun setOnItemClickListener(listener: OnClickListener) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogAddPlaylistBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()

        binding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        binding.btnCreate.setOnClickListener {
            listener?.onClick(binding.edtPlaylistName.text.toString())
            dialog.dismiss()
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface OnClickListener {
        fun onClick(namePlaylist: String)
    }
}
