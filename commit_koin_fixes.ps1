# Script to commit Koin initialization fixes with specified dates in April 2025
# and merge to master branch

# Function to commit with a specific date
function Commit-WithDate {
    param (
        [string]$message,
        [string]$date,
        [string[]]$files
    )
    
    Write-Host "Creating commit: $message with date: $date" -ForegroundColor Green
    
    # Add specified files
    foreach ($file in $files) {
        git add "$file"
    }
    
    # Set environment variables for commit date
    $env:GIT_AUTHOR_DATE = $date
    $env:GIT_COMMITTER_DATE = $date
    
    # Commit with message
    git commit -m "$message"
    
    # Clear environment variables
    Remove-Item Env:\GIT_AUTHOR_DATE -ErrorAction SilentlyContinue
    Remove-Item Env:\GIT_COMMITTER_DATE -ErrorAction SilentlyContinue
}

# Start of main script
Write-Host "Starting Koin fixes commit process with dated commits..." -ForegroundColor Cyan

# Use the current branch (feature--genre)
$currentBranch = git rev-parse --abbrev-ref HEAD
Write-Host "Using current branch: $currentBranch" -ForegroundColor Yellow

# Commit 1: Android Application class for Koin initialization
Write-Host "`nPreparing Commit 1: Android Application class for Koin initialization" -ForegroundColor Yellow
$files1 = @(
    "composeApp/src/androidMain/kotlin/org/sea/rawg/RawgApplication.kt",
    "composeApp/src/commonMain/kotlin/org/sea/rawg/di/AppModule.kt"
)
Commit-WithDate "Add Android Application class for Koin initialization" "2025-04-20T10:30:00" $files1

# Commit 2: Update AndroidManifest.xml
Write-Host "`nPreparing Commit 2: Update AndroidManifest.xml" -ForegroundColor Yellow
$files2 = @(
    "composeApp/src/androidMain/AndroidManifest.xml",
    "composeApp/src/androidMain/kotlin/org/sea/rawg/MainActivity.kt"
)
Commit-WithDate "Update AndroidManifest to use RawgApplication class" "2025-04-21T14:45:00" $files2

# Commit 3: iOS and Desktop platforms
Write-Host "`nPreparing Commit 3: Koin initialization for iOS and Desktop" -ForegroundColor Yellow
$files3 = @(
    "composeApp/src/iosMain/kotlin/org/sea/rawg/MainViewController.kt",
    "composeApp/src/desktopMain/kotlin/org/sea/rawg/Main.kt"
)
Commit-WithDate "Add Koin initialization to iOS and Desktop platforms" "2025-04-22T09:15:00" $files3

# Commit 4: JVM and WASM platforms
Write-Host "`nPreparing Commit 4: Koin initialization for JVM and WASM" -ForegroundColor Yellow
$files4 = @(
    "composeApp/src/jvmMain/kotlin/org/sea/rawg/main.kt",
    "composeApp/src/wasmJsMain/kotlin/org/sea/rawg/Main.kt"
)
Commit-WithDate "Add Koin initialization to JVM and WASM JS platforms" "2025-04-23T16:20:00" $files4

# Merge to master with specified date
Write-Host "`nMerging to master branch..." -ForegroundColor Cyan

# Switch to master branch
git checkout master

# Set environment variables for merge date
$env:GIT_AUTHOR_DATE = "2025-04-23T18:00:00"
$env:GIT_COMMITTER_DATE = "2025-04-23T18:00:00"

# Merge the feature branch with a merge commit
git merge --no-ff $currentBranch -m "Merge Koin initialization fixes for Genres feature"

# Clear environment variables
Remove-Item Env:\GIT_AUTHOR_DATE -ErrorAction SilentlyContinue
Remove-Item Env:\GIT_COMMITTER_DATE -ErrorAction SilentlyContinue

Write-Host "`nCommit process completed successfully!" -ForegroundColor Green