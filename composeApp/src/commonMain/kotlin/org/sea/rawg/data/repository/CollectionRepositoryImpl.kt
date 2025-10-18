package org.sea.rawg.data.repository

import org.sea.rawg.data.remote.GamesApiService
import org.sea.rawg.domain.models.Collection
import org.sea.rawg.domain.models.CollectionQueryFilter
import org.sea.rawg.domain.models.Game
import org.sea.rawg.domain.models.PagedResponse
import org.sea.rawg.domain.repository.CollectionRepository

class CollectionRepositoryImpl(
    private val gamesApiService: GamesApiService
) : CollectionRepository {

    // Predefined collections
    private val predefinedCollections = listOf(
        Collection(
            id = 1,
            name = "Top Rated Games",
            description = "The highest rated games of all time",
            imageUrl = "https://media.rawg.io/media/games/618/618c2031a07bbff6b4f611f10b6bcdbc.jpg",
            gameType = "Top Rated",
            queryFilter = CollectionQueryFilter(
                ordering = "-rating"
            )
        ),
        Collection(
            id = 2,
            name = "Popular RPGs",
            description = "The most popular role-playing games",
            imageUrl = "https://media.rawg.io/media/games/da1/da1b267764d77221f07a4386b6548e5a.jpg",
            gameType = "RPG",
            queryFilter = CollectionQueryFilter(
                genres = "5", // RPG genre ID
                ordering = "-added"
            )
        ),
        Collection(
            id = 3,
            name = "Indie Gems",
            description = "Outstanding independent games",
            imageUrl = "https://media.rawg.io/media/games/713/713269608dc8f2f40f5a670a14b2de94.jpg",
            gameType = "Indie",
            queryFilter = CollectionQueryFilter(
                tags = "indie",
                ordering = "-rating"
            )
        ),
        Collection(
            id = 4,
            name = "Action Adventures",
            description = "Exciting action-adventure games",
            imageUrl = "https://media.rawg.io/media/games/b45/b45575f34285f2c4479c9a5f719d972e.jpg",
            gameType = "Action",
            queryFilter = CollectionQueryFilter(
                genres = "3,4", // Action and Adventure genre IDs
                ordering = "-added"
            )
        ),
        Collection(
            id = 5,
            name = "Best Strategy Games",
            description = "Top-rated strategy games",
            imageUrl = "https://media.rawg.io/media/games/0bd/0bd5646a3d8ee0ac3314bced91ea306d.jpg",
            gameType = "Strategy",
            queryFilter = CollectionQueryFilter(
                genres = "10,14", // Strategy and Simulation genre IDs
                ordering = "-rating"
            )
        ),
        Collection(
            id = 6,
            name = "Recent Releases",
            description = "Games released in the last 3 months",
            imageUrl = "https://media.rawg.io/media/games/f87/f87457e8347484033cb34cde6101d08d.jpg",
            gameType = "Recent",
            queryFilter = CollectionQueryFilter(
                ordering = "-released",
                dates = "2023-01-01,2023-12-31"
            )
        ),
        Collection(
            id = 7,
            name = "PlayStation Exclusives",
            description = "Games exclusive to PlayStation platforms",
            imageUrl = "https://media.rawg.io/media/games/618/618c2031a07bbff6b4f611f10b6bcdbc.jpg",
            gameType = "PlayStation",
            queryFilter = CollectionQueryFilter(
                platforms = "187,18,16", // PS5, PS4, PS3
                ordering = "-added"
            )
        ),
        Collection(
            id = 8,
            name = "Xbox Games",
            description = "Top games for Xbox platforms",
            imageUrl = "https://media.rawg.io/media/games/4a0/4a0a1316102366260e6f38fd2a9cfdce.jpg",
            gameType = "Xbox",
            queryFilter = CollectionQueryFilter(
                platforms = "186,1,14", // Xbox Series S/X, Xbox One, Xbox 360
                ordering = "-added"
            )
        ),
        Collection(
            id = 9,
            name = "Nintendo Favorites",
            description = "Popular games for Nintendo platforms",
            imageUrl = "https://media.rawg.io/media/games/713/713269608dc8f2f40f5a670a14b2de94.jpg",
            gameType = "Nintendo",
            queryFilter = CollectionQueryFilter(
                platforms = "7,8,9,13,83", // Nintendo platforms
                ordering = "-added"
            )
        )
    )

    override suspend fun getCollections(): List<Collection> {
        return predefinedCollections
    }

    override suspend fun getCollectionById(collectionId: Int): Collection? {
        return predefinedCollections.find { it.id == collectionId }
    }

    override suspend fun getGamesForCollection(
        collectionId: Int,
        page: Int,
        pageSize: Int
    ): PagedResponse<Game> {
        val collection = getCollectionById(collectionId)
            ?: throw IllegalArgumentException("Collection not found for ID: $collectionId")

        val filter = collection.queryFilter

        return gamesApiService.getGames(
            page = page,
            pageSize = pageSize,
            ordering = filter.ordering,
            dates = filter.dates,
            platforms = filter.platforms,
            developers = filter.developers,
            publishers = filter.publishers,
            genres = filter.genres,
            tags = filter.tags,
            search = null,
            search_exact = null,
            search_precise = null
        )
    }
}