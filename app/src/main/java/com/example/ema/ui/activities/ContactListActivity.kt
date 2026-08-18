package com.example.ema.ui.activities

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountBox
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ema.api.Repository.CampaignRepository
import com.example.ema.api.Repository.ContactRepository
import com.example.ema.components.GenerateOutlinedTextField
import com.example.ema.components.OpenDialog
import com.example.ema.model.CampaignDto
import com.example.ema.model.ContactDto
import com.example.ema.ui.theme.EmaTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContactListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EmaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    ContactListPage()
                }
            }
        }
    }
}

@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
fun ContactListPage() {
    var showContactDialog by remember { mutableStateOf(false) }
    var confirmContact by remember { mutableStateOf(false) }
    var inputText by remember { mutableStateOf(TextFieldValue()) }
    var contacts by remember { mutableStateOf(emptyList<ContactDto>()) }
    var campaigns by remember { mutableStateOf(emptyList<CampaignDto>()) }

    var selectedCampaign by remember {
        mutableStateOf(
            CampaignDto(
                0, "", "",
                null, mutableListOf()
            )
        )
    }
    var selectedContact by remember {
        mutableStateOf(
            ContactDto(
                0, "", ""
            )
        )
    }
    var showCampaignDialog by remember { mutableStateOf(false) }
    fun onCampaignButtonClick(contact: ContactDto) {
        showCampaignDialog = true
        selectedContact = contact
    }

    fun addContactToCampaign(
        context: Context,
        selectedContact: ContactDto,
        selectedCampaign: CampaignDto
    ) {
        GlobalScope.launch {
            val repository = CampaignRepository(context)
            if (selectedCampaign.contacts.isNullOrEmpty())
                selectedCampaign.contacts = mutableListOf()
            selectedCampaign.contacts.add(selectedContact)
            val response = repository.addContactToCampaign(selectedCampaign)
            if (response != null)
                showCampaignDialog = false
        }
    }


    val context = LocalContext.current
    LaunchedEffect(Unit) {
        val contactRepository = ContactRepository(context = context)
        val campaignRepository = CampaignRepository(context = context)
        val contactList = withContext(Dispatchers.IO) {
            contactRepository.getContactList()
        }
        val campaignList = withContext(Dispatchers.IO) {
            campaignRepository.getClientCampaigns()
        }
        contacts = contactList?.result ?: emptyList()
        campaigns = campaignList?.result ?: emptyList()
    }

    if (confirmContact) {
        LaunchedEffect(Unit) {
            val contact = ContactDto(email = inputText.text)
            val repository = ContactRepository(context = context)
            withContext(Dispatchers.IO) {
                repository.addContact(contact)
            }
            val updatedContacts = withContext(Dispatchers.IO) {
                repository.getContactList()
            }
            contacts = updatedContacts?.result ?: emptyList()
            inputText = TextFieldValue("")
            confirmContact = false
            showContactDialog = false
        }
    }


    if (showContactDialog) {
        OpenDialog(
            confirmText = "Add",
            cancelText = "Cancel",
            onConfirm = {
                confirmContact = true
            },
            onCancel = { showContactDialog = false },
            content = {
                GenerateOutlinedTextField(
                    label = "Email",
                    value = inputText,
                    onValueChange = { inputText = it },
                    keyboardType = KeyboardType.Email,
                )
            }
        )
    }

    if (showCampaignDialog) {
        AlertDialog(
            onDismissRequest = { showCampaignDialog = false },
            title = { Text(text = "Select a Campaign") },
            text = {
                Column {
                    campaigns.forEach { campaign ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedCampaign = campaign
                                }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (selectedCampaign == campaign),
                                onClick = { selectedCampaign = campaign },
                                modifier = Modifier.padding(end = 16.dp)
                            )
                            Text(text = campaign.title)
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    showCampaignDialog = false
                    addContactToCampaign(
                        context,
                        selectedContact,
                        selectedCampaign
                    )
                }) {
                    Text(text = "OK")
                }
            },
            dismissButton = {
                Button(onClick = { showCampaignDialog = false }) {
                    Text(text = "Cancel")
                }
            }
        )
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showContactDialog = true
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(imageVector = Icons.Rounded.Add, contentDescription = "Add contact")
            }
        },
        content = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp),
                verticalArrangement = Arrangement.Top
            ) {
                itemsIndexed(contacts) { _: Int, contact: ContactDto ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp)
                            .height(60.dp)
                            .background(
                                color = MaterialTheme.colorScheme.inverseOnSurface,
                                shape = RoundedCornerShape(15.dp)
                            )
                            .border(
                                BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                                shape = RoundedCornerShape(15.dp)
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.AccountBox,
                            contentDescription = contact.email,
                            modifier = Modifier
                                .size(50.dp)
                                .padding(5.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = contact.email,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(
                            onClick = { onCampaignButtonClick(contact) },
                            modifier = Modifier
                                .size(50.dp)
                                .padding(5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.AddCircle,
                                contentDescription = contact.email,
                                modifier = Modifier.size(50.dp)
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
@Preview(showBackground = true)
fun PreviewContactList() {
    EmaTheme {
        ContactListPage()
    }
}