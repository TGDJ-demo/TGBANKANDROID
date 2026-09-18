package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.DemoBadge
import com.example.ui.components.TGLogo
import com.example.ui.theme.BankBlueAccent
import com.example.ui.theme.BankErrorRed
import com.example.ui.theme.BankNavyDark
import com.example.ui.theme.BankNavyLight
import com.example.ui.theme.BankNavyPrimary
import com.example.ui.theme.BankTextPrimary
import com.example.ui.theme.BankTextSecondary
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.example.ui.utils.canAuthenticateBiometric
import com.example.ui.utils.showBiometricPrompt
import com.example.viewmodel.BankViewModel

@Composable
fun LoginScreen(
  viewModel: BankViewModel
) {
  val username by viewModel.loginUsername.collectAsState()
  val pin by viewModel.loginPin.collectAsState()
  val loginError by viewModel.loginError.collectAsState()

  var pinVisible by remember { mutableStateOf(false) }
  var showForgotPinDialog by remember { mutableStateOf(false) }
  val ctx = LocalContext.current

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFFF8FAFC))
      .imePadding()
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(24.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      Spacer(modifier = Modifier.height(20.dp))

      // App Logo & Identity
      TGLogo(size = 72.dp, fontSize = 32)
      Spacer(modifier = Modifier.height(16.dp))

      Text(
        text = "TG Bank",
        fontSize = 28.sp,
        fontWeight = FontWeight.Black,
        color = BankNavyDark,
        letterSpacing = (-0.5).sp
      )

      Text(
        text = "TestGrid Banking Simulation App",
        fontSize = 13.sp,
        color = BankTextSecondary,
        fontWeight = FontWeight.Medium
      )

      Spacer(modifier = Modifier.height(8.dp))
      DemoBadge()

      Spacer(modifier = Modifier.height(32.dp))

      // Login Card
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("login_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(
          modifier = Modifier.padding(24.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(
            text = "Sign In to Your Account",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = BankTextPrimary
          )

          Spacer(modifier = Modifier.height(20.dp))

          // Username Field
          OutlinedTextField(
            value = username,
            onValueChange = { viewModel.updateLoginUsername(it) },
            label = { Text("Username") },
            placeholder = { Text("Sanjay G") },
            leadingIcon = {
              Icon(Icons.Default.Person, contentDescription = "Username", tint = BankNavyPrimary)
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
              keyboardType = KeyboardType.Text,
              imeAction = ImeAction.Next
            ),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("login_username_input"),
            shape = RoundedCornerShape(12.dp)
          )

          Spacer(modifier = Modifier.height(16.dp))

          // PIN Field
          OutlinedTextField(
            value = pin,
            onValueChange = { viewModel.updateLoginPin(it) },
            label = { Text("Password / PIN") },
            placeholder = { Text("1234") },
            leadingIcon = {
              Icon(Icons.Default.Lock, contentDescription = "PIN", tint = BankNavyPrimary)
            },
            trailingIcon = {
              IconButton(
                onClick = { pinVisible = !pinVisible },
                modifier = Modifier.testTag("login_toggle_pin_visibility")
              ) {
                Icon(
                  imageVector = if (pinVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                  contentDescription = if (pinVisible) "Hide PIN" else "Show PIN"
                )
              }
            },
            visualTransformation = if (pinVisible) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
              keyboardType = KeyboardType.NumberPassword,
              imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
              onDone = { viewModel.performLogin() }
            ),
            modifier = Modifier
              .fillMaxWidth()
              .testTag("login_pin_input"),
            shape = RoundedCornerShape(12.dp)
          )

          if (loginError != null) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
              text = loginError ?: "",
              color = BankErrorRed,
              fontSize = 12.sp,
              fontWeight = FontWeight.Medium,
              modifier = Modifier.testTag("login_error_text")
            )
          }

          Spacer(modifier = Modifier.height(20.dp))

          // Login Button
          Button(
            onClick = { viewModel.performLogin() },
            modifier = Modifier
              .fillMaxWidth()
              .height(50.dp)
              .testTag("login_submit_button"),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = BankNavyPrimary
            )
          ) {
            Text(
              text = "Sign In",
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Biometric login (mockable via Test Controls)
          OutlinedButton(
            onClick = {
              if (viewModel.testControls.value.mockBiometricSuccess) {
                viewModel.performLogin()
              } else {
                val activity = (ctx as? FragmentActivity)
                if (activity != null && canAuthenticateBiometric(ctx)) {
                  showBiometricPrompt(
                    activity = activity,
                    title = "Login to TG Bank",
                    subtitle = "Use fingerprint or Face ID",
                    onSuccess = { viewModel.performLogin() },
                    onError = { msg -> Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show() }
                  )
                } else {
                  Toast.makeText(ctx, "Biometric sensor simulated - logging in", Toast.LENGTH_SHORT).show()
                  viewModel.performLogin()
                }
              }
            },
            modifier = Modifier
              .fillMaxWidth()
              .height(46.dp)
              .testTag("login_biometric_button"),
            shape = RoundedCornerShape(12.dp)
          ) {
            Icon(Icons.Default.Fingerprint, contentDescription = null, tint = BankNavyPrimary, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Login with Fingerprint / Face ID", fontWeight = FontWeight.SemiBold, color = BankNavyPrimary, fontSize = 13.sp)
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Quick Fill Demo Credentials
          OutlinedButton(
            onClick = { viewModel.useDemoCredentials() },
            modifier = Modifier
              .fillMaxWidth()
              .height(44.dp)
              .testTag("login_quick_fill_button"),
            shape = RoundedCornerShape(12.dp)
          ) {
            Text(
              text = "Use Demo Credentials (Sanjay G / 1234)",
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold,
              color = BankNavyLight
            )
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Forgot PIN Link
          TextButton(
            onClick = { showForgotPinDialog = true },
            modifier = Modifier.testTag("login_forgot_pin_button")
          ) {
            Text(
              text = "Forgot PIN?",
              color = BankBlueAccent,
              fontSize = 13.sp,
              fontWeight = FontWeight.SemiBold
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      // Demo Account Notice
      Surface(
        color = Color(0xFFEFF6FF),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("demo_account_indicator")
      ) {
        Row(
          modifier = Modifier.padding(14.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.Info,
            contentDescription = null,
            tint = BankBlueAccent,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = "Predefined Demo Account",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = BankNavyDark
            )
            Text(
              text = "Username: Sanjay G  |  PIN: 1234\nStrictly local mock simulation for mobile app testing.",
              fontSize = 11.sp,
              color = BankTextSecondary,
              lineHeight = 16.sp
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))
    }
  }

  // Forgot PIN Dialog
  if (showForgotPinDialog) {
    AlertDialog(
      onDismissRequest = { showForgotPinDialog = false },
      title = {
        Text("Demo PIN Recovery", fontWeight = FontWeight.Bold)
      },
      text = {
        Text("Because this is a local simulated demo app, your demo credentials are fixed:\n\n• Username: Sanjay G\n• PIN: 1234\n\nClick 'Auto-Fill' to restore them instantly.")
      },
      confirmButton = {
        Button(
          onClick = {
            viewModel.useDemoCredentials()
            showForgotPinDialog = false
          },
          modifier = Modifier.testTag("dialog_autofill_button")
        ) {
          Text("Auto-Fill")
        }
      },
      dismissButton = {
        TextButton(
          onClick = { showForgotPinDialog = false },
          modifier = Modifier.testTag("dialog_dismiss_button")
        ) {
          Text("Close")
        }
      }
    )
  }
}
