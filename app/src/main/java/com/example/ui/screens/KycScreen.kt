package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.Manifest
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.camera.InAppFaceScanner
import com.example.ui.components.DemoBadge
import com.example.ui.theme.BankBlueAccent
import com.example.ui.theme.BankNavyDark
import com.example.ui.theme.BankNavyPrimary
import com.example.ui.theme.BankSuccessGreen
import com.example.ui.theme.BankTextPrimary
import com.example.ui.theme.BankTextSecondary
import com.example.viewmodel.BankViewModel

@Composable
fun KycScreen(
  viewModel: BankViewModel
) {
  val step by viewModel.kycStep.collectAsState()
  val docUploaded by viewModel.kycDocUploaded.collectAsState()
  val selfieUploaded by viewModel.kycSelfieUploaded.collectAsState()
  val profile by viewModel.userProfile.collectAsState()

  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()
  var selfieBitmap by remember { mutableStateOf<Bitmap?>(null) }
  var showFaceScanner by remember { mutableStateOf(false) }

  if (showFaceScanner) {
    InAppFaceScanner(
      onFaceCaptured = { bitmap ->
        selfieBitmap = bitmap
        coroutineScope.launch { viewModel.uploadDemoSelfie() }
        showFaceScanner = false
      },
      onClose = { showFaceScanner = false }
    )
    return
  }

  var nameInput by remember { mutableStateOf(profile.name) }
  var dobInput by remember { mutableStateOf("1988-08-24") }
  var addressInput by remember { mutableStateOf("742 Evergreen Terrace, Springfield, OR") }
  var docType by remember { mutableStateOf("Passport") }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(0xFFF8FAFC))
      .verticalScroll(rememberScrollState())
      .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    // Header
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text("KYC Verification", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = BankNavyDark)
        Text("Simulated identity verification wizard", fontSize = 11.sp, color = BankTextSecondary)
      }
      DemoBadge()
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Wizard Progress Bar (Steps 1 to 6)
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 4.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      (1..6).forEach { i ->
        Box(
          modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(
              when {
                step == i -> BankNavyPrimary
                step > i -> BankSuccessGreen
                else -> Color(0xFFE2E8F0)
              }
            ),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = if (step > i) "✓" else i.toString(),
            color = if (step >= i) Color.White else BankTextSecondary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
          )
        }
        if (i < 6) {
          Box(
            modifier = Modifier
              .weight(1f)
              .height(2.dp)
              .background(if (step > i) BankSuccessGreen else Color(0xFFE2E8F0))
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Step Content Card
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .testTag("kyc_wizard_card"),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
      Column(modifier = Modifier.padding(20.dp)) {
        when (step) {
          1 -> {
            // STEP 1: Personal Information
            Text("Step 1: Personal Information", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = BankNavyDark)
            Text("Verify your official demo profile data", fontSize = 12.sp, color = BankTextSecondary)

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
              value = nameInput,
              onValueChange = { nameInput = it },
              label = { Text("Full Legal Name") },
              singleLine = true,
              modifier = Modifier.fillMaxWidth().testTag("kyc_name_input"),
              shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
              value = dobInput,
              onValueChange = { dobInput = it },
              label = { Text("Date of Birth (YYYY-MM-DD)") },
              singleLine = true,
              modifier = Modifier.fillMaxWidth().testTag("kyc_dob_input"),
              shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
              value = addressInput,
              onValueChange = { addressInput = it },
              label = { Text("Residential Address") },
              singleLine = false,
              maxLines = 2,
              modifier = Modifier.fillMaxWidth().testTag("kyc_address_input"),
              shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
              onClick = { viewModel.advanceKycStep() },
              modifier = Modifier.fillMaxWidth().height(48.dp).testTag("kyc_step1_next_button"),
              shape = RoundedCornerShape(12.dp),
              colors = ButtonDefaults.buttonColors(containerColor = BankNavyPrimary)
            ) {
              Text("Next: ID Upload", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
          }

          2 -> {
            // STEP 2: ID Upload
            Text("Step 2: Document Verification", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = BankNavyDark)
            Text("Select document type and attach simulated file", fontSize = 12.sp, color = BankTextSecondary)

            Spacer(modifier = Modifier.height(16.dp))

            Text("Document Type", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              listOf("Passport", "National ID", "Driver License").forEach { type ->
                FilterChip(
                  selected = docType == type,
                  onClick = { docType = type },
                  label = { Text(type, fontSize = 11.sp) },
                  modifier = Modifier.testTag("doc_type_chip_${type.replace(" ", "_")}")
                )
              }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Simulated upload dropzone
            Surface(
              modifier = Modifier
                .fillMaxWidth()
                .border(1.5.dp, if (docUploaded) BankSuccessGreen else Color(0xFFCBD5E1), RoundedCornerShape(12.dp)),
              color = if (docUploaded) Color(0xFFDCFCE7) else Color(0xFFF8FAFC),
              shape = RoundedCornerShape(12.dp)
            ) {
              Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
              ) {
                Icon(
                  imageVector = if (docUploaded) Icons.Default.CheckCircle else Icons.Default.UploadFile,
                  contentDescription = null,
                  tint = if (docUploaded) BankSuccessGreen else BankBlueAccent,
                  modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                  text = if (docUploaded) "$docType Attached: demo_${docType.lowercase()}.pdf" else "Upload $docType",
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp,
                  color = if (docUploaded) Color(0xFF166534) else BankNavyDark
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = if (docUploaded) "Simulated file ready for verification" else "Click below to simulate instant file upload",
                  fontSize = 11.sp,
                  color = BankTextSecondary
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                  onClick = { viewModel.uploadDemoId() },
                  shape = RoundedCornerShape(8.dp),
                  modifier = Modifier.testTag("kyc_upload_doc_button")
                ) {
                  Text(if (docUploaded) "Replace Document" else "Upload Document", fontSize = 12.sp)
                }
              }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
              onClick = {
                if (!docUploaded) viewModel.uploadDemoId()
                viewModel.advanceKycStep()
              },
              modifier = Modifier.fillMaxWidth().height(48.dp).testTag("kyc_step2_next_button"),
              shape = RoundedCornerShape(12.dp),
              colors = ButtonDefaults.buttonColors(containerColor = BankNavyPrimary)
            ) {
              Text("Next: Address Verification", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
          }

          3 -> {
            // STEP 3: Address Verification
            Text("Step 3: Address Verification", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = BankNavyDark)
            Text("Confirm address against utility database (Simulated)", fontSize = 12.sp, color = BankTextSecondary)

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
              color = Color(0xFFEFF6FF),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Home, contentDescription = null, tint = BankBlueAccent, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                  Text("Address Match Verified", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = BankNavyDark)
                  Text("Matched with public records for $addressInput", fontSize = 11.sp, color = BankTextSecondary)
                }
              }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
              onClick = { viewModel.advanceKycStep() },
              modifier = Modifier.fillMaxWidth().height(48.dp).testTag("kyc_step3_next_button"),
              shape = RoundedCornerShape(12.dp),
              colors = ButtonDefaults.buttonColors(containerColor = BankNavyPrimary)
            ) {
              Text("Next: Face Verification", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
          }

          4 -> {
            // STEP 4: Selfie / Face Verification
            Text("Step 4: Selfie / Face Verification", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = BankNavyDark)
            Text("Simulated biometric liveliness check", fontSize = 12.sp, color = BankTextSecondary)

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
              modifier = Modifier
                .fillMaxWidth()
                .border(1.5.dp, if (selfieUploaded) BankSuccessGreen else Color(0xFFCBD5E1), RoundedCornerShape(12.dp)),
              color = if (selfieUploaded) Color(0xFFDCFCE7) else Color(0xFFF8FAFC),
              shape = RoundedCornerShape(12.dp)
            ) {
              Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
              ) {
                if (selfieBitmap != null) {
                  Image(
                    bitmap = selfieBitmap!!.asImageBitmap(),
                    contentDescription = "selfie",
                    modifier = Modifier
                      .size(80.dp)
                      .clip(CircleShape),
                    contentScale = ContentScale.Crop
                  )
                } else {
                  Icon(
                    imageVector = if (selfieUploaded) Icons.Default.CheckCircle else Icons.Default.CameraAlt,
                    contentDescription = null,
                    tint = if (selfieUploaded) BankSuccessGreen else BankBlueAccent,
                    modifier = Modifier.size(40.dp)
                  )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                  text = if (selfieUploaded) "Selfie Captured (Demo Match 99.4%)" else "Simulate Face Capture",
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp,
                  color = if (selfieUploaded) Color(0xFF166534) else BankNavyDark
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                  onClick = {
                    showFaceScanner = true
                  },
                  shape = RoundedCornerShape(10.dp),
                  colors = ButtonDefaults.buttonColors(containerColor = BankNavyPrimary),
                  modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .testTag("kyc_capture_selfie_button")
                ) {
                  Icon(Icons.Default.CameraAlt, contentDescription = null, tint = Color.White)
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(
                    text = if (selfieUploaded) "Re-scan Face with Real Camera" else "Scan Face with Real Phone Camera",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                  )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                  onClick = {
                    viewModel.uploadDemoSelfie()
                  },
                  shape = RoundedCornerShape(10.dp),
                  modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp)
                    .testTag("kyc_simulate_selfie_button")
                ) {
                  Text("Use Instant Demo Selfie", fontSize = 12.sp, color = BankNavyDark, fontWeight = FontWeight.SemiBold)
                }
              }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
              onClick = {
                if (!selfieUploaded) viewModel.uploadDemoSelfie()
                viewModel.advanceKycStep()
              },
              modifier = Modifier.fillMaxWidth().height(48.dp).testTag("kyc_step4_next_button"),
              shape = RoundedCornerShape(12.dp),
              colors = ButtonDefaults.buttonColors(containerColor = BankNavyPrimary)
            ) {
              Text("Next: Review", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
          }

          5 -> {
            // STEP 5: Review
            Text("Step 5: Review & Submit", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = BankNavyDark)
            Text("Confirm all submitted verification details", fontSize = 12.sp, color = BankTextSecondary)

            Spacer(modifier = Modifier.height(16.dp))

            Surface(color = Color(0xFFF8FAFC), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
              Column(modifier = Modifier.padding(14.dp)) {
                KycReviewRow("Applicant Name", nameInput)
                HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp), color = Color(0xFFE2E8F0))
                KycReviewRow("Date of Birth", dobInput)
                HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp), color = Color(0xFFE2E8F0))
                KycReviewRow("ID Document", "$docType (Uploaded)")
                HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp), color = Color(0xFFE2E8F0))
                KycReviewRow("Address Check", "Verified")
                HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp), color = Color(0xFFE2E8F0))
                KycReviewRow("Biometric Check", "Passed (Simulated Liveliness)")
              }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
              onClick = { viewModel.advanceKycStep() },
              modifier = Modifier.fillMaxWidth().height(48.dp).testTag("kyc_submit_button"),
              shape = RoundedCornerShape(12.dp),
              colors = ButtonDefaults.buttonColors(containerColor = BankNavyPrimary)
            ) {
              Text("Submit KYC Verification", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
          }

          6 -> {
            // STEP 6: KYC Completed Successfully
            Column(
              modifier = Modifier.fillMaxWidth(),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Box(
                modifier = Modifier.size(64.dp).clip(CircleShape).background(Color(0xFFDCFCE7)),
                contentAlignment = Alignment.Center
              ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = BankSuccessGreen, modifier = Modifier.size(36.dp))
              }

              Spacer(modifier = Modifier.height(14.dp))

              Text(
                text = "KYC Completed Successfully",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = BankNavyDark,
                modifier = Modifier.testTag("kyc_completed_title")
              )

              Text(
                text = "Your identity has been fully verified for TG Bank Demo.",
                fontSize = 12.sp,
                color = BankTextSecondary
              )

              Spacer(modifier = Modifier.height(20.dp))

              Surface(
                color = Color(0xFFF8FAFC),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
              ) {
                Column(modifier = Modifier.padding(14.dp)) {
                  KycReviewRow("KYC Status", "Verified (Full)")
                  HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp), color = Color(0xFFE2E8F0))
                  KycReviewRow("Reference No.", "KYC-2026-TG9982")
                  HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp), color = Color(0xFFE2E8F0))
                  KycReviewRow("Verified On", "Sep 18, 2026")
                }
              }

              Spacer(modifier = Modifier.height(24.dp))

              Button(
                onClick = { viewModel.navigateBack() },
                modifier = Modifier.fillMaxWidth().height(48.dp).testTag("kyc_done_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BankNavyPrimary)
              ) {
                Text("Return to Dashboard", fontSize = 14.sp, fontWeight = FontWeight.Bold)
              }
            }
          }
        }
      }
    }
  }
}

@Composable
private fun KycReviewRow(label: String, value: String) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(label, fontSize = 12.sp, color = BankTextSecondary)
    Text(value, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = BankTextPrimary)
  }
}
