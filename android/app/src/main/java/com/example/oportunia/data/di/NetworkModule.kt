package com.example.oportunia.data.di

import com.example.oportunia.data.mapper.CompanyMapper
import com.example.oportunia.data.mapper.InternshipMapper
import com.example.oportunia.data.remote.api.ApplicationService
import com.example.oportunia.data.remote.api.AreaOfInterestService
import com.example.oportunia.data.remote.api.AuthService
import com.example.oportunia.data.remote.api.CVService
import com.example.oportunia.data.remote.api.CompanyService
import com.example.oportunia.data.remote.api.CountryService
import com.example.oportunia.data.remote.api.GenderService
import com.example.oportunia.data.remote.dto.InternshipDto
import com.example.oportunia.data.remote.interceptor.ResponseInterceptor
import com.example.oportunia.data.remote.api.InternshipService
import com.example.oportunia.data.remote.api.InterviewService
import com.example.oportunia.data.remote.api.SectorService
import com.example.oportunia.data.remote.api.SkillService
import com.example.oportunia.data.remote.api.StudentService
import com.example.oportunia.data.remote.api.UserService
import com.example.oportunia.data.remote.interceptor.AuthInterceptor
import com.example.oportunia.data.remote.serializer.InternshipDeserializer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Dagger Hilt module that provides network-related dependencies for the application.
 *
 * This module is responsible for providing singleton instances of:
 * - Gson for JSON serialization/deserialization
 * - HTTP client configuration with logging
 * - Retrofit service configuration
 * - API service interfaces
 *
 * @property BASE_URL The base URL for the API endpoints
 */

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    // 10.0.2.2 is the host machine when running on the Android emulator
    private const val BASE_URL = "http://10.0.2.2:8080/v1/"
    private const val DATE_FORMAT = "yyyy-MM-dd"

    /**
     * Provides a singleton Gson instance configured with custom type adapters.
     *
     * @return Configured [Gson] instance
     */
    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder()
        .registerTypeAdapter(InternshipDto::class.java, InternshipDeserializer())
        .setDateFormat(DATE_FORMAT)
        .create()

    /**
     * Provides a logging interceptor for HTTP request/response logging.
     *
     * @return Configured [HttpLoggingInterceptor]
     */
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor()
        .apply { level = HttpLoggingInterceptor.Level.BODY }

    /**
     * Provides a configured OkHttpClient with interceptors.
     *
     * @param loggingInterceptor For logging HTTP traffic
     * @param responseInterceptor For handling API responses
     * @return Configured [OkHttpClient]
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        responseInterceptor: ResponseInterceptor,
        authInterceptor: AuthInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(responseInterceptor)
        .addInterceptor(authInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    /**
     * Provides a configured Retrofit instance.
     *
     * @param okHttpClient The HTTP client to use
     * @param gson The Gson instance for JSON conversion
     * @return Configured [Retrofit] instance
     */
    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gson: Gson
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient)
        .build()

    /**
     * Provides the IntershipService implementation.
     *
     * @param retrofit The Retrofit instance
     * @return Implementation of [InternshipService]
     */
    @Provides
    @Singleton
    fun provideInternshipService(retrofit: Retrofit): InternshipService =
        retrofit.create(InternshipService::class.java)

    /**
     * Provides the StudentService implementation.
     *
     * @param retrofit The Retrofit instance
     * @return Implementation of [StudentService]
     */
     @Provides
     @Singleton
     fun provideStudentService(retrofit: Retrofit): StudentService =
        retrofit.create(StudentService::class.java)

    /**
     * Provides the UserService implementation
     *
     * @param retrofit The Retrofit instance
     * @return Implmentation of [UserService]
     */
    @Provides
    @Singleton
    fun provideUserService(retrofit: Retrofit): UserService =
        retrofit.create(UserService::class.java)


    /**
     * Provides the InterviewService implementation.
     *
     * @param retrofit The Retrofit instance
     * @return Implementation of [InterviewService]
     */
    @Provides
    @Singleton
    fun provideInterviewService(retrofit: Retrofit): InterviewService =
        retrofit.create(InterviewService::class.java)

    @Provides
    @Singleton
    fun provideApplicationService(retrofit: Retrofit): ApplicationService =
        retrofit.create(ApplicationService::class.java)

    @Provides
    @Singleton
    fun provideCVService(retrofit: Retrofit): CVService =
        retrofit.create(CVService::class.java)

    @Provides
    @Singleton
    fun provideCompanyService(retrofit: Retrofit): CompanyService =
        retrofit.create(CompanyService::class.java)

    @Provides
    @Singleton
    fun provideAreaOfInterestService(retrofit: Retrofit): AreaOfInterestService =
        retrofit.create(AreaOfInterestService::class.java)

    @Provides
    @Singleton
    fun provideCountryServiceService(retrofit: Retrofit): CountryService =
        retrofit.create(CountryService::class.java)

    @Provides
    @Singleton
    fun provideGenderServiceService(retrofit: Retrofit): GenderService =
        retrofit.create(GenderService::class.java)

    @Provides
    @Singleton
    fun provideSkillServiceService(retrofit: Retrofit): SkillService =
        retrofit.create(SkillService::class.java)

    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)

    @Provides
    @Singleton
    fun provideSectorServiceService(retrofit: Retrofit): SectorService =
        retrofit.create(SectorService::class.java)


}