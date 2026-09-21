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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
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
              color = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            DemoBadge(onClick = onTestControlsClick)
          }
          if (subtitle != null) {
            Text(
              text = subtitle,
              fontSize = 12.sp,
              color = Color(0xFFCBD5E1)
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

// ─── Unified Payment Security Auth Dialog ──────────────────────────────────────
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PaymentSecurityAuthDialog(
  visible: Boolean,
  amount: Double,
  recipientOrPurpose: String,
  accountInfo: String = "TG Bank • **** 4588",
  initialMethod: PaymentAuthMethod = PaymentAuthMethod.MPIN,
  onAuthorized: () -> Unit,
  onDismiss: () -> Unit
) {
  if (!visible) return

  var currentMethod by remember { mutableStateOf(initialMethod) }
  var pinDigits by remember { mutableStateOf("") }
  var otpInput by remember { mutableStateOf("") }
  var errorMessage by remember { mutableStateOf<String?>(null) }
  var isSuccess by remember { mutableStateOf(false) }
  val context = androidx.compose.ui.platform.LocalContext.current

  androidx.compose.ui.window.Dialog(
    onDismissRequest = onDismiss,
    properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Card(
      modifier = Modifier
        .fillMaxWidth(0.92f)
        .semantics { testTagsAsResourceId = true }
        .testTag("payment_security_dialog"),
      shape = RoundedCornerShape(24.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(22.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Security header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(Color(0xFFEFF6FF)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Security",
                tint = BankBlueAccent,
                modifier = Modifier.size(20.dp)
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "Authorize Payment",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = BankTextPrimary
              )
              Text(
                text = "Protected with Multi-Factor Security",
                fontSize = 11.sp,
                color = BankTextSecondary
              )
            }
          }
          DemoBadge()
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Amount and recipient summary badge
        Surface(
          color = Color(0xFFF8FAFC),
          shape = RoundedCornerShape(16.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text(
              text = "Amount to Debit",
              fontSize = 11.sp,
              color = BankTextSecondary,
              fontWeight = FontWeight.Medium
            )
            Text(
              text = "$${String.format(Locale.US, "%,.2f", amount)}",
              fontSize = 24.sp,
              fontWeight = FontWeight.Black,
              color = BankNavyDark,
              modifier = Modifier.testTag("auth_dialog_amount")
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "To: $recipientOrPurpose",
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold,
              color = BankTextPrimary,
              maxLines = 1
            )
            Text(
              text = "From: $accountInfo",
              fontSize = 11.sp,
              color = BankTextSecondary
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Auth Method Selector Chips
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .testTag("auth_method_selector_row"),
          horizontalArrangement = Arrangement.SpaceEvenly
        ) {
          FilterChip(
            selected = currentMethod == PaymentAuthMethod.MPIN,
            onClick = {
              currentMethod = PaymentAuthMethod.MPIN
              errorMessage = null
            },
            label = { Text("mPIN (1234)", fontSize = 11.sp) },
            modifier = Modifier.testTag("auth_chip_mpin")
          )
          FilterChip(
            selected = currentMethod == PaymentAuthMethod.BIOMETRIC,
            onClick = {
              currentMethod = PaymentAuthMethod.BIOMETRIC
              errorMessage = null
            },
            label = { Text("Fingerprint", fontSize = 11.sp) },
            modifier = Modifier.testTag("auth_chip_biometric")
          )
          FilterChip(
            selected = currentMethod == PaymentAuthMethod.OTP,
            onClick = {
              currentMethod = PaymentAuthMethod.OTP
              errorMessage = null
            },
            label = { Text("OTP (123456)", fontSize = 11.sp) },
            modifier = Modifier.testTag("auth_chip_otp")
          )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Content per method
        when (currentMethod) {
          PaymentAuthMethod.MPIN -> {
            Text(
              text = "Enter your 4-digit Security mPIN",
              fontSize = 12.sp,
              color = BankTextSecondary
            )
            Spacer(modifier = Modifier.height(10.dp))

            // PIN bubble indicators
            Row(
              horizontalArrangement = Arrangement.spacedBy(14.dp),
              modifier = Modifier.testTag("mpin_dots_row")
            ) {
              repeat(4) { idx ->
                val isFilled = idx < pinDigits.length
                Box(
                  modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(if (isFilled) BankNavyDark else Color(0xFFE2E8F0))
                    .testTag("mpin_dot_$idx")
                )
              }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Numeric keypad grid
            Column(
              verticalArrangement = Arrangement.spacedBy(8.dp),
              horizontalAlignment = Alignment.CenterHorizontally,
              modifier = Modifier.fillMaxWidth()
            ) {
              val keypad = listOf(
                listOf("1", "2", "3"),
                listOf("4", "5", "6"),
                listOf("7", "8", "9"),
                listOf("C", "0", "⌫")
              )
              keypad.forEach { row ->
                Row(
                  horizontalArrangement = Arrangement.spacedBy(12.dp),
                  modifier = Modifier.fillMaxWidth(),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  row.forEach { key ->
                    Surface(
                      modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .clickable {
                          when (key) {
                            "C" -> {
                              pinDigits = ""
                              errorMessage = null
                            }
                            "⌫" -> {
                              if (pinDigits.isNotEmpty()) {
                                pinDigits = pinDigits.dropLast(1)
                                errorMessage = null
                              }
                            }
                            else -> {
                              if (pinDigits.length < 4) {
                                val next = pinDigits + key
                                pinDigits = next
                                if (next.length == 4) {
                                  if (next == "1234") {
                                    isSuccess = true
                                    onAuthorized()
                                  } else {
                                    errorMessage = "Incorrect PIN. Demo PIN is 1234"
                                    pinDigits = ""
                                  }
                                }
                              }
                            }
                          }
                        }
                        .testTag("keypad_btn_$key"),
                      color = when (key) {
                        "C", "⌫" -> Color(0xFFF1F5F9)
                        else -> Color(0xFFF8FAFC)
                      },
                      shape = RoundedCornerShape(10.dp),
                      border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                      Box(contentAlignment = Alignment.Center) {
                        Text(
                          text = key,
                          fontSize = 18.sp,
                          fontWeight = FontWeight.Bold,
                          color = BankNavyDark
                        )
                      }
                    }
                  }
                }
              }
            }
          }

          PaymentAuthMethod.BIOMETRIC -> {
            Column(
              horizontalAlignment = Alignment.CenterHorizontally,
              modifier = Modifier.padding(vertical = 10.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(72.dp)
                  .clip(CircleShape)
                  .background(Color(0xFFEFF6FF))
                  .clickable {
                    isSuccess = true
                    onAuthorized()
                  }
                  .testTag("biometric_touch_sensor"),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.CheckCircle,
                  contentDescription = "Fingerprint Sensor",
                  tint = BankNavyPrimary,
                  modifier = Modifier.size(42.dp)
                )
              }
              Spacer(modifier = Modifier.height(12.dp))
              Text(
                text = "Touch Fingerprint Sensor",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = BankNavyDark
              )
              Text(
                text = "Place your registered finger on the sensor or click below to simulate instant match.",
                fontSize = 11.sp,
                color = BankTextSecondary,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
              )
              Spacer(modifier = Modifier.height(16.dp))
              Button(
                onClick = {
                  isSuccess = true
                  onAuthorized()
                },
                modifier = Modifier
                  .fillMaxWidth()
                  .height(46.dp)
                  .testTag("biometric_verify_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BankNavyPrimary)
              ) {
                Text("Simulate Fingerprint Match", color = Color.White, fontWeight = FontWeight.Bold)
              }
            }
          }

          PaymentAuthMethod.OTP -> {
            Column(
              horizontalAlignment = Alignment.CenterHorizontally,
              modifier = Modifier.fillMaxWidth()
            ) {
              Text(
                text = "One-Time Password sent to +1 (555) 019-4588",
                fontSize = 12.sp,
                color = BankTextSecondary
              )
              Spacer(modifier = Modifier.height(12.dp))
              OutlinedTextField(
                value = otpInput,
                onValueChange = {
                  if (it.length <= 6 && it.all { c -> c.isDigit() }) {
                    otpInput = it
                    errorMessage = null
                    if (it == "123456") {
                      isSuccess = true
                      onAuthorized()
                    }
                  }
                },
                label = { Text("Enter 6-Digit OTP") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                modifier = Modifier
                  .fillMaxWidth()
                  .testTag("otp_input_field"),
                shape = RoundedCornerShape(12.dp)
              )
              Spacer(modifier = Modifier.height(10.dp))
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                TextButton(
                  onClick = {
                    otpInput = "123456"
                    isSuccess = true
                    onAuthorized()
                  },
                  modifier = Modifier.testTag("otp_autofill_button")
                ) {
                  Text("Autofill Demo OTP (123456)", fontSize = 12.sp, color = BankBlueAccent)
                }
              }
              Spacer(modifier = Modifier.height(6.dp))
              Button(
                onClick = {
                  if (otpInput == "123456") {
                    isSuccess = true
                    onAuthorized()
                  } else {
                    errorMessage = "Invalid OTP. Use demo OTP: 123456"
                  }
                },
                modifier = Modifier
                  .fillMaxWidth()
                  .height(46.dp)
                  .testTag("otp_confirm_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BankNavyPrimary)
              ) {
                Text("Verify & Pay", color = Color.White, fontWeight = FontWeight.Bold)
              }
            }
          }
        }

        if (errorMessage != null) {
          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = errorMessage ?: "",
            color = BankErrorRed,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.testTag("auth_error_text")
          )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Cancel button
        TextButton(
          onClick = onDismiss,
          modifier = Modifier.testTag("auth_cancel_button")
        ) {
          Text("Cancel", color = BankTextSecondary, fontSize = 13.sp)
        }
      }
    }
  }
}

