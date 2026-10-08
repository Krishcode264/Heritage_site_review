
package com.example.myapplication.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.HeritageDatabaseHelper
import com.example.myapplication.data.model.HeritageSite
import com.example.myapplication.data.model.Review
import com.example.myapplication.data.repository.HeritageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class HeritageViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: HeritageRepository

    // Search state
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Category state
    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    // All heritage sites
    private val _allSites = MutableStateFlow<List<HeritageSite>>(emptyList())
    val allSites: StateFlow<List<HeritageSite>> = _allSites.asStateFlow()

    // Favorite sites
    private val _favoriteSites = MutableStateFlow<List<HeritageSite>>(emptyList())
    val favoriteSites: StateFlow<List<HeritageSite>> = _favoriteSites.asStateFlow()

    // Currently selected site
    private val _selectedSite = MutableStateFlow<HeritageSite?>(null)
    val selectedSite: StateFlow<HeritageSite?> = _selectedSite.asStateFlow()

    // Reviews for selected site
    private val _selectedSiteReviews = MutableStateFlow<List<Review>>(emptyList())
    val selectedSiteReviews: StateFlow<List<Review>> = _selectedSiteReviews.asStateFlow()

    // Write review dialog
    private val _showWriteReviewDialog = MutableStateFlow(false)
    val showWriteReviewDialog: StateFlow<Boolean> = _showWriteReviewDialog.asStateFlow()

    // Available categories
    val categories = listOf(
        "All",
        "Forts",
        "Temples",
        "Monuments",
        "Caves"
    )

    // Initialize repository and load initial data
    // IMPORTANT: This comes AFTER the StateFlows are initialized.
    init {
        val dbHelper = HeritageDatabaseHelper(application)
        repository = HeritageRepository(dbHelper)

        loadSites()
        loadFavorites()
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        loadSites()
    }

    fun onCategorySelect(category: String) {
        _selectedCategory.value = category
        loadSites()
    }

    fun loadSites() {
        viewModelScope.launch {
            _allSites.value = repository.getAllSites(
                searchQuery = _searchQuery.value,
                category = _selectedCategory.value
            )
        }
    }

    fun loadFavorites() {
        viewModelScope.launch {
            _favoriteSites.value = repository.getFavoriteSites()
        }
    }

    fun selectSite(siteId: String) {
        viewModelScope.launch {
            val site = repository.getSiteById(siteId)

            _selectedSite.value = site

            if (site != null) {
                _selectedSiteReviews.value =
                    repository.getReviewsForSite(site.id)
            }
        }
    }

    fun toggleFavorite(site: HeritageSite) {
        viewModelScope.launch {
            val newFavStatus = !site.isFavorite

            repository.toggleFavorite(
                site.id,
                newFavStatus
            )

            loadSites()
            loadFavorites()

            if (_selectedSite.value?.id == site.id) {
                _selectedSite.value =
                    _selectedSite.value?.copy(
                        isFavorite = newFavStatus
                    )
            }
        }
    }

    fun setShowWriteReviewDialog(show: Boolean) {
        _showWriteReviewDialog.value = show
    }

    fun submitReview(
        siteId: String,
        userName: String,
        rating: Float,
        reviewText: String
    ) {
        if (
            userName.isBlank() ||
            reviewText.isBlank() ||
            rating <= 0f
        ) {
            return
        }

        viewModelScope.launch {

            val dateFormat =
                SimpleDateFormat(
                    "MMM dd, yyyy",
                    Locale.getDefault()
                )

            val currentDate =
                dateFormat.format(Date())

            val review = Review(
                id = UUID.randomUUID().toString(),
                siteId = siteId,
                userName = userName.trim(),
                rating = rating,
                reviewText = reviewText.trim(),
                date = currentDate
            )

            repository.addReview(review)

            setShowWriteReviewDialog(false)

            // Refresh site details and reviews
            selectSite(siteId)

            // Refresh site list
            loadSites()
        }
    }
}

