$headers = @{"Content-Type" = "application/json"}

# Get admin token
$adminBody = '{"username":"admin","password":"admin"}'
$adminResponse = Invoke-RestMethod -Uri "http://localhost:8080/authenticate" -Method Post -Headers $headers -Body $adminBody
$adminToken = $adminResponse.token

$adminAuthHeaders = @{
    "Content-Type" = "application/json"
    "Authorization" = "Bearer $adminToken"
}

Write-Host "Testing CRUD operations with admin token..."

# Test CREATE operation
Write-Host "`n1. Testing CREATE operation:"
$createBody = '{"countryCode":"DE","cost":6.50}'
try {
    $createResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/v1/payment-cards-cost/create" -Method Post -Headers $adminAuthHeaders -Body $createBody
    Write-Host "Create successful:"
    Write-Host ($createResponse | ConvertTo-Json -Depth 10)
} catch {
    Write-Host "Create failed: $($_.Exception.Response.StatusCode)"
    $streamReader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
    $errorResponse = $streamReader.ReadToEnd()
    Write-Host $errorResponse
}

# Test invalid CREATE operation
Write-Host "`nTesting CREATE operation with invalid data:"
$invalidCreateBody = '{"countryCode":"INVALID","cost":-5}'
try {
    $invalidCreateResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/v1/payment-cards-cost/create" -Method Post -Headers $adminAuthHeaders -Body $invalidCreateBody
} catch {
    Write-Host "Expected error for invalid data: $($_.Exception.Response.StatusCode)"
}

# Test READ operations
Write-Host "`n2. Testing READ by ID operation:"
try {
    $readResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/v1/payment-cards-cost/1" -Method Get -Headers $adminAuthHeaders
    Write-Host "Read by ID successful:"
    Write-Host ($readResponse | ConvertTo-Json -Depth 10)
} catch {
    Write-Host "Read by ID failed: $($_.Exception.Response.StatusCode)"
    $streamReader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
    $errorResponse = $streamReader.ReadToEnd()
    Write-Host $errorResponse
}

# Test invalid READ operation
Write-Host "`nTesting READ operation for non-existent ID:"
try {
    $invalidReadResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/v1/payment-cards-cost/9999" -Method Get -Headers $adminAuthHeaders
} catch {
    Write-Host "Expected error for non-existent ID: $($_.Exception.Response.StatusCode)"
}

# Test UPDATE operation
Write-Host "`n3. Testing UPDATE operation:"
$updateBody = '{"cost":7.00}'
try {
    $updateResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/v1/payment-cards-cost/1" -Method Put -Headers $adminAuthHeaders -Body $updateBody
    Write-Host "Update successful:"
    Write-Host ($updateResponse | ConvertTo-Json -Depth 10)
} catch {
    Write-Host "Update failed: $($_.Exception.Response.StatusCode)"
    $streamReader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
    $errorResponse = $streamReader.ReadToEnd()
    Write-Host $errorResponse
}

# Test pagination
Write-Host "`n4. Testing PAGINATION:"
try {
    $pageResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/v1/payment-cards-cost/all/paged?page=0&size=2" -Method Get -Headers $adminAuthHeaders
    Write-Host "Pagination successful:"
    Write-Host ($pageResponse | ConvertTo-Json -Depth 10)
} catch {
    Write-Host "Pagination failed: $($_.Exception.Response.StatusCode)"
    $streamReader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
    $errorResponse = $streamReader.ReadToEnd()
    Write-Host $errorResponse
}

# Test DELETE operation (optional - uncomment if you want to test)
# Write-Host "`n5. Testing DELETE operation:"
# try {
#     $deleteResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/v1/payment-cards-cost/4" -Method Delete -Headers $adminAuthHeaders
#     Write-Host "Delete successful - Status Code: 204"
# } catch {
#     Write-Host "Delete failed: $($_.Exception.Response.StatusCode)"
#     $streamReader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
#     $errorResponse = $streamReader.ReadToEnd()
#     Write-Host $errorResponse
# }
