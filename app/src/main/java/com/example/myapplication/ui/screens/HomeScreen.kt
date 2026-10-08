package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.model.HeritageSite
import com.example.myapplication.ui.components.CategoryChip
import com.example.myapplication.ui.components.HeritageCard

@Composable
fun HomeScreen(
    searchQuery: String,
    selectedCategory: String,
    categories: List<String>,
    sites: List<HeritageSite>,
    onSearchQueryChange: (String) -> Unit,
    onCategorySelect: (String) -> Unit,
    onSiteClick: (String) -> Unit,
    onFavoriteToggle: (HeritageSite) -> Unit
) {
    val popularSites = sites.sortedByDescending { it.averageRating }.take(4)

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // Top Header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "HERITAGE EXPLORER",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = MaterialTheme.typography.labelMedium.letterSpacing
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Discover Historic Sites",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Search Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = { Text("Search heritage sites or locations...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search"
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchQueryChange("") }) {
                            Icon(
                                imageVector = Icons.Filled.Clear,
                                contentDescription = "Clear search"
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }

        // Categories Row
        item {
            Column(modifier = Modifier.padding(vertical = 12.dp)) {
                Text(
                    text = "Categories",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(categories) { category ->
                        CategoryChip(
                            category = category,
                            isSelected = category == selectedCategory,
                            onSelected = { onCategorySelect(category) }
                        )
                    }
                }
            }
        }

        // Popular Sites Row (horizontal)
        if (popularSites.isNotEmpty() && searchQuery.isEmpty()) {
            item {
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    Text(
                        text = "Popular Sites",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(popularSites, key = { it.id }) { site ->
                            HeritageCard(
                                site = site,
                                onSiteClick = onSiteClick,
                                onFavoriteToggle = onFavoriteToggle,
                                modifier = Modifier.width(280.dp)
                            )
                        }
                    }
                }
            }
        }

        // Section Title: Explore All
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (searchQuery.isNotEmpty()) "Search Results (${sites.size})" else "All Heritage Sites",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Empty Search Result State
        if (sites.isEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "No heritage sites found",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Try searching with a different name or category",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }
        } else {
            // Main List Items
            items(sites, key = { "all_${it.id}" }) { site ->
                HeritageCard(
                    site = site,
                    onSiteClick = onSiteClick,
                    onFavoriteToggle = onFavoriteToggle,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    com.example.myapplication.ui.theme.MyApplicationTheme {
        HomeScreen(
            searchQuery = "",
            selectedCategory = "All",
            categories = listOf("All", "Forts", "Temples", "Monuments", "Caves"),
            sites = listOf(
                HeritageSite(
                    id = "1",
                    name = "Taj Mahal",
                    location = "Agra, India",
                    description = "Famous ivory-white marble mausoleum.",
                    category = "Monuments",
                    imageUrl = "",
                    builtYear = "1632 AD",
                    isFavorite = true,
                    averageRating = 4.8f,
                    reviewCount = 12
                ),
                HeritageSite(
                    id = "2",
                    name = "Red Fort",
                    location = "Delhi, India",
                    description = "Historic fort in Delhi.",
                    category = "Forts",
                    imageUrl = "",
                    builtYear = "1639 AD",
                    isFavorite = false,
                    averageRating = 4.6f,
                    reviewCount = 8
                )
            ),
            onSearchQueryChange = {},
            onCategorySelect = {},
            onSiteClick = {},
            onFavoriteToggle = {}
        )
    }
}
