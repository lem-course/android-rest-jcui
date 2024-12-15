package ep.rest

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.await


class MainActivityViewModel : ViewModel() {
    val books: MutableState<List<Book>> = mutableStateOf(emptyList())

    fun getAll() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                books.value = BookService.instance.getAll().await()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
