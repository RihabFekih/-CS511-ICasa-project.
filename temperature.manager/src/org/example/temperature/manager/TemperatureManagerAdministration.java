package org.example.temperature.manager;

public interface TemperatureManagerAdministration {

    public void temperatureIsTooHigh(String roomName);
    
    public void temperatureIsTooLow(String roomName);
}
