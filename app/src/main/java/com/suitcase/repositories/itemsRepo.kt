package com.suitcase.repositories

import com.suitcase.data.DataOrException
import com.suitcase.data.Item
import kotlinx.coroutines.flow.Flow

interface itemsRepo {
    suspend fun getItems(userId : String):Flow<DataOrException<Item,String,Boolean>>
}