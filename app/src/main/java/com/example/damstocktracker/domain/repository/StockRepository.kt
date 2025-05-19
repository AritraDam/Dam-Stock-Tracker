package com.example.damstocktracker.domain.repository

import com.example.damstocktracker.domain.model.CompanyListing
import com.example.damstocktracker.utils.Resource
import kotlinx.coroutines.flow.Flow

interface StockRepository {
    suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ) : Flow<Resource<List<CompanyListing>>>
}