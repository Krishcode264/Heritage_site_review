package com.example.myapplication.data.local

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.myapplication.data.model.HeritageSite
import com.example.myapplication.data.model.Review
import kotlin.math.round

class HeritageDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "heritage_explorer.db"
        private const val DATABASE_VERSION = 1

        // Sites Table
        private const val TABLE_SITES = "heritage_sites"
        private const val KEY_SITE_ID = "id"
        private const val KEY_SITE_NAME = "name"
        private const val KEY_SITE_LOCATION = "location"
        private const val KEY_SITE_DESC = "description"
        private const val KEY_SITE_CATEGORY = "category"
        private const val KEY_SITE_IMAGE_URL = "imageUrl"
        private const val KEY_SITE_BUILT_YEAR = "builtYear"
        private const val KEY_SITE_IS_FAVORITE = "isFavorite"

        // Reviews Table
        private const val TABLE_REVIEWS = "reviews"
        private const val KEY_REVIEW_ID = "id"
        private const val KEY_REVIEW_SITE_ID = "siteId"
        private const val KEY_REVIEW_USER_NAME = "userName"
        private const val KEY_REVIEW_RATING = "rating"
        private const val KEY_REVIEW_TEXT = "reviewText"
        private const val KEY_REVIEW_DATE = "date"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createSitesTable = """
            CREATE TABLE $TABLE_SITES (
                $KEY_SITE_ID TEXT PRIMARY KEY,
                $KEY_SITE_NAME TEXT NOT NULL,
                $KEY_SITE_LOCATION TEXT NOT NULL,
                $KEY_SITE_DESC TEXT NOT NULL,
                $KEY_SITE_CATEGORY TEXT NOT NULL,
                $KEY_SITE_IMAGE_URL TEXT NOT NULL,
                $KEY_SITE_BUILT_YEAR TEXT NOT NULL,
                $KEY_SITE_IS_FAVORITE INTEGER DEFAULT 0
            )
        """.trimIndent()

        val createReviewsTable = """
            CREATE TABLE $TABLE_REVIEWS (
                $KEY_REVIEW_ID TEXT PRIMARY KEY,
                $KEY_REVIEW_SITE_ID TEXT NOT NULL,
                $KEY_REVIEW_USER_NAME TEXT NOT NULL,
                $KEY_REVIEW_RATING REAL NOT NULL,
                $KEY_REVIEW_TEXT TEXT NOT NULL,
                $KEY_REVIEW_DATE TEXT NOT NULL,
                FOREIGN KEY($KEY_REVIEW_SITE_ID) REFERENCES $TABLE_SITES($KEY_SITE_ID)
            )
        """.trimIndent()

        db.execSQL(createSitesTable)
        db.execSQL(createReviewsTable)

        seedInitialData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_REVIEWS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_SITES")
        onCreate(db)
    }

    private fun seedInitialData(db: SQLiteDatabase) {
        val sites = listOf(
            HeritageSite(
                id = "1",
                name = "Taj Mahal",
                location = "Agra, India",
                description = "The Taj Mahal is an ivory-white marble mausoleum on the right bank of the river Yamuna in Agra, India. It was commissioned in 1631 by the fifth Mughal emperor, Shah Jahan, to house the tomb of his favourite wife, Mumtaz Mahal.",
                category = "Monuments",
                imageUrl = "https://images.unsplash.com/photo-1564507592333-c60657eea523?q=80&w=800&auto=format&fit=crop",
                builtYear = "1632 AD",
                isFavorite = true
            ),
            HeritageSite(
                id = "2",
                name = "Red Fort",
                location = "Delhi, India",
                description = "The Red Fort is a historic fort in the city of Delhi in India that served as the main residence of the Mughal Emperors. Emperor Shah Jahan commissioned construction of the Red Fort on 12 May 1638, when he decided to shift his capital from Agra to Delhi.",
                category = "Forts",
                imageUrl = "https://images.unsplash.com/photo-1598324789736-4861f89564a0?q=80&w=800&auto=format&fit=crop",
                builtYear = "1639 AD",
                isFavorite = false
            ),
            HeritageSite(
                id = "3",
                name = "Gateway of India",
                location = "Mumbai, India",
                description = "The Gateway of India is an arch-monument built in the early twentieth century in the city of Mumbai, India. It was erected to commemorate the landing in India of King George V and Queen Mary at Apollo Bunder on their visit to India in 1911.",
                category = "Monuments",
                imageUrl = "https://images.unsplash.com/photo-1570168007204-dfb528c6958f?q=80&w=800&auto=format&fit=crop",
                builtYear = "1924 AD",
                isFavorite = false
            ),
            HeritageSite(
                id = "4",
                name = "Hawa Mahal",
                location = "Jaipur, India",
                description = "The Hawa Mahal is a palace in the city of Jaipur, India. Built from red and pink sandstone, it is on the edge of the City Palace, Jaipur, and extends to the Zenana, or women's chambers.",
                category = "Monuments",
                imageUrl = "https://images.unsplash.com/photo-1599661046289-e31897846e41?q=80&w=800&auto=format&fit=crop",
                builtYear = "1799 AD",
                isFavorite = true
            ),
            HeritageSite(
                id = "5",
                name = "Meenakshi Temple",
                location = "Madurai, India",
                description = "Meenakshi Temple is a historic Hindu temple located on the southern bank of the Vaigai River in Madurai, Tamil Nadu, India. Dedicated to Meenakshi and Sundareshwarar, it features iconic colorful gopurams.",
                category = "Temples",
                imageUrl = "https://images.unsplash.com/photo-1600100397608-f010e423b971?q=80&w=800&auto=format&fit=crop",
                builtYear = "1623 AD",
                isFavorite = false
            ),
            HeritageSite(
                id = "6",
                name = "Ajanta Caves",
                location = "Aurangabad, India",
                description = "The Ajanta Caves are 30 rock-cut Buddhist cave monuments dating from the second century BCE to about 480 CE in Maharashtra state, famous for ancient wall paintings and rock sculptures.",
                category = "Caves",
                imageUrl = "https://images.unsplash.com/photo-1627894483216-2138af692e32?q=80&w=800&auto=format&fit=crop",
                builtYear = "2nd Century BCE",
                isFavorite = false
            ),
            HeritageSite(
                id = "7",
                name = "Agra Fort",
                location = "Agra, India",
                description = "Agra Fort is a historical fort in the city of Agra in India. It was the main residence of the emperors of the Mughal Dynasty until 1638, offering stunning views of the Taj Mahal.",
                category = "Forts",
                imageUrl = "https://images.unsplash.com/photo-1548013146-72479768bada?q=80&w=800&auto=format&fit=crop",
                builtYear = "1573 AD",
                isFavorite = false
            ),
            HeritageSite(
                id = "8",
                name = "Qutub Minar",
                location = "Delhi, India",
                description = "The Qutb Minar is a minaret and victory tower that forms part of the Qutb complex in Delhi, recognized as a UNESCO World Heritage Site.",
                category = "Monuments",
                imageUrl = "https://images.unsplash.com/photo-1587474260584-136574528ed5?q=80&w=800&auto=format&fit=crop",
                builtYear = "1192 AD",
                isFavorite = false
            )
        )

        for (site in sites) {
            val cv = ContentValues().apply {
                put(KEY_SITE_ID, site.id)
                put(KEY_SITE_NAME, site.name)
                put(KEY_SITE_LOCATION, site.location)
                put(KEY_SITE_DESC, site.description)
                put(KEY_SITE_CATEGORY, site.category)
                put(KEY_SITE_IMAGE_URL, site.imageUrl)
                put(KEY_SITE_BUILT_YEAR, site.builtYear)
                put(KEY_SITE_IS_FAVORITE, if (site.isFavorite) 1 else 0)
            }
            db.insert(TABLE_SITES, null, cv)
        }

        val initialReviews = listOf(
            Review("r1", "1", "Rahul", 5.0f, "Beautiful architecture and majestic marble work!", "May 10, 2024"),
            Review("r2", "1", "Priya", 4.0f, "Very peaceful and worth visiting in the early morning.", "May 15, 2024"),
            Review("r3", "1", "Aman", 5.0f, "Definitely worth visiting! Truly one of the wonders of the world.", "Jun 01, 2024"),
            Review("r4", "2", "Sneha", 5.0f, "Rich history and incredible red sandstone structure.", "Apr 12, 2024"),
            Review("r5", "2", "Rohit", 4.0f, "Great light and sound show in the evening!", "Apr 20, 2024"),
            Review("r6", "3", "Vikram", 4.5f, "Amazing sea breeze and iconic photo spot in Mumbai.", "Mar 05, 2024"),
            Review("r7", "3", "Ananya", 5.0f, "Bustling surrounding area near the iconic Taj Hotel.", "Mar 11, 2024"),
            Review("r8", "4", "Meera", 5.0f, "Intricate 953 small windows (jharokhas). Simply mesmerizing!", "Feb 18, 2024"),
            Review("r9", "5", "Suresh", 5.0f, "Vibrant colors and towers filled with sculpted figures!", "Jan 22, 2024"),
            Review("r10", "6", "Dev", 5.0f, "Ancient rock carvings and wall paintings are breathtaking.", "Jan 10, 2024")
        )

        for (review in initialReviews) {
            val cv = ContentValues().apply {
                put(KEY_REVIEW_ID, review.id)
                put(KEY_REVIEW_SITE_ID, review.siteId)
                put(KEY_REVIEW_USER_NAME, review.userName)
                put(KEY_REVIEW_RATING, review.rating)
                put(KEY_REVIEW_TEXT, review.reviewText)
                put(KEY_REVIEW_DATE, review.date)
            }
            db.insert(TABLE_REVIEWS, null, cv)
        }
    }

    fun getAllSites(searchQuery: String = "", category: String = "All"): List<HeritageSite> {
        val sites = mutableListOf<HeritageSite>()
        val db = readableDatabase

        var selection: String? = null
        val selectionArgs = mutableListOf<String>()

        if (category != "All") {
            selection = "$KEY_SITE_CATEGORY = ?"
            selectionArgs.add(category)
        }

        if (searchQuery.isNotBlank()) {
            val searchPattern = "%${searchQuery.trim()}%"
            val querySelection = "($KEY_SITE_NAME LIKE ? OR $KEY_SITE_LOCATION LIKE ?)"
            selection = if (selection == null) {
                querySelection
            } else {
                "$selection AND $querySelection"
            }
            selectionArgs.add(searchPattern)
            selectionArgs.add(searchPattern)
        }

        val cursor = db.query(
            TABLE_SITES,
            null,
            selection,
            if (selectionArgs.isNotEmpty()) selectionArgs.toTypedArray() else null,
            null,
            null,
            "$KEY_SITE_NAME ASC"
        )

        cursor.use {
            while (it.moveToNext()) {
                val id = it.getString(it.getColumnIndexOrThrow(KEY_SITE_ID))
                val name = it.getString(it.getColumnIndexOrThrow(KEY_SITE_NAME))
                val location = it.getString(it.getColumnIndexOrThrow(KEY_SITE_LOCATION))
                val desc = it.getString(it.getColumnIndexOrThrow(KEY_SITE_DESC))
                val cat = it.getString(it.getColumnIndexOrThrow(KEY_SITE_CATEGORY))
                val imageUrl = it.getString(it.getColumnIndexOrThrow(KEY_SITE_IMAGE_URL))
                val builtYear = it.getString(it.getColumnIndexOrThrow(KEY_SITE_BUILT_YEAR))
                val isFav = it.getInt(it.getColumnIndexOrThrow(KEY_SITE_IS_FAVORITE)) == 1

                val (avgRating, count) = getRatingAndCountForSite(db, id)

                sites.add(
                    HeritageSite(
                        id = id,
                        name = name,
                        location = location,
                        description = desc,
                        category = cat,
                        imageUrl = imageUrl,
                        builtYear = builtYear,
                        isFavorite = isFav,
                        averageRating = avgRating,
                        reviewCount = count
                    )
                )
            }
        }

        return sites
    }

    fun getFavoriteSites(): List<HeritageSite> {
        return getAllSites().filter { it.isFavorite }
    }

    fun getSiteById(siteId: String): HeritageSite? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_SITES,
            null,
            "$KEY_SITE_ID = ?",
            arrayOf(siteId),
            null,
            null,
            null
        )

        cursor.use {
            if (it.moveToFirst()) {
                val id = it.getString(it.getColumnIndexOrThrow(KEY_SITE_ID))
                val name = it.getString(it.getColumnIndexOrThrow(KEY_SITE_NAME))
                val location = it.getString(it.getColumnIndexOrThrow(KEY_SITE_LOCATION))
                val desc = it.getString(it.getColumnIndexOrThrow(KEY_SITE_DESC))
                val cat = it.getString(it.getColumnIndexOrThrow(KEY_SITE_CATEGORY))
                val imageUrl = it.getString(it.getColumnIndexOrThrow(KEY_SITE_IMAGE_URL))
                val builtYear = it.getString(it.getColumnIndexOrThrow(KEY_SITE_BUILT_YEAR))
                val isFav = it.getInt(it.getColumnIndexOrThrow(KEY_SITE_IS_FAVORITE)) == 1

                val (avgRating, count) = getRatingAndCountForSite(db, id)

                return HeritageSite(
                    id = id,
                    name = name,
                    location = location,
                    description = desc,
                    category = cat,
                    imageUrl = imageUrl,
                    builtYear = builtYear,
                    isFavorite = isFav,
                    averageRating = avgRating,
                    reviewCount = count
                )
            }
        }
        return null
    }

    private fun getRatingAndCountForSite(db: SQLiteDatabase, siteId: String): Pair<Float, Int> {
        val query = "SELECT AVG($KEY_REVIEW_RATING), COUNT(*) FROM $TABLE_REVIEWS WHERE $KEY_REVIEW_SITE_ID = ?"
        val cursor = db.rawQuery(query, arrayOf(siteId))
        cursor.use {
            if (it.moveToFirst()) {
                val count = it.getInt(1)
                if (count == 0) return Pair(0f, 0)
                val avg = it.getFloat(0)
                // Round to 1 decimal place (e.g., 4.67 -> 4.7)
                val roundedAvg = round(avg * 10) / 10f
                return Pair(roundedAvg, count)
            }
        }
        return Pair(0f, 0)
    }

    fun getReviewsForSite(siteId: String): List<Review> {
        val reviews = mutableListOf<Review>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_REVIEWS,
            null,
            "$KEY_REVIEW_SITE_ID = ?",
            arrayOf(siteId),
            null,
            null,
            "$KEY_REVIEW_ID DESC"
        )

        cursor.use {
            while (it.moveToNext()) {
                val id = it.getString(it.getColumnIndexOrThrow(KEY_REVIEW_ID))
                val sId = it.getString(it.getColumnIndexOrThrow(KEY_REVIEW_SITE_ID))
                val userName = it.getString(it.getColumnIndexOrThrow(KEY_REVIEW_USER_NAME))
                val rating = it.getFloat(it.getColumnIndexOrThrow(KEY_REVIEW_RATING))
                val text = it.getString(it.getColumnIndexOrThrow(KEY_REVIEW_TEXT))
                val date = it.getString(it.getColumnIndexOrThrow(KEY_REVIEW_DATE))

                reviews.add(Review(id, sId, userName, rating, text, date))
            }
        }
        return reviews
    }

    fun addReview(review: Review) {
        val db = writableDatabase
        val cv = ContentValues().apply {
            put(KEY_REVIEW_ID, review.id)
            put(KEY_REVIEW_SITE_ID, review.siteId)
            put(KEY_REVIEW_USER_NAME, review.userName)
            put(KEY_REVIEW_RATING, review.rating)
            put(KEY_REVIEW_TEXT, review.reviewText)
            put(KEY_REVIEW_DATE, review.date)
        }
        db.insert(TABLE_REVIEWS, null, cv)
    }

    fun toggleFavorite(siteId: String, isFavorite: Boolean) {
        val db = writableDatabase
        val cv = ContentValues().apply {
            put(KEY_SITE_IS_FAVORITE, if (isFavorite) 1 else 0)
        }
        db.update(TABLE_SITES, cv, "$KEY_SITE_ID = ?", arrayOf(siteId))
    }
}
