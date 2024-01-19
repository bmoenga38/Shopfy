package com.suitcase.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.suitcase.data.Item
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetItemsViewModel @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase,
    private val currentUser: FirebaseUser?
): ViewModel (){
    private val _items= MutableStateFlow<List<Item>>(emptyList())
    val items = _items



    init {
        getItems()
    }

    private fun getItems() = viewModelScope.launch {
        if (currentUser != null){
            firebaseDatabase.reference.child("users")
                .child(currentUser.uid)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val orderList = mutableListOf<Item>()

                        for (snap in snapshot.children) {
                            val item = snap.getValue(Item::class.java)
                            if (item != null) {
                                orderList.add(item)
                            }
                            _items.value = orderList

                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d("error", error.toString())
                    }

                })
        }

    }


}