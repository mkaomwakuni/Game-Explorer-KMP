package org.sea.rawg.di
import io.ktor.client.HttpClient
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import org.sea.rawg.data.remote.ApiClient
import org.sea.rawg.data.remote.GamesApiService
import org.sea.rawg.data.remote.GamesApiServiceImpl
import org.sea.rawg.data.repository.CollectionRepositoryImpl
import org.sea.rawg.data.repository.RawgRepositoryImpl
import org.sea.rawg.data.repository.ThemePreferencesRepository
import org.sea.rawg.domain.repository.CollectionRepository
import org.sea.rawg.domain.repository.RawgRepository
import org.sea.rawg.domain.usecases.GetGameDetailsUseCase
import org.sea.rawg.domain.usecases.GetPopularGamesUseCase
import org.sea.rawg.features.games.GameFeature
import org.sea.rawg.features.collections.CollectionFeature
import org.sea.rawg.features.settings.SettingsFeature
import org.sea.rawg.ui.viewmodel.GameDetailsViewModel
import org.sea.rawg.ui.viewmodel.GenresViewModel
import org.sea.rawg.ui.viewmodel.HomeViewModel
import org.sea.rawg.ui.viewmodel.PublishersViewModel
import org.sea.rawg.ui.viewmodel.SettingsViewModel
import org.sea.rawg.ui.viewmodel.UpcomingGamesViewModel
import org.sea.rawg.ui.viewmodel.SearchViewModel

object AppModule {

    fun init() {
        startKoin {
            modules(
                networkModule,
                repositoryModule,
                useCaseModule,
                viewModelModule,
                preferencesModule,
                GameFeature.getModule(),
                CollectionFeature.getModule(),
                SettingsFeature.getModule()
            )
        }
    }

    private val networkModule = module {
        single<HttpClient> { ApiClient.client }
        single<GamesApiService> { GamesApiServiceImpl(get()) }
    }

    private val repositoryModule = module {
        single<RawgRepository> { RawgRepositoryImpl(get()) }
        single<CollectionRepository> { CollectionRepositoryImpl(get()) }
    }
    
    private val useCaseModule = module {
        factoryOf(::GetGameDetailsUseCase)
        factoryOf(::GetPopularGamesUseCase)
    }

    private val viewModelModule = module {
        factory { HomeViewModel(get(), get()) }
        factoryOf(::GameDetailsViewModel)
        factoryOf(::GenresViewModel)
        factoryOf(::PublishersViewModel)
        factoryOf(::UpcomingGamesViewModel)
        factoryOf(::SettingsViewModel)
        factoryOf(::SearchViewModel)
    }
    
    private val preferencesModule = module {
        single { ThemePreferencesRepository() }
    }
}