package com.example.lab4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import com.example.lab4.ui.theme.Lab4Theme
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter

data class Recipe (val name: String, val url: String)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab4Theme {
                Scaffold(modifier = Modifier
                    .fillMaxSize()
                ) { innerPadding ->
                    ListRecipe(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

val CustomColor1 = Color(0xFF001996)

@Composable
fun ListRecipe(modifier: Modifier = Modifier) {
    var text1 by remember { mutableStateOf("") }
    var text2 by remember { mutableStateOf("") }
    var recipes by remember { mutableStateOf(listOf<Recipe>()) }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = CustomColor1)
                .padding(15.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = modifier
                    .width(200.dp)
                    .height(175.dp)
            ) {
                // Nombre
                OutlinedTextField(
                    value = text1,
                    onValueChange = { text1 = it },
                    label = { Text("Ingrese el nombre") },
                    modifier = Modifier
                        .height(55.dp)
                        .weight(1f)
                        .padding(end = 8.dp, bottom = 20.dp),
                )
                // URL
                OutlinedTextField(
                    value = text2,
                    onValueChange = { text2 = it },
                    label = { Text("Ingrese el URL de la imagen") },
                    modifier = Modifier
                        .height(40.dp)
                        .weight(1f)
                        .padding(end = 8.dp)
                )
            }

            // Botón
            Button(
                onClick = {
                    if (text1.isNotEmpty() && text2.isNotEmpty()) {
                        val newRecipe = Recipe(text1, text2)

                        if (recipes.any {it.name == newRecipe.name && it.url == newRecipe.url}) {
                            errorMessage = "La receta ya existe en la lista."
                        } else {
                            recipes = recipes + Recipe(
                                text1,
                                text2
                            )
                            text1 = ""
                            text2 = ""
                            errorMessage = ""
                        }
                    }
                },
                modifier = Modifier
                    .height(50.dp)
                    .weight(1f)
            ) {
                Text("Agregar")
            }
        }

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(8.dp)
            )
        }

        LazyColumn (
            modifier = Modifier
                .padding()
                .fillMaxSize()
        ) {
            items(recipes) { recipe ->
                RecipeCard(
                    recipe = recipe,
                    onDelete = { deletedRecipe ->
                        recipes = recipes.filter { it != deletedRecipe }
                    }
                )
            }
        }
    }
}


@Composable
fun RecipeCard(recipe: Recipe, onDelete: (Recipe) -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onDelete(recipe) },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(recipe.url),
                contentDescription = "Imagen de la receta",
                modifier = Modifier
                    .size(100.dp)
                    .padding(start = 20.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = recipe.name,
                fontWeight = FontWeight.Bold
            )
        }
    }
}