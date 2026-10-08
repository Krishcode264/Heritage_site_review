package com.example.myapplication.data.repository

import com.example.myapplication.data.local.HeritageDatabaseHelper
import com.example.myapplication.data.model.HeritageSite
import com.example.myapplication.data.model.Review
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HeritageRepository(private val dbHelper: HeritageDatabaseHelper) {

    suspend fun getAllSites(searchQuery: String = "", category: String = "All"): List<HeritageSite> {
        return withContext(Dispatchers.IO) {
            dbHelper.getAllSites(searchQuery, category)
        }
    }

    suspend fun getFavoriteSites(): List<HeritageSite> {
        return withContext(Dispatchers.IO) {
            dbHelper.getFavoriteSites()
        }
    }

    suspend fun getSiteById(siteId: String): HeritageSite? {
        return withContext(Dispatchers.IO) {
            dbHelper.getSiteById(siteId)
        }
    }

    suspend fun getReviewsForSite(siteId: String): List<Review> {
        return withContext(Dispatchers.IO) {
            dbHelper.getReviewsForSite(siteId)
        }
    }

    suspend fun addReview(review: Review) {
        withContext(Dispatchers.IO) {
            dbHelper.addReview(review)
        }
    }

    suspend fun toggleFavorite(siteId: String, isFavorite: Boolean) {
        withContext(Dispatchers.IO) {
            dbHelper.toggleFavorite(siteId, isFavorite)
        }
    }
}
