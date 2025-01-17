package com.example.solarpredictionapp

import ThingSpeakAPI
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class MainActivity : ComponentActivity() {

    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private val refreshInterval = 15000L // 15 seconds
    private var humidity = 0;
    private var temperature = 0;
    private var pressure = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sensor1: CircleSensorView = findViewById(R.id.circleSensor1) // Humidity
        val sensor2: CircleSensorView = findViewById(R.id.circleSensor2) // Temperature
        val sensor3: CircleSensorView = findViewById(R.id.circleSensor3) // Pressure
        val radiationSensor: CircleSensorView = findViewById(R.id.circleRadiation) // Radiation

        // Initialize Retrofit for ThingSpeak API
        val thingSpeakRetrofit = Retrofit.Builder()
            .baseUrl("https://api.thingspeak.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val thingSpeakApi = thingSpeakRetrofit.create(ThingSpeakAPI::class.java)

        // Initialize Retrofit for Custom Prediction API
        val predictionRetrofit = Retrofit.Builder()
            .baseUrl("https://your_google_API_URL/") // Replace with your Google Cloud API URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val predictionApi = predictionRetrofit.create(PredictionAPI::class.java)

        // Handler and Runnable for periodic updates
        handler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            override fun run() {
                fetchSensorData(thingSpeakApi, sensor1, sensor2, sensor3)
                fetchRadiationData(predictionApi, radiationSensor)
                handler.postDelayed(this, refreshInterval) // Schedule the next execution
            }
        }

        // Start the periodic task
        handler.post(runnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Stop the periodic task when the activity is destroyed
        handler.removeCallbacks(runnable)
    }

    private fun fetchSensorData(
        api: ThingSpeakAPI,
        sensor1: CircleSensorView,
        sensor2: CircleSensorView,
        sensor3: CircleSensorView
    ) {

        val channelId = 2755631
        val fieldNumber1 = 1
        val fieldNumber2 = 2
        val fieldNumber3 = 3
        val apiKey = "your_thingspeark_api_key"
        val results = 10

        // Fetch data for sensor 1 (Humidity)
        api.getSensorData(channelId, fieldNumber1, apiKey, results).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val jsonData = response.body()?.string()
                    try {
                        val jsonObject = JSONObject(jsonData)
                        val feedsArray = jsonObject.getJSONArray("feeds")
                        if (feedsArray.length() > 0) {
                            val lastEntry = feedsArray.getJSONObject(feedsArray.length() - 1)
                            val value = lastEntry.getString("field1")
                            humidity = value.toInt();
                            sensor1.setSensorValue("$value %")
                        } else {
                            sensor1.setSensorValue("No Data")
                        }
                    } catch (e: Exception) {
                        sensor1.setSensorValue("Error")
                    }
                } else {
                    sensor1.setSensorValue("Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                sensor1.setSensorValue("Error: ${t.message}")
            }
        })

        // Fetch data for sensor 2 (Temperature)
        api.getSensorData(channelId, fieldNumber2, apiKey, results).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val jsonData = response.body()?.string()
                    try {
                        val jsonObject = JSONObject(jsonData)
                        val feedsArray = jsonObject.getJSONArray("feeds")
                        if (feedsArray.length() > 0) {
                            val lastEntry = feedsArray.getJSONObject(feedsArray.length() - 1)
                            val value = lastEntry.optString("field2", "N/A")
                            temperature = value.toInt();
                            sensor2.setSensorValue("$value °C")
                        } else {
                            sensor2.setSensorValue("No Data")
                        }
                    } catch (e: Exception) {
                        sensor2.setSensorValue("Error")
                    }
                } else {
                    sensor2.setSensorValue("Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                sensor2.setSensorValue("Error: ${t.message}")
            }
        })

        // Fetch data for sensor 3 (Pressure)
        api.getSensorData(channelId, fieldNumber3, apiKey, results).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val jsonData = response.body()?.string()
                    try {
                        val jsonObject = JSONObject(jsonData)
                        val feedsArray = jsonObject.getJSONArray("feeds")
                        if (feedsArray.length() > 0) {
                            val lastEntry = feedsArray.getJSONObject(feedsArray.length() - 1)
                            val value = lastEntry.optString("field3", "N/A")
                            pressure = value.toInt();
                            sensor3.setSensorValue("$value hPa")
                        } else {
                            sensor3.setSensorValue("No Data")
                        }
                    } catch (e: Exception) {
                        sensor3.setSensorValue("Error")
                    }
                } else {
                    sensor3.setSensorValue("Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                sensor3.setSensorValue("Error: ${t.message}")
            }
        })
    }

    private fun fetchRadiationData(api: PredictionAPI, radiationSensor: CircleSensorView) {

        // Call the custom API to get predicted radiation
        api.getPredictedRadiation(humidity, temperature, pressure).enqueue(object : Callback<RadiationResponse> {
            override fun onResponse(call: Call<RadiationResponse>, response: Response<RadiationResponse>) {
                if (response.isSuccessful) {
                    val radiationValue = response.body()?.radiation?.let {
                        String.format("%.3f", it).toDouble()
                    }

                    if (radiationValue != null) {
                        radiationSensor.setSensorValue("$radiationValue W/m²")
                    } else {
                        radiationSensor.setSensorValue("No Data")
                    }
                } else {
                    radiationSensor.setSensorValue("Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RadiationResponse>, t: Throwable) {
                radiationSensor.setSensorValue("Error: ${t.message}")
            }
        })
    }
}

// Custom API Interface
interface PredictionAPI {
    @GET("predict")
    fun getPredictedRadiation(
        @Query("humidity") humidity: Int,
        @Query("temperature") temperature: Int,
        @Query("pressure") pressure: Int
    ): Call<RadiationResponse>
}

// Response Model for Custom API
data class RadiationResponse(
    val radiation: Double
)