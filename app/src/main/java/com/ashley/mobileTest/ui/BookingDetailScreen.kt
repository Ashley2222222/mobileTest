package com.ashley.mobileTest.ui

/**
 * @description: compose写页面
 * @author: liangxy
 */

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ashley.mobileTest.model.Booking
import com.ashley.mobileTest.model.Segment
import com.ashley.mobileTest.viewmodel.BookingViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.livedata.observeAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun mobileTestDetailScreen(viewModel: BookingViewModel) {

    val mobileTestDataState = viewModel.mobileTestData.observeAsState()
    val mobileTestData = mobileTestDataState.value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("mobileTest Details") },
                Modifier.background(MaterialTheme.colorScheme.primary),
            )
        }
    ) { paddingValues ->
        if (mobileTestData == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 1. 基础信息卡片
                item {
                    mobileTestBaseInfoCard(mobileTest = mobileTestData)
                }
                // 2. 航段列表
                items(mobileTestData.segments) { segment ->
                    SegmentItem(segment = segment)
                }
            }
        }
    }
}

//基础信息卡片
@Composable
fun mobileTestBaseInfoCard(mobileTest: Booking?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp, // 默认阴影
            pressedElevation = 8.dp  // 点击时阴影（可选）
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Ship Reference: ${mobileTest?.shipReference}", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Ship Token: ${mobileTest?.shipToken}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Can Issue Ticket Checking: ${mobileTest?.canIssueTicketChecking}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Expiry Time: ${formatExpiryTime(mobileTest?.expiryTime)}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Duration: ${mobileTest?.duration} minutes")
        }
    }
}

private fun formatExpiryTime(timestamp: String?): String {
    if (timestamp.isNullOrEmpty()) return "N/A"
    return try {
        // Assume timestamp is in seconds
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val netDate = Date(timestamp.toLong() * 1000)
        sdf.format(netDate)
    } catch (e: Exception) {
        timestamp
    }
}


// 通用信息行
@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontWeight = FontWeight.Bold, color =colorScheme.onSurface)
        Text(text = value, fontWeight = FontWeight.Medium)
    }
}

// 航段列表项（支持展开/收起）
@Composable
fun SegmentItem(segment: Segment) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        onClick = { expanded = !expanded },
    ) {
        //航段头部 展开/收起状态下的内容
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Segment ${segment.id}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "${segment.originAndDestinationPair.originCity} ➝ ${segment.originAndDestinationPair.destinationCity}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            Icon(
                imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = if (expanded) "Collapse" else "Expand",
                tint = MaterialTheme.colorScheme.primary
            )
        }
        //展开后的内容
        if (expanded) {
            Column(modifier = Modifier.padding(16.dp)) {
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                // Origin Section
                Text(
                    text = "Origin",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                LocationInfo(location = segment.originAndDestinationPair.origin)
                InfoRow(label = "City", value = segment.originAndDestinationPair.originCity)

                Spacer(modifier = Modifier.height(12.dp))

                // Destination Section
                Text(
                    text = "Destination",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                LocationInfo(location = segment.originAndDestinationPair.destination)
                InfoRow(label = "City", value = segment.originAndDestinationPair.destinationCity)
            }
        }
    }
}

// 地点信息展示
@Composable
fun LocationInfo(location: com.ashley.mobileTest.model.Location) {
    Column(modifier = Modifier.padding(start = 8.dp)) {
        InfoRow(label = "Code", value = location.code)
        InfoRow(label = "Name", value = location.displayName)
        InfoRow(label = "URL", value = location.url)
    }
}

