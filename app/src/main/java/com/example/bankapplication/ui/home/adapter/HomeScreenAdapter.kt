package com.example.bankapplication.ui.home.adapter

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bankapplication.core.BaseViewHolder
import com.example.bankapplication.data.model.Movements
import com.example.bankapplication.databinding.MovementsItemViewBinding
import java.util.Locale

class HomeScreenAdapter(private val movementsList: List<Movements>): RecyclerView.Adapter<BaseViewHolder<*>>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val itemBinding = MovementsItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeScreenViewHolder(itemBinding, parent.context)
    }

    override fun getItemCount(): Int = movementsList.size

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (holder) {
            is HomeScreenViewHolder -> holder.bind(movementsList[position])
        }
    }

    private inner class HomeScreenViewHolder(
        val binding: MovementsItemViewBinding,
        val context: Context
    ): BaseViewHolder<Movements>(binding.root) {
        override fun bind(item: Movements) {
            Glide.with(context).load(item.imageMovement).centerCrop().into(binding.movementImage)
            binding.movementName.text = item.nameMovement
            val dateMov = item.dateMovement?.toDate()
            val formatDate = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(dateMov)
            binding.movementTimestap.text = formatDate
            if (item.approveMovement == true) {
                binding.movementApproved.text = "approved"
            }
            else {
                binding.movementApproved.text = "dismissed"
            }
            binding.movementPay.text = item.paymentMovement
        }
    }
}