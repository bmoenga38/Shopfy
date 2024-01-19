package com.suitcase.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.suitcase.data.Item
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class AddItemToStorageViewModel @Inject constructor(
    private val firebaseStorage: FirebaseStorage,
    private val firebaseDatabase: FirebaseDatabase,
    private val currentUser: FirebaseUser?
) : ViewModel() {

    private val _uploadImageStatus = MutableStateFlow("")
    val uploadStatus: MutableStateFlow<String>
        get() = _uploadImageStatus


    private val _uploadProduct = MutableStateFlow("")
    val uploadProduct: MutableStateFlow<String>
        get() = _uploadProduct

    // Getting Values from Order
    private val _getItems = MutableStateFlow<List<Item>>(emptyList())
//    val getItems = _getItems

    var delegatedItem: Item? = null

    init {
        getDelegatedItems()
    }

    fun uploadItem(name: String, selectedImage: Uri) {
        if (currentUser != null) {
            var imageRef = firebaseStorage.reference.child("images")
            imageRef = imageRef.child(System.currentTimeMillis().toString())

            imageRef.putFile(selectedImage).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    imageRef.downloadUrl.addOnSuccessListener { uri ->

                        val item = Item(
                            name = name,
                            imageUrl = uri.toString(),
                            bought = false,
                            delegated = false
                        )
                        firebaseDatabase.reference.child("users")
                            .child(currentUser.uid)
                            .child(name)
                            .setValue(item).addOnCompleteListener { databaseTask ->
                                if (databaseTask.isSuccessful) {
                                    _uploadImageStatus.value = "Upload Successful"

                                } else {
                                    _uploadImageStatus.value = "Upload failed reason"

                                }
                            }
                    }
                }
            }
        }


    }

    fun manipulateDelegated(item: Item, bought: Boolean, delegated: Boolean) {
        if (currentUser != null) {
            val newValue = HashMap<String, Any>()
            newValue["bought"] = bought
            newValue["delegated"] = delegated
            newValue["imageUrl"] = item.imageUrl
            newValue["name"] = item.name

            firebaseDatabase.reference.child("users")
                .child(currentUser.uid)
                .child(item.name)
                .updateChildren(newValue)
        }
    }

    fun removeItem(item: Item) {

        if (currentUser != null) {

            firebaseDatabase.reference.child("users")
                .child(currentUser.uid)
                .child(item.name)
                .removeValue()
        }
    }

    private fun getDelegatedItems() {
        if (currentUser != null) {
            firebaseDatabase.reference.child("users")
                .child(currentUser.uid)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val orderList = mutableListOf<Item>()
                        for (snap in snapshot.children) {
                            val orderItem = snap.getValue(Item::class.java)
                            if (orderItem != null) {
                                if (orderItem.delegated) {
                                    orderList.add(orderItem)
                                }

                            }
                            _getItems.value = orderList

                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d("error", error.toString())
                    }

                })
        }
    }

}
