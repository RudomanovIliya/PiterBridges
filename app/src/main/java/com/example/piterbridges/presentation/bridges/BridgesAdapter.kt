package com.example.piterbridges.presentation.bridges

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.piterbridges.presentation.bridges.model.Bridge

class BridgesAdapter : RecyclerView.Adapter<BridgeViewHolder>() {
    private val items = mutableListOf<Bridge>()
    lateinit var bridgeListener: BridgeListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BridgeViewHolder {
        return BridgeViewHolder(parent, bridgeListener)
    }

    override fun onBindViewHolder(holder: BridgeViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(bridges: List<Bridge>) {
        items.clear()
        items.addAll(bridges)
        notifyDataSetChanged()
    }
}