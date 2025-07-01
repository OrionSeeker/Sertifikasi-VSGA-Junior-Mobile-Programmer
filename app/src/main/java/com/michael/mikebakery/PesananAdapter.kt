package com.michael.mikebakery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.michael.mikebakery.databinding.ItemPesananBinding
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale
import android.content.Intent

class PesananAdapter(private val pesananList: List<Pesanan>) : RecyclerView.Adapter<PesananAdapter.PesananViewHolder>() {

    inner class PesananViewHolder(private val binding: ItemPesananBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pesanan: Pesanan) {
            binding.textViewNamaPelanggan.text = pesanan.namaPelanggan

            val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
            binding.textViewTotalHargaPesanan.text = "Total: ${currencyFormatter.format(pesanan.totalHarga)}"

            pesanan.timestamp?.let {
                val dateFormatter = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale("in", "ID"))
                binding.textViewTanggalPesanan.text = dateFormatter.format(it)
            }

            itemView.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, DetailPesananActivity::class.java).apply {
                    putExtra("EXTRA_PESANAN", pesanan)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PesananViewHolder {
        val binding = ItemPesananBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PesananViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PesananViewHolder, position: Int) {
        holder.bind(pesananList[position])
    }

    override fun getItemCount(): Int = pesananList.size
}