package com.ashley.mobileTest.ui

/**
 * @description: 登录页
 * @author: liangxy
 */

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.content.ContentProviderCompat.requireContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                LoginScreen(onLoginSuccess = {
                    startActivity(Intent(this, BookingListActivity::class.java))
                    finish()
                })
            }
        }
    }
}

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Login", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = username,
                onValueChange = {username = it
                    // 清除错误信息
                    if (errorMessage.isNotEmpty()) {
                        errorMessage = ""
                    } },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    // 清除错误信息
                    if (errorMessage.isNotEmpty()) {
                        errorMessage = ""
                    }
                },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(32.dp))

            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = {
                        // 验证用户名和密码
                        when {
                            username.isEmpty() -> {
                                Toast.makeText(context, "Please enter username", Toast.LENGTH_SHORT).show()
                            }
                            password.isEmpty() -> {
                                Toast.makeText(context, "Please enter password", Toast.LENGTH_SHORT).show()
                            }
                            else -> {
                                // 验证通过，执行登录
                                scope.launch {
                                    isLoading = true
                                    delay(2000) // Simulate network login
                                    isLoading = false
                                    onLoginSuccess()
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Login")
                }
            }
        }
    }
}
