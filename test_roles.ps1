$headers = @{"Content-Type" = "application/json"}

# Test with regular user
Write-Host "Testing with regular user credentials:"
$userBody = '{"username":"user","password":"password"}'
try {
    $userResponse = Invoke-RestMethod -Uri "http://localhost:8080/authenticate" -Method Post -Headers $headers -Body $userBody
    $userToken = $userResponse.token
    Write-Host "User authenticated successfully"
    
    # Test access to admin endpoint with user token
    $userAuthHeaders = @{
        "Authorization" = "Bearer $userToken"
    }
    
    Write-Host "Testing user access to admin endpoint (should fail):"
    try {
        $adminResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/v1/payment-cards-cost/all" -Method Get -Headers $userAuthHeaders
        Write-Host "Unexpected success: $($adminResponse | ConvertTo-Json -Depth 10)"
    } catch {
        Write-Host "Expected error for forbidden access: $($_.Exception.Response.StatusCode)"
        $streamReader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $errorResponse = $streamReader.ReadToEnd()
        Write-Host $errorResponse
    }
    
} catch {
    Write-Host "User authentication failed: $_"
}

Write-Host "`nTesting with admin credentials:"
$adminBody = '{"username":"admin","password":"admin"}'
try {
    $adminResponse = Invoke-RestMethod -Uri "http://localhost:8080/authenticate" -Method Post -Headers $headers -Body $adminBody
    $adminToken = $adminResponse.token
    Write-Host "Admin authenticated successfully"
    
    # Test access to admin endpoint with admin token
    $adminAuthHeaders = @{
        "Authorization" = "Bearer $adminToken"
    }
    
    Write-Host "Testing admin access to admin endpoint (should succeed):"
    try {
        $adminEndpointResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/v1/payment-cards-cost/all" -Method Get -Headers $adminAuthHeaders
        Write-Host "Admin endpoint access successful:"
        Write-Host ($adminEndpointResponse | ConvertTo-Json -Depth 10)
    } catch {
        Write-Host "Error accessing admin endpoint: $_"
        $streamReader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $errorResponse = $streamReader.ReadToEnd()
        Write-Host $errorResponse
    }
    
} catch {
    Write-Host "Admin authentication failed: $_"
}
