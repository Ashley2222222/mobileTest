package com.ashley.mobileTest.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ashley.mobileTest.data.BookingDataManager
import com.ashley.mobileTest.model.Booking
import com.ashley.mobileTest.model.Segment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingListScreen(dataManager: BookingDataManager) {
    val mobileTestData by dataManager.mobileTestData.observeAsState()
    val isLoading by dataManager.isLoading.observeAsState(false)
    val error by dataManager.error.observeAsState()

    Scaffold(
    topBar =
    {
        TopAppBar(
            title = { Text("mobileTest List") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            actions = {
                IconButton(onClick = { dataManager.refreshMobileTestData() }) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        )
    }
    )
    {
        paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isLoading) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Text(
                        text = "Loading...",
                        modifier = Modifier.padding(top = 16.dp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            } else if (error != null) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Error: $error",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            } else if (mobileTestData != null) {
                MobileTestDetailsCard(mobileTest = mobileTestData!!)
                SegmentsList(segments = mobileTestData!!.segments)
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "No mobileTest data available",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
fun MobileTestDetailsCard(mobileTest: Booking) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "mobileTest Details",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Text(text = "Reference: ${mobileTest.shipReference}")
            Text(text = "Token: ${mobileTest.shipToken}")
            Text(text = "Can Issue Ticket: ${mobileTest.canIssueTicketChecking}")
            Text(text = "Duration: ${mobileTest.duration} minutes")
        }
    }
}

@Composable
fun SegmentsList(segments: List<Segment>) {
    Text(
        text = "Segments",
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier.padding(start = 16.dp, top = 8.dp),
        color = MaterialTheme.colorScheme.primary
    )
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(segments) { segment ->
            SegmentCard(segment = segment)
        }
    }
}

@Composable
fun SegmentCard(segment: Segment) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Segment ${segment.id}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "${segment.originAndDestinationPair.originCity} → ${segment.originAndDestinationPair.destinationCity}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                text = "Origin: ${segment.originAndDestinationPair.origin.displayName}",
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                text = "Destination: ${segment.originAndDestinationPair.destination.displayName}",
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

