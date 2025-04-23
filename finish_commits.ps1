# Script to commit remaining changes and merge to master

# Function to commit with a specific date
function Commit-WithDate {
    param (
        [string]$message,
        [string]$date
    )
    
    Write-Host "Creating commit: $message with date: $date" -ForegroundColor Green
    
    # Set environment variables for commit date
    $env:GIT_AUTHOR_DATE = $date
    $env:GIT_COMMITTER_DATE = $date
    
    # Commit all changes
    git add .
    git commit -m "$message"
    
    # Clear environment variables
    Remove-Item Env:\GIT_AUTHOR_DATE -ErrorAction SilentlyContinue
    Remove-Item Env:\GIT_COMMITTER_DATE -ErrorAction SilentlyContinue
}

# Get current branch
$currentBranch = git rev-parse --abbrev-ref HEAD
Write-Host "Current branch: $currentBranch" -ForegroundColor Yellow

# Commit all remaining changes
Write-Host "`nCommitting all remaining changes..." -ForegroundColor Cyan
Commit-WithDate "Complete Genres feature with GenreDetailsScreen and navigation fixes" "2025-04-23T17:30:00"

# Now try to merge to master
Write-Host "`nMerging to master branch..." -ForegroundColor Cyan

# Stash any remaining changes (just in case)
git stash

# Switch to master branch
git checkout master

# Set environment variables for merge date
$env:GIT_AUTHOR_DATE = "2025-04-23T18:00:00"
$env:GIT_COMMITTER_DATE = "2025-04-23T18:00:00"

# Merge the feature branch with a merge commit
git merge --no-ff $currentBranch -m "Merge Genres feature with Koin initialization fixes"

# Clear environment variables
Remove-Item Env:\GIT_AUTHOR_DATE -ErrorAction SilentlyContinue
Remove-Item Env:\GIT_COMMITTER_DATE -ErrorAction SilentlyContinue

Write-Host "`nAll changes committed and merged to master!" -ForegroundColor Green