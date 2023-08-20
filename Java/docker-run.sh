#!/bin/bash

set -eu

SCRIPTDIR="$( cd -- "$(dirname "$0")" >/dev/null 2>&1 ; pwd -P )"

echo $SCRIPTDIR

#docker run -rm -it --name a-test fn-search:run

# docker run --rm --name abc fn-search-java:es

docker run --platform linux/amd64 --rm -it -d \
  --name fn-search-java-app \
  --network bridge \
  -v "$SCRIPTDIR:/app/ES-Services/" \
  fn-search-java:es \




