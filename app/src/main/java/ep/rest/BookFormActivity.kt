package ep.rest

import android.os.Bundle
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

class BookFormActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BookForm()
        }
    }
}

@Composable
fun BookForm(viewModel: BookAddViewModel = viewModel()) {
    // referenca na aktivnost
    val activity = LocalContext.current

    // rezultat poizvedbe za dodajanje knjige
    val saveResult = viewModel.addResult.collectAsState().value
    LaunchedEffect(saveResult) {
        saveResult.let {
            if (it.isSuccess) {
                // pojdimo na aktivnost
                println("Success: ${it.getOrNull()}")
            } else if (it.isFailure) {
                // prikažimo napako
                println("Failure: ${it.exceptionOrNull()?.message}")
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        OutlinedTextField(
            value = viewModel.title.value,
            onValueChange = viewModel::onTitleChange,
            label = { Text("Naslov") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        OutlinedTextField(
            value = viewModel.author.value,
            onValueChange = viewModel::onAuthorChange,
            label = { Text("Avtor") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
        )

        OutlinedTextField(
            value = viewModel.price.value.toString(),
            onValueChange = viewModel::onPriceChange,
            label = { Text("Cena") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        OutlinedTextField(
            value = viewModel.year.value.toString(),
            onValueChange = viewModel::onYearChange,
            label = { Text("Leto izdaje") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )

        OutlinedTextField(
            value = viewModel.description.value,
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
            onClick = viewModel::onSaveClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Shrani")
        }
    }
}
