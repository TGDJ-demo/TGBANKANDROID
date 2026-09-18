package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.DemoBadge
import com.example.ui.theme.BankBlueAccent
import com.example.ui.theme.BankErrorRed
import com.example.ui.theme.BankNavyDark
import com.example.ui.theme.BankNavyPrimary
import com.example.ui.theme.BankTextPrimary
import com.example.ui.theme.BankTextSecondary
import com.example.viewmodel.BankViewModel

@Composable
fun TestControlsScreen(
  viewModel: BankViewModel
) {
  val testControls by viewModel.testControls.collectAsState()
  val userMessage by viewModel.userMessage.collectAsState()

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFFF8FAFC))
      .verticalScroll(rememberScrollState())
      .padding(16.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          text = "Test Controls & Edge Cases",
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          color = BankNavyDark
        )
        Text(
          text = "Deterministic simulation for TestGrid mobile automation",
          fontSize = 11.sp,
          color = BankTextSecondary
        )
      }
      DemoBadge()
    }

    Spacer(modifier = Modifier.height(16.dp))

    Card(
      modifier = Modifier
        .fillMaxWidth()
        .testTag("test_controls_card"),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
      Column(modifier = Modifier.padding(18.dp)) {
        Text("Simulation Overrides", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = BankNavyDark)
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "Toggle deterministic failure modes to verify Appium test suites and error UI states.",
          fontSize = 12.sp,
          color = BankTextSecondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Toggle 1: Force Insufficient Balance
        TestToggleRow(
          title = "Force Insufficient Balance",
          subtitle = "Rejects all transfer and payment attempts with balance error",
          checked = testControls.forceInsufficientBalance,
          onCheckedChange = { viewModel.updateTestControls(testControls.copy(forceInsufficientBalance = it)) },
          testTag = "toggle_force_insufficient_balance"
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFF1F5F9))

        // Toggle 2: Force Transaction Failure
        TestToggleRow(
          title = "Force Transaction Failure",
          subtitle = "Simulates generic processor / gateway refusal",
          checked = testControls.forceFailedTransaction,
          onCheckedChange = { viewModel.updateTestControls(testControls.copy(forceFailedTransaction = it)) },
          testTag = "toggle_force_tx_failure"
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFF1F5F9))

        // Toggle 3: Simulate Network Timeout
        TestToggleRow(
          title = "Simulate Network Timeout",
          subtitle = "Throws deterministic 504 gateway timeout exception",
          checked = testControls.simulateNetworkError,
          onCheckedChange = { viewModel.updateTestControls(testControls.copy(simulateNetworkError = it)) },
          testTag = "toggle_simulate_network_error"
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFF1F5F9))

        // Toggle 4: Force KYC Incomplete
        TestToggleRow(
          title = "Simulate Unverified KYC",
          subtitle = "Switches profile KYC status to 'Action Required'",
          checked = testControls.forceKycIncomplete,
          onCheckedChange = { viewModel.updateTestControls(testControls.copy(forceKycIncomplete = it)) },
          testTag = "toggle_force_kyc_incomplete"
        )
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Reset Demo Data Card
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .testTag("reset_demo_data_card"),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
      Column(modifier = Modifier.padding(18.dp)) {
        
    // ── New auth & seed controls ──
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .testTag("test_auth_seed_card"),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
      Column(modifier = Modifier.padding(18.dp)) {
        Text("Auth & Backend Controls", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = BankNavyDark)
        Spacer(modifier = Modifier.height(10.dp))
        TestToggleRow(
          title = "Mock Biometric Success",
          subtitle = "When ON, fingerprint/Face ID succeeds without hardware. When OFF, biometric auth fails.",
          checked = testControls.mockBiometricSuccess,
          onCheckedChange = { viewModel.updateTestControls(testControls.copy(mockBiometricSuccess = it)) },
          testTag = "toggle_mock_biometric"
        )
        Spacer(modifier = Modifier.height(10.dp))
        TestToggleRow(
          title = "Require Payment Auth",
          subtitle = "Force MPIN / OTP / Biometric before confirming transfers.",
          checked = testControls.requirePaymentAuth,
          onCheckedChange = { viewModel.updateTestControls(testControls.copy(requirePaymentAuth = it)) },
          testTag = "toggle_require_payment_auth"
        )
        Spacer(modifier = Modifier.height(10.dp))
        TestToggleRow(
          title = "Force OTP Always",
          subtitle = "Always show OTP dialog before payment success.",
          checked = testControls.forceOtpAlways,
          onCheckedChange = { viewModel.updateTestControls(testControls.copy(forceOtpAlways = it)) },
          testTag = "toggle_force_otp"
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
          onClick = { viewModel.pushDatabaseSeed() },
          modifier = Modifier
            .fillMaxWidth()
            .height(46.dp)
            .testTag("push_database_seed_button"),
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.buttonColors(containerColor = BankBlueAccent)
        ) {
          Text("Push / Seed Database to Backend", fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          "Resets profile, beneficiaries, elite cards and transactions so Appium assertions validate against known seed data.",
          fontSize = 11.sp,
          color = BankTextSecondary
        )
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Refresh, contentDescription = null, tint = BankNavyPrimary)
          Spacer(modifier = Modifier.width(8.dp))
          Text("Reset Demo State", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = BankNavyDark)
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "Restores initial demo balance ($24,588,338,510.70), default user profile, clean transactions, and default notifications.",
          fontSize = 12.sp,
          color = BankTextSecondary,
          lineHeight = 16.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
          onClick = { viewModel.resetDemoData() },
          modifier = Modifier
            .fillMaxWidth()
            .height(46.dp)
            .testTag("reset_demo_data_button"),
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.buttonColors(containerColor = BankNavyDark)
        ) {
          Icon(Icons.Default.Refresh, contentDescription = null)
          Spacer(modifier = Modifier.width(8.dp))
          Text("Reset Demo Data to Default", fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
      }
    }

    if (userMessage != null) {
      Spacer(modifier = Modifier.height(14.dp))
      Surface(
        color = Color(0xFFDCFCE7),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Text(
          text = userMessage ?: "",
          color = Color(0xFF166534),
          fontSize = 12.sp,
          fontWeight = FontWeight.Bold,
          modifier = Modifier.padding(12.dp)
        )
      }
    }

    Spacer(modifier = Modifier.height(20.dp))
  }
}

@Composable
private fun TestToggleRow(
  title: String,
  subtitle: String,
  checked: Boolean,
  onCheckedChange: (Boolean) -> Unit,
  testTag: String
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
      Text(title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = BankTextPrimary)
      Text(subtitle, fontSize = 11.sp, color = BankTextSecondary, lineHeight = 15.sp)
    }
    Switch(
      checked = checked,
      onCheckedChange = onCheckedChange,
      modifier = Modifier.testTag(testTag),
      colors = SwitchDefaults.colors(
        checkedThumbColor = Color.White,
        checkedTrackColor = BankBlueAccent
      )
    )
  }
}
