package com.demo.sabilabapp.Adapters.SequenceAdmin

import androidx.recyclerview.widget.RecyclerView

interface OnItemUpdateListener {
    fun onItemUpdated(idPedido: Int, recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>)
}