package com.egs.ansroid.samples.lesson8.flows.callbackFlow

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.egs.ansroid.samples.lesson8.flows.errorhandling.TAG

class ZipFragment : Fragment() {
    private val viewModel: ZipViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.zipEvents.observe(this, Observer { zipEvent ->
            zipEvent?.also {
                handleZipEvent(it)
            }
        })
    }

    fun zipContent() {
        /* Simulate the generation of an outputStream after firing an Intent
         * to open a directory, using the Storage Access Framework */
        val outputStream = requireContext().contentResolver.openOutputStream(Uri.EMPTY)

        if (outputStream != null) {
            viewModel.zipContent(Content(), outputStream)
        }
    }

    private fun handleZipEvent(event: ZipEvent) {
        // do something with the event (show progression, display message, etc)
        Log.i(TAG, "$event")
    }
}