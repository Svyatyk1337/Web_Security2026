#!/usr/bin/env bash
# Sends the 5 CRUD requests from basic-authorization.http and prints HTTP status codes.
BASE=http://localhost:8080/api/v1/transfers
AUTH=admin:admin
JSON='Content-Type: application/json'

req() { # name, curl args...
  local name=$1; shift
  local code
  code=$(curl -s -o /dev/null -w '%{http_code}' -u "$AUTH" "$@")
  if [ "$code" = "200" ]; then mark="✔"; else mark="✘"; fi
  printf '%s %-28s HTTP/1.1 %s\n' "$mark" "$name" "$code"
}

echo "HTTP Requests (basic-authorization)"
echo "-----------------------------------"
req "1. GET all transfers"   -H 'Accept: application/json' "$BASE"
req "2. GET one transfer"    -H 'Accept: application/json' "$BASE/1"
req "3. CREATE one transfer" -X POST -H "$JSON" -d '{"id":"4","playerName":"Victor Osimhen","fromClub":"Napoli","toClub":"Galatasaray","transferFee":75000000}' "$BASE"
req "4. UPDATE one transfer" -X PUT  -H "$JSON" -d '{"id":"4","playerName":"Victor Osimhen","fromClub":"Napoli","toClub":"Chelsea","transferFee":80000000}' "$BASE"
req "5. DELETE one transfer" -X DELETE "$BASE/4"
