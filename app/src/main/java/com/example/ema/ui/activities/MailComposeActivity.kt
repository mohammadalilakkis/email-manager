package com.example.ema.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.ema.api.Repository.CampaignRepository
import com.example.ema.api.Repository.MailRepository
import com.example.ema.components.GenerateExposedDropdownMenuBox
import com.example.ema.components.GenerateOutlinedTextField
import com.example.ema.model.CampaignDto
import com.example.ema.model.CategoryDto
import com.example.ema.model.MailDto
import com.example.ema.ui.theme.EmaTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MailComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EmaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    MailComposePage()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MailComposePage() {
    var contacts by remember { mutableStateOf(emptyList<CampaignDto>()) }
    var categories by remember { mutableStateOf(emptyList<CategoryDto>()) }

    var selectedCategory by remember { mutableStateOf(CategoryDto(0, "")) }

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        val campaignRepository = CampaignRepository(context = context)
        val mailRepository = MailRepository(context = context)
        val campaignList = withContext(Dispatchers.IO) {
            campaignRepository.getClientCampaigns()
        }
        val categoryList = withContext(Dispatchers.IO) {
            mailRepository.getCategories()
        }
        contacts = campaignList?.result ?: emptyList()
        categories = categoryList?.result ?: emptyList()
    }

    val selectedCampaigns = remember { mutableStateListOf<String>() }
    var title by remember { mutableStateOf(TextFieldValue()) }
    var body by remember { mutableStateOf(TextFieldValue()) }

    val validationErrors = remember { mutableStateOf(emptyMap<String, String>()) }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = CenterHorizontally,
        modifier = Modifier.padding(10.dp)
    ) {
        GenerateMultiSelectDropdown(
            campaignList = contacts,
            onSelectionChanged = { updatedSelection ->
                selectedCampaigns.clear()
                selectedCampaigns.addAll(updatedSelection)
            })
        Spacer(modifier = Modifier.width(10.dp))
        GenerateOutlinedTextField(
            value = title, label = "Title", onValueChange = { newValue ->
                title = newValue
                validationErrors.value =
                    validationErrors.value + ("title" to validateContent(newValue, "title"))
            },
            errorMessage = validationErrors.value["title"] ?: "",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.width(10.dp))
        GenerateExposedDropdownMenuBox(items = categories,
            selected = selectedCategory,
            onItemSelected = { item ->
                selectedCategory = item
            }, label = "Mail category")
        Spacer(modifier = Modifier.width(10.dp))
        GenerateOutlinedTextField(
            value = body, label = "Message", onValueChange = { newValue ->
                body = newValue
                validationErrors.value =
                    validationErrors.value + ("body" to validateContent(newValue, "message"))
            },
            errorMessage = validationErrors.value["body"] ?: "",
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            maxLines = Int.MAX_VALUE
        )
        Spacer(modifier = Modifier.width(10.dp))
        Row(Modifier.fillMaxWidth()) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                onClick = {
                    sendMessage(
                        context = context,
                        selectedCampaigns = contacts.filter { campaign ->
                            selectedCampaigns.contains(
                                campaign.title
                            )
                        }, category = selectedCategory,
                        title = title.text, body = body.text,
                        validationErrors = validationErrors.value
                    )
                }) {
                Text(text = "Send", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(10.dp))
                Icon(
                    imageVector = Icons.Rounded.Send,
                    contentDescription = "send", modifier = Modifier.size(30.dp)
                )
            }
        }
    }

}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun GenerateMultiSelectDropdown(
    campaignList: List<CampaignDto>,
    onSelectionChanged: (List<String>) -> Unit
) {
    val campaignNames: List<String> = campaignList.map { campaign ->
        campaign.title
    }

    val selectedCampaignNames = remember { mutableStateListOf<String>() }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center) {
        OutlinedTextField(
            value = selectedCampaignNames.joinToString(", "),
            onValueChange = { /* No-op */ },
            label = { Text("To") },
            readOnly = true,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Expand",
                    modifier = Modifier.clickable { isDropdownExpanded = !isDropdownExpanded }
                )
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        DropdownMenu(
            expanded = isDropdownExpanded,
            onDismissRequest = { isDropdownExpanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            campaignNames.forEach { contactName ->
                val isChecked = selectedCampaignNames.contains(contactName)

                DropdownMenuItem(onClick = {
                    if (isChecked) {
                        selectedCampaignNames.remove(contactName)
                    } else {
                        selectedCampaignNames.add(contactName)
                    }
                    onSelectionChanged(selectedCampaignNames)
                }, text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = null // Disable individual checkbox interaction
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = contactName)
                    }
                })
            }
        }
    }
}

private fun sendMessage(
    context: Context,
    selectedCampaigns: List<CampaignDto>,
    title: String, category: CategoryDto,
    body: String, validationErrors: Map<String, String>
) {
    val result = validationErrors.filter { (key, value) -> value.isNotBlank() }
    if (result.isNotEmpty() || validationErrors.isEmpty()) {
        Toast.makeText(context, "Fill all the fields correctly!", Toast.LENGTH_SHORT).show()
        return
    }
    GlobalScope.launch {
        val mail = MailDto(
            title = title,
            body = body, category = if (category.title.isEmpty()) null else category,
            campaigns = selectedCampaigns
        )
        val response = MailRepository(context).sendMail(mail)
        if (response != null) {
            val intent = Intent(context, DashboardActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }
}

private fun validateContent(value: TextFieldValue, content: String): String {
    return if (value.text.isEmpty()) "Fill the $content" else ""
}