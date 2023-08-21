#!/bin/bash
set -e

source .venv/bin/activate
# Start
poetry run python ./Search-indexing-script.py

# Unit-Test
# py.test -v basic/tests/