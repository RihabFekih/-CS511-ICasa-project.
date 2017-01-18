package org.example.temperature.manager.impl;

import org.example.temperature.manager.TemperatureManagerAdministration;

import com.example.temperature.Controller.configuration.TemperatureConfiguration;

public class TemperatureManagerImpl implements TemperatureManagerAdministration {

	/** Field for temperatureConfiguration dependency */
	private TemperatureConfiguration temperatureConfiguration;

	/** Component Lifecycle Method */
	public void stop() {
		
		System.out.println("stop temperature manager 5555555");
	}

	/** Component Lifecycle Method */
	public void start() {
		System.out.println("start temperature manager 5555555");
		// TODO: Add your implementation code here
	}

	@Override
	public void temperatureIsTooHigh(String roomName) {
		float temp= temperatureConfiguration.getTargetedTemperature(roomName);
		temperatureConfiguration.setTargetedTemperature(roomName,temp-10);
		System.out.print("changement high temp dans temperature manager");
		
	}

	@Override
	public void temperatureIsTooLow(String roomName) {
		float temp= temperatureConfiguration.getTargetedTemperature(roomName);
		temperatureConfiguration.setTargetedTemperature(roomName,temp+10);
		System.out.print("changement  low temp dans temperature manager");
		
	}

}
