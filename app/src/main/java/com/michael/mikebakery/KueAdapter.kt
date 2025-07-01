package com.michael.mikebakery

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast // <-- TAMBAHKAN IMPORT INI
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.michael.mikebakery.databinding.ItemKueBinding
import java.text.NumberFormat
import java.util.Locale

class KueAdapter(private var kueList: List<Kue>) : RecyclerView.Adapter<KueAdapter.KueViewHolder>() {

    inner class KueViewHolder(private val binding: ItemKueBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(kue: Kue) {
            binding.textViewNamaKue.text = kue.nama

            val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
            binding.textViewHargaKue.text = formatter.format(kue.harga)

            Glide.with(itemView.context)
                .load(kue.imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .into(binding.imageViewKue)

            binding.buttonAddToCart.setOnClickListener {
                Keranjang.addItem(kue)
                Toast.makeText(itemView.context, "${kue.nama} ditambahkan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KueViewHolder {
        val binding = ItemKueBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return KueViewHolder(binding)
    }

    override fun onBindViewHolder(holder: KueViewHolder, position: Int) {
        holder.bind(kueList[position])
    }

    override fun getItemCount(): Int = kueList.size

    fun updateData(newKueList: List<Kue>) {
        this.kueList = newKueList
        notifyDataSetChanged()
    }
}