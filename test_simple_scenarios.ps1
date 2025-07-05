$headers = @{"Content-Type" = "application/json"}
$body = '{"username":"admin","password":"admin"}'

try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/authenticate" -Method Post -Headers $headers -Body $body
    $token = $response.token
    Write-Host "Authentication successful. Token received."
    
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
        Write-Host "Unexpected success"
    } catch {
        Write-Host "Expected error (400): $($_.Exception.Response.StatusCode)"
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $errorResponse = $reader.ReadToEnd()
        Write-Host "Response: $errorResponse"
    }
    
    # Test 2: Valid card number
    Write-Host "`n2. Testing valid card number:"
    $validCardBody = '{"cardNumber":"4111111111111111"}'
    try {
        $result = Invoke-RestMethod -Uri "http://localhost:8080/api/v1/payment-cards-cost" -Method Post -Headers $authHeaders -Body $validCardBody
        Write-Host "Success: $($result | ConvertTo-Json)"
    } catch {
        Write-Host "Unexpected error: $($_.Exception.Response.StatusCode)"
    }
    
    Write-Host "`n=== All tests completed ==="
    
} catch {
    Write-Host "Authentication failed"
}
