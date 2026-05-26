package com.example.demokateringnena.viewmodel

import androidx.lifecycle.ViewModel
import com.example.demokateringnena.data.Order
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AdminViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders

    init {
        listenToOrdersRealtime()
    }

    private fun listenToOrdersRealtime() {
        db.collection("orders")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener

                if (snapshot != null) {
                    val list = snapshot.map { doc ->
                        val phone = doc.getString("customerPhone") ?: "628123456789" // Masukkan nomor WA Anda untuk uji coba
                        Order(
                            id = doc.id,
                            customerName = doc.getString("customerName") ?: "",
                            customerPhone = phone,
                            description = doc.getString("description") ?: "",
                            quantity = (doc.getLong("quantity") ?: 0L).toInt(),
                            scheduleDate = doc.getString("scheduleDate") ?: "",
                            status = doc.getString("status") ?: "MENUNGGU_KONFIRMASI"
                        )
                    }
                    _orders.value = list
                }
            }
    }

    fun updateOrderStatus(orderId: String, newStatus: String) {
        db.collection("orders").document(orderId)
            .update("status", newStatus)
    }
}