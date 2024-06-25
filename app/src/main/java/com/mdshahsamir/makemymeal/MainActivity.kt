package com.mdshahsamir.makemymeal

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.mdshahsamir.makemymeal.ui.theme.MakeMyMealApp
import com.mdshahsamir.makemymeal.ui.theme.MakeMyMealAppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            MakeMyMealAppTheme {
                MakeMyMealApp(navHostController = navController)
            }
        }

        val generativeModel = GenerativeModel(
            modelName = "gemini-1.5-flash",
            // Access your API key as a Build Configuration variable (see "Set up your API key" above)
            apiKey = BuildConfig.apiKey
        )

        lifecycleScope.launch(Dispatchers.IO) {
            val prompt = "Write a story about a AI and magic"
            val contet = content {

            }
            val response = generativeModel.generateContent(prompt)
            Log.i("Main Activitty::::", response.text.toString())
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MakeMyMealAppTheme {
        Greeting("Android")
    }
}