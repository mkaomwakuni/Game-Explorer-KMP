package org.sea.rawg.features.collections

import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import org.sea.rawg.domain.usecases.GetCollectionByIdUseCase
import org.sea.rawg.domain.usecases.GetCollectionsUseCase
import org.sea.rawg.domain.usecases.GetGamesForCollectionUseCase
import org.sea.rawg.ui.viewmodel.CollectionDetailsViewModel
import org.sea.rawg.ui.viewmodel.CollectionsViewModel

/**
 * Collection feature module - Contains all collection-related functionality
 * 
 * This helps with organizing code by features rather than by technical layers
 */
object CollectionFeature {
    
    /**
     * Get Koin module for the collection feature
     */
    fun getModule(): Module = module {
        // Use cases
        factory { GetCollectionsUseCase(get()) }
        factory { GetCollectionByIdUseCase(get()) }
        factory { GetGamesForCollectionUseCase(get()) }
        
        // ViewModels
        factoryOf(::CollectionsViewModel)
        factoryOf(::CollectionDetailsViewModel)
    }
}