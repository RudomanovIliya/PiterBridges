package com.example.piterbridges.presentation.bridges

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.piterbridges.R
import com.example.piterbridges.databinding.ViewBridgeRowInfoBinding
import com.example.piterbridges.presentation.bridges.model.Bridge
import com.example.piterbridges.presentation.bridges.model.stateBridge

class BridgeRowViewInfoView : ConstraintLayout {
    private val binding = ViewBridgeRowInfoBinding.inflate(LayoutInflater.from(context), this)

    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(
        context, attributeSet, defStyleAttr
    )

    fun bind(bridge: Bridge) = with(binding) {
        textViewTitle.text = bridge.title
        bridge.divorces.forEach { position ->
            val stringBuilderTime = StringBuilder()
            stringBuilderTime.append(position.startTime).append(" - ").append(position.endTime)
                .append("    ")
            textViewTime.append(stringBuilderTime)
        }

        when (stateBridge(bridge.divorces)) {
            0 -> imageViewBridge.setImageResource(R.drawable.ic_brige_soon)
            1 -> imageViewBridge.setImageResource(R.drawable.ic_brige_normal)
            2 -> imageViewBridge.setImageResource(R.drawable.ic_brige_late)
        }
    }

    fun setTitleText(text: String) {
        binding.textViewTitle.text = text
    }

    fun setImage(@DrawableRes id: Int) {
        binding.imageViewBridge.setImageResource(id)
    }

    fun setTimeText(text: StringBuilder) {
        binding.textViewTime.text = text
    }
}