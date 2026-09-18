package com.example.model

enum class TransactionType(val displayName: String) {
  ALL("All"),
  UPI("UPI Payment"),
  BANK_TRANSFER("Bank Transfer"),
  CARD("Card Payment"),
  ATM("ATM Withdrawal"),
  SALARY("Salary Credit"),
  ADD_MONEY("Money Added"),
  BILL_PAY("Bill Payment"),
  CREDIT_PAY("Credit Payment"),
  IMPS("IMPS Transfer"),
  NEFT("NEFT Transfer"),
  ACH("ACH Transfer"),
  RTGS("RTGS Transfer")
}

data class Transaction(
  val id: String,
  val title: String,
  val recipientOrMerchant: String,
  val amount: Double,
  val isCredit: Boolean,
  val type: TransactionType,
  val timestamp: String,
  val status: String = "Completed",
  val category: String,
  val note: String = "",
  val rail: String = "",
  val referenceId: String = ""
)

data class UserProfile(
  val name: String = "Sanjay G",
  val balance: Double = 24588338510.70,
  val accountNumber: String = "**** **** 4588",
  val rawAccountNumber: String = "458890123456",
  val accountType: String = "Ultra High Net Worth Private Banking",
  val currency: String = "USD",
  val creditScore: Int = 850,
  val creditRating: String = "World Elite / Exceptional",
  val bankingScore: Int = 998,
  val creditLimit: Double = 5000000.0,
  val availableCredit: Double = 4825000.0,
  val usedCredit: Double = 175000.0,
  val nextPayment: Double = 12500.0,
  val paymentDue: String = "Oct 15, 2026",
  val kycStatus: String = "Verified",
  val phone: String = "+1 (555) 019-4588",
  val email: String = "sanjay.g@testgrid.demo",
  val address: String = "458 Tech Park Blvd, Suite 200, San Jose, CA 95110",
  val dob: String = "14 Aug 1988",
  val ifsc: String = "TGBN0004588",
  val upiId: String = "sanjay@tg",
  val relationshipManager: String = "Priya Menon • Private Banking Desk",
  val clientSince: String = "2012",
  val tier: String = "Black Diamond Ultra"
)

data class SpendingItem(
  val category: String,
  val amount: Double,
  val percentage: Float
)

enum class NotificationType {
  TRANSACTION,
  SECURITY,
  REMINDER,
  OFFER,
  SYSTEM
}

data class NotificationItem(
  val id: String,
  val title: String,
  val message: String,
  val timestamp: String,
  val isRead: Boolean = false,
  val type: NotificationType = NotificationType.TRANSACTION
)

data class TestControlState(
  val forceInsufficientBalance: Boolean = false,
  val forceFailedTransaction: Boolean = false,
  val simulateNetworkError: Boolean = false,
  val forceKycIncomplete: Boolean = false,
  val creditAppPending: Boolean = false,
  val mockBiometricSuccess: Boolean = true,
  val requirePaymentAuth: Boolean = true,
  val forceOtpAlways: Boolean = false,
  val seedDatabaseOnLaunch: Boolean = false
  ,
  // When true, route demo payment API calls to a local proxy (useful for Charles/mitm)
  val useLocalProxy: Boolean = false,
  val localProxyHost: String = "10.0.2.2",
  val localProxyPort: Int = 8888
  ,
  // Simulate device/network state for testing
  val simulateUnsecuredWifi: Boolean = false,
  val developerOptionsEnabled: Boolean = false
)
