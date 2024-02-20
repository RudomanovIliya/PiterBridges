package com.example.piterbridges.presentation.bridges

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.piterbridges.R
import com.example.piterbridges.databinding.FragmentInfoBridgeBinding
import com.example.piterbridges.presentation.BaseFragment
import com.example.piterbridges.presentation.LoadState
import com.example.piterbridges.presentation.bridges.model.Divorces
import com.example.piterbridges.presentation.bridges.model.stateBridge
import kotlinx.coroutines.launch

class InfoBridgeFragment : BaseFragment(R.layout.fragment_info_bridge) {
    private val binding by viewBinding(FragmentInfoBridgeBinding::bind)
    private val viewModel: InfoBridgeViewModel by appViewModels()
    private val arguments: InfoBridgeFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            viewModel.loadBridge(arguments.bridgeId)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        viewModel.bridgeLiveData.observe(viewLifecycleOwner) { state ->
            when (state) {
                is LoadState.Data -> {
                    binding.progressBarBridge.visibility = View.GONE
                    binding.bridgeRowViewInfo.setTitleText(state.data.title)
                    val listTime: List<Divorces> = arguments.divorces.toList()
                    when (stateBridge(listTime)) {
                        0 -> {
                            binding.bridgeRowViewInfo.setImage(R.drawable.ic_brige_soon)
                            loadImage(state.data.photoOpenUrl)
                        }

                        1 -> {
                            binding.bridgeRowViewInfo.setImage(R.drawable.ic_brige_normal)
                            loadImage(state.data.photoOpenUrl)
                        }

                        2 -> {
                            binding.bridgeRowViewInfo.setImage(R.drawable.ic_brige_late)
                            loadImage(state.data.photoCloseUrl)
                        }
                    }
                    val stringBuilderTime = StringBuilder()
                    listTime.forEach { position ->
                        stringBuilderTime.append(position.startTime).append(" - ")
                            .append(position.endTime).append("    ")
                    }
                    binding.bridgeRowViewInfo.setTimeText(stringBuilderTime)
                    binding.textViewDescription.text = state.data.description
                }

                is LoadState.Loading -> {
                    binding.progressBarBridge.visibility = View.VISIBLE
                }

                is LoadState.Error -> {
                    binding.progressBarBridge.visibility = View.GONE
                    binding.bridgeRowViewInfo.visibility = View.GONE
                    binding.textViewDescription.visibility = View.GONE
                    binding.textViewError.visibility = View.VISIBLE
                    binding.textViewError.text = state.toString()
                }
            }
        }
    }

    private fun loadImage(imageURL: String?) {
        lifecycleScope.launch {
            binding.progressBarGlide.isVisible = true
            Glide.with(binding.root)
                .load(imageURL)
                .into(binding.imageViewTitle)
            binding.progressBarGlide.isVisible = false
        }
    }
}