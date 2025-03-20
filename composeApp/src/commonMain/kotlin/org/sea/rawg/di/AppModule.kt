package org.sea.rawg.di

import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.sea.rawg.data.remote.ApiClient
import org.sea.rawg.data.remote.GamesApiService
import org.sea.rawg.data.remote.GamesApiServiceImpl
import org.sea.rawg.data.repository.RawgRepositoryImpl
import org.sea.rawg.domain.repository.RawgRepository
import org.sea.rawg.domain.usecases.GetGameDetailsUseCase
import org.sea.rawg.presentation.viewmodels.GameDetailsViewModel

/**
 * Dependency injection module for the app
 * Using Koin for KMP-compatible DI
 */
object AppModule {

    /**
     * Initialize dependencies
     */
    fun init() {
        // Initialize Koin
        startKoin {
            modules(
                networkModule,
                repositoryModule,
                useCaseModule,
                viewModelModule
            )
        }
    }

    /**
     * Network related dependencies
     */
    private val networkModule = module {
        // HTTP Client
        single { ApiClient.client }

        // API Service
        single<GamesApiService> { GamesApiServiceImpl(get()) }
    }

    /**
     * Repository dependencies
     */
    private val repositoryModule = module {
        // Game repository
        single<RawgRepository> { RawgRepositoryImpl(get()) }
    }

    /**
     * Use cases
     */
    private val useCaseModule = module {
        // Game detail use case
        factory { GetGameDetailsUseCase(get()) }

        // Other use cases would be defined here
    }

    /**
     * ViewModels
     */
    private val viewModelModule = module {
        // Game details view model
        factory { GameDetailsViewModel() }

        // Other view models would be defined here
    }
}