package com.example.data

import android.util.Log
import com.example.model.Beneficiary
import com.example.model.EliteCreditCard
import com.example.model.NotificationItem
import com.example.model.NotificationType
import com.example.model.SpendingItem
import com.example.model.TestControlState
import com.example.model.Transaction
import com.example.model.TransactionType
import com.example.model.TransferRail
import com.example.model.UserProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

class BankRepository {

  companion object {
    private const val TAG = "TGBankAPI"
    private val txSequence = AtomicInteger(1001)
    private val client = OkHttpClient.Builder()
      .connectTimeout(8, TimeUnit.SECONDS)
      .readTimeout(8, TimeUnit.SECONDS)
      .build()

    fun generateTxId(): String {
      val datePart = SimpleDateFormat("yyyyMMdd", Locale.US).format(Date())
      return "TGX$datePart${String.format(Locale.US, "%04d", txSequence.getAndIncrement())}"
    }

    val INITIAL_USER = UserProfile(
      name = "Sanjay G",
      balance = 24588338510.70,
      accountNumber = "**** **** 4588",
      rawAccountNumber = "458890123456",
      accountType = "Ultra High Net Worth Private Banking",
      currency = "USD",
      creditScore = 850,
      creditRating = "World Elite / Exceptional",
      bankingScore = 998,
      creditLimit = 5000000.0,
      availableCredit = 4825000.0,
      usedCredit = 175000.0,
      nextPayment = 12500.0,
      paymentDue = "Oct 15, 2026",
      kycStatus = "Verified",
      phone = "+1 (555) 019-4588",
      email = "sanjay.g@testgrid.demo",
      address = "458 Tech Park Blvd, Suite 200, San Jose, CA 95110",
      dob = "14 Aug 1988",
      ifsc = "TGBN0004588",
      upiId = "sanjay@tg",
      relationshipManager = "Priya Menon • Private Banking Desk",
      clientSince = "2012",
      tier = "Black Diamond Ultra"
    )

    val INITIAL_BENEFICIARIES = listOf(
      Beneficiary(
        id = "BEN001",
        name = "Aarav Mehta",
        nickname = "Aarav",
        accountNumber = "998877665544",
        ifscOrRouting = "HDFC0001234",
        bankName = "HDFC Bank",
        transferType = TransferRail.IMPS,
        dailyLimit = 5000000.0,
        isVerified = true,
        avatarInitials = "AM",
        isFavorite = true
      ),
      Beneficiary(
        id = "BEN002",
        name = "Priya Sharma",
        nickname = "Priya",
        accountNumber = "112233445566",
        ifscOrRouting = "ICIC0005678",
        bankName = "ICICI Bank",
        transferType = TransferRail.NEFT,
        dailyLimit = 10000000.0,
        isVerified = true,
        avatarInitials = "PS",
        isFavorite = true
      ),
      Beneficiary(
        id = "BEN003",
        name = "Global Holdings LLC",
        nickname = "Global HQ",
        accountNumber = "445566778899",
        ifscOrRouting = "021000021",
        bankName = "JPMorgan Chase",
        transferType = TransferRail.ACH,
        dailyLimit = 10000000.0,
        isVerified = true,
        avatarInitials = "GH",
        isFavorite = false
      ),
      Beneficiary(
        id = "BEN004",
        name = "Singapore Trust Co",
        nickname = "SG Trust",
        accountNumber = "778899001122",
        ifscOrRouting = "DBSSSGSGXXX",
        bankName = "DBS Bank",
        transferType = TransferRail.RTGS,
        dailyLimit = 50000000.0,
        isVerified = true,
        avatarInitials = "ST",
        isFavorite = false
      )
    )

    val INITIAL_ELITE_CARDS = listOf(
      EliteCreditCard(
        id = "CARD_BLACK",
        name = "TG Black Diamond Infinite",
        tier = "World Elite",
        cardNumber = "**** **** **** 8899",
        expiry = "12/30",
        cvv = "***",
        cardHolder = "SANJAY G",
        creditLimit = 2500000.0,
        availableCredit = 2412500.0,
        usedCredit = 87500.0,
        primaryGradientStart = 0xFF0F172A,
        primaryGradientEnd = 0xFF1E293B,
        perk = "Unlimited lounge + 5% cashback on travel",
        billingDue = "Oct 15, 2026",
        minDue = 8750.0
      ),
      EliteCreditCard(
        id = "CARD_INVITE",
        name = "TG Invitation-Only Metal",
        tier = "Private Invitation",
        cardNumber = "**** **** **** 4588",
        expiry = "09/29",
        cvv = "***",
        cardHolder = "SANJAY G",
        creditLimit = 2500000.0,
        availableCredit = 2412500.0,
        usedCredit = 87500.0,
        primaryGradientStart = 0xFF7C3AED,
        primaryGradientEnd = 0xFF4C1D95,
        perk = "Concierge 24/7 + private jet credits",
        billingDue = "Oct 20, 2026",
        minDue = 5000.0
      )
    )

    val INITIAL_TRANSACTIONS = listOf(
      Transaction(
        id = "TGX202609160001",
        title = "Amazon",
        recipientOrMerchant = "Amazon.com Payments",
        amount = 249.99,
        isCredit = false,
        type = TransactionType.CARD,
        timestamp = "Sep 16, 2026, 14:22",
        status = "Completed",
        category = "Shopping",
        note = "Electronics order #402-99128"
      ),
      Transaction(
        id = "TGX202609160002",
        title = "Salary Credit",
        recipientOrMerchant = "TestGrid Technologies Inc",
        amount = 15000.00,
        isCredit = true,
        type = TransactionType.SALARY,
        timestamp = "Sep 15, 2026, 09:00",
        status = "Completed",
        category = "Income",
        note = "Monthly Payroll Deposit"
      ),
      Transaction(
        id = "TGX202609150003",
        title = "Starbucks",
        recipientOrMerchant = "Starbucks Coffee #301",
        amount = 18.45,
        isCredit = false,
        type = TransactionType.CARD,
        timestamp = "Sep 15, 2026, 08:45",
        status = "Completed",
        category = "Food",
        note = "Breakfast & Espresso"
      ),
      Transaction(
        id = "TGX202609140004",
        title = "UPI Payment",
        recipientOrMerchant = "TG Demo Store",
        amount = 125.00,
        isCredit = false,
        type = TransactionType.UPI,
        timestamp = "Sep 14, 2026, 17:30",
        status = "Completed",
        category = "Shopping",
        note = "QR Payment at Store"
      ),
      Transaction(
        id = "TGX202609130005",
        title = "IMPS Transfer",
        recipientOrMerchant = "Aarav Mehta",
        amount = 250000.00,
        isCredit = false,
        type = TransactionType.IMPS,
        timestamp = "Sep 13, 2026, 11:05",
        status = "Completed",
        category = "Transfers",
        note = "Family transfer",
        rail = "IMPS"
      ),
      Transaction(
        id = "TGX202609120006",
        title = "Electricity Bill",
        recipientOrMerchant = "Pacific Gas & Electric",
        amount = 186.40,
        isCredit = false,
        type = TransactionType.BILL_PAY,
        timestamp = "Sep 12, 2026, 16:10",
        status = "Completed",
        category = "Bills",
        note = "Account 445-9921"
      ),
      Transaction(
        id = "TGX202609100007",
        title = "Dividend Credit",
        recipientOrMerchant = "Vanguard Index Fund",
        amount = 87500.00,
        isCredit = true,
        type = TransactionType.SALARY,
        timestamp = "Sep 10, 2026, 08:00",
        status = "Completed",
        category = "Income",
        note = "Quarterly dividend"
      )
    )

    val INITIAL_NOTIFICATIONS = listOf(
      NotificationItem(
        id = "N1",
        title = "Transfer Successful",
        message = "IMPS of $250,000.00 to Aarav Mehta completed.",
        timestamp = "Sep 13, 11:06",
        isRead = false,
        type = NotificationType.TRANSACTION
      ),
      NotificationItem(
        id = "N2",
        title = "Security Alert",
        message = "New device login detected from San Jose, CA.",
        timestamp = "Sep 16, 09:12",
        isRead = false,
        type = NotificationType.SECURITY
      ),
      NotificationItem(
        id = "N3",
        title = "Credit Card Due",
        message = "Black Diamond card minimum due $8,750.00 by Oct 15.",
        timestamp = "Sep 15, 18:00",
        isRead = true,
        type = NotificationType.REMINDER
      ),
      NotificationItem(
        id = "N4",
        title = "KYC Verified",
        message = "Your demo KYC profile is fully verified and active for global transactions.",
        timestamp = "Sep 01, 10:00",
        isRead = true,
        type = NotificationType.SYSTEM
      ),
      NotificationItem(
        id = "N5",
        title = "Exclusive Offer",
        message = "Invitation-Only Metal card annual fee waived for UHNW clients this quarter.",
        timestamp = "Sep 08, 12:30",
        isRead = false,
        type = NotificationType.OFFER
      )
    )
  }

  private val _userProfile = MutableStateFlow(INITIAL_USER)
  val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

  private val _transactions = MutableStateFlow(INITIAL_TRANSACTIONS)
  val transactions: StateFlow<List<Transaction>> = _transactions.asStateFlow()

  private val _notifications = MutableStateFlow(INITIAL_NOTIFICATIONS)
  val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

  private val _testControls = MutableStateFlow(TestControlState())
  val testControls: StateFlow<TestControlState> = _testControls.asStateFlow()

  private val _beneficiaries = MutableStateFlow(INITIAL_BENEFICIARIES)
  val beneficiaries: StateFlow<List<Beneficiary>> = _beneficiaries.asStateFlow()

  private val _eliteCards = MutableStateFlow(INITIAL_ELITE_CARDS)
  val eliteCards: StateFlow<List<EliteCreditCard>> = _eliteCards.asStateFlow()

  private val _lastApiResponse = MutableStateFlow<String?>(null)
  val lastApiResponse: StateFlow<String?> = _lastApiResponse.asStateFlow()

  fun resetDemoData() {
    _userProfile.value = INITIAL_USER.copy(
      kycStatus = "Incomplete",
      usedCredit = 0.0,
      availableCredit = 5000000.0,
      nextPayment = 0.0,
      balance = 24588338510.70
    )
    _transactions.value = INITIAL_TRANSACTIONS
    _notifications.value = INITIAL_NOTIFICATIONS
    _testControls.value = TestControlState(forceKycIncomplete = true, requirePaymentAuth = true)
    _beneficiaries.value = INITIAL_BENEFICIARIES
    _eliteCards.value = INITIAL_ELITE_CARDS
    _lastApiResponse.value = null
  }

  fun resetKycState() {
    _userProfile.update { it.copy(kycStatus = "Incomplete") }
    _testControls.update { it.copy(forceKycIncomplete = true) }
    addNotification("KYC Reset", "KYC status reset to Incomplete. Verification required.")
  }

  fun resetCreditState() {
    _userProfile.update {
      it.copy(
        usedCredit = 0.0,
        availableCredit = it.creditLimit,
        nextPayment = 0.0
      )
    }
    _eliteCards.update { INITIAL_ELITE_CARDS }
    addNotification("Credit Reset", "Credit utilization and dues reset to zero.")
  }

  /** Pushes/seeds the in-memory backend database so subsequent validations see consistent data. */
  fun pushDatabaseSeed(): String {
    resetDemoData()
    val count = _transactions.value.size + _beneficiaries.value.size + _eliteCards.value.size
    addNotification(
      "Database Seeded",
      "Backend store refreshed with $count core records (UHNW profile, beneficiaries, elite cards)."
    )
    return "Seeded: profile + ${_transactions.value.size} txs + ${_beneficiaries.value.size} beneficiaries + ${_eliteCards.value.size} cards"
  }

  fun updateTestControls(newControls: TestControlState) {
    _testControls.value = newControls
    if (newControls.forceKycIncomplete) {
      _userProfile.update { it.copy(kycStatus = "Incomplete") }
    } else {
      _userProfile.update { it.copy(kycStatus = "Verified") }
    }
  }

  fun addBeneficiary(beneficiary: Beneficiary) {
    _beneficiaries.update { listOf(beneficiary) + it }
    addNotification("Beneficiary Added", "${beneficiary.name} (${beneficiary.bankName}) saved successfully.")
  }

  /**
   * Real HTTP call so network proxies / Logcat show actual traffic.
   * Uses httpbin.org/post (public echo service) to simulate bank rail API.
   */
  suspend fun callPaymentApi(
    endpointLabel: String,
    payload: Map<String, Any?>
  ): Result<String> = withContext(Dispatchers.IO) {
    try {
      if (_testControls.value.simulateNetworkError) {
        return@withContext Result.failure(Exception("Simulated network timeout. Please check your connection."))
      }
      val json = JSONObject()
      payload.forEach { (k, v) -> json.put(k, v ?: JSONObject.NULL) }
      val body = json.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
      val targetUrl = if (_testControls.value.useLocalProxy) {
        // route to local machine (emulator host) so Charles/proxy can capture traffic
        "http://${_testControls.value.localProxyHost}:${_testControls.value.localProxyPort}/post"
      } else {
        "https://httpbin.org/post"
      }
      val requestBuilder = Request.Builder()
        .url(targetUrl)
        .addHeader("X-TG-Bank-Endpoint", endpointLabel)
        .addHeader("X-TG-Client", "TG-Bank-Android-Demo")
        .addHeader("Content-Type", "application/json")

      if (_testControls.value.simulateUnsecuredWifi) {
        requestBuilder.addHeader("X-Simulated-Unsecured-Wifi", "true")
      }
      if (_testControls.value.developerOptionsEnabled) {
        requestBuilder.addHeader("X-Developer-Options", "true")
      }

      val request = requestBuilder.post(body).build()

      Log.i(TAG, "API REQUEST [$endpointLabel] -> ${request.url}")
      Log.i(TAG, "API PAYLOAD: $json")

      // Add diagnostic headers when test controls request simulated network/device states
      if (_testControls.value.simulateUnsecuredWifi) {
        // Some proxies may require explicit header to mark traffic
        Log.w(TAG, "Simulating unsecured WiFi for request: $endpointLabel")
      }
      if (_testControls.value.developerOptionsEnabled) {
        Log.w(TAG, "Developer options flag enabled for request: $endpointLabel")
      }

      // Build a client that routes through the emulator host proxy when requested so tools like Charles can capture traffic.
      val clientToUse = if (_testControls.value.useLocalProxy) {
        try {
          val proxy = java.net.Proxy(java.net.Proxy.Type.HTTP, java.net.InetSocketAddress(_testControls.value.localProxyHost, _testControls.value.localProxyPort))

          // Trust-all SSL context for local proxy capture ONLY (testing). Do not enable in production.
          val trustAllCerts = arrayOf<javax.net.ssl.TrustManager>(object : javax.net.ssl.X509TrustManager {
            override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {}
            override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {}
            override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> = arrayOf()
          })
          val sslContext = javax.net.ssl.SSLContext.getInstance("TLS")
          sslContext.init(null, trustAllCerts, java.security.SecureRandom())
          val sslSocketFactory = sslContext.socketFactory

          OkHttpClient.Builder()
            .connectTimeout(12, TimeUnit.SECONDS)
            .readTimeout(12, TimeUnit.SECONDS)
            .proxy(proxy)
            .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as javax.net.ssl.X509TrustManager)
            .hostnameVerifier { _, _ -> true }
            .build()
        } catch (e: Exception) {
          Log.w(TAG, "Failed to create proxied client: ${e.message}")
          client
        }
      } else client

      clientToUse.newCall(request).execute().use { response ->
        val responseBody = response.body?.string().orEmpty()
        Log.i(TAG, "API RESPONSE [${response.code}] $responseBody")
        _lastApiResponse.value = "HTTP ${response.code} | $endpointLabel | ${responseBody.take(200)}"
        if (!response.isSuccessful) {
          return@withContext Result.failure(Exception("Upstream bank API returned ${response.code}"))
        }
        Result.success(responseBody)
      }
    } catch (e: Exception) {
      Log.e(TAG, "API ERROR [$endpointLabel]", e)
      _lastApiResponse.value = "ERROR: ${e.message}"
      Result.failure(e)
    }
  }

  suspend fun sendMoney(
    recipientName: String,
    accountNumber: String,
    ifsc: String,
    amount: Double,
    note: String,
    rail: TransferRail = TransferRail.IMPS,
    beneficiaryId: String? = null
  ): Result<Transaction> {
    if (_testControls.value.forceFailedTransaction) {
      return Result.failure(Exception("Transaction failed: Simulated bank network rejection."))
    }
    if (_testControls.value.forceInsufficientBalance || _userProfile.value.balance < amount) {
      return Result.failure(Exception("Insufficient demo balance."))
    }

    val apiResult = callPaymentApi(
      endpointLabel = "rail/${rail.name.lowercase()}/transfer",
      payload = mapOf(
        "rail" to rail.name,
        "amount" to amount,
        "currency" to "USD",
        "beneficiary_name" to recipientName,
        "account" to accountNumber,
        "ifsc_or_routing" to ifsc,
        "note" to note,
        "client_ref" to generateTxId(),
        "beneficiary_id" to beneficiaryId
      )
    )
    if (apiResult.isFailure) {
      return Result.failure(apiResult.exceptionOrNull() ?: Exception("Payment API failed"))
    }

    val type = when (rail) {
      TransferRail.IMPS -> TransactionType.IMPS
      TransferRail.NEFT -> TransactionType.NEFT
      TransferRail.ACH -> TransactionType.ACH
      TransferRail.RTGS -> TransactionType.RTGS
    }

    val tx = Transaction(
      id = generateTxId(),
      title = "${rail.displayName} Transfer",
      recipientOrMerchant = recipientName,
      amount = amount,
      isCredit = false,
      type = type,
      timestamp = SimpleDateFormat("MMM dd, yyyy, HH:mm", Locale.US).format(Date()),
      status = "Completed",
      category = "Transfers",
      note = if (note.isNotBlank()) note else "Transfer to $accountNumber ($ifsc) via ${rail.displayName}",
      rail = rail.displayName,
      referenceId = "REF${System.currentTimeMillis()}"
    )

    _userProfile.update { it.copy(balance = it.balance - amount) }
    _transactions.update { listOf(tx) + it }
    addNotification(
      "Transfer Successful",
      "Sent $${String.format(Locale.US, "%,.2f", amount)} to $recipientName via ${rail.displayName}."
    )
    return Result.success(tx)
  }

  suspend fun payUpi(
    upiId: String,
    amount: Double,
    note: String,
    merchantName: String? = null
  ): Result<Transaction> {
    if (_testControls.value.forceFailedTransaction) {
      return Result.failure(Exception("UPI payment rejected by simulated NPCI switch."))
    }
    if (_testControls.value.forceInsufficientBalance || _userProfile.value.balance < amount) {
      return Result.failure(Exception("Insufficient demo balance."))
    }

    val apiResult = callPaymentApi(
      endpointLabel = "upi/pay",
      payload = mapOf(
        "upi_id" to upiId,
        "amount" to amount,
        "note" to note,
        "merchant" to (merchantName ?: upiId)
      )
    )
    if (apiResult.isFailure) {
      return Result.failure(apiResult.exceptionOrNull() ?: Exception("UPI API failed"))
    }

    val tx = Transaction(
      id = generateTxId(),
      title = "UPI Payment",
      recipientOrMerchant = merchantName ?: upiId,
      amount = amount,
      isCredit = false,
      type = TransactionType.UPI,
      timestamp = SimpleDateFormat("MMM dd, yyyy, HH:mm", Locale.US).format(Date()),
      status = "Completed",
      category = "Shopping",
      note = note.ifBlank { "UPI to $upiId" },
      referenceId = "UPI${System.currentTimeMillis()}"
    )
    _userProfile.update { it.copy(balance = it.balance - amount) }
    _transactions.update { listOf(tx) + it }
    addNotification("UPI Payment Successful", "Paid $${String.format(Locale.US, "%,.2f", amount)} to ${merchantName ?: upiId}.")
    return Result.success(tx)
  }

  fun addMoney(amount: Double, method: String): Result<Transaction> {
    if (_testControls.value.simulateNetworkError) {
      return Result.failure(Exception("Simulated network timeout."))
    }
    val tx = Transaction(
      id = generateTxId(),
      title = "Money Added",
      recipientOrMerchant = method,
      amount = amount,
      isCredit = true,
      type = TransactionType.ADD_MONEY,
      timestamp = SimpleDateFormat("MMM dd, yyyy, HH:mm", Locale.US).format(Date()),
      status = "Completed",
      category = "Funding",
      note = "Added via $method"
    )
    _userProfile.update { it.copy(balance = it.balance + amount) }
    _transactions.update { listOf(tx) + it }
    addNotification("Money Added", "$${String.format(Locale.US, "%,.2f", amount)} credited via $method.")
    return Result.success(tx)
  }

  fun withdrawMoney(amount: Double, method: String): Result<Transaction> {
    if (_testControls.value.forceInsufficientBalance || _userProfile.value.balance < amount) {
      return Result.failure(Exception("Insufficient demo balance."))
    }
    if (_testControls.value.forceFailedTransaction) {
      return Result.failure(Exception("Withdrawal declined by simulated ATM network."))
    }
    val tx = Transaction(
      id = generateTxId(),
      title = "ATM Withdrawal",
      recipientOrMerchant = method,
      amount = amount,
      isCredit = false,
      type = TransactionType.ATM,
      timestamp = SimpleDateFormat("MMM dd, yyyy, HH:mm", Locale.US).format(Date()),
      status = "Completed",
      category = "Cash",
      note = "Withdrawn via $method"
    )
    _userProfile.update { it.copy(balance = it.balance - amount) }
    _transactions.update { listOf(tx) + it }
    addNotification("Withdrawal Successful", "$${String.format(Locale.US, "%,.2f", amount)} withdrawn.")
    return Result.success(tx)
  }

  suspend fun payCreditCardBill(amount: Double, cardId: String? = null): Result<Transaction> {
    if (_testControls.value.forceInsufficientBalance || _userProfile.value.balance < amount) {
      return Result.failure(Exception("Insufficient demo balance to pay credit bill."))
    }
    if (_testControls.value.forceFailedTransaction) {
      return Result.failure(Exception("Credit payment declined."))
    }

    val apiResult = callPaymentApi(
      endpointLabel = "credit/pay-bill",
      payload = mapOf(
        "amount" to amount,
        "card_id" to (cardId ?: "CARD_BLACK"),
        "account" to _userProfile.value.rawAccountNumber
      )
    )
    if (apiResult.isFailure) {
      return Result.failure(apiResult.exceptionOrNull() ?: Exception("Credit API failed"))
    }

    val tx = Transaction(
      id = generateTxId(),
      title = "Credit Card Payment",
      recipientOrMerchant = "TG Credit Services",
      amount = amount,
      isCredit = false,
      type = TransactionType.CREDIT_PAY,
      timestamp = SimpleDateFormat("MMM dd, yyyy, HH:mm", Locale.US).format(Date()),
      status = "Completed",
      category = "Credit",
      note = "Bill payment"
    )
    _userProfile.update {
      val newUsed = (it.usedCredit - amount).coerceAtLeast(0.0)
      val newAvailable = (it.creditLimit - newUsed).coerceAtLeast(0.0)
      val newNextPayment = (it.nextPayment - amount).coerceAtLeast(0.0)
      it.copy(
        balance = it.balance - amount,
        usedCredit = newUsed,
        availableCredit = newAvailable,
        nextPayment = newNextPayment
      )
    }
    _transactions.update { listOf(tx) + it }
    addNotification("Credit Payment Successful", "Payment of $${String.format(Locale.US, "%,.2f", amount)} credited to your account.")
    return Result.success(tx)
  }

  fun requestCreditIncrease(): Result<Double> {
    val newLimit = _userProfile.value.creditLimit + 250000.0
    _userProfile.update {
      it.copy(
        creditLimit = newLimit,
        availableCredit = newLimit - it.usedCredit,
        creditScore = (it.creditScore + 2).coerceAtMost(850),
        bankingScore = (it.bankingScore + 1).coerceAtMost(999)
      )
    }
    addNotification("Credit Limit Increased", "Your demo credit limit has been increased.")
    return Result.success(newLimit)
  }

  fun submitCreditApplication(
    requestedAmount: Double,
    employmentType: String,
    monthlyIncome: Double,
    purpose: String,
    duration: String
  ): Result<String> {
    if (_testControls.value.creditAppPending) {
      return Result.success("Application Submitted: Pending simulated underwriting review.")
    }
    val approvalMessage = "Congratulations! Pre-approved for $${String.format(Locale.US, "%,.2f", requestedAmount)} at 6.8% APR (UHNW rate)."
    addNotification("Credit Application Approved", approvalMessage)
    return Result.success(approvalMessage)
  }

  fun completeKyc(): Result<String> {
    _userProfile.update { it.copy(kycStatus = "Verified") }
    addNotification("KYC Completed", "Your simulated KYC identity verification has been re-verified successfully.")
    return Result.success("Identity Verified")
  }

  fun addNotification(title: String, message: String, type: NotificationType = NotificationType.TRANSACTION) {
    val item = NotificationItem(
      id = "NOTIF_${System.currentTimeMillis()}",
      title = title,
      message = message,
      timestamp = "Just now",
      isRead = false,
      type = type
    )
    _notifications.update { listOf(item) + it }
  }

  fun markAllNotificationsRead() {
    _notifications.update { list -> list.map { it.copy(isRead = true) } }
  }

  fun clearNotifications() {
    _notifications.value = emptyList()
  }

  fun getSpendingSummary(): List<SpendingItem> {
    return listOf(
      SpendingItem("Shopping", 856.99, 0.35f),
      SpendingItem("Food", 245.50, 0.18f),
      SpendingItem("Bills", 360.70, 0.22f),
      SpendingItem("Transfers", 212.50, 0.15f),
      SpendingItem("Entertainment", 99.99, 0.10f)
    )
  }

  fun validateSeededData(): Boolean {
    return _userProfile.value.balance > 20_000_000_000 &&
      _beneficiaries.value.size >= 4 &&
      _eliteCards.value.size >= 2 &&
      _transactions.value.isNotEmpty()
  }
}
