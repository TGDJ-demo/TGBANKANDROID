package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.BankRepository
import com.example.model.NotificationItem
import com.example.model.SpendingItem
import com.example.model.TestControlState
import com.example.model.Transaction
import com.example.model.TransactionType
import com.example.model.UserProfile
import com.example.model.Beneficiary
import com.example.model.TransferRail
import com.example.model.PaymentAuthMethod
import com.example.model.EliteCreditCard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class Screen {
  data object Login : Screen()
  data object Main : Screen()
  data object SendMoney : Screen()
  data object UpiPay : Screen()
  data object AddMoney : Screen()
  data object Withdraw : Screen()
  data object ReceiveMoney : Screen()
  data object PayBills : Screen()
  data object CreditApplication : Screen()
  data object KycFlow : Screen()
  data class TransactionDetail(val transaction: Transaction) : Screen()
  data object NotificationCenter : Screen()
  data object TestControls : Screen()
}

enum class BottomTab {
  HOME,
  PAYMENTS,
  TRANSACTIONS,
  CREDIT,
  PROFILE
}

class BankViewModel(
  private val repository: BankRepository = BankRepository()
) : ViewModel() {

  val userProfile: StateFlow<UserProfile> = repository.userProfile
  val transactions: StateFlow<List<Transaction>> = repository.transactions
  val notifications: StateFlow<List<NotificationItem>> = repository.notifications
  val testControls: StateFlow<TestControlState> = repository.testControls
  val beneficiaries: StateFlow<List<Beneficiary>> = repository.beneficiaries
  val eliteCards: StateFlow<List<EliteCreditCard>> = repository.eliteCards
  val lastApiResponse: StateFlow<String?> = repository.lastApiResponse

  private val _screenStack = MutableStateFlow<List<Screen>>(listOf(Screen.Login))
  val currentScreen: StateFlow<Screen> = MutableStateFlow<Screen>(Screen.Login).apply {
    viewModelScope.launch {
      _screenStack.collect { stack ->
        value = stack.lastOrNull() ?: Screen.Login
      }
    }
  }

  private val _selectedTab = MutableStateFlow(BottomTab.HOME)
  val selectedTab: StateFlow<BottomTab> = _selectedTab.asStateFlow()

  private val _isBalanceVisible = MutableStateFlow(true)
  val isBalanceVisible: StateFlow<Boolean> = _isBalanceVisible.asStateFlow()

  // Login State
  private val _loginUsername = MutableStateFlow("Sanjay G")
  val loginUsername: StateFlow<String> = _loginUsername.asStateFlow()

  private val _loginPin = MutableStateFlow("1234")
  val loginPin: StateFlow<String> = _loginPin.asStateFlow()

  private val _loginError = MutableStateFlow<String?>(null)
  val loginError: StateFlow<String?> = _loginError.asStateFlow()

  // Transfer State
  private val _sendStep = MutableStateFlow(1) // 1: Input, 2: Review, 3: Success
  val sendStep: StateFlow<Int> = _sendStep.asStateFlow()

  private val _sendRecipient = MutableStateFlow("")
  val sendRecipient: StateFlow<String> = _sendRecipient.asStateFlow()

  private val _sendAccount = MutableStateFlow("")
  val sendAccount: StateFlow<String> = _sendAccount.asStateFlow()

  private val _sendIfsc = MutableStateFlow("TGBN0009821")
  val sendIfsc: StateFlow<String> = _sendIfsc.asStateFlow()

  private val _sendAmount = MutableStateFlow("")
  val sendAmount: StateFlow<String> = _sendAmount.asStateFlow()

  private val _sendNote = MutableStateFlow("")
  val sendNote: StateFlow<String> = _sendNote.asStateFlow()

  private val _sendRail = MutableStateFlow(TransferRail.IMPS)
  val sendRail: StateFlow<TransferRail> = _sendRail.asStateFlow()

  private val _selectedBeneficiaryId = MutableStateFlow<String?>(null)
  val selectedBeneficiaryId: StateFlow<String?> = _selectedBeneficiaryId.asStateFlow()

  private val _paymentAuthMethod = MutableStateFlow(PaymentAuthMethod.MPIN)
  val paymentAuthMethod: StateFlow<PaymentAuthMethod> = _paymentAuthMethod.asStateFlow()

  private val _showOtpDialog = MutableStateFlow(false)
  val showOtpDialog: StateFlow<Boolean> = _showOtpDialog.asStateFlow()

  private val _showMpinDialog = MutableStateFlow(false)
  val showMpinDialog: StateFlow<Boolean> = _showMpinDialog.asStateFlow()

  private val _authPassed = MutableStateFlow(false)
  val authPassed: StateFlow<Boolean> = _authPassed.asStateFlow()

  private val _isProcessingPayment = MutableStateFlow(false)
  val isProcessingPayment: StateFlow<Boolean> = _isProcessingPayment.asStateFlow()

  private val _sendError = MutableStateFlow<String?>(null)
  val sendError: StateFlow<String?> = _sendError.asStateFlow()

  private val _lastCreatedTx = MutableStateFlow<Transaction?>(null)
  val lastCreatedTx: StateFlow<Transaction?> = _lastCreatedTx.asStateFlow()

  // UPI State
  private val _upiTab = MutableStateFlow(0) // 0: Pay UPI ID, 1: Scan & Pay, 2: Send Contact, 3: Request
  val upiTab: StateFlow<Int> = _upiTab.asStateFlow()

  private val _upiIdInput = MutableStateFlow("")
  val upiIdInput: StateFlow<String> = _upiIdInput.asStateFlow()

  private val _upiAmountInput = MutableStateFlow("")
  val upiAmountInput: StateFlow<String> = _upiAmountInput.asStateFlow()

  private val _upiNoteInput = MutableStateFlow("")
  val upiNoteInput: StateFlow<String> = _upiNoteInput.asStateFlow()

  private val _upiError = MutableStateFlow<String?>(null)
  val upiError: StateFlow<String?> = _upiError.asStateFlow()

  private val _upiSuccessTx = MutableStateFlow<Transaction?>(null)
  val upiSuccessTx: StateFlow<Transaction?> = _upiSuccessTx.asStateFlow()

  // KYC Flow State (Steps 1 to 6)
  private val _kycStep = MutableStateFlow(1)
  val kycStep: StateFlow<Int> = _kycStep.asStateFlow()

  private val _kycDocUploaded = MutableStateFlow(false)
  val kycDocUploaded: StateFlow<Boolean> = _kycDocUploaded.asStateFlow()

  private val _kycSelfieUploaded = MutableStateFlow(false)
  val kycSelfieUploaded: StateFlow<Boolean> = _kycSelfieUploaded.asStateFlow()

  // Transaction Filters
  private val _txSearchQuery = MutableStateFlow("")
  val txSearchQuery: StateFlow<String> = _txSearchQuery.asStateFlow()

  private val _txTypeFilter = MutableStateFlow(TransactionType.ALL)
  val txTypeFilter: StateFlow<TransactionType> = _txTypeFilter.asStateFlow()

  private val _txCreditsDebitsFilter = MutableStateFlow<String>("All") // "All", "Credits", "Debits"
  val txCreditsDebitsFilter: StateFlow<String> = _txCreditsDebitsFilter.asStateFlow()

  // Status banners / Snackbars
  private val _userMessage = MutableStateFlow<String?>(null)
  val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

  fun navigateTo(screen: Screen) {
    _screenStack.update { it + screen }
  }

  fun navigateBack(): Boolean {
    if (_screenStack.value.size > 1) {
      _screenStack.update { it.dropLast(1) }
      return true
    }
    return false
  }

  fun selectTab(tab: BottomTab) {
    _selectedTab.value = tab
    _screenStack.value = listOf(Screen.Main)
  }

  fun toggleBalanceVisibility() {
    _isBalanceVisible.value = !_isBalanceVisible.value
  }

  fun updateLoginUsername(value: String) {
    _loginUsername.value = value
    _loginError.value = null
  }

  fun updateLoginPin(value: String) {
    _loginPin.value = value
    _loginError.value = null
  }

  fun useDemoCredentials() {
    _loginUsername.value = "Sanjay G"
    _loginPin.value = "1234"
    _loginError.value = null
  }

  fun performLogin(): Boolean {
    if (_loginUsername.value.trim() != "Sanjay G" || _loginPin.value.trim() != "1234") {
      _loginError.value = "Invalid credentials. Use demo Username: Sanjay G and PIN: 1234"
      return false
    }
    _loginError.value = null
    _screenStack.value = listOf(Screen.Main)
    _selectedTab.value = BottomTab.HOME
    return true
  }

  fun performLogout() {
    _screenStack.value = listOf(Screen.Login)
  }

  // SEND MONEY FLOW
  fun startSendMoney() {
    _sendStep.value = 1
    _sendRecipient.value = ""
    _sendAccount.value = ""
    _sendIfsc.value = "TGBN0009821"
    _sendAmount.value = ""
    _sendNote.value = ""
    _sendError.value = null
    _lastCreatedTx.value = null
    navigateTo(Screen.SendMoney)
  }

  fun updateSendRecipient(value: String) {
    _sendRecipient.value = value
    _sendError.value = null
  }

  fun updateSendAccount(value: String) {
    _sendAccount.value = value
    _sendError.value = null
  }

  fun updateSendIfsc(value: String) {
    _sendIfsc.value = value
    _sendError.value = null
  }

  fun updateSendAmount(value: String) {
    _sendAmount.value = value
    _sendError.value = null
  }

  fun updateSendNote(value: String) {
    _sendNote.value = value
  }

  fun setQuickSendAmount(amount: Double) {
    _sendAmount.value = amount.toString()
  }

  fun continueSendReview(): Boolean {
    if (_sendRecipient.value.trim().isEmpty()) {
      _sendError.value = "Please enter the recipient name."
      return false
    }
    if (_sendAccount.value.trim().isEmpty()) {
      _sendError.value = "Please enter the account number."
      return false
    }
    val amt = _sendAmount.value.toDoubleOrNull()
    if (amt == null || amt <= 0.0) {
      _sendError.value = "Please enter a valid amount."
      return false
    }
    if (amt > userProfile.value.balance || testControls.value.forceInsufficientBalance) {
      _sendError.value = "Insufficient demo balance."
      return false
    }
    _sendError.value = null
    _sendStep.value = 2
    return true
  }

  fun backToSendInput() {
    _sendStep.value = 1
  }

  fun confirmSendTransfer() {
    viewModelScope.launch {
      _isProcessingPayment.value = true
      val amount = _sendAmount.value.toDoubleOrNull() ?: 0.0
      val result = repository.sendMoney(
        recipientName = _sendRecipient.value,
        accountNumber = _sendAccount.value,
        ifsc = _sendIfsc.value,
        amount = amount,
        note = _sendNote.value,
        rail = _sendRail.value,
        beneficiaryId = _selectedBeneficiaryId.value
      )
      result.onSuccess { tx ->
        _lastCreatedTx.value = tx
        _sendStep.value = 3
        _authPassed.value = false
      }.onFailure { e ->
        _sendError.value = e.message
        _sendStep.value = 1
      }
      _isProcessingPayment.value = false
    }
  }

  // UPI FLOW
  fun startUpiFlow() {
    _upiTab.value = 0
    _upiIdInput.value = ""
    _upiAmountInput.value = ""
    _upiNoteInput.value = ""
    _upiError.value = null
    _upiSuccessTx.value = null
    navigateTo(Screen.UpiPay)
  }

  fun setUpiTab(tab: Int) {
    _upiTab.value = tab
    _upiError.value = null
  }

  fun updateUpiId(value: String) {
    _upiIdInput.value = value
    _upiError.value = null
  }

  fun updateUpiAmount(value: String) {
    _upiAmountInput.value = value
    _upiError.value = null
  }

  fun updateUpiNote(value: String) {
    _upiNoteInput.value = value
  }

  fun useDemoUpiId(id: String) {
    _upiIdInput.value = id
  }

  fun useDemoQrCode() {
    _upiIdInput.value = "merchant@tg"
    _upiAmountInput.value = "125.00"
    _upiNoteInput.value = "TG Demo Store purchase"
  }

  fun confirmUpiPayment(merchantOverride: String? = null) {
    val upiId = _upiIdInput.value.trim()
    if (upiId.isEmpty()) {
      _upiError.value = "Please enter a valid UPI ID."
      return
    }
    val amt = _upiAmountInput.value.toDoubleOrNull()
    if (amt == null || amt <= 0.0) {
      _upiError.value = "Please enter a valid amount."
      return
    }
    if (amt > userProfile.value.balance || testControls.value.forceInsufficientBalance) {
      _upiError.value = "Insufficient demo balance."
      return
    }

    viewModelScope.launch {
      val result = repository.payUpi(
        upiId = upiId,
        amount = amt,
        note = _upiNoteInput.value.trim(),
        merchantName = merchantOverride ?: if (upiId == "merchant@tg") "TG Demo Store" else null
      )
      result.onSuccess { tx ->
        _upiSuccessTx.value = tx
        _upiError.value = null
      }.onFailure { err ->
        _upiError.value = err.message ?: "UPI transaction failed"
      }
    }
  }

  fun resetUpiSuccess() {
    _upiSuccessTx.value = null
    _upiAmountInput.value = ""
    _upiNoteInput.value = ""
  }

  // ADD MONEY
  fun executeAddMoney(amount: Double, method: String): Result<Transaction> {
    val res = repository.addMoney(amount, method)
    res.onSuccess { tx ->
      _lastCreatedTx.value = tx
      _userMessage.value = "Money added successfully: $${String.format(java.util.Locale.US, "%,.2f", amount)}"
    }
    return res
  }

  // WITHDRAW
  fun executeWithdraw(amount: Double, method: String): Result<Transaction> {
    val res = repository.withdrawMoney(amount, method)
    res.onSuccess { tx ->
      _lastCreatedTx.value = tx
      _userMessage.value = "Withdrawal successful: $${String.format(java.util.Locale.US, "%,.2f", amount)}"
    }
    return res
  }

  // BILL PAY
  fun executeBillPayment(biller: String, amount: Double, accountNo: String): Result<Transaction> {
    if (userProfile.value.balance < amount || testControls.value.forceInsufficientBalance) {
      return Result.failure(Exception("Insufficient demo balance."))
    }
    val tx = Transaction(
      id = BankRepository.generateTxId(),
      title = biller,
      recipientOrMerchant = biller,
      amount = amount,
      isCredit = false,
      type = TransactionType.BILL_PAY,
      timestamp = java.text.SimpleDateFormat("MMM dd, yyyy, HH:mm", java.util.Locale.US).format(java.util.Date()),
      status = "Completed",
      category = "Bills",
      note = "Bill account #$accountNo"
    )
    viewModelScope.launch {
      repository.sendMoney(biller, accountNo, "TGBN00100", amount, "Bill Payment")
    }
    _lastCreatedTx.value = tx
    _userMessage.value = "Bill paid successfully: $${String.format(java.util.Locale.US, "%,.2f", amount)}"
    return Result.success(tx)
  }

  // CREDIT ACTIONS
  suspend fun payCreditBill(amount: Double): Result<Transaction> {
    return repository.payCreditCardBill(amount)
  }

  fun requestCreditIncrease(): Result<Double> {
    return repository.requestCreditIncrease()
  }

  fun applyForCredit(
    amount: Double,
    employmentType: String,
    monthlyIncome: Double,
    purpose: String,
    duration: String
  ): Result<String> {
    return repository.submitCreditApplication(amount, employmentType, monthlyIncome, purpose, duration)
  }

  // KYC FLOW
  fun startKycFlow() {
    _kycStep.value = 1
    _kycDocUploaded.value = false
    _kycSelfieUploaded.value = false
    navigateTo(Screen.KycFlow)
  }

  fun advanceKycStep() {
    if (_kycStep.value < 6) {
      _kycStep.value = _kycStep.value + 1
      if (_kycStep.value == 6) {
        repository.completeKyc()
      }
    }
  }

  fun uploadDemoId() {
    _kycDocUploaded.value = true
  }

  fun uploadDemoSelfie() {
    _kycSelfieUploaded.value = true
  }

  fun resetKycFlow() {
    _kycStep.value = 1
    _kycDocUploaded.value = false
    _kycSelfieUploaded.value = false
  }

  // NOTIFICATIONS
  fun markAllNotificationsRead() {
    repository.markAllNotificationsRead()
  }

  fun clearNotifications() {
    repository.clearNotifications()
  }

  // TEST CONTROLS
  fun updateTestControls(newControls: TestControlState) {
    repository.updateTestControls(newControls)
  }

  fun resetDemoData() {
    repository.resetDemoData()
    // Reset UI / flow state held in ViewModel as well
    _sendStep.value = 1
    _sendRecipient.value = ""
    _sendAccount.value = ""
    _sendIfsc.value = "TGBN0009821"
    _sendAmount.value = ""
    _sendNote.value = ""
    _selectedBeneficiaryId.value = null

    _authPassed.value = false
    _showMpinDialog.value = false
    _showOtpDialog.value = false
    _isProcessingPayment.value = false
    _lastCreatedTx.value = null

    _upiTab.value = 0
    _upiIdInput.value = ""
    _upiAmountInput.value = ""
    _upiNoteInput.value = ""
    _upiError.value = null
    _upiSuccessTx.value = null

    _kycStep.value = 1
    _kycDocUploaded.value = false
    _kycSelfieUploaded.value = false

    _txSearchQuery.value = ""
    _txTypeFilter.value = TransactionType.ALL
    _txCreditsDebitsFilter.value = "All"

    _userMessage.value = "Full demo reset: Balance, KYC, Credit & Debit activities restored to factory state."
  }

  fun resetKycOnly() {
    repository.resetKycState()
    _kycStep.value = 1
    _kycDocUploaded.value = false
    _kycSelfieUploaded.value = false
    _userMessage.value = "KYC status reset to Incomplete. Flow ready for verification."
  }

  fun resetCreditOnly() {
    repository.resetCreditState()
    _userMessage.value = "Credit card limits, loans, and utilization reset."
  }

  fun dismissUserMessage() {
    _userMessage.value = null
  }

  // TRANSACTIONS FILTERING
  fun updateTxSearch(query: String) {
    _txSearchQuery.value = query
  }

  fun updateTxTypeFilter(type: TransactionType) {
    _txTypeFilter.value = type
  }

  fun updateTxCreditsDebitsFilter(filter: String) {
    _txCreditsDebitsFilter.value = filter
  }

  fun getFilteredTransactions(): List<Transaction> {
    val query = _txSearchQuery.value.trim().lowercase()
    val type = _txTypeFilter.value
    val crDr = _txCreditsDebitsFilter.value

    return transactions.value.filter { tx ->
      val matchesQuery = query.isEmpty() ||
        tx.title.lowercase().contains(query) ||
        tx.recipientOrMerchant.lowercase().contains(query) ||
        tx.id.lowercase().contains(query) ||
        tx.category.lowercase().contains(query) ||
        tx.amount.toString().contains(query)

      val matchesType = when (type) {
        TransactionType.ALL -> true
        else -> tx.type == type
      }

      val matchesCrDr = when (crDr) {
        "Credits" -> tx.isCredit
        "Debits" -> !tx.isCredit
        else -> true
      }

      matchesQuery && matchesType && matchesCrDr
    }
  }

  fun getSpendingSummary(): List<SpendingItem> {
    return repository.getSpendingSummary()
  }

  fun updateSendRail(rail: TransferRail) { _sendRail.value = rail }
  fun selectBeneficiary(id: String?) {
    _selectedBeneficiaryId.value = id
    if (id != null) {
      val b = repository.beneficiaries.value.find { it.id == id }
      if (b != null) {
        _sendRecipient.value = b.name
        _sendAccount.value = b.accountNumber
        _sendIfsc.value = b.ifscOrRouting
        _sendRail.value = b.transferType
      }
    }
  }
  fun updatePaymentAuthMethod(m: PaymentAuthMethod) { _paymentAuthMethod.value = m }
  fun dismissOtpDialog() { _showOtpDialog.value = false }
  fun dismissMpinDialog() { _showMpinDialog.value = false }
  fun onAuthVerified() {
    _authPassed.value = true
    _showOtpDialog.value = false
    _showMpinDialog.value = false
  }
  fun requestPaymentAuth() {
    _authPassed.value = false
    when (_paymentAuthMethod.value) {
      PaymentAuthMethod.OTP -> _showOtpDialog.value = true
      PaymentAuthMethod.MPIN -> _showMpinDialog.value = true
      PaymentAuthMethod.BIOMETRIC -> {
        // Mock biometric path controlled by TestControls
        if (repository.testControls.value.mockBiometricSuccess) {
          onAuthVerified()
          _userMessage.value = "Biometric verified (mock)"
        } else {
          _userMessage.value = "Biometric failed (mock). Enable mock success in Test Controls."
        }
      }
    }
  }
  fun pushDatabaseSeed() {
    val msg = repository.pushDatabaseSeed()
    _userMessage.value = msg
  }
  fun validateSeededData(): Boolean = repository.validateSeededData()

}
