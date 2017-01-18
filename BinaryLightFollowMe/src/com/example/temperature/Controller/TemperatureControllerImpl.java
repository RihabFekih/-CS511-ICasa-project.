package com.example.temperature.Controller;

import fr.liglab.adele.icasa.device.temperature.Heater;
import fr.liglab.adele.icasa.device.temperature.Thermometer;

import fr.liglab.adele.icasa.service.scheduler.PeriodicRunnable;

import fr.liglab.adele.icasa.device.temperature.Cooler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.example.temperature.Controller.configuration.TemperatureConfiguration;

public class TemperatureControllerImpl implements  PeriodicRunnable, TemperatureConfiguration {

	private static final String LOCATION_PROPERTY_NAME = "Location";
	/** Field for heaters dependency */
	private Heater[] heaters;
	/** Field for thermometers dependency */
	private Thermometer[] thermometers;
	/** Field for coolers dependency */
	private Cooler[] coolers;
	private String targetedroom =null;
	private float targetedTemp;
	
	/** Component Lifecycle Method */
	public void stop() {
		System.out.println("Temperature Controlleur Starting ");
	}

	/** Component Lifecycle Method */
	public void start() {
		System.out.println("Temperature Controlleur Stopping ");
	
	}

	/** Bind Method for coolers dependency */
	public void bindCooler(Cooler cooler, Map properties) {
		System.out.println("bind Cooler " + cooler.getSerialNumber());
		
	}

	/** Unbind Method for coolers dependency */
	public void unbindCooler(Cooler cooler, Map properties) {
		System.out.println("unbind Cooler " + cooler.getSerialNumber());
		
	}

	/** Bind Method for thermometers dependency */
	public void bindThermometer(Thermometer thermometer, Map properties) {
		System.out.println("bind thermometer " + thermometer.getSerialNumber());
		
	}

	/** Unbind Method for thermometers dependency */
	public void unbindThermometer(Thermometer thermometer, Map properties) {
		System.out.println("unbind thermometer " + thermometer.getSerialNumber());
		
	}

	/** Bind Method for heaters dependency */
	public void bindHeater(Heater heater, Map properties) {
		System.out.println("bind heater " + heater.getSerialNumber());
		
	}

	/** Unbind Method for heaters dependency */
	public void unbindHeater(Heater heater, Map properties) {
		System.out.println("unbind heater " + heater.getSerialNumber());
		
	}

	@Override
	public void run() {
		for(Thermometer thermo : thermometers){
			String thermometerLocation = (String) thermo.getPropertyValue(LOCATION_PROPERTY_NAME);
			List<Cooler> sameLocationCooler = getCoolerFromLocation(thermometerLocation);
			List<Heater> sameLocationHeater = getHeaterFromLocation(thermometerLocation);
			
			if(thermometerLocation.equals(targetedroom)){
			if(thermo.getTemperature() < targetedTemp-1){
				System.out.println(thermometerLocation+" : temperature heater actuelle est "+thermo.getTemperature());
			for (Heater heat: sameLocationHeater){
				heat.setPowerLevel(1);
			}

			for (Cooler cool: sameLocationCooler){
					cool.setPowerLevel(0);
				}
			}
			else if( thermo.getTemperature() >targetedTemp+1){
				System.out.println(thermometerLocation+" : temperature Cool actuelle est "+thermo.getTemperature());
				for (Cooler cool: sameLocationCooler){
					cool.setPowerLevel(1);
				}
				for (Heater heat: sameLocationHeater){
					heat.setPowerLevel(0);
				}
			}
			else{
				for (Heater heat: sameLocationHeater){
					heat.setPowerLevel(0);
				}
				
				for (Cooler cool: sameLocationCooler){
						cool.setPowerLevel(0);
					}
			}
		}}
		
		
		
	}

	@Override
	public long getPeriod() {
	
		return 10;
	}

	@Override
	public TimeUnit getUnit() {
		
		return TimeUnit.SECONDS;
	}
	
	
	private synchronized List<Cooler> getCoolerFromLocation(String location) {
		List<Cooler> coolsLocation = new ArrayList<Cooler>();
		for (Cooler cool : coolers) {
			if (cool.getPropertyValue(LOCATION_PROPERTY_NAME).equals(location)) {
				coolsLocation.add(cool);
			}
		}
		return coolsLocation;
	}

	private synchronized List<Heater> getHeaterFromLocation(String location) {
		List<Heater> heatLocation = new ArrayList<Heater>();
		for (Heater heat : heaters) {
			if (heat.getPropertyValue(LOCATION_PROPERTY_NAME).equals(location)) {
				heatLocation.add(heat );
			}
		}
		return heatLocation;
	}

	private synchronized List<Thermometer> getThermometerFromLocation(String location) {
		List<Thermometer> thermoLocation = new ArrayList<Thermometer>();
		for (Thermometer thermo : thermometers) {
			if (thermo.getPropertyValue(LOCATION_PROPERTY_NAME).equals(location)) {
				thermoLocation.add(thermo);
			}
		}
		return thermoLocation;
	}

	@Override
	public void setTargetedTemperature(String targetedRoom, float temperature) {
		this.targetedroom=targetedRoom;
		this.targetedTemp=temperature;
		System.out.println("romm :"+targetedRoom+" temp "+temperature);
		
	}

	@Override
	public float getTargetedTemperature(String room) {
	
		for(Thermometer thermo : thermometers){
			String thermometerLocation = (String) thermo.getPropertyValue(LOCATION_PROPERTY_NAME);
			if(thermometerLocation.equals(room)){
				System.out.println("local temp "+thermo.getTemperature());
				return (float) thermo.getTemperature();
				
			}
		}
		System.out.println("pas de thermo ");
		return 0;
	}

	

}
