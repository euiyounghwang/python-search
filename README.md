
### Python-Search Project for indexing based on Python

Install Poerty
```
https://python-poetry.org/docs/?ref=dylancastillo.co#installing-with-the-official-installer
```

Using Poetry: Create the virtual environment in the same directory as the project and install the dependencies:
```
poetry config virtualenvs.in-project true
poetry init
poetry install
```

Using venv and pip: Create a virtual environment and install the dependencies listed in requirements.txt:
```
python -m venv .venv && source .venv/bin/activate
pip install -r requirements.txt
```

### Poetry Enviroment
Install Poerty to Dockerfile as indexing_environment
```
FROM --platform=linux/amd64 python:3.9.7 as indexing_environment
ARG DEBIAN_FRONTEND=noninteractive

# Configure Poetry
ENV POETRY_VERSION=1.3.2
ENV POETRY_HOME=/app/poetry
ENV POETRY_VENV=/app/poetry-venv
ENV POETRY_CACHE_DIR=/app/.cache

# Install poetry separated from system interpreter
RUN python3 -m venv $POETRY_VENV \
	&& $POETRY_VENV/bin/pip install -U pip setuptools \
	&& $POETRY_VENV/bin/pip install poetry==${POETRY_VERSION}

# Add `poetry` to PATH
ENV PATH="${PATH}:${POETRY_VENV}/bin"

WORKDIR /app

# Copy Dependencies
COPY poetry.lock pyproject.toml ./

RUN /bin/bash -c 'source $POETRY_VENV/bin/activate && \
    poetry install'
```

Install Poerty to Dockerfile as indexing_runtime
```
FROM --platform=linux/amd64 python:3.9.7 as indexing_runtime

# Configure Poetry
ENV POETRY_VERSION=1.3.2
ENV POETRY_VENV=/app/poetry-venv

WORKDIR /app
#COPY --from=indexing_environment $POETRY_VENV $POETRY_VENV
COPY --from=indexing_environment /app .
COPY . ES-Services

ENV PATH="${PATH}:${POETRY_VENV}/bin"
ENV PATH="$VIRTUAL_ENV/bin:$PATH"

RUN /bin/bash -c 'source $POETRY_VENV/bin/activate'

ENTRYPOINT ["/app/ES-Services/docker-run-entrypoints.sh"]
```

## Docker run
```
docker run --platform linux/amd64 -it -d \
  --name fn-search-app \
  --network bridge \
  -e ES_HOST="http://host.docker.internal:9209" \
  -v "$SCRIPTDIR:/app/ES-Services/" \
  fn-search:run \
```

![image](https://github.com/euiyounghwang/python-search/assets/84139720/674e6559-1d7a-4755-ba1a-412369119ede)


### Python-Search Project for indexing based on Java with Maven
Run Maven install -f ".."
```
java -jar ./demo/target/java-indexing-1.0-SNAPSHOT.jar
```
