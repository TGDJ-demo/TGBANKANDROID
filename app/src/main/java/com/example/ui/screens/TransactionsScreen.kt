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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Transaction
import com.example.model.TransactionType
import com.example.ui.components.DemoBadge
import com.example.ui.components.TransactionRowItem
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
fun TransactionsScreen(
  viewModel: BankViewModel
) {
  val searchQuery by viewModel.txSearchQuery.collectAsState()
  val typeFilter by viewModel.txTypeFilter.collectAsState()
  val crDrFilter by viewModel.txCreditsDebitsFilter.collectAsState()
  val filteredTransactions = viewModel.getFilteredTransactions()

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFFF8FAFC))
  ) {
    // Top Section
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
            text = "Transaction History",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = BankNavyDark
          )
          DemoBadge()
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Search Field
        OutlinedTextField(
          value = searchQuery,
          onValueChange = { viewModel.updateTxSearch(it) },
          placeholder = { Text("Search by merchant, ID, category...") },
          leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = "Search", tint = BankTextSecondary)
          },
          trailingIcon = {
            if (searchQuery.isNotEmpty()) {
              IconButton(onClick = { viewModel.updateTxSearch("") }) {
                Icon(Icons.Default.Clear, contentDescription = "Clear search")
              }
            }
          },
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("tx_search_input"),
          shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Credit/Debit Filter Chips
        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          items(listOf("All", "Credits", "Debits")) { filterOption ->
            FilterChip(
              selected = crDrFilter == filterOption,
              onClick = { viewModel.updateTxCreditsDebitsFilter(filterOption) },
              label = { Text(filterOption, fontSize = 11.sp) },
              modifier = Modifier.testTag("tx_filter_${filterOption.lowercase()}")
            )
          }

          items(
            listOf(
              TransactionType.ALL to "All Types",
              TransactionType.UPI to "UPI",
              TransactionType.BANK_TRANSFER to "Transfers",
              TransactionType.CARD to "Card",
              TransactionType.BILL_PAY to "Bills"
            )
          ) { (tType, label) ->
            FilterChip(
              selected = typeFilter == tType,
              onClick = { viewModel.updateTxTypeFilter(tType) },
              label = { Text(label, fontSize = 11.sp) },
              modifier = Modifier.testTag("tx_type_filter_${label.lowercase().replace(" ", "_")}")
            )
          }
        }
      }
    }

    // Results Count & List
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 10.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "${filteredTransactions.size} transactions found",
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        color = BankTextSecondary,
        modifier = Modifier.testTag("tx_count_text")
      )
    }

    if (filteredTransactions.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(32.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text(
            text = "No matching transactions found",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = BankNavyDark,
            modifier = Modifier.testTag("tx_empty_state_text")
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Try clearing the search query or adjusting your filters.",
            fontSize = 12.sp,
            color = BankTextSecondary
          )
        }
      }
    } else {
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 16.dp)
      ) {
        itemsIndexed(filteredTransactions) { index, tx ->
          TransactionRowItem(
            transaction = tx,
            onClick = { viewModel.navigateTo(Screen.TransactionDetail(tx)) },
            modifier = Modifier.padding(vertical = 4.dp),
            testTag = "tx_list_item_$index"
          )
        }
        item {
          Spacer(modifier = Modifier.height(24.dp))
        }
      }
    }
  }
}

@Composable
fun TransactionDetailScreen(
  transaction: Transaction,
  onBack: () -> Unit
) {
  val context = LocalContext.current

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFFF8FAFC))
      .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .testTag("transaction_detail_card"),
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
      Column(
        modifier = Modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          IconButton(
            onClick = onBack,
            modifier = Modifier.testTag("tx_detail_back_button")
          ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
          }
          DemoBadge()
        }

        Spacer(modifier = Modifier.height(10.dp))

        Box(
          modifier = Modifier
            .size(56.dp)
            .clip(CircleShape)
            .background(if (transaction.isCredit) Color(0xFFDCFCE7) else Color(0xFFEFF6FF)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = if (transaction.isCredit) BankSuccessGreen else BankBlueAccent,
            modifier = Modifier.size(32.dp)
          )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
          text = transaction.title,
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          color = BankNavyDark,
          modifier = Modifier.testTag("tx_detail_title")
        )

        val sign = if (transaction.isCredit) "+" else "-"
        val amountColor = if (transaction.isCredit) BankSuccessGreen else BankTextPrimary
        Text(
          text = "$sign$${String.format(Locale.US, "%,.2f", transaction.amount)}",
          fontSize = 26.sp,
          fontWeight = FontWeight.Black,
          color = amountColor,
          modifier = Modifier.testTag("tx_detail_amount")
        )

        Text(
          text = transaction.timestamp,
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
            DetailRow("Transaction ID", transaction.id, "tx_detail_id")
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFE2E8F0))
            DetailRow("Counterparty", transaction.recipientOrMerchant, "tx_detail_party")
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFE2E8F0))
            DetailRow("Type", transaction.type.displayName, "tx_detail_type")
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFE2E8F0))
            DetailRow("Category", transaction.category, "tx_detail_category")
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFE2E8F0))
            DetailRow("Status", transaction.status, "tx_detail_status")
            if (transaction.note.isNotBlank()) {
              HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFE2E8F0))
              DetailRow("Note", transaction.note, "tx_detail_note")
            }
          }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
          onClick = {
            Toast.makeText(context, "Simulated Receipt downloaded: ${transaction.id}.pdf", Toast.LENGTH_SHORT).show()
          },
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("tx_download_receipt_button"),
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(containerColor = BankNavyPrimary)
        ) {
          Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text("Download Demo Receipt", fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

@Composable
private fun DetailRow(label: String, value: String, testTag: String) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(label, fontSize = 12.sp, color = BankTextSecondary)
    Text(value, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = BankTextPrimary, modifier = Modifier.testTag(testTag))
  }
}
