package com.example.damstocktracker.di

import com.example.damstocktracker.data.csv.CSVParser
import com.example.damstocktracker.data.csv.CompanyListingsParser
import com.example.damstocktracker.data.repository.StockRepositoryImpl
import com.example.damstocktracker.domain.model.CompanyListing
import com.example.damstocktracker.domain.repository.StockRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCompanyListingParser(
        companyListingsParser: CompanyListingsParser
    ): CSVParser<CompanyListing>

    @Binds
    @Singleton
    abstract fun bindStockRepository(
        stockRepositoryImpl: StockRepositoryImpl
    ): StockRepository
}