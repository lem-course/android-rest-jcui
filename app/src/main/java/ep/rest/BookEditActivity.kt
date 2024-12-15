package ep.rest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel

class BookEditActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EditBook()
        }
    }
}

@Composable
fun EditBook(viewModel: BookViewModel = viewModel()) {
    // referenca na aktivnost
    val activity = LocalContext.current as Activity

    // takoj ob prikazu naloži knjige
    LaunchedEffect(Unit) {
        val id = activity.intent.getIntExtra("id", 0)
        viewModel.get(id)
    }

    // rezultat poizvedbe za dodajanje knjige
    val editResult = viewModel.editResult.collectAsState().value
    LaunchedEffect(editResult) {
        editResult?.let {
            if (it.isSuccess) {
                val intent = Intent(activity, BookDetailActivity::class.java)
                intent.putExtra("id", viewModel.book.value.id)
                activity.startActivity(intent)
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

    BookForm(viewModel, viewModel::edit)
}
