#!/bin/bash
set -e

ES_HOST=http://localhost:9209

# Start
java -jar ./demo/target/java-indexing-1.0-SNAPSHOT.jar $ES_HOST

# Unit-Test
# py.test -v basic/tests/