package com.example.temperature.Controller.configuration;

public interface TemperatureConfiguration {

	
    public void setTargetedTemperature(String targetedRoom, float temperature);
   
    
    public float getTargetedTemperature(String room);
}
