# PowerShell script to commit changes with specific dates

# Navigate to project root (assuming script is run from project root)
# Set-Location -Path "C:\Users\X64 GRAPHICS INC\Documents\KMP-Game Explorer"

# Script to commit changes with custom dates
# Set the author identity
git config user.name "mkaomwakuni"
git config user.email "mkaoibrahim@gmail.com"

Write-Host "Starting commits with custom dates..." -ForegroundColor Cyan

# Check if we're in a Git repository
if (-not (Test-Path .git)) {
    Write-Host "Error: Not in a Git repository root directory." -ForegroundColor Red
    exit 1
}

# Function to commit with a custom date
function Commit-WithDate {
    param (
        [string]$message,
        [string]$date,
        [string[]]$files,
        [switch]$force = $false
    )
    
    Write-Host "Staging files for: $message" -ForegroundColor Yellow
    
    # Check if files exist and add them
    foreach ($file in $files) {
        if (Test-Path $file) {
            Write-Host "  Adding: $file" -ForegroundColor DarkGray
            if ($force) {
                git add -f $file
            } else {
                git add $file
            }
        } else {
            Write-Host "  Warning: File not found - $file" -ForegroundColor Red
        }
    }
    
    # Check if anything was staged
    $status = git status --porcelain
    if (-not $status) {
        Write-Host "  Nothing to commit. Files may be already committed or not tracked." -ForegroundColor Yellow
        return $false
    }
    
    # Commit with the specified date
    $env:GIT_AUTHOR_DATE = $date
    $env:GIT_COMMITTER_DATE = $date
    
    Write-Host "  Committing with date: $date" -ForegroundColor Blue
    $commitOutput = git commit -m $message 2>&1
    $success = $commitOutput -match "file changed|files changed|create mode|Committer:"
     
    # Clear the environment variables
    Remove-Item Env:\GIT_AUTHOR_DATE -ErrorAction SilentlyContinue
    Remove-Item Env:\GIT_COMMITTER_DATE -ErrorAction SilentlyContinue
    
    if ($success) {
        Write-Host "  Commit successful!" -ForegroundColor Green
        return $true
    } else {
        Write-Host "  Commit failed or nothing to commit: $commitOutput" -ForegroundColor Red
        return $false
    }
}

# Navigation components - April 1, 2025
$navigationFiles = @(
    "composeApp/src/commonMain/kotlin/org/sea/rawg/navigation/BottomNavItem.kt",
    "composeApp/src/commonMain/kotlin/org/sea/rawg/navigation/NavGraph.kt",
    "composeApp/src/commonMain/kotlin/org/sea/rawg/navigation/NavigationRoutes.kt",
    "composeApp/src/commonMain/kotlin/org/sea/rawg/App.kt"
)

$navSuccess = Commit-WithDate -message "feat: implement navigation components for cross-platform routing" -date "2025-04-01 12:00:00 +0300" -files $navigationFiles -force

if ($navSuccess) {
    Write-Host "Navigation components committed with date April 1, 2025" -ForegroundColor Green
} else {
    Write-Host "Warning: Could not commit navigation components" -ForegroundColor Yellow
}

# Placeholder screens - April 4, 2025
$placeholderFiles = @(
    "composeApp/src/commonMain/kotlin/org/sea/rawg/ui/screens/CollectionsScreen.kt",
    "composeApp/src/commonMain/kotlin/org/sea/rawg/ui/screens/GenresScreen.kt",
    "composeApp/src/commonMain/kotlin/org/sea/rawg/ui/screens/Homepage.kt",
    "composeApp/src/commonMain/kotlin/org/sea/rawg/ui/screens/MainScreen.kt",
    "composeApp/src/commonMain/kotlin/org/sea/rawg/ui/screens/PublishersScreen.kt",
    "composeApp/src/commonMain/kotlin/org/sea/rawg/ui/screens/SettingsScreen.kt"
)

$screenSuccess = Commit-WithDate -message "feat: add placeholder screens for main content sections" -date "2025-04-04 15:00:00 +0300" -files $placeholderFiles -force

if ($screenSuccess) {
    Write-Host "Placeholder screens committed with date April 4, 2025" -ForegroundColor Green
} else {
    Write-Host "Warning: Could not commit placeholder screens" -ForegroundColor Yellow
}

# Final status message
if ($navSuccess -or $screenSuccess) {
    Write-Host "Commits completed with custom dates." -ForegroundColor Cyan
    Write-Host "Your changes are now ready to be pushed." -ForegroundColor Yellow
    Write-Host "You can push your changes with: git push origin feature-navigation" -ForegroundColor Yellow
} else {
    Write-Host "No commits were made. Files may already be committed or not tracked." -ForegroundColor Red
    Write-Host "Try running 'git status' to check the status of your repository." -ForegroundColor Yellow
}