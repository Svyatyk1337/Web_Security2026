#!/usr/bin/env bash
# Gets JWTs and requests audit notes (same as audit-notes.http). Prints status and body.
T=http://localhost:8080/api/v1/audit
J='Content-Type: application/json'
token() { curl -s -X POST -H "$J" -d "{\"username\":\"$1\",\"password\":\"$1\"}" http://localhost:8080/api/v1/auth/token | sed -E 's/.*"access_token":"([^"]+)".*/\1/'; }
U=$(token user); M=$(token manager); A=$(token admin)

req() { # name expected token url
  local out code
  out=$(curl -s -w '\n%{http_code}' -H "Authorization: Bearer $3" "$4"); code=${out##*$'\n'}
  [ "$code" = "$2" ] && m="✔" || m="✘"
  printf '%s %s -> expected %s, got %s\n%s\n\n' "$m" "$1" "$2" "$code" "$(echo "${out%$'\n'*}" | cut -c1-300)"
}
req "1. ADMIN   GET all audit notes"   200 "$A" "$T"
req "2. ADMIN   GET one audit note"    200 "$A" "$T/1"
req "3. ADMIN   GET notes of manager"  200 "$A" "$T?username=manager"
req "4. MANAGER GET own audit notes"   200 "$M" "$T/me"
req "5. USER    GET all audit notes"   403 "$U" "$T"
