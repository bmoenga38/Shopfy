package com.suitcase.data

sealed class RegisterLogin<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : RegisterLogin<T>(data)
    class Loading<T>(data: T? = null) : RegisterLogin<T>(data)
    class Error<T>(message: String, data: T? = null) : RegisterLogin<T>(data, message)
}
data class LoginState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isError: String? = ""
)
data class Item(
    var name : String = "",
    var imageUrl : String = "",
    var bought : Boolean = false,
    var delegated : Boolean = false
)
sealed class DataOrException<T, E : String?, isLoading : Boolean?>
    (val data: List<T>? = null, val error: E? = null, val loading: Boolean? = false) {
    class Success<T>(data: List<T>) : DataOrException<T, String, Boolean>(data)
    class Loading<T>(loading: Boolean) : DataOrException<T, String, Boolean>(loading = loading)
    class Error<T>(error: String) : DataOrException<T, String, Boolean>(error = error)
}