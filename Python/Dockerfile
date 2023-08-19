

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
