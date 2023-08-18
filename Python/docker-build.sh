#!/bin/bash

set -eu

# python -m venv .venv
# source .venv/bin/activate

# poetry config virtualenvs.in-project true
# poetry init

docker build \
  -f "$(dirname "$0")/Dockerfile" \
  -t fn-search:run \
  --target indexing_runtime \
  "$(dirname "$0")/."