# Check if our modified files exist

Write-Host "Checking for modified files..." -ForegroundColor Cyan

# Array of files to check
$files = @(
    "composeApp\src\androidMain\kotlin\org\sea\rawg\RawgApplication.kt",
    "composeApp\src\androidMain\AndroidManifest.xml",
    "composeApp\src\iosMain\kotlin\org\sea\rawg\MainViewController.kt",
    "composeApp\src\desktopMain\kotlin\org\sea\rawg\Main.kt",
    "composeApp\src\jvmMain\kotlin\org\sea\rawg\main.kt",
    "composeApp\src\wasmJsMain\kotlin\org\sea\rawg\Main.kt"
)

# Check each file
foreach ($file in $files) {
    if (Test-Path $file) {
        Write-Host "$file exists" -ForegroundColor Green
    } else {
        Write-Host "$file does not exist" -ForegroundColor Red
    }
}

# Get list of files that were staged
Write-Host "`nChecking Git status..." -ForegroundColor Cyan
git status