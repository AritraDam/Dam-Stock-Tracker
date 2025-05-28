package com.example.damstocktracker.domain.repository

import com.example.damstocktracker.domain.model.CompanyInfo
import com.example.damstocktracker.domain.model.CompanyListing
import com.example.damstocktracker.domain.model.IntradayInfo
import com.example.damstocktracker.utils.Resource
import kotlinx.coroutines.flow.Flow

interface StockRepository {
    suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ) : Flow<Resource<List<CompanyListing>>>

    suspend fun getIntradayInfo(
        symbol: String
    ) : Resource<List<IntradayInfo>>

    suspend fun getCompanyInfo(
        symbol: String
    ) : Resource<CompanyInfo>
}