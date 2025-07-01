package com.michael.mikebakery

data class KeranjangItem(val kue: Kue, var kuantitas: Int = 1)

object Keranjang {
    private val item = mutableListOf<KeranjangItem>()

    val items: List<KeranjangItem> get() = item

    fun addItem(kue: Kue) {
        val existingItem = item.find { it.kue.id == kue.id }
        if (existingItem != null) {
            existingItem.kuantitas++
        }
        else {
            item.add(KeranjangItem(kue))
        }
    }

    fun getTotalPrice(): Double {
        return item.sumOf { it.kue.harga * it.kuantitas }
    }

    fun clearCart() {
        item.clear()
    }

}