#!/usr/bin/env bash
# Sends the 10 requests from roles-authorization.http and compares status with expected.
BASE=http://localhost:8080/api/v1/transfers
J='Content-Type: application/json'
NEW='{"id":"4","playerName":"Victor Osimhen","fromClub":"Napoli","toClub":"Galatasaray","transferFee":75000000}'
UPD='{"id":"4","playerName":"Victor Osimhen","fromClub":"Napoli","toClub":"Chelsea","transferFee":80000000}'

req() { # name expected curl-args...
  local name=$1 exp=$2; shift 2
  local code; code=$(curl -s -o /dev/null -w '%{http_code}' "$@")
  [ "$code" = "$exp" ] && m="✔" || m="✘"
  printf '%s %-34s expected %s, got %s\n' "$m" "$name" "$exp" "$code"
}

echo "Roles & access restriction"
echo "--------------------------"
req " 1. USER    GET all"       200 -u user:user "$BASE"
req " 2. USER    GET one"       200 -u user:user "$BASE/1"
req " 3. USER    CREATE"        403 -u user:user -X POST -H "$J" -d "$NEW" "$BASE"
req " 4. USER    DELETE"        403 -u user:user -X DELETE "$BASE/1"
req " 5. MANAGER CREATE"        200 -u manager:manager -X POST -H "$J" -d "$NEW" "$BASE"
req " 6. MANAGER UPDATE"        200 -u manager:manager -X PUT -H "$J" -d "$UPD" "$BASE"
req " 7. MANAGER DELETE"        403 -u manager:manager -X DELETE "$BASE/4"
req " 8. ADMIN   GET all"       200 -u admin:admin "$BASE"
req " 9. ADMIN   DELETE"        200 -u admin:admin -X DELETE "$BASE/4"
req "10. NO AUTH GET all"       401 "$BASE"
