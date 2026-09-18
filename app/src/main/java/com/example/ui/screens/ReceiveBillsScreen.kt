package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Transaction
import com.example.ui.components.DemoBadge
import com.example.ui.components.TGLogo
import com.example.ui.theme.BankBlueAccent
import com.example.ui.theme.BankNavyDark
import com.example.ui.theme.BankNavyPrimary
import com.example.ui.theme.BankSuccessGreen
import com.example.ui.theme.BankTextPrimary
import com.example.ui.theme.BankTextSecondary
import com.example.viewmodel.BankViewModel
import com.example.viewmodel.Screen
import java.util.Locale

@Composable
fun ReceiveMoneyScreen(
  viewModel: BankViewModel
) {
  val userProfile by viewModel.userProfile.collectAsState()
  val clipboardManager = LocalClipboardManager.current
  val context = LocalContext.current

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFFF8FAFC))
      .verticalScroll(rememberScrollState())
      .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .testTag("receive_money_card"),
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
      Column(
        modifier = Modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        DemoBadge()
        Spacer(modifier = Modifier.height(12.dp))

        Text(
          text = "Receive Money",
          fontSize = 20.sp,
          fontWeight = FontWeight.Bold,
          color = BankNavyDark
        )

        Text(
          text = "Scan QR code or share account details below",
          fontSize = 12.sp,
          color = BankTextSecondary
        )

        Spacer(modifier = Modifier.height(20.dp))

        // High-Fidelity Simulated QR Code Frame
        Box(
          modifier = Modifier
            .size(210.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(2.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
            .padding(14.dp)
            .testTag("receive_qr_frame"),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.QrCode,
            contentDescription = "Simulated Account QR",
            tint = BankNavyDark,
            modifier = Modifier.size(170.dp)
          )
          // Center Badge
          Surface(
            shape = CircleShape,
            color = Color.White,
            shadowElevation = 3.dp,
            modifier = Modifier.size(36.dp)
          ) {
            Box(contentAlignment = Alignment.Center) {
              TGLogo(size = 28.dp, fontSize = 12)
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
          text = userProfile.name,
          fontSize = 17.sp,
          fontWeight = FontWeight.Bold,
          color = BankNavyDark,
          modifier = Modifier.testTag("receive_user_name")
        )

        Text(
          text = "UPI ID: ${userProfile.upiId}",
          fontSize = 13.sp,
          fontWeight = FontWeight.SemiBold,
          color = BankBlueAccent,
          modifier = Modifier.testTag("receive_upi_id")
        )

        Spacer(modifier = Modifier.height(20.dp))

        Surface(
          color = Color(0xFFF8FAFC),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            CopyableItemRow(
              label = "Account Number",
              value = userProfile.rawAccountNumber,
              onCopy = {
                clipboardManager.setText(AnnotatedString(userProfile.rawAccountNumber))
                Toast.makeText(context, "Account number copied", Toast.LENGTH_SHORT).show()
              },
              testTag = "copy_raw_account_button"
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFE2E8F0))
            CopyableItemRow(
              label = "IFSC / Routing Code",
              value = userProfile.ifsc,
              onCopy = {
                clipboardManager.setText(AnnotatedString(userProfile.ifsc))
                Toast.makeText(context, "IFSC copied", Toast.LENGTH_SHORT).show()
              },
              testTag = "copy_ifsc_button"
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFE2E8F0))
            CopyableItemRow(
              label = "UPI ID",
              value = userProfile.upiId,
              onCopy = {
                clipboardManager.setText(AnnotatedString(userProfile.upiId))
                Toast.makeText(context, "UPI ID copied", Toast.LENGTH_SHORT).show()
              },
              testTag = "copy_upi_id_button"
            )
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
          onClick = {
            clipboardManager.setText(AnnotatedString("TG Bank Account: ${userProfile.rawAccountNumber}\nIFSC: ${userProfile.ifsc}\nUPI: ${userProfile.upiId}"))
            Toast.makeText(context, "All account details copied!", Toast.LENGTH_SHORT).show()
          },
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("receive_share_details_button"),
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(containerColor = BankNavyPrimary)
        ) {
          Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text("Copy Complete Bank Details", fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

@Composable
private fun CopyableItemRow(
  label: String,
  value: String,
  onCopy: () -> Unit,
  testTag: String
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Column {
      Text(label, fontSize = 11.sp, color = BankTextSecondary)
      Text(value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = BankTextPrimary)
    }
    IconButton(
      onClick = onCopy,
      modifier = Modifier
        .size(32.dp)
        .testTag(testTag)
    ) {
      Icon(Icons.Default.ContentCopy, contentDescription = "Copy $label", tint = BankBlueAccent, modifier = Modifier.size(16.dp))
    }
  }
}

@Composable
fun PayBillsScreen(
  viewModel: BankViewModel
) {
  var selectedBiller by remember { mutableStateOf("City Power & Light Corp") }
  var billAmount by remember { mutableStateOf("210.50") }
  var billAccountNo by remember { mutableStateOf("991028") }
  var lastBillTx by remember { mutableStateOf<Transaction?>(null) }
  var errorMessage by remember { mutableStateOf<String?>(null) }

  val billers = listOf(
    BillerItem("City Power & Light Corp", "Electricity", 210.50, Icons.Default.ElectricBolt, Color(0xFFFEF3C7), Color(0xFFD97706)),
    BillerItem("Metro Water Works", "Water Utility", 65.20, Icons.Default.WaterDrop, Color(0xFFE0F2FE), BankBlueAccent),
    BillerItem("Apex Telecom Wireless", "Mobile 5G", 85.00, Icons.Default.PhoneAndroid, Color(0xFFDCFCE7), BankSuccessGreen),
    BillerItem("Spectrum High-Speed Net", "Broadband", 79.99, Icons.Default.NetworkCheck, Color(0xFFF3E8FF), Color(0xFF9333EA))
  )

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFFF8FAFC))
      .verticalScroll(rememberScrollState())
      .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .testTag("pay_bills_card"),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
      Column(modifier = Modifier.padding(20.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "Pay Bills",
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold,
              color = BankNavyDark
            )
            Text(
              text = "Simulate payment for utilities & recurring bills",
              fontSize = 12.sp,
              color = BankTextSecondary
            )
          }
          DemoBadge()
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text("Select Bill Provider", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = BankTextPrimary)
        Spacer(modifier = Modifier.height(8.dp))

        billers.forEach { biller ->
          val isSelected = selectedBiller == biller.name
          Surface(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 4.dp)
              .clip(RoundedCornerShape(12.dp))
              .clickable {
                selectedBiller = biller.name
                billAmount = String.format(Locale.US, "%.2f", biller.defaultAmount)
                errorMessage = null
              }
              .testTag("biller_${biller.name.take(6).replace(" ", "_")}"),
            color = if (isSelected) Color(0xFFEFF6FF) else Color(0xFFF8FAFC),
            border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, BankBlueAccent) else null
          ) {
            Row(
              modifier = Modifier.padding(12.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(36.dp)
                  .clip(RoundedCornerShape(8.dp))
                  .background(biller.bgColor),
                contentAlignment = Alignment.Center
              ) {
                Icon(biller.icon, contentDescription = null, tint = biller.tintColor, modifier = Modifier.size(20.dp))
              }
              Spacer(modifier = Modifier.width(12.dp))
              Column(modifier = Modifier.weight(1f)) {
                Text(biller.name, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = BankTextPrimary)
                Text(biller.category, fontSize = 11.sp, color = BankTextSecondary)
              }
              Text(
                text = "$${String.format(Locale.US, "%,.2f", biller.defaultAmount)}",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = BankNavyPrimary
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
          value = billAccountNo,
          onValueChange = { billAccountNo = it },
          label = { Text("Consumer / Account ID") },
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("bill_account_input"),
          shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
          value = billAmount,
          onValueChange = {
            billAmount = it
            errorMessage = null
          },
          label = { Text("Amount Due (USD)") },
          prefix = { Text("$ ", fontWeight = FontWeight.Bold) },
          singleLine = true,
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("bill_amount_input"),
          shape = RoundedCornerShape(12.dp)
        )

        if (errorMessage != null) {
          Spacer(modifier = Modifier.height(10.dp))
          Text(errorMessage ?: "", color = Color(0xFFEF4444), fontSize = 12.sp, modifier = Modifier.testTag("bill_error_text"))
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
          onClick = {
            val amt = billAmount.toDoubleOrNull()
            if (amt == null || amt <= 0.0) {
              errorMessage = "Please enter a valid amount."
              return@Button
            }
            val res = viewModel.executeBillPayment(selectedBiller, amt, billAccountNo)
            res.onSuccess { tx ->
              lastBillTx = tx
            }.onFailure { err ->
              errorMessage = err.message ?: "Bill payment failed."
            }
          },
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("pay_bill_submit_button"),
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(containerColor = BankNavyPrimary)
        ) {
          Text("Pay Bill Now", fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }
      }
    }

    if (lastBillTx != null) {
      Spacer(modifier = Modifier.height(16.dp))
      val tx = lastBillTx!!
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("bill_success_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFDCFCE7))
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text("✓ Bill Payment Settled", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF166534))
          Spacer(modifier = Modifier.height(4.dp))
          Text("Paid $${tx.amount} to ${tx.recipientOrMerchant}. Tx ID: ${tx.id}", fontSize = 12.sp, color = Color(0xFF166534))
        }
      }
    }
  }
}

private data class BillerItem(
  val name: String,
  val category: String,
  val defaultAmount: Double,
  val icon: ImageVector,
  val bgColor: Color,
  val tintColor: Color
)
