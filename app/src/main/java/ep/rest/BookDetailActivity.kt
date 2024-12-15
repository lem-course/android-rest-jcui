package ep.rest

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

class BookDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BookDetailScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailScreen(viewModel: BookViewModel = viewModel()) {
    // referenca na aktivnost
    val activity = LocalContext.current as Activity

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    // prikazu naloži knjigo
    LaunchedEffect(Unit) { viewModel.get(activity.intent.getIntExtra("id", 0)) }

    // rezultat brisanja
    val deleteResult = viewModel.deleteResult.collectAsState().value
    LaunchedEffect(deleteResult) {
        deleteResult?.let {
            if (it.isSuccess) {
                // pojdimo na zacetno aktivnost
                activity.startActivity(Intent(activity, MainActivity::class.java))
            } else if (it.isFailure) {
                // prikažimo napako
                Toast.makeText(activity, "Napaka: Brisanje ni uspelo", Toast.LENGTH_SHORT).show()
                println("Failure: ${it.exceptionOrNull()?.message}")
            }
        }
    }

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text(viewModel.book.value.title) },
                scrollBehavior = scrollBehavior
            )
        },
        content = { innerPadding ->
            LazyColumn( // Da lahko po vsebini drsimo
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .padding(innerPadding)
            ) {
                item {
                    Text(
                        text = viewModel.book.value.description,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    )

    Box(modifier = Modifier.fillMaxSize()) {
        FloatingActionButton(
            onClick = {
                val intent = Intent(activity, BookEditActivity::class.java)
                intent.putExtra("id", viewModel.book.value.id)
                activity.startActivity(intent)
            },
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.BottomEnd),
            content = {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_edit),
                    contentDescription = null
                )
            }
        )

        FloatingActionButton(
            onClick = {
                val dialog = AlertDialog.Builder(activity)
                dialog.setTitle("Potrdi izbirs")
                dialog.setMessage("Si prepričan?")
                dialog.setPositiveButton("Briši") { _, _ -> viewModel.delete() }
                dialog.setNegativeButton("Prekliči", null)
                dialog.create().show()
            },
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.BottomStart),
            content = {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_delete),
                    contentDescription = null
                )
            }
        )
    }
}

@Preview
@Composable
private fun Preview() {
    BookDetailScreen()
}
