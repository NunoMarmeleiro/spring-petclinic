# Define your input file here
$inputFile = "nonmodular-load-10vus-raw.csv"
$outputFile = $inputFile -replace "-raw\.csv$", "-post-treated.csv"

# Read header
$header = Get-Content $inputFile -First 1
$header | Out-File $outputFile -Encoding UTF8

# Filter and append matching rows
Get-Content $inputFile | Select-String "post_owner_duration" | Out-File -Append $outputFile -Encoding UTF8

Write-Host "✅ Done: Created $outputFile"
