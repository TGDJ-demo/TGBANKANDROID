package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.NotificationType
import com.example.ui.components.DemoBadge
import com.example.ui.theme.BankBlueAccent
import com.example.ui.theme.BankErrorRed
import com.example.ui.theme.BankNavyDark
import com.example.ui.theme.BankNavyPrimary
import com.example.ui.theme.BankSuccessGreen
import com.example.ui.theme.BankTextPrimary
import com.example.ui.theme.BankTextSecondary
import com.example.viewmodel.BankViewModel

@Composable
fun NotificationsScreen(
  viewModel: BankViewModel
) {
  val notifications by viewModel.notifications.collectAsState()

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFFF8FAFC))
  ) {
    // Header & Actions
    Surface(
      color = Color.White,
      shadowElevation = 1.dp
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Notification Center",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = BankNavyDark
          )
          DemoBadge()
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.End
        ) {
          TextButton(
            onClick = { viewModel.markAllNotificationsRead() },
            modifier = Modifier.testTag("notifications_mark_read_button")
          ) {
            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Mark all read", fontSize = 12.sp)
          }

          Spacer(modifier = Modifier.width(8.dp))

          TextButton(
            onClick = { viewModel.clearNotifications() },
            modifier = Modifier.testTag("notifications_clear_all_button")
          ) {
            Icon(Icons.Default.ClearAll, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Clear all", fontSize = 12.sp, color = BankErrorRed)
          }
        }
      }
    }

    if (notifications.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(32.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Icon(
            imageVector = Icons.Default.Notifications,
            contentDescription = null,
            tint = BankTextSecondary,
            modifier = Modifier.size(48.dp)
          )
          Spacer(modifier = Modifier.height(10.dp))
          Text("No notifications", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = BankNavyDark)
          Text("You're all caught up with alerts.", fontSize = 12.sp, color = BankTextSecondary)
        }
      }
    } else {
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 16.dp, vertical = 8.dp)
      ) {
        itemsIndexed(notifications) { index, item ->
          val icon = when (item.type) {
            NotificationType.TRANSACTION -> Icons.Default.Paid
            NotificationType.SECURITY -> Icons.Default.Security
            NotificationType.REMINDER -> Icons.Default.CreditCard
            NotificationType.OFFER -> Icons.Default.Campaign
            NotificationType.SYSTEM -> Icons.Default.Notifications
          }

          val iconColor = when (item.type) {
            NotificationType.TRANSACTION -> BankSuccessGreen
            NotificationType.SECURITY -> Color(0xFFE11D48)
            NotificationType.REMINDER -> Color(0xFFD97706)
            NotificationType.OFFER -> BankBlueAccent
            NotificationType.SYSTEM -> BankNavyPrimary
          }

          val iconBg = when (item.type) {
            NotificationType.TRANSACTION -> Color(0xFFDCFCE7)
            NotificationType.SECURITY -> Color(0xFFFFE4E6)
            NotificationType.REMINDER -> Color(0xFFFEF3C7)
            NotificationType.OFFER -> Color(0xFFEFF6FF)
            NotificationType.SYSTEM -> Color(0xFFF1F5F9)
          }

          Card(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 4.dp)
              .testTag("notification_item_$index"),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
              containerColor = if (item.isRead) Color.White else Color(0xFFF1F5F9)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
              verticalAlignment = Alignment.Top
            ) {
              Box(
                modifier = Modifier
                  .size(38.dp)
                  .clip(RoundedCornerShape(10.dp))
                  .background(iconBg),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = icon,
                  contentDescription = item.type.name,
                  tint = iconColor,
                  modifier = Modifier.size(20.dp)
                )
              }

              Spacer(modifier = Modifier.width(12.dp))

              Column(modifier = Modifier.weight(1f)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = item.title,
                    fontWeight = if (item.isRead) FontWeight.SemiBold else FontWeight.Bold,
                    fontSize = 13.sp,
                    color = BankNavyDark
                  )
                  if (!item.isRead) {
                    Box(
                      modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(BankBlueAccent)
                    )
                  }
                }
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                  text = item.message,
                  fontSize = 12.sp,
                  color = BankTextPrimary,
                  lineHeight = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = item.timestamp,
                  fontSize = 10.sp,
                  color = BankTextSecondary
                )
              }
            }
          }
        }
      }
    }
  }
}
