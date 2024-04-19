package org.unh.kujiba_v001





import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import org.unh.kujiba_v001.fragment.login.App
import org.unh.kujiba_v001.ui.theme.Kujiba_v001Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Kujiba_v001Theme {
                val navcontroller = rememberNavController()

                // A surface container using the 'background' color from the theme
                App()


                }
            }
        }
    }


