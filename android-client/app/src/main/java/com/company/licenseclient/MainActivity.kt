package com.company.licenseclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.company.licenseclient.api.LicenseApi
import com.company.licenseclient.api.dto.ProvisionRequest
import com.company.licenseclient.hardware.AndroidHardwareProvider
import com.company.licenseclient.hardware.HardwareFingerprintGenerator
import com.company.licenseclient.ui.theme.LicenseClientTheme
import kotlinx.coroutines.launch
import androidx.compose.material3.ExperimentalMaterial3Api

class MainActivity : ComponentActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        setContent {

            LicenseClientTheme {

                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {

                    LicenseScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LicenseScreen() {

    val context =
        LocalContext.current

    val coroutineScope =
        rememberCoroutineScope()

    val hardwareInfo =
        remember {

            AndroidHardwareProvider(
                context.applicationContext
            ).getHardwareInfo()
        }

    val fingerprint =
        remember {

            HardwareFingerprintGenerator
                .generate(
                    hardwareInfo
                )
        }

    val api =
        remember {

            LicenseApi(
                baseUrl =
                "http://10.0.2.2:8080/"
            )
        }

    var customerName by
    remember {
        mutableStateOf("")
    }

    var customerEmail by
    remember {
        mutableStateOf("")
    }

    var resultText by
    remember {
        mutableStateOf("")
    }

    var isLoading by
    remember {
        mutableStateOf(false)
    }

    Column(
        modifier =
        Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            )
            .padding(24.dp),

        verticalArrangement =
        Arrangement.Top
    ) {

        Text(
            text = "LICENSE CLIENT",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        OutlinedTextField(
            value = customerName,

            onValueChange = {
                customerName = it
            },

            label = {
                Text("Customer Name")
            },

            modifier =
            Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        OutlinedTextField(
            value = customerEmail,

            onValueChange = {
                customerEmail = it
            },

            label = {
                Text("Customer Email")
            },

            modifier =
            Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Text(
            text = "Hardware Fingerprint:"
        )

        Text(
            text = fingerprint,
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Button(

            onClick = {

                if (
                    customerName.isBlank() ||
                    customerEmail.isBlank()
                ) {

                    resultText =
                        "Customer information is required."

                    return@Button
                }

                coroutineScope.launch {

                    isLoading = true

                    resultText =
                        "Connecting to license server..."

                    try {

                        val response =
                            api.provision(

                                ProvisionRequest(

                                    customerName =
                                    customerName.trim(),

                                    customerEmail =
                                    customerEmail.trim(),

                                    productName =
                                    "License App",

                                    productVersion =
                                    "1.0",

                                    hardwareFingerprint =
                                    fingerprint,

                                    cpuId =
                                    hardwareInfo.cpuId,

                                    motherboardId =
                                    hardwareInfo.motherboardId,

                                    diskSerial =
                                    hardwareInfo.diskSerial,

                                    macAddress =
                                    hardwareInfo.macAddress,

                                    operatingSystem =
                                    hardwareInfo.operatingSystem
                                )
                            )

                        resultText =

                            if (response.success) {

                                """
                                LICENSE ACTIVATED
                                
                                License Key:
                                ${response.licenseKey}
                                
                                Status:
                                ${response.status}
                                
                                Expire Date:
                                ${response.expireDate}
                                
                                ${response.message}
                                """.trimIndent()

                            } else {

                                """
                                ACTIVATION FAILED
                                
                                ${response.message}
                                """.trimIndent()
                            }

                    } catch (
                        exception: Exception
                    ) {

                        resultText =
                            """
                            Could not connect to license server.
                            
                            ${exception.message ?: "Unknown error"}
                            """.trimIndent()

                    } finally {

                        isLoading = false
                    }
                }
            },

            enabled = !isLoading,

            modifier =
            Modifier.fillMaxWidth()
        ) {

            if (isLoading) {

                CircularProgressIndicator()

            } else {

                Text(
                    text = "ACTIVATE LICENSE"
                )
            }
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        if (resultText.isNotBlank()) {

            Text(
                text = resultText
            )
        }
    }
}