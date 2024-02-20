package com.example.piterbridges.presentation.bridges

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.piterbridges.R
import com.example.piterbridges.databinding.FragmentListBridgeBinding
import com.example.piterbridges.presentation.BaseFragment
import com.example.piterbridges.presentation.LoadState

class ListBridgeFragment : BaseFragment(R.layout.fragment_list_bridge) {
    private val binding by viewBinding(FragmentListBridgeBinding::bind)
    private val viewModel: ListBridgesViewModel by appViewModels()
    private val bridgesAdapter = BridgesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            viewModel.loadBridges()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(binding.appBarLayout) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.statusBars())
            view.updatePadding(top = insets.top)
            windowInsets
        }
        binding.recyclerViewBridges.adapter = bridgesAdapter.apply {
            bridgeListener = BridgeListener { bridge ->
                val action =
                    ListBridgeFragmentDirections.actionListBridgeFragmentToInfoBridgeFragment(
                        bridge.id, bridge.divorces.toTypedArray()
                    )
                findNavController().navigate(action)
            }
        }
        viewModel.bridgesLiveData.observe(viewLifecycleOwner) { state ->
            when (state) {
                is LoadState.Data -> {
                    binding.progressBar.visibility = View.GONE
                    if (state.data.isNotEmpty()) {
                        bridgesAdapter.setList(state.data)
                    } else {
                        binding.recyclerViewBridges.visibility = View.GONE
                        binding.textViewError.visibility = View.VISIBLE
                        binding.textViewError.text = getString(R.string.data_not_download)
                    }
                }

                is LoadState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is LoadState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.recyclerViewBridges.visibility = View.GONE
                    binding.textViewError.visibility = View.VISIBLE
                    binding.textViewError.text = state.toString()
                }
            }
        }
        binding.toolbar.menu.findItem(R.id.itemMap).setOnMenuItemClickListener {
            val action = ListBridgeFragmentDirections.actionListBridgeFragmentToMapFragment()
            findNavController().navigate(action)
            true
        }
    }
}