package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.DemoBadge
import com.example.ui.theme.BankBlueAccent
import com.example.ui.theme.BankErrorRed
import com.example.ui.theme.BankNavyDark
import com.example.ui.theme.BankNavyPrimary
import com.example.ui.theme.BankSuccessGreen
import com.example.ui.theme.BankTextPrimary
import com.example.ui.theme.BankTextSecondary
import com.example.viewmodel.BankViewModel
import com.example.viewmodel.Screen

@Composable
fun ProfileScreen(
  viewModel: BankViewModel
) {
  val userProfile by viewModel.userProfile.collectAsState()
  val testControls by viewModel.testControls.collectAsState()
  val context = LocalContext.current
  var showResetDialog by remember { mutableStateOf(false) }
  var showLogoutDialog by remember { mutableStateOf(false) }

  val effectiveKycStatus = if (testControls.forceKycIncomplete) "Action Required" else userProfile.kycStatus

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFFF8FAFC))
      .verticalScroll(rememberScrollState())
      .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    // Top Bar
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "Profile & Settings",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = BankNavyDark
      )
      DemoBadge()
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Profile Info Card
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .testTag("profile_info_card"),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
      Column(
        modifier = Modifier.padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Box(
          modifier = Modifier
            .size(70.dp)
            .clip(CircleShape)
            .background(Color(0xFFE0E7FF)),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "SG",
            fontSize = 26.sp,
            fontWeight = FontWeight.Black,
            color = BankNavyPrimary
          )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
          text = userProfile.name,
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          color = BankNavyDark,
          modifier = Modifier.testTag("profile_user_name")
        )

        Text(
          text = userProfile.email,
          fontSize = 12.sp,
          color = BankTextSecondary,
          modifier = Modifier.testTag("profile_user_email")
        )

        Spacer(modifier = Modifier.height(12.dp))

        Surface(
          color = if (effectiveKycStatus == "Verified") Color(0xFFDCFCE7) else Color(0xFFFEE2E2),
          shape = RoundedCornerShape(20.dp),
          modifier = Modifier.clickable { viewModel.navigateTo(Screen.KycFlow) }
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.VerifiedUser,
              contentDescription = null,
              tint = if (effectiveKycStatus == "Verified") BankSuccessGreen else BankErrorRed,
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "KYC: $effectiveKycStatus",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = if (effectiveKycStatus == "Verified") Color(0xFF166534) else BankErrorRed,
              modifier = Modifier.testTag("profile_kyc_status")
            )
          }
        }

        Spacer(modifier = Modifier.height(18.dp))

        HorizontalDivider(color = Color(0xFFF1F5F9))

        Spacer(modifier = Modifier.height(14.dp))

        ProfileDetailRow("Account Number", userProfile.accountNumber, "profile_account_number")
        Spacer(modifier = Modifier.height(8.dp))
        ProfileDetailRow("Phone", userProfile.phone, "profile_phone")
        Spacer(modifier = Modifier.height(8.dp))
        ProfileDetailRow("Account Type", userProfile.accountType, "profile_account_type")
        Spacer(modifier = Modifier.height(8.dp))
        ProfileDetailRow("Branch IFSC", userProfile.ifsc, "profile_ifsc")
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Settings Menu
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .testTag("settings_menu_card"),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
      Column(modifier = Modifier.padding(8.dp)) {
        SettingsMenuItem(
          icon = Icons.Default.BugReport,
          title = "Test Controls / Error Simulation",
          subtitle = "Toggle edge cases for TestGrid automation",
          testTag = "settings_test_controls",
          iconTint = BankNavyPrimary,
          onClick = { viewModel.navigateTo(Screen.TestControls) }
        )

        HorizontalDivider(modifier = Modifier.padding(horizontal = 12.dp), color = Color(0xFFF8FAFC))

        SettingsMenuItem(
          icon = Icons.Default.Security,
          title = "Security Settings",
          subtitle = "Biometric unlock, Change PIN (Demo: 1234)",
          testTag = "settings_security",
          onClick = { Toast.makeText(context, "Security: PIN is 1234. Biometric: Enabled.", Toast.LENGTH_SHORT).show() }
        )

        HorizontalDivider(modifier = Modifier.padding(horizontal = 12.dp), color = Color(0xFFF8FAFC))

        SettingsMenuItem(
          icon = Icons.Default.Notifications,
          title = "Notification Preferences",
          subtitle = "Manage simulated push & SMS alerts",
          testTag = "settings_notifications",
          onClick = { viewModel.navigateTo(Screen.NotificationCenter) }
        )

        HorizontalDivider(modifier = Modifier.padding(horizontal = 12.dp), color = Color(0xFFF8FAFC))

        SettingsMenuItem(
          icon = Icons.Default.Language,
          title = "Language",
          subtitle = "English (US)",
          testTag = "settings_language",
          onClick = { Toast.makeText(context, "Language: English (US)", Toast.LENGTH_SHORT).show() }
        )

        HorizontalDivider(modifier = Modifier.padding(horizontal = 12.dp), color = Color(0xFFF8FAFC))

        SettingsMenuItem(
          icon = Icons.AutoMirrored.Filled.Help,
          title = "Help & Support",
          subtitle = "TestGrid demo guides & API specs",
          testTag = "settings_help",
          onClick = { Toast.makeText(context, "TestGrid Bank Simulation v1.0", Toast.LENGTH_SHORT).show() }
        )

        HorizontalDivider(modifier = Modifier.padding(horizontal = 12.dp), color = Color(0xFFF8FAFC))

        SettingsMenuItem(
          icon = Icons.Default.Refresh,
          title = "Reset Demo Data",
          subtitle = "Restore balances & simulated transactions",
          testTag = "settings_reset_demo",
          iconTint = Color(0xFFD97706),
          onClick = { showResetDialog = true }
        )

        HorizontalDivider(modifier = Modifier.padding(horizontal = 12.dp), color = Color(0xFFF8FAFC))

        SettingsMenuItem(
          icon = Icons.AutoMirrored.Filled.Logout,
          title = "Sign Out",
          subtitle = "Return to demo login screen",
          testTag = "settings_logout",
          iconTint = BankErrorRed,
          onClick = { showLogoutDialog = true }
        )
      }
    }

    Spacer(modifier = Modifier.height(24.dp))
  }

  // Reset Confirmation Dialog
  if (showResetDialog) {
    AlertDialog(
      onDismissRequest = { showResetDialog = false },
      title = { Text("Reset Demo Data?", fontWeight = FontWeight.Bold) },
      text = { Text("This will restore the original demo balance ($24,588,338,510.70) and reset transactions and notifications.") },
      confirmButton = {
        Button(
          onClick = {
            viewModel.resetDemoData()
            showResetDialog = false
            Toast.makeText(context, "Demo data reset successfully", Toast.LENGTH_SHORT).show()
          },
          modifier = Modifier.testTag("dialog_confirm_reset_button"),
          colors = ButtonDefaults.buttonColors(containerColor = BankNavyPrimary)
        ) {
          Text("Reset")
        }
      },
      dismissButton = {
        TextButton(
          onClick = { showResetDialog = false },
          modifier = Modifier.testTag("dialog_cancel_reset_button")
        ) {
          Text("Cancel")
        }
      }
    )
  }

  // Logout Dialog
  if (showLogoutDialog) {
    AlertDialog(
      onDismissRequest = { showLogoutDialog = false },
      title = { Text("Sign Out of TG Bank?", fontWeight = FontWeight.Bold) },
      text = { Text("Are you sure you want to sign out? You can sign back in with Username: Sanjay G and PIN: 1234.") },
      confirmButton = {
        Button(
          onClick = {
            showLogoutDialog = false
            viewModel.performLogout()
          },
          modifier = Modifier.testTag("dialog_confirm_logout_button"),
          colors = ButtonDefaults.buttonColors(containerColor = BankErrorRed)
        ) {
          Text("Sign Out")
        }
      },
      dismissButton = {
        TextButton(
          onClick = { showLogoutDialog = false },
          modifier = Modifier.testTag("dialog_cancel_logout_button")
        ) {
          Text("Cancel")
        }
      }
    )
  }
}

@Composable
private fun ProfileDetailRow(label: String, value: String, testTag: String) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(label, fontSize = 12.sp, color = BankTextSecondary)
    Text(value, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = BankTextPrimary, modifier = Modifier.testTag(testTag))
  }
}

@Composable
private fun SettingsMenuItem(
  icon: ImageVector,
  title: String,
  subtitle: String,
  testTag: String,
  iconTint: Color = BankBlueAccent,
  onClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(8.dp))
      .clickable(onClick = onClick)
      .padding(horizontal = 12.dp, vertical = 10.dp)
      .testTag(testTag),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Box(
      modifier = Modifier
        .size(36.dp)
        .clip(RoundedCornerShape(8.dp))
        .background(Color(0xFFF1F5F9)),
      contentAlignment = Alignment.Center
    ) {
      Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(20.dp))
    }
    Spacer(modifier = Modifier.width(12.dp))
    Column(modifier = Modifier.weight(1f)) {
      Text(title, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = BankTextPrimary)
      Text(subtitle, fontSize = 11.sp, color = BankTextSecondary)
    }
    Icon(
      imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
      contentDescription = null,
      tint = Color(0xFFCBD5E1),
      modifier = Modifier.size(12.dp)
    )
  }
}
