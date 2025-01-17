from flask import Flask, request
import pandas as pd
import joblib
import os

app = Flask(__name__)

model_path = 'models/solar_radiation_model.pkl'

@app.route('/predict')
def predict_solar_radiation():
    humidity = request.args.get('humidity')
    temperature = request.args.get('temperature')
    pressure = request.args.get('pressure')

    predicted_radiation = predict_radiation(humidity, temperature, pressure)
    #expport as raditaion json value
    return {'radiation': predicted_radiation}

@app.route('/')
def home():
    return 'Solar Radiation Prediction API'

# Function to predict Radiation
def predict_radiation(humidity, temperature, pressure, model_path=model_path):
    # Create a DataFrame for the input
    input_data = pd.DataFrame({
        'Humidity': [humidity],
        'Temperature': [temperature],
        'Pressure': [pressure]
    })

    # Load the pre-trained model
    loaded_model = joblib.load(model_path)

    # Predict using the pre-trained model
    predicted_radiation = loaded_model.predict(input_data)

    return predicted_radiation[0]


if __name__ == "__main__":
    app.run(debug=True, host="0.0.0.0", port=int(os.environ.get("PORT", 8080)))