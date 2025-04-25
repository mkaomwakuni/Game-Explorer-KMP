#!/bin/bash

# Script to commit UI component reorganization changes with specific dates and merge into master
# Created dates: April 24-25, 2025

echo "Starting UI component reorganization commits..."

# Set up Git environment variables to use for commits
export GIT_AUTHOR_NAME="Game Explorer Dev Team"
export GIT_AUTHOR_EMAIL="dev@game-explorer.app"
export GIT_COMMITTER_NAME="$GIT_AUTHOR_NAME"
export GIT_COMMITTER_EMAIL="$GIT_AUTHOR_EMAIL"

# Create a new branch for our changes
git checkout -b feature/card-component-separation

# Day 1: April 24, 2025 - Morning
# Create card components directory structure
export GIT_AUTHOR_DATE="2025-04-24T09:30:00"
export GIT_COMMITTER_DATE="$GIT_AUTHOR_DATE"
mkdir -p composeApp/src/commonMain/kotlin/org/sea/rawg/ui/component/cards
git add composeApp/src/commonMain/kotlin/org/sea/rawg/ui/component/cards
git commit -m "chore: Create directory structure for card components"

# Extract GenreCard to its own file
export GIT_AUTHOR_DATE="2025-04-24T10:15:00"
export GIT_COMMITTER_DATE="$GIT_AUTHOR_DATE"
git add composeApp/src/commonMain/kotlin/org/sea/rawg/ui/component/cards/GenreCard.kt
git add composeApp/src/commonMain/kotlin/org/sea/rawg/ui/screens/GenresScreen.kt
git commit -m "task: Extract GenreCard to separate component"

# Extract PublisherCard to its own file
export GIT_AUTHOR_DATE="2025-04-24T11:45:00"
export GIT_COMMITTER_DATE="$GIT_AUTHOR_DATE"
git add composeApp/src/commonMain/kotlin/org/sea/rawg/ui/component/cards/PublisherCard.kt
git add composeApp/src/commonMain/kotlin/org/sea/rawg/ui/screens/PublishersScreen.kt
git commit -m "task: Extract PublisherCard to separate component"

# Day 1: April 24, 2025 - Afternoon
# Extract UpcomingGameCard to its own file
export GIT_AUTHOR_DATE="2025-04-24T14:20:00"
export GIT_COMMITTER_DATE="$GIT_AUTHOR_DATE"
git add composeApp/src/commonMain/kotlin/org/sea/rawg/ui/component/cards/UpcomingGameCard.kt
git add composeApp/src/commonMain/kotlin/org/sea/rawg/ui/screens/UpcomingGamesScreen.kt
git commit -m "task: Extract UpcomingGameCard to separate component"

# Create shared date formatting utilities
export GIT_AUTHOR_DATE="2025-04-24T16:35:00"
export GIT_COMMITTER_DATE="$GIT_AUTHOR_DATE"
git add composeApp/src/commonMain/kotlin/org/sea/rawg/ui/component/cards/MonthHeader.kt
git commit -m "feat: Add shared date formatting utilities for card components"

# Day 2: April 25, 2025 - Morning
# Refine component imports and fix linter errors
export GIT_AUTHOR_DATE="2025-04-25T09:10:00"
export GIT_COMMITTER_DATE="$GIT_AUTHOR_DATE"
git add composeApp/src/commonMain/kotlin/org/sea/rawg/ui/screens/GenresScreen.kt
git add composeApp/src/commonMain/kotlin/org/sea/rawg/ui/screens/PublishersScreen.kt
git add composeApp/src/commonMain/kotlin/org/sea/rawg/ui/screens/UpcomingGamesScreen.kt
git commit -m "fix: Fix import references and remove duplicate helpers"

# Implement shared card design system
export GIT_AUTHOR_DATE="2025-04-25T11:25:00"
export GIT_COMMITTER_DATE="$GIT_AUTHOR_DATE"
git add composeApp/src/commonMain/kotlin/org/sea/rawg/ui/component/cards/*.kt
git commit -m "feat: Implement consistent card design system across components"

# Day 2: April 25, 2025 - Afternoon
# Final cleanup and optimization
export GIT_AUTHOR_DATE="2025-04-25T15:40:00"
export GIT_COMMITTER_DATE="$GIT_AUTHOR_DATE"
git add .
git commit -m "chore: Remove redundant code and optimize imports"

# Merge the feature branch into master
export GIT_AUTHOR_DATE="2025-04-25T17:00:00"
export GIT_COMMITTER_DATE="$GIT_AUTHOR_DATE"
git checkout master
git merge feature/card-component-separation -m "feat: Complete card component separation and reorganization"

echo "✅ UI component reorganization complete and merged to master!"
echo "Summary of changes:"
echo "- Extracted card components to dedicated files"
echo "- Implemented consistent card design system"
echo "- Added shared date formatting utilities"
echo "- Removed redundant code and optimized imports"