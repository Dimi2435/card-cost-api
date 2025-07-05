$headers = @{"Content-Type" = "application/json"}
$body = '{"username":"admin","password":"admin"}'

try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/authenticate" -Method Post -Headers $headers -Body $body
    $token = $response.token
    Write-Host "Authentication successful. Token: $token"
    
    # Test error handling for various scenarios
    $authHeaders = @{
        "Content-Type" = "application/json"
        "Authorization" = "Bearer $token"
    }
    
    Write-Host "`n=== Testing Error Handling Scenarios ==="
    
    # Test 1: Invalid card number (too short)
    Write-Host "`n1. Testing invalid card number (too short):"
    $invalidCardBody = '{"cardNumber":"12345"}'
    try {
        $result = Invoke-RestMethod -Uri "http://localhost:8080/api/v1/payment-cards-cost" -Method Post -Headers $authHeaders -Body $invalidCardBody
        Write-Host "Unexpected success: $($result | ConvertTo-Json)"
    } catch {
        Write-Host "✓ Expected error (400): $($_.Exception.Response.StatusCode)"
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $errorResponse = $reader.ReadToEnd()
        Write-Host "Response: $errorResponse"
    }
    
    # Test 2: Empty card number
    Write-Host "`n2. Testing empty card number:"
    $emptyCardBody = '{"cardNumber":""}'
    try {
        $result = Invoke-RestMethod -Uri "http://localhost:8080/api/v1/payment-cards-cost" -Method Post -Headers $authHeaders -Body $emptyCardBody
        Write-Host "Unexpected success: $($result | ConvertTo-Json)"
    } catch {
        Write-Host "✓ Expected error (400): $($_.Exception.Response.StatusCode)"
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $errorResponse = $reader.ReadToEnd()
        Write-Host "Response: $errorResponse"
    }
    
    # Test 3: Null card number (omitted field)
    Write-Host "`n3. Testing missing card number field:"
    $nullCardBody = '{}'
    try {
        $result = Invoke-RestMethod -Uri "http://localhost:8080/api/v1/payment-cards-cost" -Method Post -Headers $authHeaders -Body $nullCardBody
        Write-Host "Unexpected success: $($result | ConvertTo-Json)"
    } catch {
        Write-Host "✓ Expected error (400): $($_.Exception.Response.StatusCode)"
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $errorResponse = $reader.ReadToEnd()
        Write-Host "Response: $errorResponse"
    }
    
    # Test 4: Valid card number
    Write-Host "`n4. Testing valid card number:"
    $validCardBody = '{"cardNumber":"4111111111111111"}'
    try {
        $result = Invoke-RestMethod -Uri "http://localhost:8080/api/v1/payment-cards-cost" -Method Post -Headers $authHeaders -Body $validCardBody
        Write-Host "✓ Success: $($result | ConvertTo-Json)"
    } catch {
        Write-Host "✗ Unexpected error: $($_.Exception.Response.StatusCode)"
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $errorResponse = $reader.ReadToEnd()
        Write-Host "Response: $errorResponse"
    }
    
    # Test 5: Card number with non-digit characters
    Write-Host "`n5. Testing card number with spaces and hyphens:"
    $spacedCardBody = '{"cardNumber":"4111-1111-1111-1111"}'
    try {
        $result = Invoke-RestMethod -Uri "http://localhost:8080/api/v1/payment-cards-cost" -Method Post -Headers $authHeaders -Body $spacedCardBody
        Write-Host "✓ Success (cleaned): $($result | ConvertTo-Json)"
    } catch {
        Write-Host "✗ Unexpected error: $($_.Exception.Response.StatusCode)"
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $errorResponse = $reader.ReadToEnd()
        Write-Host "Response: $errorResponse"
    }
    
    Write-Host "`n=== All tests completed ==="
    
} catch {
    Write-Host "Authentication failed: $($_.Exception.Message)"
}
