#!/bin/sh

docker build -t orders-api .
docker-compose up -d