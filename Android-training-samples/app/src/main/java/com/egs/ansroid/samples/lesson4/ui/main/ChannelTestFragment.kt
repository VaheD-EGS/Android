package com.egs.ansroid.samples.lesson4.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.viewModelScope
import com.egs.ansroid.samples.R
import com.egs.ansroid.samples.lesson4.data.ShapeCollector
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChannelTestFragment : Fragment() {

    companion object {
        fun newInstance() = ChannelTestFragment()
    }

    private lateinit var viewModel: ChannelViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ChannelViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.btnLocations).setOnClickListener {
            viewModel.getShapesByLocations()
        }
    }
}