package com.example.damstocktracker.presentation.company_info

import com.example.damstocktracker.domain.model.CompanyInfo
import com.example.damstocktracker.domain.model.IntradayInfo

data class CompanyInfoState(
    val stockInfos: List<IntradayInfo> = emptyList(),
    val company: CompanyInfo? = null,
    val isLoading : Boolean = false,
    val error: String? = null
)
