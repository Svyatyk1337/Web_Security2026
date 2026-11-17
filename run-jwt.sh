#!/usr/bin/env bash
# Gets JWT tokens for user/manager/admin and calls routes with them (same as jwt-authorization.http).
BASE=http://localhost:8080/api/v1/transfers
J='Content-Type: application/json'
NEW='{"id":"4","playerName":"Victor Osimhen","fromClub":"Napoli","toClub":"Galatasaray","transferFee":75000000}'
UPD='{"id":"4","playerName":"Victor Osimhen","fromClub":"Napoli","toClub":"Chelsea","transferFee":80000000}'

token() { curl -s -X POST -H "$J" -d "{\"username\":\"$1\",\"password\":\"$1\"}" http://localhost:8080/api/v1/auth/token | sed -E 's/.*"access_token":"([^"]+)".*/\1/'; }
T_USER=$(token user); T_MANAGER=$(token manager); T_ADMIN=$(token admin)

req() { # name expected token curl-args...
  local name=$1 exp=$2 tok=$3; shift 3
  local code; code=$(curl -s -o /dev/null -w '%{http_code}' -H "Authorization: Bearer $tok" "$@")
  [ "$code" = "$exp" ] && m="✔" || m="✘"
  printf '%s %-34s expected %s, got %s\n' "$m" "$name" "$exp" "$code"
}

echo "JWT access to routes"
echo "--------------------"
req " 1. USER JWT    GET all"    200 "$T_USER"    "$BASE"
req " 2. USER JWT    GET one"    200 "$T_USER"    "$BASE/1"
req " 3. USER JWT    CREATE"     403 "$T_USER"    -X POST -H "$J" -d "$NEW" "$BASE"
req " 4. USER JWT    DELETE"     403 "$T_USER"    -X DELETE "$BASE/1"
req " 5. MANAGER JWT CREATE"     200 "$T_MANAGER" -X POST -H "$J" -d "$NEW" "$BASE"
req " 6. MANAGER JWT UPDATE"     200 "$T_MANAGER" -X PUT -H "$J" -d "$UPD" "$BASE"
req " 7. MANAGER JWT DELETE"     403 "$T_MANAGER" -X DELETE "$BASE/4"
req " 8. ADMIN JWT   DELETE"     200 "$T_ADMIN"   -X DELETE "$BASE/4"
req " 9. ADMIN JWT   GET all"    200 "$T_ADMIN"   "$BASE"
req "10. INVALID JWT GET all"    401 "invalid.token.value" "$BASE"
