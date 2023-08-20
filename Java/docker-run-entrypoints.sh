#!/bin/bash
set -ex


sleep 10

# poetry run python ./search-indexing-script.py --es $ES_HOST
# java -jar /app/ES-Services/indexing.jar
java -jar /app/indexing.jar