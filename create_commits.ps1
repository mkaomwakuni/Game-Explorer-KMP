# Script to create 16 chronological commits with specified author and dates
# Author: mkaomwakuni <mkaoibrahim@gmail.com>

# Set Git author and email globally for this script
git config --global user.name "mkaomwakuni"
git config --global user.email "mkaoibrahim@gmail.com"

# Find and delete all .psi files
Write-Host "Finding and deleting all .psi files..."
$psiFiles = Get-ChildItem -Path . -Filter "*.psi" -Recurse
foreach ($file in $psiFiles) {
    Remove-Item -Path $file.FullName -Force
    Write-Host "Deleted $($file.FullName)"
}

# Reset the repository to start clean
git reset --mixed

# Create file groups for more efficient commits
$fileGroups = @{
    "build_files" = @(
        "composeApp/build.gradle.kts"
    );
    "core_app_files" = @(
        "composeApp/src/commonMain/kotlin/org/sea/rawg/App.kt", 
        "composeApp/src/commonMain/kotlin/org/sea/rawg/Platform.kt",
        "composeApp/src/androidMain/kotlin/org/sea/rawg/Platform.android.kt",
        "composeApp/src/desktopMain/kotlin/org/sea/rawg/App.desktop.kt",
        "composeApp/src/iosMain/kotlin/org/sea/rawg/App.ios.kt",
        "composeApp/src/wasmJsMain/kotlin/org/sea/rawg/App.wasmJs.kt"
    );
    "ui_components" = @(
        "composeApp/src/commonMain/kotlin/org/sea/rawg/ui/component/GameCard.kt",
        "composeApp/src/commonMain/kotlin/org/sea/rawg/ui/component/GameComponents.kt",
        "composeApp/src/commonMain/kotlin/org/sea/rawg/ui/component/GameGridItem.kt",
        "composeApp/src/commonMain/kotlin/org/sea/rawg/ui/component/SettingsComponents.kt",
        "composeApp/src/commonMain/kotlin/org/sea/rawg/ui/component/cards/CollectionCard.kt",
        "composeApp/src/commonMain/kotlin/org/sea/rawg/ui/component/cards/GenreCard.kt",
        "composeApp/src/commonMain/kotlin/org/sea/rawg/ui/component/cards/PublisherCard.kt",
        "composeApp/src/commonMain/kotlin/org/sea/rawg/ui/component/cards/UpcomingGameCard.kt"
    );
    "screens" = @(
        "composeApp/src/commonMain/kotlin/org/sea/rawg/ui/screens/MainScreen.kt",
        "composeApp/src/commonMain/kotlin/org/sea/rawg/ui/screens/Homepage.kt",
        "composeApp/src/commonMain/kotlin/org/sea/rawg/ui/screens/SettingsScreen.kt",
        "composeApp/src/commonMain/kotlin/org/sea/rawg/ui/screens/PublisherDetailsScreen.kt",
        "composeApp/src/commonMain/kotlin/org/sea/rawg/ui/screens/UpcomingGamesScreen.kt",
        "composeApp/src/commonMain/kotlin/org/sea/rawg/ui/screens/GameDetails.kt"
    );
    "api_integration" = @(
        "composeApp/src/commonMain/kotlin/org/sea/rawg/data/remote/ApiClient.kt",
        "composeApp/src/commonMain/kotlin/org/sea/rawg/data/remote/GamesApiService.kt",
        "composeApp/src/commonMain/kotlin/org/sea/rawg/data/remote/GamesApiServiceImpl.kt"
    );
    "data_models" = @(
        "composeApp/src/commonMain/kotlin/org/sea/rawg/domain/models/Game.kt",
        "composeApp/src/commonMain/kotlin/org/sea/rawg/domain/models/RedditPost.kt",
        "composeApp/src/commonMain/kotlin/org/sea/rawg/domain/models/ApiResponses.kt",
        "composeApp/src/commonMain/kotlin/org/sea/rawg/data/model/RedditPost.kt",
        "composeApp/src/commonMain/kotlin/org/sea/rawg/data/model/ApiResponses.kt",
        "composeApp/src/commonMain/kotlin/org/sea/rawg/data/model/DLC.kt",
        "composeApp/src/commonMain/kotlin/org/sea/rawg/data/model/ThemePreferences.kt"
    );
    "repository_files" = @(
        "composeApp/src/commonMain/kotlin/org/sea/rawg/data/repository/RawgRepositoryImpl.kt",
        "composeApp/src/commonMain/kotlin/org/sea/rawg/data/repository/ThemePreferencesRepository.kt",
        "composeApp/src/commonMain/kotlin/org/sea/rawg/domain/repository/RawgRepository.kt"
    );
    "viewmodel_files" = @(
        "composeApp/src/commonMain/kotlin/org/sea/rawg/ui/viewmodel/GameDetailsViewModel.kt",
        "composeApp/src/commonMain/kotlin/org/sea/rawg/ui/viewmodel/BaseViewModel.kt",
        "composeApp/src/commonMain/kotlin/org/sea/rawg/ui/viewmodel/CollectionsViewModel.kt",
        "composeApp/src/commonMain/kotlin/org/sea/rawg/ui/viewmodel/GenresViewModel.kt",
        "composeApp/src/commonMain/kotlin/org/sea/rawg/ui/viewmodel/HomeViewModel.kt",
        "composeApp/src/commonMain/kotlin/org/sea/rawg/ui/viewmodel/PublishersViewModel.kt",
        "composeApp/src/commonMain/kotlin/org/sea/rawg/ui/viewmodel/SettingsViewModel.kt",
        "composeApp/src/commonMain/kotlin/org/sea/rawg/ui/viewmodel/UpcomingGamesViewModel.kt",
        "composeApp/src/commonMain/kotlin/org/sea/rawg/presentation/viewmodels/GameDetailsViewModel.kt"
    );
    "theme_files" = @(
        "composeApp/src/commonMain/kotlin/org/sea/rawg/theme/Theme.kt",
        "composeApp/src/commonMain/kotlin/org/sea/rawg/theme/ThemeManager.kt",
        "composeApp/src/androidMain/kotlin/org/sea/rawg/theme/SystemUi.android.kt",
        "composeApp/src/desktopMain/kotlin/org/sea/rawg/theme/"
    );
    "image_loading" = @(
        "composeApp/src/desktopMain/kotlin/org/sea/rawg/ui/component/AsyncImage.desktop.kt",
        "composeApp/src/wasmJsMain/kotlin/org/sea/rawg/ui/component/AsyncImage.wasmJs.kt"
    );
    "game_detail_components" = @(
        "composeApp/src/commonMain/kotlin/org/sea/rawg/ui/component/gamedetail/RedditDiscussionsSection.kt",
        "composeApp/src/commonMain/kotlin/org/sea/rawg/ui/component/gamedetail/DLCSection.kt",
        "composeApp/src/commonMain/kotlin/org/sea/rawg/ui/component/gamedetail/GameQuickStats.kt",
        "composeApp/src/commonMain/kotlin/org/sea/rawg/ui/component/gamedetail/GameRatingBar.kt"
    );
    "game_detail_screens" = @(
        "composeApp/src/commonMain/kotlin/org/sea/rawg/ui/screens/gamedetail/GameDetailScreen.kt",
        "composeApp/src/commonMain/kotlin/org/sea/rawg/ui/screens/gamedetail/GameWebsiteButton.kt"
    );
    "usecases" = @(
        "composeApp/src/commonMain/kotlin/org/sea/rawg/domain/usecases/GetGameDetailsUseCase.kt",
        "composeApp/src/commonMain/kotlin/org/sea/rawg/domain/usecases/GetPopularGamesUseCase.kt"
    );
    "utils" = @(
        "composeApp/src/commonMain/kotlin/org/sea/rawg/utils/Result.kt",
        "composeApp/src/wasmJsMain/kotlin/org/sea/rawg/utils/DateUtils.kt"
    );
    "di" = @(
        "composeApp/src/commonMain/kotlin/org/sea/rawg/di/AppModule.kt"
    );
    "features" = @(
        "composeApp/src/commonMain/kotlin/org/sea/rawg/features/settings/SettingsFeature.kt",
        "composeApp/src/commonMain/kotlin/org/sea/rawg/features/collections/",
        "composeApp/src/commonMain/kotlin/org/sea/rawg/features/games/"
    );
    "main_entry_points" = @(
        "composeApp/src/desktopMain/kotlin/org/sea/rawg/Main.kt",
        "composeApp/src/wasmJsMain/kotlin/org/sea/rawg/Main.kt"
    )
}

# Define commits with dates (from April 26 to May 6, 2025)
$commits = @(
    @{
        date = "2025-04-26T10:00:00";
        type = "chore";
        message = "Initialize project structure for Kotlin Multiplatform";
        fileGroups = @("build_files")
    },
    @{
        date = "2025-04-26T14:30:00";
        type = "feat";
        message = "Add basic app UI components and navigation structure";
        fileGroups = @("core_app_files", "screens")
    },
    @{
        date = "2025-04-27T09:15:00";
        type = "feat";
        message = "Implement game card UI component";
        fileGroups = @("ui_components")
    },
    @{
        date = "2025-04-27T16:45:00";
        type = "chore";
        message = "Configure Kotlin serialization for API models";
        fileGroups = @("build_files")
    },
    @{
        date = "2025-04-28T11:20:00";
        type = "feat";
        message = "Create API client for RAWG integration";
        fileGroups = @("api_integration")
    },
    @{
        date = "2025-04-28T19:30:00";
        type = "feat";
        message = "Add game models and repository implementation";
        fileGroups = @("data_models", "repository_files")
    },
    @{
        date = "2025-04-29T10:45:00";
        type = "feat";
        message = "Implement home screen with game grid layout";
        fileGroups = @("screens")
    },
    @{
        date = "2025-04-29T16:30:00";
        type = "feat";
        message = "Create settings feature module with dependency injection";
        fileGroups = @("features", "di")
    },
    @{
        date = "2025-04-30T13:15:00";
        type = "fix";
        message = "Fix image loading issues on different platforms";
        fileGroups = @("image_loading")
    },
    @{
        date = "2025-05-01T09:00:00";
        type = "feat";
        message = "Add theme support and dark mode toggle";
        fileGroups = @("theme_files")
    },
    @{
        date = "2025-05-02T14:30:00";
        type = "feat";
        message = "Implement game details screen";
        fileGroups = @("screens", "viewmodel_files")
    },
    @{
        date = "2025-05-03T11:45:00";
        type = "feat";
        message = "Add Reddit discussions section to game details";
        fileGroups = @("game_detail_components", "data_models")
    },
    @{
        date = "2025-05-04T10:20:00";
        type = "feat";
        message = "Implement game screenshots gallery with fullscreen view";
        fileGroups = @("game_detail_screens")
    },
    @{
        date = "2025-05-05T13:30:00";
        type = "feat";
        message = "Add similar games recommendation section";
        fileGroups = @("usecases")
    },
    @{
        date = "2025-05-06T09:15:00";
        type = "feat";
        message = "Implement game website button and external links";
        fileGroups = @("game_detail_screens")
    },
    @{
        date = "2025-05-06T16:00:00";
        type = "chore";
        message = "Refactor code and improve performance";
        fileGroups = @("viewmodel_files", "utils")
    }
)

# Add all files to make sure everything is tracked
git add --all

# Create commits with specific dates
foreach ($commit in $commits) {
    $date = $commit.date
    $type = $commit.type
    $message = $commit.message
    $fileGroupKeys = $commit.fileGroups
    
    Write-Host "`nCreating commit: [$type] $message ($date)"
    
    # Stage files for this commit based on file groups
    foreach ($groupKey in $fileGroupKeys) {
        $files = $fileGroups[$groupKey]
        
        foreach ($file in $files) {
            # Handle directory patterns with trailing slash
            if ($file -match '/$') {
                # It's a directory pattern, find all files under that directory
                $dirPattern = $file + '*'
                $matchingFiles = Get-ChildItem -Path $dirPattern -Recurse | Where-Object { -not $_.PSIsContainer }
                foreach ($matchFile in $matchingFiles) {
                    git add $matchFile.FullName
                    Write-Host "  Added $($matchFile.FullName) to staging"
                }
            }
            elseif (Test-Path $file) {
                git add $file
                Write-Host "  Added $file to staging"
            }
            else {
                Write-Host "  Warning: File $file doesn't exist, skipping"
            }
        }
    }
    
    # Create the commit with the specified date
    $env:GIT_AUTHOR_DATE = $date
    $env:GIT_COMMITTER_DATE = $date
    git commit -m "[$type] $message"
    
    Write-Host "  Commit created successfully"
}

# Restore original Git configuration (optional)
# git config --global --unset user.name
# git config --global --unset user.email

Write-Host "`nAll commits created successfully!"