import network
import urequests
from machine import I2C, Pin
from time import sleep
import time
import BME280

# Wi-Fi connections
SSID = "wifi_ssid"
PASSWORD = "wifi_password"

# ThingSpeak inofrmations
CHANNEL_ID = "your_channel_id"
WRITE_API_KEY = "your_write_api_key"
URL = f"https://api.thingspeak.com/update?api_key={WRITE_API_KEY}"

# ESP32 - Pin assignment
i2c = I2C(scl=Pin(22), sda=Pin(21), freq=10000)
# ESP8266 - Pin assignment
#i2c = I2C(scl=Pin(5), sda=Pin(4), freq=10000)

# Wi-Fi
def connect_wifi(ssid, password):
    """
        Connect to Wi-Fi
        :param ssid: Wi-Fi name
        :param password: Wi-Fi password
        :return: None
    """
    wlan = network.WLAN(network.STA_IF)
    wlan.active(True)
    wlan.connect(ssid, password)

    print("Connecting Wi-Fi...")
    while not wlan.isconnected():
        time.sleep(1)
        print(".", end="")
    print("\nWi-Fi connection succesfull!")
    print(f"IP address: {wlan.ifconfig()[0]}")

# ThingSpeak
def send_to_thingspeak(temp, hum, press):
    """
        Send data to ThingSpeak
        :param temp: Temperature
        :param hum: Humidity
        :param press: Pressure
        :return: None
    """
    try:
        response = urequests.get(f"{URL}&field1={hum}&field2={temp}&field3={press}")
        if response.status_code == 200:
            print("ThingSpeak update succesfull!")
        else:
            print(f"HTTP error code: {response.status_code}")
        response.close()
    except Exception as e:
        print(f"ThingSpeak sending data error: {e}")

def main():
    connect_wifi(SSID, PASSWORD)
    last_time = time.ticks_ms()
    timer_delay = 30000  # 30 seconds
    bme = BME280.BME280(i2c=i2c)
    while True:
        current_time = time.ticks_ms()
        if time.ticks_diff(current_time, last_time) > timer_delay:
            try:
                # Read Sensor Values
                temp = bme.temperature
                hum = bme.humidity
                press = bme.pressure
                print(f"Temperature: {temp} °C")
                print(f"Humidity: {hum} %")
                print(f"Pressure: {press} hPa")

                # Sending Data ThingSpeak
                send_to_thingspeak(temp, hum, press)

            except Exception as e:
                print(f"Sensor read error: {e}")

            last_time = current_time

# Programı başlat
if __name__ == "__main__":
    main()

