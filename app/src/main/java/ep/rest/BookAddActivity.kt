package ep.rest

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

class BookAddActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AddBook()
        }
    }
}

@Composable
fun AddBook(viewModel: BookViewModel = viewModel()) {
    // referenca na aktivnost
    val activity = LocalContext.current

    // rezultat poizvedbe za dodajanje knjige
    val saveResult = viewModel.addResult.collectAsState().value
    LaunchedEffect(saveResult) {
        saveResult?.let {
            if (it.isSuccess) {
                activity.startActivity(Intent(activity, MainActivity::class.java))
            } else if (it.isFailure) {
                Toast.makeText(
                    activity,
                    "Napaka: ${it.exceptionOrNull()?.message}",
                    Toast.LENGTH_SHORT
                ).show()
                println("Failure: ${it.exceptionOrNull()?.message}")
            }
        }
    }

    BookForm(viewModel, viewModel::insert)
}

@Composable
fun BookForm(viewModel: BookViewModel = viewModel(), buttonFunction: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        OutlinedTextField(
            value = viewModel.book.value.title,
            onValueChange = viewModel::onTitleChange,
            label = { Text("Naslov") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        OutlinedTextField(
            value = viewModel.book.value.author,
            onValueChange = viewModel::onAuthorChange,
            label = { Text("Avtor") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
        )

        OutlinedTextField(
            value = viewModel.inputPrice.value,
            onValueChange = viewModel::onPriceChange,
            label = { Text("Cena") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        OutlinedTextField(
            value = viewModel.inputYear.value,
            onValueChange = viewModel::onYearChange,
            label = { Text("Leto izdaje") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        OutlinedTextField(
            value = viewModel.book.value.description,
            onValueChange = viewModel::onDescriptionChange,
            label = { Text("Opis") },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 8.dp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
            maxLines = 5
        )

        Button(
            onClick = buttonFunction,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Shrani")
        }
    }
}