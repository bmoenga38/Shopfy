package com.suitcase.repositories

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.suitcase.data.DataOrException
import com.suitcase.data.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class itemsRepoImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) : itemsRepo {
    override suspend fun getItems(userId: String): Flow<DataOrException<Item, String, Boolean>> {
        return flow {
            emit(DataOrException.Loading(loading = true))

            try {
                val tempList = mutableListOf<Item>()

                firebaseDatabase.reference
                    .child("users")
                    .child(userId)
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {

                            for (snap in snapshot.children) {
                                val item = snap.getValue(Item::class.java)
                                if (item != null) {
                                    tempList.add(item)
                                }

                            }
                        }

                        override fun onCancelled(error: DatabaseError) {

                        }

                    })
                emit(DataOrException.Success(tempList))
            }
            catch (e : Exception){
                emit(DataOrException.Error(e.toString()))
            }
        }
    }
}