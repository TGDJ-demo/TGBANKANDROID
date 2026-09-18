package com.example.ui.screens

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.LocalAtm
import androidx.compose.material.icons.filled.QrCode
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Transaction
import com.example.ui.components.DemoBadge
import com.example.ui.components.PaymentSecurityAuthDialog
import com.example.ui.theme.BankBlueAccent
import com.example.ui.theme.BankErrorRed
import com.example.ui.theme.BankNavyDark
import com.example.ui.theme.BankNavyPrimary
import com.example.ui.theme.BankSuccessGreen
import com.example.ui.theme.BankTextPrimary
import com.example.ui.theme.BankTextSecondary
import com.example.viewmodel.BankViewModel
import com.example.viewmodel.Screen
import java.util.Locale

@Composable
fun AddMoneyScreen(
  viewModel: BankViewModel
) {
  var selectedMethod by remember { mutableStateOf("Debit Card") }
  var amountInput by remember { mutableStateOf("1000") }
  var errorText by remember { mutableStateOf<String?>(null) }
  var successTx by remember { mutableStateOf<Transaction?>(null) }

  val methods = listOf("Debit Card", "Bank Transfer", "UPI", "Demo Load")
  val presetAmounts = listOf(100.0, 500.0, 1000.0, 5000.0)

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFFF8FAFC))
      .verticalScroll(rememberScrollState())
      .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    if (successTx != null) {
      val tx = successTx!!
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("add_money_success_card"),
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
            text = "Money Added Successfully",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = BankNavyDark,
            modifier = Modifier.testTag("add_money_success_title")
          )

          Text(
            text = "Simulated deposit has been credited to your TG Bank account balance.",
            fontSize = 12.sp,
            color = BankTextSecondary,
            lineHeight = 16.sp
          )

          Spacer(modifier = Modifier.height(20.dp))

          Surface(
            color = Color(0xFFF8FAFC),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text("Transaction ID", fontSize = 12.sp, color = BankTextSecondary)
                Text(tx.id, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.testTag("add_money_tx_id"))
              }
              HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFE2E8F0))
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text("Amount Credited", fontSize = 12.sp, color = BankTextSecondary)
                Text("+$${String.format(Locale.US, "%,.2f", tx.amount)}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BankSuccessGreen)
              }
              HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFE2E8F0))
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text("Method", fontSize = 12.sp, color = BankTextSecondary)
                Text(selectedMethod, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
              }
            }
          }

          Spacer(modifier = Modifier.height(24.dp))

          Button(
            onClick = { viewModel.navigateBack() },
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp)
              .testTag("add_money_done_button"),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BankNavyPrimary)
          ) {
            Text("Done", fontSize = 15.sp, fontWeight = FontWeight.Bold)
          }

          Spacer(modifier = Modifier.height(10.dp))

          OutlinedButton(
            onClick = { viewModel.navigateTo(Screen.TransactionDetail(tx)) },
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp)
              .testTag("add_money_view_tx_button"),
            shape = RoundedCornerShape(12.dp)
          ) {
            Text("View in History", color = BankBlueAccent, fontWeight = FontWeight.SemiBold)
          }
        }
      }
      return
    }

    // Input Card
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .testTag("add_money_card"),
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
              text = "Add Money",
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold,
              color = BankNavyDark
            )
            Text(
              text = "Simulate loading funds into your checking account",
              fontSize = 12.sp,
              color = BankTextSecondary
            )
          }
          DemoBadge()
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
          text = "Select Method",
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          color = BankTextPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Methods list
        methods.forEach { method ->
          val isSelected = selectedMethod == method
          val icon = when (method) {
            "Debit Card" -> Icons.Default.CreditCard
            "Bank Transfer" -> Icons.Default.AccountBalance
            "UPI" -> Icons.Default.QrCode
            else -> Icons.Default.AccountBalanceWallet
          }

          Surface(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 4.dp)
              .clip(RoundedCornerShape(10.dp))
              .clickable { selectedMethod = method }
              .testTag("add_method_${method.replace(" ", "_")}"),
            color = if (isSelected) Color(0xFFEFF6FF) else Color(0xFFF8FAFC),
            shape = RoundedCornerShape(10.dp),
            border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, BankBlueAccent) else null
          ) {
            Row(
              modifier = Modifier.padding(14.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) BankBlueAccent else BankTextSecondary,
                modifier = Modifier.size(22.dp)
              )
              Spacer(modifier = Modifier.width(12.dp))
              Text(
                text = method,
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) BankNavyPrimary else BankTextPrimary
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
          text = "Choose Amount",
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          color = BankTextPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          presetAmounts.forEach { amt ->
            FilterChip(
              selected = amountInput == amt.toInt().toString(),
              onClick = {
                amountInput = amt.toInt().toString()
                errorText = null
              },
              label = { Text("$${amt.toInt()}", fontSize = 11.sp) },
              modifier = Modifier.testTag("add_amt_chip_${amt.toInt()}")
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Custom Amount
        OutlinedTextField(
          value = amountInput,
          onValueChange = {
            amountInput = it
            errorText = null
          },
          label = { Text("Custom Amount (USD)") },
          placeholder = { Text("100.00") },
          prefix = { Text("$ ", fontWeight = FontWeight.Bold) },
          singleLine = true,
          keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal,
            imeAction = ImeAction.Done
          ),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("add_money_amount_input"),
          shape = RoundedCornerShape(12.dp)
        )

        if (errorText != null) {
          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = errorText ?: "",
            color = BankErrorRed,
            fontSize = 12.sp,
            modifier = Modifier.testTag("add_money_error_text")
          )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
          onClick = {
            val amt = amountInput.toDoubleOrNull()
            if (amt == null || amt <= 0.0) {
              errorText = "Please enter a valid amount."
              return@Button
            }
            val res = viewModel.executeAddMoney(amt, selectedMethod)
            res.onSuccess { tx ->
              successTx = tx
            }.onFailure { err ->
              errorText = err.message ?: "Failed to add money."
            }
          },
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("add_money_submit_button"),
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(containerColor = BankNavyPrimary)
        ) {
          Text("Add Money Now", fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

@Composable
fun WithdrawScreen(
  viewModel: BankViewModel
) {
  val userProfile by viewModel.userProfile.collectAsState()
  var selectedMethod by remember { mutableStateOf("ATM") }
  var amountInput by remember { mutableStateOf("200") }
  var errorText by remember { mutableStateOf<String?>(null) }
  var successTx by remember { mutableStateOf<Transaction?>(null) }
  var showSecurityAuthDialog by remember { mutableStateOf(false) }

  val methods = listOf("ATM", "Bank Transfer", "Debit Account")

  PaymentSecurityAuthDialog(
    visible = showSecurityAuthDialog,
    amount = amountInput.toDoubleOrNull() ?: 200.0,
    recipientOrPurpose = "Withdrawal via $selectedMethod",
    onAuthorized = {
      showSecurityAuthDialog = false
      val amt = amountInput.toDoubleOrNull() ?: 200.0
      val res = viewModel.executeWithdraw(amt, selectedMethod)
      res.onSuccess { tx ->
        successTx = tx
      }.onFailure { err ->
        errorText = err.message ?: "Withdrawal failed."
      }
    },
    onDismiss = { showSecurityAuthDialog = false }
  )

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFFF8FAFC))
      .verticalScroll(rememberScrollState())
      .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    if (successTx != null) {
      val tx = successTx!!
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("withdraw_success_card"),
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
            text = "Withdrawal Successful",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = BankNavyDark,
            modifier = Modifier.testTag("withdraw_success_title")
          )

          Text(
            text = "Funds dispensed / transferred successfully in simulation.",
            fontSize = 12.sp,
            color = BankTextSecondary
          )

          Spacer(modifier = Modifier.height(20.dp))

          Surface(
            color = Color(0xFFF8FAFC),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text("Transaction ID", fontSize = 12.sp, color = BankTextSecondary)
                Text(tx.id, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.testTag("withdraw_tx_id"))
              }
              HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFE2E8F0))
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text("Amount Withdrawn", fontSize = 12.sp, color = BankTextSecondary)
                Text("-$${String.format(Locale.US, "%,.2f", tx.amount)}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BankTextPrimary)
              }
              HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFE2E8F0))
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text("Method", fontSize = 12.sp, color = BankTextSecondary)
                Text(selectedMethod, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
              }
            }
          }

          Spacer(modifier = Modifier.height(24.dp))

          Button(
            onClick = { viewModel.navigateBack() },
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp)
              .testTag("withdraw_done_button"),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BankNavyPrimary)
          ) {
            Text("Done", fontSize = 15.sp, fontWeight = FontWeight.Bold)
          }
        }
      }
      return
    }

    // Input Card
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .testTag("withdraw_card"),
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
              text = "Withdraw Money",
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold,
              color = BankNavyDark
            )
            Text(
              text = "Simulate cash withdrawal or outward account debit",
              fontSize = 12.sp,
              color = BankTextSecondary
            )
          }
          DemoBadge()
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
          text = "Withdrawal Method",
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          color = BankTextPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        methods.forEach { method ->
          val isSelected = selectedMethod == method
          val icon = when (method) {
            "ATM" -> Icons.Default.LocalAtm
            "Bank Transfer" -> Icons.Default.AccountBalance
            else -> Icons.Default.CreditCard
          }

          Surface(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 4.dp)
              .clip(RoundedCornerShape(10.dp))
              .clickable { selectedMethod = method }
              .testTag("withdraw_method_${method.replace(" ", "_")}"),
            color = if (isSelected) Color(0xFFEFF6FF) else Color(0xFFF8FAFC),
            shape = RoundedCornerShape(10.dp),
            border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, BankBlueAccent) else null
          ) {
            Row(
              modifier = Modifier.padding(14.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) BankBlueAccent else BankTextSecondary,
                modifier = Modifier.size(22.dp)
              )
              Spacer(modifier = Modifier.width(12.dp))
              Text(
                text = method,
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) BankNavyPrimary else BankTextPrimary
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(18.dp))

        OutlinedTextField(
          value = amountInput,
          onValueChange = {
            amountInput = it
            errorText = null
          },
          label = { Text("Amount (USD)") },
          placeholder = { Text("100.00") },
          prefix = { Text("$ ", fontWeight = FontWeight.Bold) },
          singleLine = true,
          keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal,
            imeAction = ImeAction.Done
          ),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("withdraw_amount_input"),
          shape = RoundedCornerShape(12.dp)
        )

        if (errorText != null) {
          Spacer(modifier = Modifier.height(10.dp))
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Warning, contentDescription = null, tint = BankErrorRed, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = errorText ?: "",
              color = BankErrorRed,
              fontSize = 12.sp,
              modifier = Modifier.testTag("withdraw_error_text")
            )
          }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
          onClick = {
            val amt = amountInput.toDoubleOrNull()
            if (amt == null || amt <= 0.0) {
              errorText = "Please enter a valid amount."
              return@Button
            }
            if (amt > userProfile.balance) {
              errorText = "Insufficient demo balance."
              return@Button
            }
            errorText = null
            showSecurityAuthDialog = true
          },
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("withdraw_submit_button"),
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(containerColor = BankNavyPrimary)
        ) {
          Text("Authorize & Withdraw Funds", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
      }
    }
  }
}
