$headers = @{"Content-Type" = "application/json"}
$body = '{"username":"admin","password":"admin"}'

try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/authenticate" -Method Post -Headers $headers -Body $body
    $token = $response.token
    
    # Test with invalid card number (too short)
    $authHeaders = @{
        "Content-Type" = "application/json"
        "Authorization" = "Bearer $token"
    }
    
    Write-Host "Testing invalid card number (too short):"
    $invalidCardBody = '{"cardNumber":"12345"}'
    try {
        $cardResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/v1/payment-cards-cost" -Method Post -Headers $authHeaders -Body $invalidCardBody
        Write-Host ($cardResponse | ConvertTo-Json -Depth 10)
    } catch {
        Write-Host "Expected error for invalid card: $($_.Exception.Response.StatusCode)"
        $streamReader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $errorResponse = $streamReader.ReadToEnd()
        Write-Host $errorResponse
    }
    
    Write-Host "`nTesting malformed JSON:"
    $malformedBody = '{"cardNumber":"12345"'
    try {
        $cardResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/v1/payment-cards-cost" -Method Post -Headers $authHeaders -Body $malformedBody
        Write-Host ($cardResponse | ConvertTo-Json -Depth 10)
    } catch {
        Write-Host "Expected error for malformed JSON: $($_.Exception.Response.StatusCode)"
        $streamReader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $errorResponse = $streamReader.ReadToEnd()
        Write-Host $errorResponse
    }
    
    Write-Host "`nTesting unauthorized access (no token):"
    $noAuthHeaders = @{"Content-Type" = "application/json"}
    $validCardBody = '{"cardNumber":"4111111111111111"}'
    try {
        $cardResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/v1/payment-cards-cost" -Method Post -Headers $noAuthHeaders -Body $validCardBody
        Write-Host ($cardResponse | ConvertTo-Json -Depth 10)
    } catch {
        Write-Host "Expected error for unauthorized access: $($_.Exception.Response.StatusCode)"
        $streamReader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $errorResponse = $streamReader.ReadToEnd()
        Write-Host $errorResponse
    }
    
    # Additional tests for empty card number
    Write-Host "`nTesting empty card number:" 
    $emptyCardBody = '{"cardNumber":""}'
    try {
        $cardResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/v1/payment-cards-cost" -Method Post -Headers $authHeaders -Body $emptyCardBody
    } catch {
        Write-Host "Expected error for empty card number: $($_.Exception.Response.StatusCode)"
        $streamReader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $errorResponse = $streamReader.ReadToEnd()
        Write-Host $errorResponse
    }
    
} catch {
    Write-Host "Error: $_"
    Write-Host $_.Exception.Message
}
