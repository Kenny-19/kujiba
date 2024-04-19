import android.content.Context
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.preference.PreferenceManager
import kotlinx.coroutines.coroutineScope
import org.unh.kujiba_v001.fragment.search.JsonFileHandler

fun getUserName(context: Context): String {
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    return sharedPreferences.getString("username", "Invité") ?: "Invité"
}



@Composable
fun Quota(jsonFileHandler: JsonFileHandler) {
    val context = LocalContext.current
    var userName by remember { mutableStateOf("") }
    val recipeCount = remember { mutableStateOf(0) }
    val animatedProgress = remember { Animatable(0f) }  // Initial value for animation
    val maxQuota = 10  // Define your max quota

    LaunchedEffect(key1 = true) {
        userName = getUserName(context)
        recipeCount.value = jsonFileHandler.countFavorites()
        coroutineScope {
            animatedProgress.animateTo(
                targetValue = if (recipeCount.value > maxQuota) 1f else recipeCount.value.toFloat() / maxQuota,
                animationSpec = tween(durationMillis = 1000)  // Duration of the animation
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Jambo, $userName!",
                style = MaterialTheme.typography.h5
            )
            Spacer(modifier = Modifier.height(20.dp))
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = animatedProgress.value,
                    modifier = Modifier.size(100.dp),  // Size of the progress bar
                    strokeWidth = 8.dp,
                    color = MaterialTheme.colors.primary
                )
                Text(
                    text = "${recipeCount.value}/$maxQuota",
                    fontSize = 14.sp,
                    color = MaterialTheme.colors.onSurface
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = when {
                    recipeCount.value > 5 -> "Vous mangez bien!"
                    recipeCount.value == 5 -> "Continuez comme ça!"
                    else -> "Faut quand même manger!"
                },
                style = MaterialTheme.typography.subtitle1
            )
        }
    }
}


