package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AccountBalanceCard
import com.example.ui.components.QuickActionItem
import com.example.ui.components.TransactionRowItem
import com.example.ui.theme.BankBlueAccent
import com.example.ui.theme.BankIceBlue
import com.example.ui.theme.BankNavyDark
import com.example.ui.theme.BankNavyLight
import com.example.ui.theme.BankNavyPrimary
import com.example.ui.theme.BankSkyBlue
import com.example.ui.theme.BankSuccessGreen
import com.example.ui.theme.BankTextPrimary
import com.example.ui.theme.BankTextSecondary
import com.example.viewmodel.BankViewModel
import com.example.viewmodel.BottomTab
import com.example.viewmodel.Screen
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
  viewModel: BankViewModel
) {
  val userProfile by viewModel.userProfile.collectAsState()
  val transactions by viewModel.transactions.collectAsState()
  val isBalanceVisible by viewModel.isBalanceVisible.collectAsState()
  val spendingSummary = viewModel.getSpendingSummary()
  val clipboardManager = LocalClipboardManager.current
  val context = LocalContext.current

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFFF8FAFC))
      .padding(horizontal = 16.dp)
      .testTag("home_screen_content")
  ) {
    item {
      Spacer(modifier = Modifier.height(8.dp))

      // User Greeting Header
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Welcome back,",
            fontSize = 13.sp,
            color = BankTextSecondary
          )
          Text(
            text = userProfile.name,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = BankNavyDark,
            modifier = Modifier.testTag("home_user_name")
          )
        }

        Surface(
          color = Color(0xFFDCFCE7),
          shape = RoundedCornerShape(20.dp)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(BankSuccessGreen)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Online",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF166534)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Main Account Balance Card
      AccountBalanceCard(
        profile = userProfile,
        isBalanceVisible = isBalanceVisible,
        onToggleVisibility = { viewModel.toggleBalanceVisibility() },
        onCopyAccountNumber = {
          clipboardManager.setText(AnnotatedString(userProfile.rawAccountNumber))
          Toast.makeText(context, "Account number copied to clipboard", Toast.LENGTH_SHORT).show()
        }
      )

      Spacer(modifier = Modifier.height(20.dp))

      // Quick Actions Header
      Text(
        text = "Quick Actions",
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = BankNavyDark,
        modifier = Modifier.testTag("quick_actions_header")
      )

      Spacer(modifier = Modifier.height(12.dp))

      // Quick Actions 2x4 Grid
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
      ) {
        FlowRow(
          modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
          horizontalArrangement = Arrangement.SpaceAround,
          verticalArrangement = Arrangement.spacedBy(10.dp),
          maxItemsInEachRow = 4
        ) {
          QuickActionItem(
            icon = Icons.Default.Send,
            label = "Send",
            testTag = "quick_action_send_money",
            backgroundColor = Color(0xFFEFF6FF),
            iconColor = BankBlueAccent,
            onClick = { viewModel.startSendMoney() }
          )

          QuickActionItem(
            icon = Icons.Default.ArrowDownward,
            label = "Receive",
            testTag = "quick_action_receive_money",
            backgroundColor = Color(0xFFDCFCE7),
            iconColor = BankSuccessGreen,
            onClick = { viewModel.navigateTo(Screen.ReceiveMoney) }
          )

          QuickActionItem(
            icon = Icons.Default.QrCodeScanner,
            label = "UPI Pay",
            testTag = "quick_action_upi_pay",
            backgroundColor = Color(0xFFF3E8FF),
            iconColor = Color(0xFF9333EA),
            onClick = { viewModel.startUpiFlow() }
          )

          QuickActionItem(
            icon = Icons.Default.AddCard,
            label = "Add Money",
            testTag = "quick_action_add_money",
            backgroundColor = Color(0xFFE0F2FE),
            iconColor = BankSkyBlue,
            onClick = { viewModel.navigateTo(Screen.AddMoney) }
          )

          QuickActionItem(
            icon = Icons.Default.MoneyOff,
            label = "Withdraw",
            testTag = "quick_action_withdraw",
            backgroundColor = Color(0xFFFEF3C7),
            iconColor = Color(0xFFD97706),
            onClick = { viewModel.navigateTo(Screen.Withdraw) }
          )

          QuickActionItem(
            icon = Icons.Default.Receipt,
            label = "Pay Bills",
            testTag = "quick_action_pay_bills",
            backgroundColor = Color(0xFFFFEDD5),
            iconColor = Color(0xFFEA580C),
            onClick = { viewModel.navigateTo(Screen.PayBills) }
          )

          QuickActionItem(
            icon = Icons.Default.CreditCard,
            label = "Credit",
            testTag = "quick_action_credit",
            backgroundColor = Color(0xFFE0E7FF),
            iconColor = BankNavyLight,
            onClick = { viewModel.selectTab(BottomTab.CREDIT) }
          )

          QuickActionItem(
            icon = Icons.Default.VerifiedUser,
            label = "KYC",
            testTag = "quick_action_kyc",
            backgroundColor = Color(0xFFCCFBF1),
            iconColor = Color(0xFF0D9488),
            onClick = { viewModel.navigateTo(Screen.KycFlow) }
          )
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Spending Summary Card
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("spending_summary_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "Spending Summary",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = BankNavyDark
              )
              Text(
                text = "September 2026 • Simulated Outflow",
                fontSize = 11.sp,
                color = BankTextSecondary
              )
            }
            Surface(
              color = Color(0xFFEFF6FF),
              shape = RoundedCornerShape(8.dp)
            ) {
              Text(
                text = "$1,675.68 Total",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = BankNavyPrimary,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Proportional visual category breakdown
          spendingSummary.forEach { item ->
            val barColor = when (item.category) {
              "Shopping" -> BankBlueAccent
              "Food" -> Color(0xFFF59E0B)
              "Bills" -> Color(0xFFEF4444)
              "Transfers" -> Color(0xFF8B5CF6)
              else -> Color(0xFF10B981)
            }
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(
                  text = item.category,
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Medium,
                  color = BankTextPrimary
                )
                Text(
                  text = "$${String.format(Locale.US, "%,.2f", item.amount)} (${(item.percentage * 100).toInt()}%)",
                  fontSize = 12.sp,
                  fontWeight = FontWeight.SemiBold,
                  color = BankTextSecondary
                )
              }
              Spacer(modifier = Modifier.height(3.dp))
              LinearProgressIndicator(
                progress = { item.percentage },
                modifier = Modifier
                  .fillMaxWidth()
                  .height(6.dp)
                  .clip(RoundedCornerShape(3.dp)),
                color = barColor,
                trackColor = Color(0xFFF1F5F9)
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Recent Transactions Header
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Recent Transactions",
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          color = BankNavyDark
        )
        TextButton(
          onClick = { viewModel.selectTab(BottomTab.TRANSACTIONS) },
          modifier = Modifier.testTag("view_all_transactions_button")
        ) {
          Text(
            text = "View All",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = BankBlueAccent
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))
    }

    // First 6 Recent Transactions
    val recentList = transactions.take(6)
    itemsIndexed(recentList) { index, tx ->
      TransactionRowItem(
        transaction = tx,
        onClick = { viewModel.navigateTo(Screen.TransactionDetail(tx)) },
        modifier = Modifier.padding(vertical = 4.dp),
        testTag = "recent_tx_item_$index"
      )
    }

    item {
      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}
