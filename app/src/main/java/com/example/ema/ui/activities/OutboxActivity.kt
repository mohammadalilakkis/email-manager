package com.example.ema.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ema.api.Repository.MailRepository
import com.example.ema.model.CategoryDto
import com.example.ema.model.MailRecordDto
import com.example.ema.ui.theme.EmaTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OutboxActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EmaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    OutboxPage()
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun OutboxPage() {
    var mails by remember { mutableStateOf(emptyList<MailRecordDto>()) }
    var categories by remember { mutableStateOf(emptyList<CategoryDto>()) }
    var isLoading by remember { mutableStateOf(true) }
    val selectedChip = remember { mutableStateOf(CategoryDto(0, "")) }

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        val repository = MailRepository(context = context)
        val mailList = withContext(Dispatchers.IO) {
            repository.getOutbox()
        }
        val categoryList = withContext(Dispatchers.IO) {
            repository.getCategories()
        }
        categories = categoryList?.result ?: emptyList()
        mails = mailList?.result ?: emptyList()
        isLoading = false
    }

    LaunchedEffect(selectedChip.value) {
        isLoading = true
        val repository = MailRepository(context = context)
        val mailList = withContext(Dispatchers.IO) {
            repository.getOutbox(selectedChip.value.title)
        }
        mails = mailList?.result ?: emptyList()
        isLoading = false
    }

    if (isLoading && categories.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        }
    } else {
        val scrollState = rememberScrollState()

        Column {
            Row(modifier = Modifier
                .horizontalScroll(scrollState)
                .height(40.dp)
                .padding(5.dp)) {
                categories.forEach { category ->
                    FilterChip(
                        modifier = Modifier.padding(5.dp, 0.dp),
                        label = { Text(category.title) },
                        selected = category == selectedChip.value,
                        onClick = {
                            if (selectedChip.value == category) {
                                selectedChip.value = CategoryDto(0, "")
                                return@FilterChip
                            }
                            selectedChip.value = category
                                  },
                        leadingIcon = if (category == selectedChip.value && selectedChip.value.title.isNotBlank()) {
                            {
                                Icon(
                                    imageVector = Icons.Filled.Done,
                                    contentDescription = "Done icon",
                                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                                )
                            }
                        } else {
                            null
                        }
                    )
                }
            }
            Divider(modifier = Modifier.height(2.dp))
            Row {
                LazyColumn(
                    modifier = Modifier
                        .padding(10.dp)
                        .weight(1f)
                ) {
                    itemsIndexed(mails) { _: Int, item: MailRecordDto ->
                        var isExpanded by remember { mutableStateOf(false) }

                        Column(modifier = Modifier
                            .clickable { isExpanded = !isExpanded }
                            .animateContentSize()
                            .padding(10.dp)
                            .defaultMinSize(minHeight = 20.dp)
                            .background(
                                color = MaterialTheme.colorScheme.inverseOnSurface,
                                shape = RoundedCornerShape(15.dp)
                            )
                            .border(
                                BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                                shape = RoundedCornerShape(15.dp)
                            )) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(5.dp)
                            ) {
                                Text(text = item.mail.title, fontWeight = FontWeight.Bold)
                                val icon =
                                    if (isExpanded) Icons.Rounded.KeyboardArrowDown
                                    else Icons.Rounded.KeyboardArrowUp
                                Icon(imageVector = icon, contentDescription = "expand arrow")
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(5.dp)
                            ) {
                                Text(
                                    text = if (item.campaigns.isEmpty())
                                        ""
                                    else item.campaigns
                                        .map { campaign -> campaign.title }.joinToString(", "),
                                    fontWeight = FontWeight.Light,
                                    maxLines = if (isExpanded) Int.MAX_VALUE else 1
                                )
                            }
                            if (isExpanded) {
                                Divider(color = MaterialTheme.colorScheme.outline, thickness = 2.dp)
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(5.dp)
                                ) {
                                    Text(
                                        text = "Message: ${item.mail.body}",
                                        modifier = Modifier.padding(start = 16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}