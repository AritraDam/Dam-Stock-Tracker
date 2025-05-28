package com.example.damstocktracker.data.repository

import com.example.damstocktracker.data.csv.CSVParser
import com.example.damstocktracker.data.csv.CompanyListingsParser
import com.example.damstocktracker.data.csv.IntradayInfoParser
import com.example.damstocktracker.data.local.StockDatabase
import com.example.damstocktracker.data.mapper.toCompanyInfo
import com.example.damstocktracker.data.mapper.toCompanyListing
import com.example.damstocktracker.data.mapper.toCompanyListingEntity
import com.example.damstocktracker.data.remote.StockApi
import com.example.damstocktracker.domain.model.CompanyInfo
import com.example.damstocktracker.domain.model.CompanyListing
import com.example.damstocktracker.domain.model.IntradayInfo
import com.example.damstocktracker.domain.repository.StockRepository
import com.example.damstocktracker.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepositoryImpl @Inject constructor(
    private val api: StockApi,
    private val db: StockDatabase,
    private val companyListingsParser: CSVParser<CompanyListing>,
    private val intradayInfoParser: CSVParser<IntradayInfo>
) : StockRepository {
    private val dao = db.dao
    override suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>> {
        return flow {
            emit(Resource.Loading(true))
            val localListings = dao.searchCompanyListing(query)
            emit(Resource.Success(
                data = localListings.map { it.toCompanyListing() }
            ))
            val isDbEmpty = localListings.isEmpty() && query.isBlank()
            val shouldJustLoadFromCache = !isDbEmpty && !fetchFromRemote
            if(shouldJustLoadFromCache) {
                emit(Resource.Loading(false))
                return@flow
            }
            val remoteListings = try {
                val response = api.getListings()
                companyListingsParser.parse(response.byteStream())
            }catch (e: IOException){
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                null
            }catch (e: HttpException){
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                null
            }
            remoteListings?.let { listings ->
                dao.clearCompanyListings()
                dao.insertCompanyListings(
                    listings.map {
                        it.toCompanyListingEntity()
                    }
                )
                emit(Resource.Success(
                    data = dao
                        .searchCompanyListing("")
                        .map { it.toCompanyListing() }
                ))
                emit(Resource.Loading(false))
            }
        }
    }

    override suspend fun getIntradayInfo(symbol: String): Resource<List<IntradayInfo>> {
        return try {
            val response = api.getIntradayInfo(symbol)
            val result = intradayInfoParser.parse(response.byteStream())
            Resource.Success(result)
        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error(
                "Couldn't load data : IOException"
            )
        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error(
                "Couldn't load data: HttpsException"
            )
        }
    }

    override suspend fun getCompanyInfo(symbol: String): Resource<CompanyInfo> {
        return try {
            val response = api.getCompanyInfo(symbol)
            Resource.Success(response.toCompanyInfo())
        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error(
                "Couldn't load data : IOException"
            )
        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error(
                "Couldn't load data: HttpsException"
            )
        }
    }
}