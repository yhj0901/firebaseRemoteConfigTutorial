package com.yangheejune.firebaseremoteconfigtutorial

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import coil.compose.rememberImagePainter
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.yangheejune.firebaseremoteconfigtutorial.ui.theme.FirebaseRemoteConfigTutorialTheme
import kotlinx.coroutines.tasks.await

class MainActivity : ComponentActivity() {
    private lateinit var remoteConfig : FirebaseRemoteConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        remoteConfig = Firebase.remoteConfig

        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0   // 새로고침 할 시간 0이면 무조건 파이어베이스 리모트 컴피그에서 받아옴
        }

        remoteConfig.setConfigSettingsAsync(configSettings)


        setContent {
            FirebaseRemoteConfigTutorialTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting(remoteConfig)
                }
            }
        }
    }
}

@Composable
fun Greeting(remoteConfig: FirebaseRemoteConfig) {
    val splashUrlState = remember { mutableStateOf("")}

    LaunchedEffect(Unit) {
        try {
            remoteConfig.fetchAndActivate().await()
            val splashUrl = remoteConfig.getString("splash_url")
            val splashUrl1 = remoteConfig.getString("splash_url_1")
            splashUrlState.value = splashUrl
        } catch (exception: Exception) {

        }
    }

    if (splashUrlState.value.isNotEmpty()) {
        Image(
            painter = rememberImagePainter(splashUrlState.value),
            contentDescription = "image"
        )
    } else {

    }
}