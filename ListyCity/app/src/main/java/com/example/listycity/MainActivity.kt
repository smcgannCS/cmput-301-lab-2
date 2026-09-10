package com.example.listycity

// some of these imports were suggested by gemini following a prompt regarding "squiggly lines under some keywords"
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val cityRepository = CityRepository()

        setContent {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                CityListScreen(
                    cities = cityRepository.cities,
                    onAddCity = { cityRepository.addCity(it) },
                    onDeleteCity = {cityRepository.delCity(it)},
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}
class CityRepository {
    private val _cities = mutableStateListOf(
        "Edmonton", "Calgary", "Seattle", "Chicago",
        "San Jose", "Toronto", "Montreal", "Vancouver",
        "Ottawa", "Quebec", "Colarado", "St Louis",
        "New Jersey", "New York City", "Anaheim"
    )

    val cities: List<String>
        get() = _cities

    fun addCity(city: String) {
        _cities.add(city)
    }

    fun delCity(city: String) {
        _cities.remove(city)
    }
}

@Composable
fun CityListScreen(
    cities: List<String>,
    onAddCity: (String) -> Unit,
    onDeleteCity: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var newCityName by remember { mutableStateOf("") }
    var selectedCity by remember { mutableStateOf<String?>(null) }

    Column(modifier = modifier.fillMaxSize()) {
        Row(modifier = Modifier.padding(16.dp)) {
            OutlinedTextField(
                value = newCityName,
                onValueChange = { newCityName = it },
                label = { Text("City name") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Button(
                    onClick = {
                        if (newCityName.isNotBlank()) {
                            onAddCity(newCityName)
                            newCityName = ""
                            selectedCity = null
                        }
                    },
                    modifier = Modifier.fillMaxWidth(0.4f)
                ) {
                    Text("Add City")
                }

                Spacer(modifier = Modifier.height(4.dp))

                Button(
                    onClick = {
                        if (newCityName.isNotBlank()) {
                            onDeleteCity(newCityName)
                            newCityName = ""
                            selectedCity = null
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    modifier = Modifier.fillMaxWidth(0.4f)
                ) {
                    Text("Delete")
                }
            }
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(cities) { city ->
                CityRow(
                    city = city,
                    isSelected = (city == selectedCity),
                    onClick = {
                        selectedCity = city
                        newCityName = city
                    }
                )
            }
        }
    }
}

@Composable
fun CityRow(
    city: String,
    onClick: () -> Unit,
    isSelected: Boolean
){
    Text(
        text = city,
        fontSize = 28.sp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {onClick()}
            .padding(horizontal = 18.dp, vertical = 14.dp)
    )
}
