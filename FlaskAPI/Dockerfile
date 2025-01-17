# Solar Radiation Prediction Flask APP Dockerfile

FROM python:3.10.6-slim-buster

COPY requirements.txt .

RUN pip3 install -r requirements.txt

RUN mkdir /src

COPY ./src /src

WORKDIR /src

EXPOSE 8080

CMD ["flask", "run", "--host=0.0.0.0", "--port", "8080"]
