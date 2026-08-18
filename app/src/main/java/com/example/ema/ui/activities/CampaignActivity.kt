package com.example.ema.ui.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ema.api.Repository.CampaignRepository
import com.example.ema.api.Repository.ContactRepository
import com.example.ema.components.GenerateOutlinedTextField
import com.example.ema.model.CampaignDto
import com.example.ema.model.ContactDto
import com.example.ema.ui.theme.EmaTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CampaignActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EmaTheme {
                CompositionLocalProvider(
                    LocalContext provides this
                ) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        CampaignPage()
                    }
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CampaignPage() {
    var campaignTitle by remember { mutableStateOf(TextFieldValue()) }
    var campaignDescription by remember { mutableStateOf(TextFieldValue()) }
    var contacts by remember { mutableStateOf(emptyList<ContactDto>()) }
    val selectedContacts = remember { mutableStateListOf<ContactDto>() }
    val validationErrors = remember { mutableStateOf(emptyMap<String, String>()) }

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        val repository = ContactRepository(context = context)
        val contactList = withContext(Dispatchers.IO) {
            repository.getContactList()
        }
        contacts = contactList?.result ?: emptyList()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        GenerateOutlinedTextField(
            label = "Name",
            value = campaignTitle,
            errorMessage = validationErrors.value["name"] ?: "",
            onValueChange = { newValue ->
                campaignTitle = newValue
                validationErrors.value =
                    validationErrors.value + ("name" to validateCampaignTitle(newValue))
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(5.dp))
        GenerateOutlinedTextField(
            label = "Description",
            value = campaignDescription,
            onValueChange = { campaignDescription = it },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(5.dp))
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(contacts) { contact ->
                ContactItem(contact = contact, selectedContacts = selectedContacts)
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
        Button(modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            onClick = {
                createCampaign(
                    validationErrors.value,
                    campaignTitle.text, campaignDescription.text,
                    selectedContacts,
                    context
                )
            }) {
            Text(
                text = "Create",
                fontSize = MaterialTheme.typography.headlineMedium.fontSize
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewCampaignPage() {
    EmaTheme {
        CampaignPage()
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ContactItem(contact: ContactDto, selectedContacts: MutableList<ContactDto>) {
    val isChecked = remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = { isChecked.value = !isChecked.value }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isChecked.value,
                onCheckedChange = { isChecked.value = it },
                modifier = Modifier.padding(end = 16.dp)
            )
            Text(text = contact.email)
        }
    }
    Spacer(modifier = Modifier.height(4.dp))

    // Update selected items when the checkbox state changes
    LaunchedEffect(isChecked.value) {
        if (isChecked.value) {
            selectedContacts.add(contact)
        } else {
            selectedContacts.remove(contact)
        }
    }
}

private fun createCampaign(
    validationErrors: Map<String, String>,
    campaignTitle: String, campaignDescription: String,
    selectedContacts: MutableList<ContactDto>,
    context: Context
) {
    val result = validationErrors.filter { (key, value) -> value.isNotBlank() }
    if (result.isNotEmpty() || validationErrors.isEmpty()) {
        Toast.makeText(context, "Enter campaign title at least!", Toast.LENGTH_SHORT).show()
        return
    }

    GlobalScope.launch {
        val campaignRepository = CampaignRepository(context)
        val campaignDto = CampaignDto(
            title = campaignTitle, description = campaignDescription,
            contacts = selectedContacts
        )
        val response = campaignRepository.createCampaign(campaignDto)
        if (response != null) {
            val intent = Intent(context, DashboardActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }
}

private fun validateCampaignTitle(value: TextFieldValue): String {
    return if (value.text.isEmpty()) "Enter campaign name" else ""
}