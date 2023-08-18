
# Python-Search Project for indexing based on Python, Java

Install Poerty
```
https://python-poetry.org/docs/?ref=dylancastillo.co#installing-with-the-official-installer
```

Using Poetry: Create the virtual environment in the same directory as the project and install the dependencies:
```
poetry config virtualenvs.in-project true
poetry install
```

Using venv and pip: Create a virtual environment and install the dependencies listed in requirements.txt:
```
python -m venv .venv && source .venv/bin/activate
pip install -r requirements.txt
```