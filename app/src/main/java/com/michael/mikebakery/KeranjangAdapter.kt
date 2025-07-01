package com.michael.mikebakery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.michael.mikebakery.databinding.ItemKeranjangBinding
import java.text.NumberFormat // <-- Tambahkan import jika belum ada
import java.util.Locale

class KeranjangAdapter(private var keranjangList: List<KeranjangItem>) : RecyclerView.Adapter<KeranjangAdapter.KeranjangViewHolder>() {

    inner class KeranjangViewHolder(private val binding: ItemKeranjangBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: KeranjangItem) {
            binding.textViewNamaKueKeranjang.text = item.kue.nama
            binding.textViewKuantitasKeranjang.text = "x ${item.kuantitas}"

            // Hitung subtotal
            val subtotal = item.kue.harga * item.kuantitas

            // Format dan tampilkan subtotal
            val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
            binding.textViewSubtotalKeranjang.text = formatter.format(subtotal)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeranjangViewHolder {
        val binding = ItemKeranjangBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return KeranjangViewHolder(binding)
    }

    override fun onBindViewHolder(holder: KeranjangViewHolder, position: Int) {
        holder.bind(keranjangList[position])
    }

    override fun getItemCount(): Int = keranjangList.size

    fun updateData(newKeranjangList: List<KeranjangItem>) {
        this.keranjangList = newKeranjangList
        notifyDataSetChanged()
    }
}