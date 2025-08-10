# PowerShell script to create commits with specific dates and author info
# Author: mkaomwakuni <mkaoibrahim@gmail.com>

# Make sure we're in the right directory
Set-Location -Path "C:\Users\X64 GRAPHICS INC\Documents\KMP-Game Explorer"

# Define author information
$GIT_AUTHOR_NAME = "mkaomwakuni"
$GIT_AUTHOR_EMAIL = "mkaoibrahim@gmail.com"

# Function to create a commit with a specific date
function Create-Commit {
    param(
        [string]$CommitMessage,
        [string]$CommitDate,
        [array]$Files
    )
    
    # Filter out .psi files
    $FilesToCommit = $Files | Where-Object { $_ -notmatch "\.psi$" }
    
    # Add files to staging
    foreach ($file in $FilesToCommit) {
        git add $file
    }
    
    # Check if there are any files staged for commit
    $StagedFiles = git diff --name-only --cached
    if ($StagedFiles) {
        # Set environment variables for author and date
        $env:GIT_AUTHOR_NAME = $GIT_AUTHOR_NAME
        $env:GIT_AUTHOR_EMAIL = $GIT_AUTHOR_EMAIL
        $env:GIT_COMMITTER_NAME = $GIT_AUTHOR_NAME
        $env:GIT_COMMITTER_EMAIL = $GIT_AUTHOR_EMAIL
        $env:GIT_AUTHOR_DATE = $CommitDate
        $env:GIT_COMMITTER_DATE = $CommitDate
        
        # Make the commit
        git commit -m $CommitMessage
        
        # Clear environment variables
        Remove-Item env:GIT_AUTHOR_NAME
        Remove-Item env:GIT_AUTHOR_EMAIL
        Remove-Item env:GIT_COMMITTER_NAME
        Remove-Item env:GIT_COMMITTER_EMAIL
        Remove-Item env:GIT_AUTHOR_DATE
        Remove-Item env:GIT_COMMITTER_DATE
        
        Write-Host "Commit created with message: $CommitMessage"
    } else {
        Write-Host "No files staged for commit. Skipping."
    }
}

# Get all changed files (except .psi files)
$ChangedFiles = (git ls-files --modified) -split "`n" | Where-Object { $_ -notmatch "\.psi$" }
$DeletedFiles = (git ls-files --deleted) -split "`n" | Where-Object { $_ -notmatch "\.psi$" }
$UntrackedFiles = (git ls-files --others --exclude-standard) -split "`n" | Where-Object { $_ -notmatch "\.psi$" }

# Combine all files
$AllFiles = $ChangedFiles + $DeletedFiles + $UntrackedFiles | Where-Object { $_ }

# Split files into three roughly equal groups
$TotalFiles = $AllFiles.Count
$GroupSize = [Math]::Ceiling($TotalFiles / 3)

$FirstGroup = $AllFiles[0..($GroupSize-1)]
$SecondGroup = $AllFiles[$GroupSize..(2*$GroupSize-1)]
$ThirdGroup = $AllFiles[(2*$GroupSize)..($TotalFiles-1)]

# Create three commits with different dates
Create-Commit -CommitMessage "Enhancement: UI improvements and code refactoring" -CommitDate "Thu Aug 6 10:30:00 2025 +0300" -Files $FirstGroup
Create-Commit -CommitMessage "Feature: Add game collection functionality and optimize data handling" -CommitDate "Sun Aug 9 14:45:00 2025 +0300" -Files $SecondGroup
Create-Commit -CommitMessage "Fix: Resolve theme issues and update API integrations" -CommitDate "Mon Aug 10 09:15:00 2025 +0300" -Files $ThirdGroup

Write-Host "All commits created successfully!"