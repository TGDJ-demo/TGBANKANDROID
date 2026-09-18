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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PaymentAuthMethod
import com.example.model.TransferRail
import com.example.ui.components.DemoBadge
import com.example.ui.components.MpinVerificationDialog
import com.example.ui.components.OtpVerificationDialog
import com.example.ui.components.PaymentAuthMethodSelector
import com.example.ui.components.PaymentSecurityAuthDialog
import com.example.ui.components.SlideToPay
import com.example.ui.theme.BankBlueAccent
import com.example.ui.theme.BankErrorRed
import com.example.ui.theme.BankIndigo
import com.example.ui.theme.BankNavyDark
import com.example.ui.theme.BankNavyPrimary
import com.example.ui.theme.BankPurple
import com.example.ui.theme.BankSuccessGreen
import com.example.ui.theme.BankTextPrimary
import com.example.ui.theme.BankTextSecondary
import com.example.viewmodel.BankViewModel
import java.util.Locale
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import com.example.ui.utils.canAuthenticateBiometric
import com.example.ui.utils.showBiometricPrompt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendMoneyScreen(
  viewModel: BankViewModel
) {
  val step by viewModel.sendStep.collectAsState()
  val recipient by viewModel.sendRecipient.collectAsState()
  val account by viewModel.sendAccount.collectAsState()
  val ifsc by viewModel.sendIfsc.collectAsState()
  val amount by viewModel.sendAmount.collectAsState()
  val note by viewModel.sendNote.collectAsState()
  val error by viewModel.sendError.collectAsState()
  val lastTx by viewModel.lastCreatedTx.collectAsState()
  val userProfile by viewModel.userProfile.collectAsState()
  val beneficiaries by viewModel.beneficiaries.collectAsState()
  val selectedBenId by viewModel.selectedBeneficiaryId.collectAsState()
  val sendRail by viewModel.sendRail.collectAsState()
  val authMethod by viewModel.paymentAuthMethod.collectAsState()
  val showOtp by viewModel.showOtpDialog.collectAsState()
  val showMpin by viewModel.showMpinDialog.collectAsState()
  val authPassed by viewModel.authPassed.collectAsState()
  val testControls by viewModel.testControls.collectAsState()
  val isProcessing by viewModel.isProcessingPayment.collectAsState()
  val lastApi by viewModel.lastApiResponse.collectAsState()

  val ctx = LocalContext.current

  var saveAsBeneficiary by remember { mutableStateOf(false) }
  var railExpanded by remember { mutableStateOf(false) }
  var showSecurityAuthDialog by remember { mutableStateOf(false) }

  PaymentSecurityAuthDialog(
    visible = showSecurityAuthDialog,
    amount = amount.toDoubleOrNull() ?: 50000.0,
    recipientOrPurpose = if (recipient.isNotEmpty()) recipient else "Wire Transfer Beneficiary",
    onAuthorized = {
      showSecurityAuthDialog = false
      viewModel.onAuthVerified()
      viewModel.confirmSendTransfer()
    },
    onDismiss = { showSecurityAuthDialog = false }
  )

  OtpVerificationDialog(
    visible = showOtp,
    onVerified = { viewModel.onAuthVerified() },
    onDismiss = { viewModel.dismissOtpDialog() }
  )
  MpinVerificationDialog(
    visible = showMpin,
    onVerified = { viewModel.onAuthVerified() },
    onDismiss = { viewModel.dismissMpinDialog() }
  )

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(
        Brush.verticalGradient(listOf(Color(0xFFF0F4FF), Color(0xFFF8FAFC)))
      )
      .verticalScroll(rememberScrollState())
      .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    // Step Indicator
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)
        .testTag("send_step_indicator"),
      horizontalArrangement = Arrangement.Center,
      verticalAlignment = Alignment.CenterVertically
    ) {
      StepCircle(num = "1", label = "Details", isActive = step >= 1, isCurrent = step == 1)
      Spacer(modifier = Modifier.width(12.dp))
      Box(modifier = Modifier.width(36.dp).height(2.dp).background(if (step >= 2) BankBlueAccent else Color(0xFFCBD5E1)))
      Spacer(modifier = Modifier.width(12.dp))
      StepCircle(num = "2", label = "Review", isActive = step >= 2, isCurrent = step == 2)
      Spacer(modifier = Modifier.width(12.dp))
      Box(modifier = Modifier.width(36.dp).height(2.dp).background(if (step >= 3) BankSuccessGreen else Color(0xFFCBD5E1)))
      Spacer(modifier = Modifier.width(12.dp))
      StepCircle(num = "3", label = "Success", isActive = step == 3, isCurrent = step == 3)
    }

    Spacer(modifier = Modifier.height(16.dp))

    when (step) {
      1 -> {
        Card(
          modifier = Modifier.fillMaxWidth().testTag("send_money_input_card"),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
          Column(modifier = Modifier.padding(18.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text("Transfer Details", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = BankNavyDark)
              DemoBadge()
            }
            Spacer(modifier = Modifier.height(12.dp))

            // Beneficiaries horizontal chips
            Text("Saved Beneficiaries", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = BankTextSecondary)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .testTag("beneficiary_list_row"),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              beneficiaries.take(4).forEach { b ->
                FilterChip(
                  selected = selectedBenId == b.id,
                  onClick = { viewModel.selectBeneficiary(if (selectedBenId == b.id) null else b.id) },
                  label = { Text(b.nickname, fontSize = 11.sp) },
                  modifier = Modifier.testTag("beneficiary_chip_${b.id}")
                )
              }
            }
            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
              value = recipient,
              onValueChange = { viewModel.updateSendRecipient(it) },
              label = { Text("Recipient Name") },
              leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = BankNavyPrimary) },
              singleLine = true,
              modifier = Modifier.fillMaxWidth().testTag("send_recipient_input")
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
              value = account,
              onValueChange = { viewModel.updateSendAccount(it) },
              label = { Text("Account Number") },
              leadingIcon = { Icon(Icons.Default.AccountBalance, contentDescription = null, tint = BankNavyPrimary) },
              singleLine = true,
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
              modifier = Modifier.fillMaxWidth().testTag("send_account_input")
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
              value = ifsc,
              onValueChange = { viewModel.updateSendIfsc(it) },
              label = { Text("IFSC / Routing Number") },
              singleLine = true,
              modifier = Modifier.fillMaxWidth().testTag("send_ifsc_input")
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Transfer Rail dropdown
            ExposedDropdownMenuBox(
              expanded = railExpanded,
              onExpandedChange = { railExpanded = it },
              modifier = Modifier.fillMaxWidth().testTag("send_rail_dropdown")
            ) {
              OutlinedTextField(
                value = "${sendRail.displayName} • ${sendRail.speed}",
                onValueChange = {},
                readOnly = true,
                label = { Text("Transfer Rail (ACH / IMPS / NEFT / RTGS)") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = railExpanded) },
                modifier = Modifier
                  .menuAnchor()
                  .fillMaxWidth()
                  .testTag("send_rail_field")
              )
              ExposedDropdownMenu(
                expanded = railExpanded,
                onDismissRequest = { railExpanded = false }
              ) {
                TransferRail.entries.forEach { rail ->
                  DropdownMenuItem(
                    text = {
                      Column {
                        Text(rail.displayName, fontWeight = FontWeight.Bold)
                        Text("${rail.description} • ${rail.speed}", fontSize = 11.sp, color = BankTextSecondary)
                      }
                    },
                    onClick = {
                      viewModel.updateSendRail(rail)
                      railExpanded = false
                    },
                    modifier = Modifier.testTag("rail_option_${rail.name.lowercase()}")
                  )
                }
              }
            }
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
              value = amount,
              onValueChange = { viewModel.updateSendAmount(it) },
              label = { Text("Amount (USD)") },
              singleLine = true,
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Next),
              modifier = Modifier.fillMaxWidth().testTag("send_amount_input")
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              listOf(100.0, 500.0, 1000.0, 5000.0).forEach { amt ->
                FilterChip(
                  selected = amount == amt.toInt().toString(),
                  onClick = { viewModel.updateSendAmount(amt.toInt().toString()) },
                  label = { Text("$${amt.toInt()}") },
                  modifier = Modifier.testTag("send_quick_amt_${amt.toInt()}")
                )
              }
            }
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
              value = note,
              onValueChange = { viewModel.updateSendNote(it) },
              label = { Text("Note (optional)") },
              singleLine = true,
              modifier = Modifier.fillMaxWidth().testTag("send_note_input")
            )

            // Checkbox: save as beneficiary
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .testTag("save_beneficiary_row")
            ) {
              Checkbox(
                checked = saveAsBeneficiary,
                onCheckedChange = { saveAsBeneficiary = it },
                modifier = Modifier.testTag("save_beneficiary_checkbox")
              )
              Text("Save as new beneficiary", fontSize = 13.sp, color = BankTextPrimary)
            }

            if (error != null) {
              Spacer(modifier = Modifier.height(8.dp))
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Warning, contentDescription = null, tint = BankErrorRed, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(error ?: "", color = BankErrorRed, fontSize = 12.sp, modifier = Modifier.testTag("send_error_text"))
              }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
              onClick = { viewModel.continueSendReview() },
              modifier = Modifier.fillMaxWidth().height(48.dp).testTag("send_continue_button"),
              shape = RoundedCornerShape(12.dp),
              colors = ButtonDefaults.buttonColors(containerColor = BankIndigo)
            ) {
              Text("Continue to Review", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
              onClick = { viewModel.navigateBack() },
              modifier = Modifier.fillMaxWidth().height(44.dp).testTag("send_cancel_button"),
              shape = RoundedCornerShape(12.dp)
            ) {
              Text("Cancel")
            }
          }
        }
      }

      2 -> {
        val fee = 0.00
        val amtVal = amount.toDoubleOrNull() ?: 0.0
        Card(
          modifier = Modifier.fillMaxWidth().testTag("send_review_card"),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
          Column(modifier = Modifier.padding(18.dp)) {
            Text("Review & Authorize", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = BankNavyDark)
            Spacer(modifier = Modifier.height(12.dp))
            ReviewRow("Recipient", recipient, "review_recipient_text")
            ReviewRow("Account", account, "review_account_text")
            ReviewRow("IFSC / Routing", ifsc, "review_ifsc_text")
            ReviewRow("Rail", "${sendRail.displayName} (${sendRail.speed})", "review_rail_text")
            ReviewRow("Amount", "$${String.format(Locale.US, "%,.2f", amtVal)}", "review_amount_text")
            ReviewRow("Fee", "$0.00 (Demo Free)", "review_fee_text")
            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text("Total Debit", fontWeight = FontWeight.Bold, color = BankTextPrimary)
              Text(
                "$${String.format(Locale.US, "%,.2f", amtVal + fee)}",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = BankNavyPrimary,
                modifier = Modifier.testTag("review_total_amount_text")
              )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Authorization Method", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = BankTextSecondary)
            Spacer(modifier = Modifier.height(8.dp))
            PaymentAuthMethodSelector(
              selected = authMethod,
              onSelect = { viewModel.updatePaymentAuthMethod(it) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (!authPassed) {
              Button(
                onClick = {
                  showSecurityAuthDialog = true
                },
                modifier = Modifier.fillMaxWidth().height(48.dp).testTag("request_auth_button"),
                colors = ButtonDefaults.buttonColors(containerColor = BankNavyPrimary),
                shape = RoundedCornerShape(12.dp)
              ) {
                Text("Authorize Transfer (mPIN / OTP / Fingerprint)", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
              }
            } else {
              Surface(
                color = Color(0xFFDCFCE7),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
              ) {
                Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                  Icon(Icons.Default.CheckCircle, contentDescription = null, tint = BankSuccessGreen, modifier = Modifier.size(18.dp))
                  Spacer(modifier = Modifier.width(8.dp))
                  Text("Security Authentication Verified", color = Color(0xFF166534), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
              }

              Spacer(modifier = Modifier.height(14.dp))
              Text("Slide to execute transfer", fontSize = 12.sp, color = BankTextSecondary)
              Spacer(modifier = Modifier.height(8.dp))
              SlideToPay(
                enabled = !isProcessing,
                onCompleted = { viewModel.confirmSendTransfer() },
                label = if (isProcessing) "Processing…" else "Slide to Pay $${String.format(Locale.US, "%,.0f", amtVal)}"
              )
            }

            lastApi?.let {
              Spacer(modifier = Modifier.height(10.dp))
              Surface(
                color = Color(0xFFEEF2FF),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth().testTag("last_api_response_banner")
              ) {
                Text(
                  "API: $it",
                  fontSize = 10.sp,
                  color = BankIndigo,
                  modifier = Modifier.padding(8.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(
              onClick = { viewModel.backToSendInput() },
              modifier = Modifier.fillMaxWidth().testTag("send_back_to_edit_button"),
              shape = RoundedCornerShape(12.dp)
            ) {
              Text("Edit Details")
            }
          }
        }
      }

      3 -> {
        Card(
          modifier = Modifier.fillMaxWidth().testTag("send_success_card"),
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
          Column(
            modifier = Modifier.padding(24.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Box(
              modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(Color(0xFFDCFCE7)),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.CheckCircle, contentDescription = null, tint = BankSuccessGreen, modifier = Modifier.size(42.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("Transfer Successful!", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = BankNavyDark, modifier = Modifier.testTag("send_success_title"))
            Spacer(modifier = Modifier.height(8.dp))
            lastTx?.let { tx ->
              Text(
                "$${String.format(Locale.US, "%,.2f", tx.amount)} sent to ${tx.recipientOrMerchant}",
                fontSize = 14.sp,
                color = BankTextSecondary,
                modifier = Modifier.testTag("send_success_amount")
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text("Ref: ${tx.id} • ${tx.rail}", fontSize = 12.sp, color = BankTextSecondary, modifier = Modifier.testTag("send_success_ref"))
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(
              onClick = { viewModel.navigateBack() },
              modifier = Modifier.fillMaxWidth().height(48.dp).testTag("send_done_button"),
              colors = ButtonDefaults.buttonColors(containerColor = BankSuccessGreen),
              shape = RoundedCornerShape(12.dp)
            ) {
              Text("Done", fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }
  }
}

@Composable
private fun StepCircle(num: String, label: String, isActive: Boolean, isCurrent: Boolean) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Box(
      modifier = Modifier
        .size(28.dp)
        .clip(CircleShape)
        .background(
          when {
            isCurrent -> BankBlueAccent
            isActive -> BankSuccessGreen
            else -> Color(0xFFCBD5E1)
          }
        ),
      contentAlignment = Alignment.Center
    ) {
      Text(num, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
    Spacer(modifier = Modifier.height(4.dp))
    Text(label, fontSize = 10.sp, color = if (isActive) BankNavyPrimary else BankTextSecondary)
  }
}

@Composable
private fun ReviewRow(label: String, value: String, testTag: String) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp),
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Text(label, fontSize = 13.sp, color = BankTextSecondary)
    Text(value, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = BankTextPrimary, modifier = Modifier.testTag(testTag))
  }
}
