package org.sea.rawg.features.collections

import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import org.sea.rawg.domain.usecases.GetCollectionByIdUseCase
import org.sea.rawg.domain.usecases.GetCollectionsUseCase
import org.sea.rawg.domain.usecases.GetGamesForCollectionUseCase
import org.sea.rawg.ui.viewmodel.CollectionDetailsViewModel
import org.sea.rawg.ui.viewmodel.CollectionsViewModel


object CollectionFeature {
    
    fun getModule(): Module = module {
        
        factory { GetCollectionsUseCase(get()) }
        factory { GetCollectionByIdUseCase(get()) }
        factory { GetGamesForCollectionUseCase(get()) }
        
        
        factoryOf(::CollectionsViewModel)
        factoryOf(::CollectionDetailsViewModel)
    }
}