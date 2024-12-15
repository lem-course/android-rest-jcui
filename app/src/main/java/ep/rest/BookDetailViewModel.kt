package ep.rest

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.await

class BookDetailViewModel : ViewModel() {
    var book: MutableState<Book> = mutableStateOf(Book())
        private set

    var deleteResult = MutableStateFlow<Result<Unit>?>(null)
        private set

    fun get(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                book.value = BookService.instance.get(id).await()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun delete() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                BookService.instance.delete(book.value.id).await()
                deleteResult.value = Result.success(Unit)
            } catch (e: Exception) {
                deleteResult.value = Result.failure(e)
            }
        }
    }
}
