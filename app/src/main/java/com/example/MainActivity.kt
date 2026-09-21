package com.example

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import com.example.ui.components.BankBottomNavigationBar
import com.example.ui.components.BankTopAppBar
import com.example.ui.screens.AddMoneyScreen
import com.example.ui.screens.CreditApplicationScreen
import com.example.ui.screens.CreditScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.KycScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.NotificationsScreen
import com.example.ui.screens.PayBillsScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.ReceiveMoneyScreen
import com.example.ui.screens.SendMoneyScreen
import com.example.ui.screens.TestControlsScreen
import com.example.ui.screens.TransactionDetailScreen
import com.example.ui.screens.TransactionsScreen
import com.example.ui.screens.UpiScreen
import com.example.ui.screens.WithdrawScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.BankViewModel
import com.example.viewmodel.BottomTab
import com.example.viewmodel.Screen

class MainActivity : FragmentActivity() {
  private val viewModel: BankViewModel by viewModels()

  @OptIn(ExperimentalComposeUiApi::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        Box(
          modifier = Modifier
            .fillMaxSize()
            .semantics { testTagsAsResourceId = true }
        ) {
          MainAppContent(viewModel = viewModel)
        }
      }
    }
  }
}

@Composable
fun MainAppContent(viewModel: BankViewModel) {
  val currentScreen by viewModel.currentScreen.collectAsState()
  val selectedTab by viewModel.selectedTab.collectAsState()
  val notifications by viewModel.notifications.collectAsState()
  val userMessage by viewModel.userMessage.collectAsState()
  val snackbarHostState = remember { SnackbarHostState() }

  val unreadNotifications = notifications.count { !it.isRead }

  LaunchedEffect(userMessage) {
    userMessage?.let { msg ->
      snackbarHostState.showSnackbar(msg)
      viewModel.dismissUserMessage()
    }
  }

  // Handle Android Back Navigation
  BackHandler(enabled = currentScreen != Screen.Login && currentScreen != Screen.Main) {
    viewModel.navigateBack()
  }

  when (currentScreen) {
    Screen.Login -> {
      LoginScreen(viewModel = viewModel)
    }

    Screen.Main -> {
      Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
          BankTopAppBar(
            title = when (selectedTab) {
              BottomTab.HOME -> "TG Bank"
              BottomTab.PAYMENTS -> "UPI & Pay"
              BottomTab.TRANSACTIONS -> "History"
              BottomTab.CREDIT -> "Credit & Loan"
              BottomTab.PROFILE -> "Profile"
            },
            subtitle = if (selectedTab == BottomTab.HOME) "Checking Account • 4588" else null,
            showBack = false,
            unreadNotificationsCount = unreadNotifications,
            onNotificationClick = { viewModel.navigateTo(Screen.NotificationCenter) },
            onTestControlsClick = { viewModel.navigateTo(Screen.TestControls) }
          )
        },
        bottomBar = {
          BankBottomNavigationBar(
            selectedTab = selectedTab,
            onTabSelected = { viewModel.selectTab(it) }
          )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
      ) { innerPadding ->
        Box(
          modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        ) {
          when (selectedTab) {
            BottomTab.HOME -> HomeScreen(viewModel = viewModel)
            BottomTab.PAYMENTS -> UpiScreen(viewModel = viewModel)
            BottomTab.TRANSACTIONS -> TransactionsScreen(viewModel = viewModel)
            BottomTab.CREDIT -> CreditScreen(viewModel = viewModel)
            BottomTab.PROFILE -> ProfileScreen(viewModel = viewModel)
          }
        }
      }
    }

    Screen.SendMoney -> {
      SubScreenScaffold(
        title = "Send Money",
        unreadNotifications = unreadNotifications,
        snackbarHostState = snackbarHostState,
        viewModel = viewModel
      ) {
        SendMoneyScreen(viewModel = viewModel)
      }
    }

    Screen.UpiPay -> {
      SubScreenScaffold(
        title = "UPI Payment",
        unreadNotifications = unreadNotifications,
        snackbarHostState = snackbarHostState,
        viewModel = viewModel
      ) {
        UpiScreen(viewModel = viewModel)
      }
    }

    Screen.AddMoney -> {
      SubScreenScaffold(
        title = "Add Money",
        unreadNotifications = unreadNotifications,
        snackbarHostState = snackbarHostState,
        viewModel = viewModel
      ) {
        AddMoneyScreen(viewModel = viewModel)
      }
    }

    Screen.Withdraw -> {
      SubScreenScaffold(
        title = "Withdraw Money",
        unreadNotifications = unreadNotifications,
        snackbarHostState = snackbarHostState,
        viewModel = viewModel
      ) {
        WithdrawScreen(viewModel = viewModel)
      }
    }

    Screen.ReceiveMoney -> {
      SubScreenScaffold(
        title = "Receive Money",
        unreadNotifications = unreadNotifications,
        snackbarHostState = snackbarHostState,
        viewModel = viewModel
      ) {
        ReceiveMoneyScreen(viewModel = viewModel)
      }
    }

    Screen.PayBills -> {
      SubScreenScaffold(
        title = "Pay Bills",
        unreadNotifications = unreadNotifications,
        snackbarHostState = snackbarHostState,
        viewModel = viewModel
      ) {
        PayBillsScreen(viewModel = viewModel)
      }
    }

    Screen.CreditApplication -> {
      SubScreenScaffold(
        title = "Loan Application",
        unreadNotifications = unreadNotifications,
        snackbarHostState = snackbarHostState,
        viewModel = viewModel
      ) {
        CreditApplicationScreen(viewModel = viewModel)
      }
    }

    Screen.KycFlow -> {
      SubScreenScaffold(
        title = "KYC Verification",
        unreadNotifications = unreadNotifications,
        snackbarHostState = snackbarHostState,
        viewModel = viewModel
      ) {
        KycScreen(viewModel = viewModel)
      }
    }

    is Screen.TransactionDetail -> {
      val tx = (currentScreen as Screen.TransactionDetail).transaction
      Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
      ) { innerPadding ->
        Box(
          modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        ) {
          TransactionDetailScreen(
            transaction = tx,
            onBack = { viewModel.navigateBack() }
          )
        }
      }
    }

    Screen.NotificationCenter -> {
      SubScreenScaffold(
        title = "Notifications",
        unreadNotifications = unreadNotifications,
        snackbarHostState = snackbarHostState,
        viewModel = viewModel
      ) {
        NotificationsScreen(viewModel = viewModel)
      }
    }

    Screen.TestControls -> {
      SubScreenScaffold(
        title = "Test Controls",
        unreadNotifications = unreadNotifications,
        snackbarHostState = snackbarHostState,
        viewModel = viewModel
      ) {
        TestControlsScreen(viewModel = viewModel)
      }
    }
  }
}

@Composable
private fun SubScreenScaffold(
  title: String,
  unreadNotifications: Int,
  snackbarHostState: SnackbarHostState,
  viewModel: BankViewModel,
  content: @Composable () -> Unit
) {
  Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
      BankTopAppBar(
        title = title,
        showBack = true,
        unreadNotificationsCount = unreadNotifications,
        onBackClick = { viewModel.navigateBack() },
        onNotificationClick = { viewModel.navigateTo(Screen.NotificationCenter) },
        onTestControlsClick = { viewModel.navigateTo(Screen.TestControls) }
      )
    },
    snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      content()
    }
  }
}
