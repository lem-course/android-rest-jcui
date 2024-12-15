package ep.rest

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.await
import retrofit2.awaitResponse
import java.util.Calendar
import kotlin.text.split
import kotlin.text.toInt

class BookViewModel : ViewModel() {
    // Hrani vsebino obrazca
    var book = mutableStateOf(Book())

    // zaradi validacije hranimo ceno in leto posebej
    var inputPrice = mutableStateOf("")
    var inputYear = mutableStateOf("")

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
        inputYear.value = newYear
    }

    fun onDescriptionChange(newDescription: String) {
        book.value = book.value.copy(description = newDescription)
    }

    var addResult = MutableStateFlow<Result<Int>?>(null)
        private set

    var editResult = MutableStateFlow<Result<Int>?>(null)
        private set

    fun insert() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // nastavi ceno in leto
                book.value = book.value.copy(price = inputPrice.value.toDoubleOrNull() ?: 0.0)
                book.value = book.value.copy(
                    year = inputYear.value.toIntOrNull() ?: Calendar.getInstance()
                        .get(Calendar.YEAR)
                )

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

    fun edit() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // nastavi ceno in leto
                book.value = book.value.copy(price = inputPrice.value.toDoubleOrNull() ?: 0.0)
                book.value = book.value.copy(
                    year = inputYear.value.toIntOrNull() ?: Calendar.getInstance()
                        .get(Calendar.YEAR)
                )

                // Pošlji zahtevo
                val response = BookService.instance.update(
                    book.value.id,
                    book.value.author,
                    book.value.title,
                    book.value.price,
                    book.value.year,
                    book.value.description
                ).awaitResponse()

                // Vnos uspel, sporočimo URL novega vira
                if (response.isSuccessful) {
                    editResult.value = Result.success(book.value.id)
                } else { // Vnos ni uspel
                    editResult.value = Result.failure(Exception(response.message()))
                }
            } catch (e: Exception) {
                editResult.value = Result.failure(e)
            }
        }
    }

    fun get(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                book.value = BookService.instance.get(id).await()
                inputPrice.value = book.value.price.toString()
                inputYear.value = book.value.year.toString()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
