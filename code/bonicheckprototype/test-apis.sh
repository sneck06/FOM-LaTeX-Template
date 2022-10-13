#!/bin/bash

api_base_url_local=https://localhost:8080


for i in {0..10}
  do
    printf "Iteration # %s...\n" "${i}"
    curl -k "${api_base_url_local}/super-fast"
    printf "\n"
    curl -k "${api_base_url_local}/fast"
    printf "\n"
    curl -k "${api_base_url_local}/slow"
    printf "\n"
    curl -k "${api_base_url_local}/super-slow"
    # The super-slow api is not on prod
    printf "\n\n"
done