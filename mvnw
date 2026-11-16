#!/bin/sh
command -v mvn >/dev/null 2>&1 || { echo "Maven 3.9+ belum terpasang"; exit 1; }
exec mvn "$@"
