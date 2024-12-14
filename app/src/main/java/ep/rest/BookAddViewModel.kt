package ep.rest

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.awaitResponse
import java.util.Calendar

class BookAddViewModel : ViewModel() {
    // Hranijo vsebino obrazca
    var title = mutableStateOf("")
    var author = mutableStateOf("")
    var price = mutableStateOf("")
    var year = mutableStateOf("")
    var description = mutableStateOf("")

    fun onTitleChange(newTitle: String) {
        title.value = newTitle
    }

    fun onAuthorChange(newAuthor: String) {
        author.value = newAuthor
    }

    fun onPriceChange(newPrice: String) {
        price.value = newPrice
    }

    fun onYearChange(newYear: String) {
        year.value = newYear
    }

    fun onDescriptionChange(newDescription: String) {
        description.value = newDescription
    }

    var addResult = MutableStateFlow<Result<String>>(Result.failure(Exception("Initial value")))
        private set

    fun onSaveClick() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Pošlji zahtevo
                val response = BookService.instance.insert(
                    author.value,
                    title.value,
                    price.value.toDoubleOrNull() ?: 0.1,
                    year.value.toIntOrNull() ?: Calendar.getInstance().get(Calendar.YEAR),
                    description.value
                ).awaitResponse()

                // Vnos uspel, sporočimo URL novega vira
                if (response.isSuccessful && response.headers().get("Location") != null) {
                    addResult.value = Result.success(
                        BookService.RestApi.ADDRESS + response.headers().get("Location")
                    )
                } else { // Vnos ni uspel
                    addResult.value = Result.failure(Exception(response.message()))
                }
            } catch (e: Exception) {
                addResult.value = Result.failure(e)
            }
        }
    }
}
