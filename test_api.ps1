$headers = @{"Content-Type" = "application/json"}
$body = '{"username":"admin","password":"admin"}'

try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/authenticate" -Method Post -Headers $headers -Body $body
    Write-Host "Authentication successful:"
    Write-Host ($response | ConvertTo-Json -Depth 10)
    
    # Store the token
    $token = $response.token
    Write-Host "Token: $token"
    
    # Test card cost calculation
    $authHeaders = @{
        "Content-Type" = "application/json"
        "Authorization" = "Bearer $token"
    }
    
    $cardBody = '{"cardNumber":"4111111111111111"}'
    $cardResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/v1/payment-cards-cost" -Method Post -Headers $authHeaders -Body $cardBody
    Write-Host "Card cost calculation successful:"
    Write-Host ($cardResponse | ConvertTo-Json -Depth 10)
    
    # Test with invalid token
    $invalidAuthHeaders = @{
        "Content-Type" = "application/json"
        "Authorization" = "Bearer invalid_token"
    }
    try {
        $cardResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/v1/payment-cards-cost" -Method Post -Headers $invalidAuthHeaders -Body $cardBody
    } catch {
        Write-Host "Expected error for invalid token: $($_.Exception.Response.StatusCode)"
    }

} catch {
    Write-Host "Error: $_"
    Write-Host $_.Exception.Message
}
