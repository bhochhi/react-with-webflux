#!/usr/bin/env bash
port=${1:-8080}

curl -H "content-type: application/json" -d'{"name":" Rupeellll product 1","type":"bank"}' http://localhost:${port}/products