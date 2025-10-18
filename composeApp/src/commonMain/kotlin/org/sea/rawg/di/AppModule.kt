package org.sea.rawg.di

import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import org.sea.rawg.data.remote.ApiClient
import org.sea.rawg.data.remote.GamesApiService
import org.sea.rawg.data.remote.GamesApiServiceImpl
import org.sea.rawg.data.repository.CollectionRepositoryImpl
import org.sea.rawg.data.repository.RawgRepositoryImpl
import org.sea.rawg.domain.repository.CollectionRepository
import org.sea.rawg.domain.repository.RawgRepository
import org.sea.rawg.domain.usecases.GetCollectionByIdUseCase
import org.sea.rawg.domain.usecases.GetCollectionsUseCase
import org.sea.rawg.domain.usecases.GetGameDetailsUseCase
import org.sea.rawg.domain.usecases.GetGamesForCollectionUseCase
import org.sea.rawg.presentation.viewmodels.GameDetailsViewModel
import org.sea.rawg.ui.viewmodel.CollectionDetailsViewModel
import org.sea.rawg.ui.viewmodel.CollectionsViewModel
import org.sea.rawg.ui.viewmodel.GenresViewModel
import org.sea.rawg.ui.viewmodel.PublishersViewModel

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

        // Collections repository
        single<CollectionRepository> { CollectionRepositoryImpl(get()) }
    }

    /**
     * Use cases
     */
    private val useCaseModule = module {
        // Game detail use case
        factory { GetGameDetailsUseCase(get()) }

        // Collection use cases
        factory { GetCollectionsUseCase(get()) }
        factory { GetCollectionByIdUseCase(get()) }
        factory { GetGamesForCollectionUseCase(get()) }

        // Other use cases would be defined here
    }

    /**
     * ViewModels
     */
    private val viewModelModule = module {
        // Game details view model
        factory { GameDetailsViewModel() }
        // Genres view model
        factory { GenresViewModel(get()) }
        // Publishers view model
        factory { PublishersViewModel(get()) }
        // Collection view models
        factory { CollectionsViewModel(get(), get(), get()) }
    }
}