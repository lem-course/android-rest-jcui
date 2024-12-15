package ep.rest

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DisplayBooks()
        }
    }
}

@Composable
fun DisplayBooks(viewModel: ListBooksViewModel = viewModel()) {
    // referenca na aktivnost
    val activity = LocalContext.current

    // takoj ob prikazu naloži knjige
    LaunchedEffect(Unit) { viewModel.getAll() }

    // Vmesnik
    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = {
            activity.startActivity(Intent(activity, BookFormActivity::class.java))
        }) {
            Icon(Icons.Default.Add, contentDescription = "Dodaj")
        }
    }) { paddingValues ->
        LazyColumn {
            items(viewModel.books.value) {
                Book(it, {
                    val intent = Intent(activity, BookDetailActivity::class.java)
                    intent.putExtra("id", it.id)
                    activity.startActivity(intent)
                }, paddingValues)
            }
        }
    }
}

@Composable
fun Book(book: Book, onClick: () -> Unit, paddingValues: PaddingValues = PaddingValues()) {
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .padding(5.dp)
    ) {
        Text(book.title, fontSize = 28.sp, modifier = Modifier.padding(paddingValues))
        Row {
            Text(
                book.author,
                fontSize = 18.sp,
                textAlign = TextAlign.Left,
                modifier = Modifier
                    .weight(3f)
                    .padding(5.dp)
            )
            Text(
                "%.2f EUR".format(book.price),
                fontSize = 18.sp,
                textAlign = TextAlign.Right,
                modifier = Modifier
                    .weight(2f)
                    .padding(5.dp)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Preview
@Composable
private fun Show() {
    DisplayBooks()
}