#!/bin/bash

set -eu

SCRIPTDIR="$( cd -- "$(dirname "$0")" >/dev/null 2>&1 ; pwd -P )"

#docker run --rm -it -d --publish 9081:8081 --expose 9200 \
#  -e node.name=fn-dm-bees-data-01 \
#  -v "$SCRIPTDIR:/FN-BEES-Services/" \
#  fn-flask-api:es

docker run --platform linux/amd64 --rm -it -d \
  --name fn-search-app \
  --network bridge \
  -v "$SCRIPTDIR:/ES-Services/" \
  fn-search:run



