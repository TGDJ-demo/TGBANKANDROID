package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.model.TransactionType
import com.example.viewmodel.BankViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("TG Bank", appName)
  }

  @Test
  fun `verify initial bank viewmodel state`() {
    val viewModel = BankViewModel()
    val profile = viewModel.userProfile.value
    assertEquals("Sanjay G", profile.name)
    assertEquals(24588338510.70, profile.balance, 0.01)
    assertEquals("Verified", profile.kycStatus)
  }

  @Test
  fun `verify successful money transfer simulation`() {
    val viewModel = BankViewModel()
    val initialBalance = viewModel.userProfile.value.balance

    viewModel.startSendMoney()
    viewModel.updateSendRecipient("John Doe")
    viewModel.updateSendAccount("123456789012")
    viewModel.updateSendAmount("500.00")
    val reviewed = viewModel.continueSendReview()
    assertTrue(reviewed)

    viewModel.confirmSendTransfer()
    val tx = viewModel.lastCreatedTx.value
    assertNotNull(tx)
    assertEquals(500.0, tx?.amount ?: 0.0, 0.01)
    assertEquals(initialBalance - 500.0, viewModel.userProfile.value.balance, 0.01)
  }

  @Test
  fun `verify deterministic failure toggle via test controls`() {
    val viewModel = BankViewModel()
    viewModel.updateTestControls(viewModel.testControls.value.copy(forceInsufficientBalance = true))

    viewModel.startSendMoney()
    viewModel.updateSendRecipient("John Doe")
    viewModel.updateSendAccount("123456789012")
    viewModel.updateSendAmount("10.00")
    val reviewed = viewModel.continueSendReview()

    assertTrue(!reviewed)
    assertTrue(viewModel.sendError.value?.contains("Insufficient") == true)
  }

  @Test
  fun `verify UPI payment flow`() {
    val viewModel = BankViewModel()
    val initialTxCount = viewModel.transactions.value.size

    viewModel.updateUpiId("merchant@tg")
    viewModel.updateUpiAmount("75.50")
    viewModel.updateUpiNote("Coffee purchase")
    viewModel.confirmUpiPayment()

    val upiTx = viewModel.upiSuccessTx.value
    assertNotNull(upiTx)
    assertEquals(initialTxCount + 1, viewModel.transactions.value.size)
    assertEquals(TransactionType.UPI, viewModel.transactions.value.first().type)
  }
}
