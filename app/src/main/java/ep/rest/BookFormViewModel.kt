package ep.rest

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.awaitResponse
import java.util.Calendar
import kotlin.text.split
import kotlin.text.toInt

class BookFormViewModel : ViewModel() {
    // Hrani vsebino obrazca
    var book = mutableStateOf(Book())

    // zaradi validacije hranimo ceno posebej
    var inputPrice = mutableStateOf("")

    fun onTitleChange(newTitle: String) {
        book.value = book.value.copy(title = newTitle)
    }

    fun onAuthorChange(newAuthor: String) {
        book.value = book.value.copy(author = newAuthor)
    }

    fun onPriceChange(newPrice: String) {
        inputPrice.value = newPrice
    }

    fun onYearChange(newYear: String) {
        book.value = book.value.copy(
            year = newYear.toIntOrNull() ?: Calendar.getInstance().get(Calendar.YEAR)
        )
    }

    fun onDescriptionChange(newDescription: String) {
        book.value = book.value.copy(description = newDescription)
    }

    var addResult = MutableStateFlow<Result<Int>?>(null)
        private set

    fun onSaveClick() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // preveri ceno
                val price = inputPrice.value.toDoubleOrNull() ?: 0.0
                book.value = book.value.copy(price = price)

                // Pošlji zahtevo
                val response = BookService.instance.insert(
                    book.value.author,
                    book.value.title,
                    book.value.price,
                    book.value.year,
                    book.value.description
                ).awaitResponse()

                // Vnos uspel, sporočimo URL novega vira
                if (response.isSuccessful && response.headers().get("Location") != null) {

                    val id = response.headers().get("Location")?.split("/".toRegex())
                        ?.dropLastWhile { it.isEmpty() }
                        ?.toTypedArray()?.last()?.toInt()!!

                    addResult.value = Result.success(id)
                } else { // Vnos ni uspel
                    addResult.value = Result.failure(Exception(response.message()))
                }
            } catch (e: Exception) {
                addResult.value = Result.failure(e)
            }
        }
    }
}
