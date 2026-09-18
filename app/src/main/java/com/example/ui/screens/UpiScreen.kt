package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.TransactionType
import com.example.ui.camera.InAppCameraScanner
import com.example.ui.components.DemoBadge
import com.example.ui.components.PaymentSecurityAuthDialog
import com.example.ui.components.TransactionRowItem
import com.example.ui.theme.BankBlueAccent
import com.example.ui.theme.BankErrorRed
import com.example.ui.theme.BankNavyDark
import com.example.ui.theme.BankNavyPrimary
import com.example.ui.theme.BankSuccessGreen
import com.example.ui.theme.BankTextPrimary
import com.example.ui.theme.BankTextSecondary
import com.example.viewmodel.BankViewModel
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.example.ui.utils.canAuthenticateBiometric
import com.example.ui.utils.showBiometricPrompt
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.example.viewmodel.Screen
import java.util.Locale
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.Manifest
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import android.graphics.Bitmap

@Composable
fun UpiScreen(
  viewModel: BankViewModel
) {
  val upiTab by viewModel.upiTab.collectAsState()
  val upiId by viewModel.upiIdInput.collectAsState()
  val amount by viewModel.upiAmountInput.collectAsState()
  val note by viewModel.upiNoteInput.collectAsState()
  val error by viewModel.upiError.collectAsState()
  val successTx by viewModel.upiSuccessTx.collectAsState()
  val transactions by viewModel.transactions.collectAsState()
  val testControls by viewModel.testControls.collectAsState()
  val authMethod by viewModel.paymentAuthMethod.collectAsState()
  val authPassed by viewModel.authPassed.collectAsState()
  val ctx = LocalContext.current
  var showInAppScanner by remember { mutableStateOf(false) }
  var showSecurityAuthDialog by remember { mutableStateOf(false) }
  var pendingMerchant by remember { mutableStateOf<String?>(null) }
  val coroutineScope = rememberCoroutineScope()

  if (showInAppScanner) {
    InAppCameraScanner(
      onQrScanned = { payload, merchant, amt ->
        viewModel.updateUpiId("merchant@tg")
        viewModel.updateUpiAmount(String.format(Locale.US, "%.2f", amt))
        pendingMerchant = merchant
        showInAppScanner = false
        showSecurityAuthDialog = true
      },
      onClose = { showInAppScanner = false }
    )
    return
  }

  // Payment Security Multi-Factor Authentication Dialog
  PaymentSecurityAuthDialog(
    visible = showSecurityAuthDialog,
    amount = amount.toDoubleOrNull() ?: 125.00,
    recipientOrPurpose = pendingMerchant ?: if (upiId.isNotEmpty()) upiId else "TG Demo Merchant",
    onAuthorized = {
      showSecurityAuthDialog = false
      viewModel.confirmUpiPayment(pendingMerchant)
    },
    onDismiss = { showSecurityAuthDialog = false }
  )

  // If a UPI payment succeeded, show the UPI Success Screen
  if (successTx != null) {
    val tx = successTx!!
    Column(
      modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFF8FAFC))
        .verticalScroll(rememberScrollState())
        .padding(20.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("upi_success_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(
          modifier = Modifier.padding(24.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Box(
            modifier = Modifier
              .size(64.dp)
              .clip(CircleShape)
              .background(Color(0xFFDCFCE7)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.CheckCircle,
              contentDescription = null,
              tint = BankSuccessGreen,
              modifier = Modifier.size(36.dp)
            )
          }

          Spacer(modifier = Modifier.height(14.dp))

          Text(
            text = "Payment Successful",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = BankNavyDark,
            modifier = Modifier.testTag("upi_success_title")
          )

          Text(
            text = "Simulated UPI payment transferred instantly via TG Switch",
            fontSize = 12.sp,
            color = BankTextSecondary
          )

          Spacer(modifier = Modifier.height(20.dp))

          Surface(
            color = Color(0xFFF8FAFC),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              UpiDetailRow("Merchant / Recipient", tx.recipientOrMerchant, "upi_success_merchant")
              HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFE2E8F0))
              UpiDetailRow("Amount", "$${String.format(Locale.US, "%,.2f", tx.amount)}", "upi_success_amount")
              HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFE2E8F0))
              UpiDetailRow("UPI ID", if (upiId.isNotBlank()) upiId else "merchant@tg", "upi_success_vpa")
              HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFE2E8F0))
              UpiDetailRow("Date / Time", tx.timestamp, "upi_success_timestamp")
              HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFE2E8F0))
              UpiDetailRow("Transaction ID", tx.id, "upi_success_tx_id")
              HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFE2E8F0))
              UpiDetailRow("Payment Status", "Completed (Simulated)", "upi_success_status")
            }
          }

          Spacer(modifier = Modifier.height(24.dp))

          Button(
            onClick = {
              viewModel.resetUpiSuccess()
              viewModel.navigateBack()
            },
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp)
              .testTag("upi_success_done_button"),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BankNavyPrimary)
          ) {
            Text("Done", fontSize = 15.sp, fontWeight = FontWeight.Bold)
          }

          Spacer(modifier = Modifier.height(10.dp))

          OutlinedButton(
            onClick = {
              val currentTx = tx
              viewModel.resetUpiSuccess()
              viewModel.navigateTo(Screen.TransactionDetail(currentTx))
            },
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp)
              .testTag("upi_success_view_button"),
            shape = RoundedCornerShape(12.dp)
          ) {
            Text("View Transaction", color = BankBlueAccent, fontWeight = FontWeight.SemiBold)
          }
        }
      }
    }
    return
  }

  // UPI HOME SCREEN
  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFFF8FAFC))
  ) {
    // Header
    Surface(
      color = Color.White,
      shadowElevation = 1.dp
    ) {
      Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFF3E8FF)),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.QrCodeScanner, contentDescription = null, tint = Color(0xFF9333EA), modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "UPI Payments",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = BankNavyDark
              )
              Text(
                text = "Simulated Unified Payments Interface",
                fontSize = 11.sp,
                color = BankTextSecondary
              )
            }
          }
          DemoBadge()
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Navigation Tabs
        ScrollableTabRow(
          selectedTabIndex = upiTab,
          edgePadding = 0.dp,
          containerColor = Color.White,
          contentColor = BankNavyPrimary,
          modifier = Modifier.fillMaxWidth()
        ) {
          Tab(
            selected = upiTab == 0,
            onClick = { viewModel.setUpiTab(0) },
            text = { Text("Pay UPI ID") },
            modifier = Modifier.testTag("upi_pay_id_tab")
          )
          Tab(
            selected = upiTab == 1,
            onClick = { viewModel.setUpiTab(1) },
            text = { Text("Scan & Pay") },
            modifier = Modifier.testTag("upi_scan_pay_tab")
          )
          Tab(
            selected = upiTab == 2,
            onClick = { viewModel.setUpiTab(2) },
            text = { Text("Send Contact") },
            modifier = Modifier.testTag("upi_send_contact_tab")
          )
          Tab(
            selected = upiTab == 3,
            onClick = { viewModel.setUpiTab(3) },
            text = { Text("History") },
            modifier = Modifier.testTag("upi_history_tab")
          )
        }
      }
    }

    // Tab Body
    Box(modifier = Modifier.fillMaxSize()) {
      when (upiTab) {
        0 -> {
          // Pay UPI ID
          Column(
            modifier = Modifier
              .fillMaxSize()
              .verticalScroll(rememberScrollState())
              .padding(16.dp)
          ) {
            Card(
              modifier = Modifier
                .fillMaxWidth()
                .testTag("upi_pay_id_card"),
              shape = RoundedCornerShape(16.dp),
              colors = CardDefaults.cardColors(containerColor = Color.White),
              elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
              Column(modifier = Modifier.padding(20.dp)) {
                Text(
                  text = "Pay to Virtual Payment Address (UPI ID)",
                  fontSize = 15.sp,
                  fontWeight = FontWeight.Bold,
                  color = BankNavyDark
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                  value = upiId,
                  onValueChange = { viewModel.updateUpiId(it) },
                  label = { Text("UPI ID") },
                  placeholder = { Text("e.g. sanjay@tg, merchant@tg") },
                  leadingIcon = {
                    Icon(Icons.Default.Payment, contentDescription = null, tint = BankNavyPrimary)
                  },
                  singleLine = true,
                  isError = error?.contains("UPI ID", ignoreCase = true) == true,
                  modifier = Modifier
                    .fillMaxWidth()
                    .testTag("upi_id_input"),
                  shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Suggestion chips
                Text(
                  text = "Quick Demo UPI IDs:",
                  fontSize = 11.sp,
                  color = BankTextSecondary,
                  fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                  listOf("sanjay@tg", "merchant@tg", "demo@tg").forEach { chipId ->
                    FilterChip(
                      selected = upiId == chipId,
                      onClick = { viewModel.useDemoUpiId(chipId) },
                      label = { Text(chipId, fontSize = 11.sp) },
                      modifier = Modifier.testTag("upi_chip_${chipId.replace("@", "_")}")
                    )
                  }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                  value = amount,
                  onValueChange = { viewModel.updateUpiAmount(it) },
                  label = { Text("Amount (USD)") },
                  placeholder = { Text("0.00") },
                  prefix = { Text("$ ", fontWeight = FontWeight.Bold) },
                  singleLine = true,
                  keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                  ),
                  isError = error?.contains("amount", ignoreCase = true) == true,
                  modifier = Modifier
                    .fillMaxWidth()
                    .testTag("upi_amount_input"),
                  shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                  value = note,
                  onValueChange = { viewModel.updateUpiNote(it) },
                  label = { Text("Message / Note (Optional)") },
                  placeholder = { Text("e.g. Dinner, Coffee, Rent") },
                  singleLine = true,
                  modifier = Modifier
                    .fillMaxWidth()
                    .testTag("upi_message_input"),
                  shape = RoundedCornerShape(12.dp)
                )

                if (error != null) {
                  Spacer(modifier = Modifier.height(12.dp))
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = BankErrorRed, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                      text = error ?: "",
                      color = BankErrorRed,
                      fontSize = 12.sp,
                      fontWeight = FontWeight.Medium,
                      modifier = Modifier.testTag("upi_error_text")
                    )
                  }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                  onClick = {
                    val amt = amount.toDoubleOrNull()
                    if (amt == null || amt <= 0.0) {
                      Toast.makeText(ctx, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
                      return@Button
                    }
                    pendingMerchant = null
                    showSecurityAuthDialog = true
                  },
                  modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("upi_pay_button"),
                  shape = RoundedCornerShape(12.dp),
                  colors = ButtonDefaults.buttonColors(containerColor = BankNavyPrimary)
                ) {
                  Text("Authorize & Pay via UPI", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
              }
            }
          }
        }

        1 -> {
          // Scan & Pay (Simulated QR Scanner)
          Column(
            modifier = Modifier
              .fillMaxSize()
              .verticalScroll(rememberScrollState())
              .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Card(
              modifier = Modifier
                .fillMaxWidth()
                .testTag("upi_scanner_card"),
              shape = RoundedCornerShape(16.dp),
              colors = CardDefaults.cardColors(containerColor = Color.White),
              elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
              Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
              ) {
                Text(
                  text = "Simulated QR Code Scanner",
                  fontSize = 16.sp,
                  fontWeight = FontWeight.Bold,
                  color = BankNavyDark
                )
                Text(
                  text = "No camera required for automated mobile testing.",
                  fontSize = 12.sp,
                  color = BankTextSecondary
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Camera-backed scanner (opens device camera to capture a frame and simulate QR scanning)
                Box(
                  modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF0F172A))
                    .border(2.dp, BankBlueAccent, RoundedCornerShape(16.dp))
                    .testTag("mock_qr_viewfinder"),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Default.QrCode,
                    contentDescription = "Simulated QR code",
                    tint = Color.White.copy(alpha = 0.35f),
                    modifier = Modifier.size(120.dp)
                  )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Open Real Camera Scanner
                Button(
                  onClick = {
                    showInAppScanner = true
                  },
                  modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("upi_open_camera_button"),
                  shape = RoundedCornerShape(12.dp),
                  colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C3AED))
                ) {
                  Icon(Icons.Default.QrCodeScanner, contentDescription = null, tint = Color.White)
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(
                    text = "Open Real Phone Camera Scanner",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                  )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Surface(
                  color = Color(0xFFEFF6FF),
                  shape = RoundedCornerShape(10.dp),
                  modifier = Modifier.fillMaxWidth()
                ) {
                  Text(
                    text = "Clicking 'Use Demo QR' loads merchant: TG Demo Store with amount: $125.00 and confirms payment instantly.",
                    fontSize = 11.sp,
                    color = BankNavyDark,
                    modifier = Modifier.padding(12.dp)
                  )
                }
              }
            }
          }
        }

        2 -> {
          // Send to Contact
          Column(
            modifier = Modifier
              .fillMaxSize()
              .verticalScroll(rememberScrollState())
              .padding(16.dp)
          ) {
            Text(
              text = "Select Demo Contact",
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = BankNavyDark
            )
            Spacer(modifier = Modifier.height(10.dp))

            val demoContacts = listOf(
              Triple("Priya K", "priya@tg", "+1 555-010-9921"),
              Triple("Alex Chen", "alex@tg", "+1 555-010-4429"),
              Triple("TG Coffee Co", "coffee@tg", "+1 555-010-8800"),
              Triple("David Miller", "david@tg", "+1 555-010-1234")
            )

            demoContacts.forEach { (name, id, phone) ->
              Card(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 4.dp)
                  .clickable {
                    viewModel.useDemoUpiId(id)
                    viewModel.updateUpiAmount("50.00")
                    pendingMerchant = name
                    showSecurityAuthDialog = true
                  }
                  .testTag("upi_contact_${name.replace(" ", "_")}"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
              ) {
                Row(
                  modifier = Modifier.padding(14.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Box(
                    modifier = Modifier
                      .size(40.dp)
                      .clip(CircleShape)
                      .background(Color(0xFFE0E7FF)),
                    contentAlignment = Alignment.Center
                  ) {
                    Text(
                      text = name.take(2).uppercase(),
                      color = BankNavyPrimary,
                      fontWeight = FontWeight.Bold,
                      fontSize = 13.sp
                    )
                  }
                  Spacer(modifier = Modifier.width(12.dp))
                  Column(modifier = Modifier.weight(1f)) {
                    Text(name, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = BankTextPrimary)
                    Text("$id • $phone", fontSize = 11.sp, color = BankTextSecondary)
                  }
                  Text("Pay", color = BankBlueAccent, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
              }
            }
          }
        }

        3 -> {
          // UPI Transaction History
          val upiList = transactions.filter { it.type == TransactionType.UPI }
          if (upiList.isEmpty()) {
            Box(
              modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
              contentAlignment = Alignment.Center
            ) {
              Text("No UPI transactions recorded yet.", color = BankTextSecondary)
            }
          } else {
            LazyColumn(
              modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
              items(upiList) { tx ->
                TransactionRowItem(
                  transaction = tx,
                  onClick = { viewModel.navigateTo(Screen.TransactionDetail(tx)) },
                  modifier = Modifier.padding(vertical = 4.dp),
                  testTag = "upi_history_tx_${tx.id}"
                )
              }
            }
          }
        }
      }
    }
  }
}

@Composable
private fun UpiDetailRow(label: String, value: String, testTag: String) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(text = label, fontSize = 12.sp, color = BankTextSecondary)
    Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = BankTextPrimary, modifier = Modifier.testTag(testTag))
  }
}
