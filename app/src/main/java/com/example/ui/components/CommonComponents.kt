package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Transaction
import com.example.model.TransactionType
import com.example.model.UserProfile
import com.example.ui.theme.BankBlueAccent
import com.example.ui.theme.BankErrorRed
import com.example.ui.theme.BankNavyDark
import com.example.ui.theme.BankNavyLight
import com.example.ui.theme.BankNavyPrimary
import com.example.ui.theme.BankSuccessGreen
import com.example.ui.theme.BankTextPrimary
import com.example.ui.theme.BankTextSecondary
import com.example.viewmodel.BottomTab
import java.util.Locale
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.IntOffset
import com.example.model.PaymentAuthMethod
import com.example.ui.theme.BankIndigo
import com.example.ui.theme.BankMagenta
import com.example.ui.theme.BankPurple
import com.example.ui.theme.BankTextSecondary
import kotlin.math.roundToInt

@Composable
fun TGLogo(
  modifier: Modifier = Modifier,
  size: Dp = 44.dp,
  fontSize: Int = 20
) {
  Box(
    modifier = modifier
      .size(size)
      .clip(RoundedCornerShape(size * 0.28f))
      .background(
        Brush.linearGradient(
          colors = listOf(BankNavyDark, BankNavyLight, BankBlueAccent)
        )
      )
      .testTag("tg_bank_logo"),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = "TG",
      color = Color.White,
      fontSize = fontSize.sp,
      fontWeight = FontWeight.Black,
      letterSpacing = 0.5.sp
    )
  }
}

@Composable
fun DemoBadge(
  modifier: Modifier = Modifier,
  onClick: (() -> Unit)? = null
) {
  Surface(
    modifier = modifier
      .clip(RoundedCornerShape(6.dp))
      .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
      .testTag("demo_indicator_chip"),
    color = Color(0xFFFEF3C7),
    contentColor = Color(0xFF92400E)
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(6.dp)
          .clip(CircleShape)
          .background(Color(0xFFD97706))
      )
      Spacer(modifier = Modifier.width(5.dp))
      Text(
        text = "DEMO SIMULATION",
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.5.sp
      )
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BankTopAppBar(
  title: String = "TG Bank",
  subtitle: String? = null,
  showBack: Boolean = false,
  unreadNotificationsCount: Int = 0,
  onBackClick: () -> Unit = {},
  onNotificationClick: () -> Unit = {},
  onTestControlsClick: () -> Unit = {}
) {
  TopAppBar(
    title = {
      Row(verticalAlignment = Alignment.CenterVertically) {
        if (!showBack) {
          TGLogo(size = 36.dp, fontSize = 16)
          Spacer(modifier = Modifier.width(10.dp))
        }
        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = title,
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold,
              color = BankTextPrimary
            )
            Spacer(modifier = Modifier.width(8.dp))
            DemoBadge(onClick = onTestControlsClick)
          }
          if (subtitle != null) {
            Text(
              text = subtitle,
              fontSize = 12.sp,
              color = BankTextSecondary
            )
          }
        }
      }
    },
    navigationIcon = {
      if (showBack) {
        IconButton(
          onClick = onBackClick,
          modifier = Modifier.testTag("top_bar_back_button")
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = Color.White
          )
        }
      }
    },
    actions = {
      // Test Controls shortcut button for Appium automation / TestGrid testing
      IconButton(
        onClick = onTestControlsClick,
        modifier = Modifier.testTag("top_bar_test_controls_button")
      ) {
        Icon(
          imageVector = Icons.Default.BugReport,
          contentDescription = "Demo Test Controls",
          tint = Color.White
        )
      }

      // Notifications button with badge
      IconButton(
        onClick = onNotificationClick,
        modifier = Modifier.testTag("top_bar_notifications_button")
      ) {
        BadgedBox(
          badge = {
            if (unreadNotificationsCount > 0) {
              Badge(
                containerColor = BankErrorRed,
                contentColor = Color.White
              ) {
                Text(unreadNotificationsCount.toString(), fontSize = 10.sp)
              }
            }
          }
        ) {
          Icon(
            imageVector = Icons.Default.Notifications,
            contentDescription = "Notifications",
            tint = Color.White
          )
        }
      }
    },
    colors = TopAppBarDefaults.topAppBarColors(
      containerColor = BankNavyPrimary,
      titleContentColor = Color.White,
      actionIconContentColor = Color.White
    )
  )
}

@Composable
fun BankBottomNavigationBar(
  selectedTab: BottomTab,
  onTabSelected: (BottomTab) -> Unit
) {
  NavigationBar(
    containerColor = Color.White,
    tonalElevation = 8.dp,
    modifier = Modifier.testTag("bottom_navigation_bar")
  ) {
    NavigationBarItem(
      selected = selectedTab == BottomTab.HOME,
      onClick = { onTabSelected(BottomTab.HOME) },
      icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
      label = { Text("Home") },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = BankNavyPrimary,
        selectedTextColor = BankNavyPrimary,
        indicatorColor = Color(0xFFDBEAFE)
      ),
      modifier = Modifier.testTag("nav_home")
    )
    NavigationBarItem(
      selected = selectedTab == BottomTab.PAYMENTS,
      onClick = { onTabSelected(BottomTab.PAYMENTS) },
      icon = { Icon(Icons.Default.SwapHoriz, contentDescription = "Payments") },
      label = { Text("Payments") },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = BankNavyPrimary,
        selectedTextColor = BankNavyPrimary,
        indicatorColor = Color(0xFFDBEAFE)
      ),
      modifier = Modifier.testTag("nav_payments")
    )
    NavigationBarItem(
      selected = selectedTab == BottomTab.TRANSACTIONS,
      onClick = { onTabSelected(BottomTab.TRANSACTIONS) },
      icon = { Icon(Icons.AutoMirrored.Filled.ReceiptLong, contentDescription = "Transactions") },
      label = { Text("History") },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = BankNavyPrimary,
        selectedTextColor = BankNavyPrimary,
        indicatorColor = Color(0xFFDBEAFE)
      ),
      modifier = Modifier.testTag("nav_transactions")
    )
    NavigationBarItem(
      selected = selectedTab == BottomTab.CREDIT,
      onClick = { onTabSelected(BottomTab.CREDIT) },
      icon = { Icon(Icons.Default.CreditCard, contentDescription = "Credit") },
      label = { Text("Credit") },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = BankNavyPrimary,
        selectedTextColor = BankNavyPrimary,
        indicatorColor = Color(0xFFDBEAFE)
      ),
      modifier = Modifier.testTag("nav_credit")
    )
    NavigationBarItem(
      selected = selectedTab == BottomTab.PROFILE,
      onClick = { onTabSelected(BottomTab.PROFILE) },
      icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
      label = { Text("Profile") },
      colors = NavigationBarItemDefaults.colors(
        selectedIconColor = BankNavyPrimary,
        selectedTextColor = BankNavyPrimary,
        indicatorColor = Color(0xFFDBEAFE)
      ),
      modifier = Modifier.testTag("nav_profile")
    )
  }
}

@Composable
fun AccountBalanceCard(
  profile: UserProfile,
  isBalanceVisible: Boolean,
  onToggleVisibility: () -> Unit,
  onCopyAccountNumber: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("account_balance_card"),
    shape = RoundedCornerShape(20.dp),
    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    colors = CardDefaults.cardColors(containerColor = BankNavyPrimary)
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(
          Brush.linearGradient(
            colors = listOf(BankNavyDark, BankNavyPrimary, Color(0xFF1E3A8A))
          )
        )
        .padding(20.dp)
    ) {
      Column {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            TGLogo(size = 32.dp, fontSize = 14)
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(
                text = "TG Bank",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
              )
              Text(
                text = profile.accountType,
                color = Color(0xFF93C5FD),
                fontSize = 11.sp
              )
            }
          }
          Surface(
            color = Color(0x33FFFFFF),
            shape = RoundedCornerShape(12.dp)
          ) {
            Text(
              text = profile.currency,
              color = Color.White,
              fontWeight = FontWeight.SemiBold,
              fontSize = 11.sp,
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
          text = "Available Balance",
          color = Color(0xFFCBD5E1),
          fontSize = 12.sp,
          fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.fillMaxWidth()
        ) {
          val displayBalance = if (isBalanceVisible) {
            "$${String.format(Locale.US, "%,.2f", profile.balance)}"
          } else {
            "$ ••••••••••••"
          }

          Text(
            text = displayBalance,
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = (-0.5).sp,
            modifier = Modifier
              .weight(1f)
              .testTag("account_balance_text")
          )

          IconButton(
            onClick = onToggleVisibility,
            modifier = Modifier.testTag("toggle_balance_visibility")
          ) {
            Icon(
              imageVector = if (isBalanceVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
              contentDescription = if (isBalanceVisible) "Hide balance" else "Show balance",
              tint = Color.White.copy(alpha = 0.85f)
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Account Number Footer
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = "Account: ${profile.accountNumber}",
              color = Color(0xFFE2E8F0),
              fontSize = 13.sp,
              fontWeight = FontWeight.SemiBold,
              modifier = Modifier.testTag("account_number_text")
            )
            Spacer(modifier = Modifier.width(4.dp))
            IconButton(
              onClick = onCopyAccountNumber,
              modifier = Modifier
                .size(28.dp)
                .testTag("copy_account_number_button")
            ) {
              Icon(
                imageVector = Icons.Default.ContentCopy,
                contentDescription = "Copy account number",
                tint = Color(0xFF93C5FD),
                modifier = Modifier.size(16.dp)
              )
            }
          }

          Surface(
            color = Color(0x3310B981),
            shape = RoundedCornerShape(8.dp)
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = BankSuccessGreen,
                modifier = Modifier.size(12.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "Active",
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
      }
    }
  }
}

@Composable
fun QuickActionItem(
  icon: ImageVector,
  label: String,
  testTag: String,
  backgroundColor: Color = Color(0xFFEFF6FF),
  iconColor: Color = BankBlueAccent,
  onClick: () -> Unit
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .width(76.dp)
      .clip(RoundedCornerShape(12.dp))
      .clickable(onClick = onClick)
      .padding(vertical = 8.dp)
      .testTag(testTag)
  ) {
    Box(
      modifier = Modifier
        .size(50.dp)
        .clip(RoundedCornerShape(14.dp))
        .background(backgroundColor),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = icon,
        contentDescription = label,
        tint = iconColor,
        modifier = Modifier.size(26.dp)
      )
    }
    Spacer(modifier = Modifier.height(6.dp))
    Text(
      text = label,
      fontSize = 11.sp,
      fontWeight = FontWeight.SemiBold,
      color = BankTextPrimary,
      maxLines = 1
    )
  }
}

@Composable
fun TransactionRowItem(
  transaction: Transaction,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  testTag: String = "tx_item_${transaction.id}"
) {
  val icon = when (transaction.type) {
    TransactionType.UPI -> Icons.Default.QrCode
    TransactionType.BANK_TRANSFER -> Icons.Default.AccountBalance
    TransactionType.CARD -> Icons.Default.CreditCard
    TransactionType.ATM -> Icons.Default.AccountBalanceWallet
    TransactionType.SALARY -> Icons.Default.ArrowDownward
    TransactionType.ADD_MONEY -> Icons.Default.ArrowDownward
    TransactionType.BILL_PAY -> Icons.Default.Receipt
    TransactionType.CREDIT_PAY -> Icons.Default.CreditCard
    TransactionType.ALL -> Icons.Default.SwapHoriz
    TransactionType.IMPS -> Icons.Default.AccountBalance
    TransactionType.NEFT -> Icons.Default.AccountBalance
    TransactionType.ACH -> Icons.Default.ArrowUpward
    TransactionType.RTGS -> Icons.Default.AccountBalance
  }

  val iconBg = if (transaction.isCredit) Color(0xFFDCFCE7) else Color(0xFFF1F5F9)
  val iconTint = if (transaction.isCredit) BankSuccessGreen else BankNavyPrimary

  Card(
    modifier = modifier
      .fillMaxWidth()
      .clickable(onClick = onClick)
      .testTag(testTag),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(44.dp)
          .clip(RoundedCornerShape(12.dp))
          .background(iconBg),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = transaction.type.displayName,
          tint = iconTint,
          modifier = Modifier.size(22.dp)
        )
      }

      Spacer(modifier = Modifier.width(12.dp))

      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = transaction.title,
          fontWeight = FontWeight.Bold,
          fontSize = 14.sp,
          color = BankTextPrimary
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = transaction.recipientOrMerchant,
          fontSize = 12.sp,
          color = BankTextSecondary,
          maxLines = 1
        )
        Text(
          text = transaction.timestamp,
          fontSize = 11.sp,
          color = Color(0xFF94A3B8)
        )
      }

      Column(horizontalAlignment = Alignment.End) {
        val sign = if (transaction.isCredit) "+" else "-"
        val amountColor = if (transaction.isCredit) BankSuccessGreen else BankTextPrimary
        Text(
          text = "$sign$${String.format(Locale.US, "%,.2f", transaction.amount)}",
          fontWeight = FontWeight.Bold,
          fontSize = 14.sp,
          color = amountColor
        )
        Spacer(modifier = Modifier.height(3.dp))
        Surface(
          shape = RoundedCornerShape(4.dp),
          color = if (transaction.status == "Completed") Color(0xFFEFF6FF) else Color(0xFFFEF3C7)
        ) {
          Text(
            text = transaction.status,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = if (transaction.status == "Completed") BankNavyPrimary else Color(0xFFB45309),
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
          )
        }
      }
    }
  }
}


// ─── Slide to Pay ─────────────────────────────────────────────────────────────
@Composable
fun SlideToPay(
  onCompleted: () -> Unit,
  enabled: Boolean = true,
  label: String = "Slide to Pay",
  modifier: Modifier = Modifier
) {
  var offsetX by remember { mutableStateOf(0f) }
  val maxOffset = 220f
  val progress = (offsetX / maxOffset).coerceIn(0f, 1f)

  Box(
    modifier = modifier
      .fillMaxWidth()
      .height(56.dp)
      .clip(RoundedCornerShape(28.dp))
      .background(
        Brush.horizontalGradient(
          listOf(BankIndigo, BankPurple, BankMagenta)
        )
      )
      .testTag("slide_to_pay_track")
  ) {
    Text(
      text = if (progress > 0.85f) "Release to confirm" else label,
      color = Color.White,
      fontWeight = FontWeight.Bold,
      fontSize = 14.sp,
      modifier = Modifier.align(Alignment.Center)
    )
    Box(
      modifier = Modifier
        .offset { IntOffset(offsetX.roundToInt(), 0) }
        .size(52.dp)
        .padding(2.dp)
        .clip(CircleShape)
        .background(Color.White)
        .testTag("slide_to_pay_thumb")
        .pointerInput(enabled) {
          if (!enabled) return@pointerInput
          detectHorizontalDragGestures(
            onDragEnd = {
              if (offsetX >= maxOffset * 0.9f) {
                onCompleted()
              }
              offsetX = 0f
            },
            onHorizontalDrag = { _, dragAmount ->
              offsetX = (offsetX + dragAmount).coerceIn(0f, maxOffset)
            }
          )
        },
      contentAlignment = Alignment.Center
    ) {
      Icon(
        Icons.Default.ChevronRight,
        contentDescription = "Slide to pay",
        tint = BankPurple,
        modifier = Modifier.size(28.dp)
      )
    }
  }
}

// ─── OTP Entry Dialog ─────────────────────────────────────────────────────────
@Composable
fun OtpVerificationDialog(
  visible: Boolean,
  expectedOtp: String = "123456",
  onVerified: () -> Unit,
  onDismiss: () -> Unit
) {
  if (!visible) return
  var otp by remember { mutableStateOf("") }
  var error by remember { mutableStateOf<String?>(null) }

  AlertDialog(
    onDismissRequest = onDismiss,
    modifier = Modifier.testTag("otp_dialog"),
    title = { Text("Enter OTP", fontWeight = FontWeight.Bold) },
    text = {
      Column {
        Text(
          "A 6-digit OTP was sent to your registered mobile / email. Demo OTP: $expectedOtp",
          fontSize = 13.sp,
          color = BankTextSecondary
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
          value = otp,
          onValueChange = {
            if (it.length <= 6 && it.all { c -> c.isDigit() }) {
              otp = it
              error = null
            }
          },
          label = { Text("OTP") },
          singleLine = true,
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("otp_input_field")
        )
        error?.let {
          Text(it, color = BankErrorRed, fontSize = 12.sp, modifier = Modifier.testTag("otp_error_text"))
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          if (otp == expectedOtp) onVerified() else error = "Invalid OTP. Use $expectedOtp"
        },
        modifier = Modifier.testTag("otp_verify_button"),
        colors = ButtonDefaults.buttonColors(containerColor = BankBlueAccent)
      ) { Text("Verify") }
    },
    dismissButton = {
      TextButton(onClick = onDismiss, modifier = Modifier.testTag("otp_cancel_button")) {
        Text("Cancel")
      }
    }
  )
}

// ─── MPIN Dialog ──────────────────────────────────────────────────────────────
@Composable
fun MpinVerificationDialog(
  visible: Boolean,
  expectedMpin: String = "123456",
  onVerified: () -> Unit,
  onDismiss: () -> Unit
) {
  if (!visible) return
  var mpin by remember { mutableStateOf("") }
  var error by remember { mutableStateOf<String?>(null) }

  AlertDialog(
    onDismissRequest = onDismiss,
    modifier = Modifier.testTag("mpin_dialog"),
    title = { Text("Enter MPIN", fontWeight = FontWeight.Bold) },
    text = {
      Column {
        Text("Enter your 6-digit MPIN to authorize this payment. Demo: $expectedMpin", fontSize = 13.sp, color = BankTextSecondary)
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
          value = mpin,
          onValueChange = {
            if (it.length <= 6 && it.all { c -> c.isDigit() }) {
              mpin = it
              error = null
            }
          },
          label = { Text("MPIN") },
          singleLine = true,
          visualTransformation = PasswordVisualTransformation(),
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("mpin_input_field")
        )
        error?.let {
          Text(it, color = BankErrorRed, fontSize = 12.sp, modifier = Modifier.testTag("mpin_error_text"))
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          if (mpin == expectedMpin) onVerified() else error = "Incorrect MPIN"
        },
        modifier = Modifier.testTag("mpin_verify_button"),
        colors = ButtonDefaults.buttonColors(containerColor = BankBlueAccent)
      ) { Text("Confirm") }
    },
    dismissButton = {
      TextButton(onClick = onDismiss, modifier = Modifier.testTag("mpin_cancel_button")) {
        Text("Cancel")
      }
    }
  )
}

// ─── Auth method chips ────────────────────────────────────────────────────────
@Composable
fun PaymentAuthMethodSelector(
  selected: PaymentAuthMethod,
  onSelect: (PaymentAuthMethod) -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("payment_auth_method_row"),
    horizontalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    PaymentAuthMethod.entries.forEach { method ->
      FilterChip(
        selected = selected == method,
        onClick = { onSelect(method) },
        label = { Text(method.displayName, fontSize = 11.sp) },
        modifier = Modifier.testTag("auth_method_${method.name.lowercase()}")
      )
    }
  }
}
