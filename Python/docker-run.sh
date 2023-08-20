#!/bin/bash

set -eu

SCRIPTDIR="$( cd -- "$(dirname "$0")" >/dev/null 2>&1 ; pwd -P )"

echo $SCRIPTDIR

#docker run -rm -it --name a-test fn-search:run
docker run --platform linux/amd64 --rm -it -d \
  --name fn-search-app \
  --network bridge \
  -e ES_HOST="http://host.docker.internal:9209" \
  -v "$SCRIPTDIR:/app/ES-Services/" \
  fn-search:es \




