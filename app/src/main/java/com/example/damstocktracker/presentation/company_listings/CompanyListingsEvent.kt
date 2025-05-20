package com.example.damstocktracker.presentation.company_listings

sealed class CompanyListingsEvent {
    object Refresh: CompanyListingsEvent()
    data class onSearchQueryChange(val query: String): CompanyListingsEvent()
}