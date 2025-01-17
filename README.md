# IOT based solar radiation prediction with machine learning models

| You can read the full article from here https://medium.com/@berkayguzel43/predict-solar-radiation-with-iot-device-2ecfb4ec6b7f

![image](https://github.com/user-attachments/assets/49a77c77-7b72-4c66-8333-3fafab6d450e)

In today's rapidly advancing technological landscape, integrating Internet of Things (IoT) devices with machine learning (ML) models offers unprecedented possibilities. This article delves into a groundbreaking project aimed at predicting solar radiation using IoT devices. This project leverages technology to monitor environmental parameters and predict hazardous solar radiation conditions in real time with BM280 sensor and ESP32 dev board. We will explore the journey, methodology, and outcomes of this project.

## Introduction
The project focuses on real-time detection and prediction of solar radiation, coupled with environmental factors such as humidity, temperature, and pressure. Using IoT-enabled BME280 sensors and ESP32 microcontrollers, the data is collected and transmitted to the ThingSpeak cloud platform to enable data storage and analysis for the future works. The integration of a machine learning model enables predictive analysis, providing timely alerts for hazardous solar radiation levels. The ultimate goal is to enhance environmental monitoring and ensure public safety.

## Technical Framework
### Hardware Components
* BME280 Sensor: Monitors humidity, temperature, and pressure to capture environmental conditions.
* ESP32 Microcontroller: Facilitates data acquisition and transmission to the cloud platform.

![image](https://github.com/user-attachments/assets/4f1b9271-147f-494a-9c30-937d2c9eedaf)

## Software Ecosystem
* Programming Environment: ESP32 microcontrollers were programmed using Thonny IDE and micropython.
* ThingSpeak: A cloud platform for data storage, visualization, and integration with the ML model.
* Machine Learning Tools: Torch and Scikit-Learn frameworks were used for developing the predictive model in Google Colaboratory.

## Methodology
Sensor Integration: The BME280 sensors were connected to the ESP32 microcontrollers using the I2C protocol, ensuring efficient data retrieval. You can find the soruce code of ESP32 in the Github address of this project.
Sensor and ESP32 ConnectionData Transmission and Storage: Sensor data was transmitted to ThingSpeak, enabling real-time visualization and storage.
ThingSpeak Cloud Platform Data VisualizationMachine Learning Model Development: The collected data served as the training set for the ML model, focusing on key features like radiation levels, temperature, and humidity. The model was trained and validated for accuracy before deployment.
Model Training EvaluationsCloud Integration: The trained model was deployed on Google Cloud using Flask API, ensuring seamless integration with ThingSpeak for real-time predictions. You can find the Docker file and Flask API codes from GitHub. We need to build a Flask API for to use .pth model file with python scripts, to ensure that and to satify the core function of IOT projects anywhere - anytime - anyplace we have to build our Flask API in Google Cloud.

![image](https://github.com/user-attachments/assets/07144a17-5c91-43d6-93fe-1c5a289a0855)

Mobile Application: A user-friendly app was developed to display radiation predictions and alert users to critical conditions.

![image](https://github.com/user-attachments/assets/5145be76-3c08-4d37-b4ac-ee253684e3d7)
