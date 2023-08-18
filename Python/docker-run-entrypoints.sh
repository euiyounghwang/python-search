#!/bin/bash
set -ex

source /opt/poetry-venv/bin/activate
cd /app/
poetry run python ./search-indexing-script.py