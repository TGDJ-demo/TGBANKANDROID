package com.example.model

enum class TransferRail(val displayName: String, val description: String, val speed: String, val limit: String) {
  IMPS("IMPS", "Immediate Payment Service (24x7 Real-time)", "Instant (< 5 sec)", "Up to $5,000,000"),
  NEFT("NEFT", "National Electronic Fund Transfer (Batch settlement)", "30 mins - 2 hrs", "No upper limit"),
  ACH("ACH", "Automated Clearing House (Domestic clearing)", "Next business day", "Up to $10,000,000"),
  RTGS("RTGS", "Real Time Gross Settlement (High value institutional)", "Instant high-speed", "Min $2,000,000")
}

data class Beneficiary(
  val id: String,
  val name: String,
  val nickname: String,
  val accountNumber: String,
  val ifscOrRouting: String,
  val bankName: String,
  val transferType: TransferRail = TransferRail.IMPS,
  val dailyLimit: Double = 5000000.0,
  val isVerified: Boolean = true,
  val avatarInitials: String = "BN",
  val isFavorite: Boolean = false
)

enum class PaymentAuthMethod(val displayName: String) {
  MPIN("6-Digit MPIN"),
  BIOMETRIC("Fingerprint / Face ID"),
  OTP("One-Time Password (SMS/Email)")
}

data class EliteCreditCard(
  val id: String,
  val name: String,
  val tier: String,
  val cardNumber: String,
  val expiry: String,
  val cvv: String,
  val cardHolder: String,
  val creditLimit: Double,
  val availableCredit: Double,
  val usedCredit: Double,
  val primaryGradientStart: Long,
  val primaryGradientEnd: Long,
  val perk: String,
  val billingDue: String,
  val minDue: Double
)
