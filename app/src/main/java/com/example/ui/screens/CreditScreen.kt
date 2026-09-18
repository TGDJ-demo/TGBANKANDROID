package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.CreditScore
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.DemoBadge
import com.example.ui.components.PaymentSecurityAuthDialog
import com.example.ui.theme.BankBlueAccent
import com.example.ui.theme.BankNavyDark
import com.example.ui.theme.BankNavyLight
import com.example.ui.theme.BankNavyPrimary
import com.example.ui.theme.BankSuccessGreen
import com.example.ui.theme.BankTextPrimary
import com.example.ui.theme.BankTextSecondary
import com.example.viewmodel.BankViewModel
import com.example.viewmodel.Screen
import java.util.Locale
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import com.example.ui.utils.canAuthenticateBiometric
import com.example.ui.utils.showBiometricPrompt
import androidx.compose.runtime.LaunchedEffect

@Composable
fun CreditScreen(
  viewModel: BankViewModel
) {
  val profile by viewModel.userProfile.collectAsState()
  val creditScore = profile.creditScore
  val creditRating = profile.creditRating
  val creditLimit = profile.creditLimit
  val availableCredit = profile.availableCredit
  val usedCredit = profile.usedCredit
  val nextPaymentDue = profile.nextPayment
  val context = LocalContext.current
  var paymentSuccessMessage by remember { mutableStateOf<String?>(null) }
  var increaseSuccessMessage by remember { mutableStateOf<String?>(null) }
  val testControls by viewModel.testControls.collectAsState()
  val authMethod by viewModel.paymentAuthMethod.collectAsState()
  val authPassed by viewModel.authPassed.collectAsState()
  var payRequested by remember { mutableStateOf(false) }
  var showSecurityAuthDialog by remember { mutableStateOf(false) }
  val coroutineScope = rememberCoroutineScope()

  PaymentSecurityAuthDialog(
    visible = showSecurityAuthDialog,
    amount = nextPaymentDue,
    recipientOrPurpose = "Credit Card Bill Settlement",
    onAuthorized = {
      showSecurityAuthDialog = false
      coroutineScope.launch {
        val res = viewModel.payCreditBill(nextPaymentDue)
        res.onSuccess {
          paymentSuccessMessage = "Payment of $${String.format(Locale.US, "%,.2f", nextPaymentDue)} settled successfully!"
        }.onFailure { err ->
          Toast.makeText(context, err.message ?: "Payment failed", Toast.LENGTH_SHORT).show()
        }
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
    // Header
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          text = "Credit & Loans",
          fontSize = 20.sp,
          fontWeight = FontWeight.Bold,
          color = BankNavyDark
        )
        Text(
          text = "Local simulation • No credit bureau connection",
          fontSize = 11.sp,
          color = BankTextSecondary
        )
      }
      DemoBadge()
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Credit Score Gauge Card
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .testTag("credit_score_card"),
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
      Column(
        modifier = Modifier.padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Simulated Credit Score",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = BankNavyDark
          )
          Surface(
            color = Color(0xFFDCFCE7),
            shape = RoundedCornerShape(12.dp)
          ) {
            Text(
              text = creditRating,
              color = Color(0xFF166534),
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 4.dp)
                .testTag("credit_rating_tag")
            )
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Circular score meter
        Box(
          contentAlignment = Alignment.Center,
          modifier = Modifier.size(140.dp)
        ) {
          CircularProgressIndicator(
            progress = { creditScore / 900f },
            modifier = Modifier.size(130.dp),
            color = BankSuccessGreen,
            strokeWidth = 10.dp,
            trackColor = Color(0xFFF1F5F9)
          )
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
              text = creditScore.toString(),
              fontSize = 32.sp,
              fontWeight = FontWeight.Black,
              color = BankNavyDark,
              modifier = Modifier.testTag("credit_score_value")
            )
            Text(
              text = "out of 900",
              fontSize = 11.sp,
              color = BankTextSecondary
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
          text = "Your simulated credit profile is in the top tier (785/900). Low credit utilization and zero late payments.",
          fontSize = 11.sp,
          color = BankTextSecondary,
          lineHeight = 16.sp,
          modifier = Modifier.padding(horizontal = 8.dp)
        )
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Credit Overview Card
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .testTag("credit_overview_card"),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
      Column(modifier = Modifier.padding(20.dp)) {
        Text(
          text = "Credit Overview",
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold,
          color = BankNavyDark
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Utilization Bar
        val usedPct = (usedCredit / creditLimit).toFloat()
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text("Credit Utilization", fontSize = 12.sp, color = BankTextSecondary)
          Text("${(usedPct * 100).toInt()}% Used", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BankNavyPrimary)
        }
        Spacer(modifier = Modifier.height(6.dp))
        LinearProgressIndicator(
          progress = { usedPct },
          modifier = Modifier
            .fillMaxWidth()
            .height(8.dp)
            .clip(RoundedCornerShape(4.dp)),
          color = BankBlueAccent,
          trackColor = Color(0xFFF1F5F9)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          CreditStatColumn("Total Limit", "$${String.format(Locale.US, "%,.0f", creditLimit)}", "credit_stat_limit")
          CreditStatColumn("Available", "$${String.format(Locale.US, "%,.0f", availableCredit)}", "credit_stat_available", BankSuccessGreen)
          CreditStatColumn("Used", "$${String.format(Locale.US, "%,.0f", usedCredit)}", "credit_stat_used")
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 14.dp), color = Color(0xFFF1F5F9))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text("Next Payment Due", fontSize = 12.sp, color = BankTextSecondary)
            Text(
              text = "$${String.format(Locale.US, "%,.2f", nextPaymentDue)}",
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold,
              color = BankNavyDark,
              modifier = Modifier.testTag("credit_next_due_amount")
            )
            Text("Due: Oct 05, 2026", fontSize = 11.sp, color = Color(0xFFF59E0B))
          }

          Button(
            onClick = {
              showSecurityAuthDialog = true
            },
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BankNavyPrimary),
            modifier = Modifier.testTag("credit_make_payment_button")
          ) {
            Text("Authorize & Pay", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
          }
        }

        if (paymentSuccessMessage != null) {
          Spacer(modifier = Modifier.height(10.dp))
          Surface(
            color = Color(0xFFDCFCE7),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(
              text = paymentSuccessMessage ?: "",
              color = Color(0xFF166534),
              fontSize = 11.sp,
              fontWeight = FontWeight.SemiBold,
              modifier = Modifier.padding(8.dp)
            )
          }
        }
      }
    }

  LaunchedEffect(authPassed) {
    if (authPassed && payRequested) {
      payRequested = false
      coroutineScope.launch {
        val res = viewModel.payCreditBill(nextPaymentDue)
        res.onSuccess {
          paymentSuccessMessage = "Payment of $${nextPaymentDue} settled successfully!"
        }.onFailure { err ->
          Toast.makeText(context, err.message ?: "Payment failed", Toast.LENGTH_SHORT).show()
        }
      }
    }
  }

    Spacer(modifier = Modifier.height(16.dp))

    // Actions Row: Request Limit Increase, View Statements, Personal Loan
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text("Credit Actions", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = BankNavyDark)
        Spacer(modifier = Modifier.height(10.dp))

        // Increase Limit
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFF8FAFC))
            .padding(12.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.AutoMirrored.Filled.TrendingUp, contentDescription = null, tint = BankBlueAccent, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text("Request Credit Limit Increase", fontSize = 13.sp, fontWeight = FontWeight.Bold)
              Text("Instant demo evaluation based on score 785", fontSize = 11.sp, color = BankTextSecondary)
            }
          }
          Button(
            onClick = {
              val res = viewModel.requestCreditIncrease()
              res.onSuccess { newLimit ->
                increaseSuccessMessage = "Limit increased to $${String.format(Locale.US, "%,.0f", newLimit)}!"
              }
            },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.testTag("request_increase_button")
          ) {
            Text("Request", fontSize = 11.sp)
          }
        }

        if (increaseSuccessMessage != null) {
          Spacer(modifier = Modifier.height(8.dp))
          Text(increaseSuccessMessage ?: "", color = BankSuccessGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Statements
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFF8FAFC))
            .padding(12.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Description, contentDescription = null, tint = BankNavyPrimary, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text("Monthly Credit Statements", fontSize = 13.sp, fontWeight = FontWeight.Bold)
              Text("Download e-statements for automated test verification", fontSize = 11.sp, color = BankTextSecondary)
            }
          }
          OutlinedButton(
            onClick = {
              Toast.makeText(context, "Simulated Statement: TG_Statement_Aug2026.pdf generated", Toast.LENGTH_SHORT).show()
            },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.testTag("view_statements_button")
          ) {
            Text("Download", fontSize = 11.sp)
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Apply for Personal Loan or Credit Card
        Button(
          onClick = { viewModel.navigateTo(Screen.CreditApplication) },
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("apply_loan_button"),
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(containerColor = BankNavyPrimary)
        ) {
          Icon(Icons.Default.CreditCard, contentDescription = null)
          Spacer(modifier = Modifier.width(8.dp))
          Text("Apply for Personal Loan / New Card", fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
      }
    }

    Spacer(modifier = Modifier.height(24.dp))
  }
}

@Composable
fun CreditApplicationScreen(
  viewModel: BankViewModel
) {
  var requestedAmount by remember { mutableStateOf("25000") }
  var employmentType by remember { mutableStateOf("Full-Time Salaried") }
  var monthlyIncome by remember { mutableStateOf("12500") }
  var loanPurpose by remember { mutableStateOf("Home Improvement") }
  var duration by remember { mutableStateOf("36 Months") }
  var approvalResult by remember { mutableStateOf<String?>(null) }
  var errorMessage by remember { mutableStateOf<String?>(null) }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFFF8FAFC))
      .verticalScroll(rememberScrollState())
      .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    if (approvalResult != null) {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("loan_approval_card"),
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
            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = BankSuccessGreen, modifier = Modifier.size(36.dp))
          }

          Spacer(modifier = Modifier.height(14.dp))

          Text(
            text = "Loan Approved (Simulated)",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = BankNavyDark,
            modifier = Modifier.testTag("loan_approval_title")
          )

          Text(
            text = "Congratulations! Your simulated loan has been pre-approved instantly based on your credit score of 785.",
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
              LoanRow("Approved Loan ID", approvalResult ?: "LN-2026-9921", "loan_id_text")
              HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFE2E8F0))
              LoanRow("Principal Amount", "$${requestedAmount}", "loan_amount_text")
              HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFE2E8F0))
              LoanRow("Interest Rate", "6.49% APR (Fixed)", "loan_apr_text")
              HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFE2E8F0))
              LoanRow("Duration / Tenure", duration, "loan_tenure_text")
              HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFE2E8F0))
              LoanRow("Estimated Monthly EMI", "$${String.format(Locale.US, "%,.2f", (requestedAmount.toDoubleOrNull() ?: 25000.0) / 36 * 1.06)}", "loan_emi_text")
            }
          }

          Spacer(modifier = Modifier.height(24.dp))

          Button(
            onClick = { viewModel.navigateBack() },
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp)
              .testTag("loan_done_button"),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BankNavyPrimary)
          ) {
            Text("Done", fontSize = 15.sp, fontWeight = FontWeight.Bold)
          }
        }
      }
      return
    }

    // Application Form
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .testTag("loan_application_card"),
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
            Text("Personal Loan Application", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = BankNavyDark)
            Text("Simulated credit assessment", fontSize = 12.sp, color = BankTextSecondary)
          }
          DemoBadge()
        }

        Spacer(modifier = Modifier.height(18.dp))

        OutlinedTextField(
          value = requestedAmount,
          onValueChange = { requestedAmount = it },
          label = { Text("Requested Amount (USD)") },
          prefix = { Text("$ ", fontWeight = FontWeight.Bold) },
          singleLine = true,
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("loan_req_amount_input"),
          shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
          value = employmentType,
          onValueChange = { employmentType = it },
          label = { Text("Employment Type") },
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("loan_employment_input"),
          shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
          value = monthlyIncome,
          onValueChange = { monthlyIncome = it },
          label = { Text("Monthly Net Income (USD)") },
          prefix = { Text("$ ", fontWeight = FontWeight.Bold) },
          singleLine = true,
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("loan_income_input"),
          shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
          value = loanPurpose,
          onValueChange = { loanPurpose = it },
          label = { Text("Loan Purpose") },
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("loan_purpose_input"),
          shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text("Tenure / Duration", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = BankTextPrimary)
        Spacer(modifier = Modifier.height(4.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          listOf("12 Months", "24 Months", "36 Months", "60 Months").forEach { ten ->
            FilterChip(
              selected = duration == ten,
              onClick = { duration = ten },
              label = { Text(ten, fontSize = 11.sp) },
              modifier = Modifier.testTag("tenure_chip_${ten.replace(" ", "_")}")
            )
          }
        }

        if (errorMessage != null) {
          Spacer(modifier = Modifier.height(10.dp))
          Text(errorMessage ?: "", color = Color(0xFFEF4444), fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
          onClick = {
            val amt = requestedAmount.toDoubleOrNull()
            val inc = monthlyIncome.toDoubleOrNull()
            if (amt == null || inc == null) {
              errorMessage = "Please enter valid numeric amounts."
              return@Button
            }
            val res = viewModel.applyForCredit(amt, employmentType, inc, loanPurpose, duration)
            res.onSuccess { id ->
              approvalResult = id
            }.onFailure { err ->
              errorMessage = err.message ?: "Application failed."
            }
          },
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("submit_loan_button"),
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(containerColor = BankNavyPrimary)
        ) {
          Text("Submit Application", fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

@Composable
private fun CreditStatColumn(title: String, value: String, testTag: String, valueColor: Color = BankTextPrimary) {
  Column {
    Text(title, fontSize = 11.sp, color = BankTextSecondary)
    Spacer(modifier = Modifier.height(2.dp))
    Text(
      text = value,
      fontSize = 14.sp,
      fontWeight = FontWeight.Bold,
      color = valueColor,
      modifier = Modifier.testTag(testTag)
    )
  }
}

@Composable
private fun LoanRow(label: String, value: String, testTag: String) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(label, fontSize = 12.sp, color = BankTextSecondary)
    Text(value, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = BankTextPrimary, modifier = Modifier.testTag(testTag))
  }
}
