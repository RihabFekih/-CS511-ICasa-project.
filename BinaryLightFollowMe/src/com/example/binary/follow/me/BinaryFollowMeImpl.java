package com.example.binary.follow.me;

import java.util.ArrayList;
import java.util.List;


import com.example.binary.follow.me.configuration.FollowMeConfiguration;

import fr.liglab.adele.icasa.device.DeviceListener;
import fr.liglab.adele.icasa.device.GenericDevice;
import fr.liglab.adele.icasa.device.light.BinaryLight;

import java.util.Map;
import fr.liglab.adele.icasa.device.presence.PresenceSensor;
import fr.liglab.adele.icasa.device.light.DimmerLight;

public class BinaryFollowMeImpl implements DeviceListener, FollowMeConfiguration {

	private static final String LOCATION_PROPERTY_NAME = "Location";
	public static final String LOCATION_UNKNOWN = "unknown";
	/** Field for binaryLights dependency */
	private BinaryLight[] binaryLights;
	/** Field for presenceSensors dependency */
	private PresenceSensor[] presenceSensors;
	private int maxLightsToTurnOnPerRoom = 1;
	/** Field for dimmerLights dependency */
	private DimmerLight[] dimmerLights;
	

	/** Bind Method for binaryLights dependency */
	public void bindbinaryLight(BinaryLight binaryLight, Map properties) {
		System.out.println("bind binary light " + binaryLight.getSerialNumber());
		binaryLight.addListener(this);
	}

	/** Unbind Method for binaryLights dependency */
	public void unbindbinaryLight(BinaryLight binaryLight, Map properties) {
		System.out.println("unbind binary light " + binaryLight.getSerialNumber());
		binaryLight.removeListener(this);
	}

	/** Bind Method for presenceSensors dependency */
	public synchronized void bindpresenceSensor(PresenceSensor presenceSensor, Map properties) {
		System.out.println("bind presence Sensor " + presenceSensor.getSerialNumber());
		presenceSensor.addListener(this);

	}

	/** Unbind Method for presenceSensors dependency */
	public synchronized void unbindpresenceSensor(PresenceSensor presenceSensor, Map properties) {
		System.out.println("unbind presence Sensor " + presenceSensor.getSerialNumber());

		presenceSensor.removeListener(this);
	}

	/** Bind Method for dimmerLights dependency */
	public void binddimmerLight(DimmerLight dimmerLight, Map properties) {
		System.out.println("bind Dimmer Lght " + dimmerLight.getSerialNumber());

		dimmerLight.addListener(this);
	}

	/** Unbind Method for dimmerLights dependency */
	public void unbinddimmerLight(DimmerLight dimmerLight, Map properties) {
		System.out.println("unbind Dimmer Lght " + dimmerLight.getSerialNumber());

		dimmerLight.removeListener(this);
	}

	@Override
	public int getMaximumNumberOfLightsToTurnOn() {

		return maxLightsToTurnOnPerRoom;
	}

	@Override
	public void setMaximumNumberOfLightsToTurnOn(int maximumNumberOfLightsToTurnOn) {
		this.maxLightsToTurnOnPerRoom = maximumNumberOfLightsToTurnOn;

	}

	/** Component Lifecycle Method */
	public void stop() {
		System.out.println("Component is stopping...2222");
		for (PresenceSensor sensor : presenceSensors) {
			sensor.removeListener(this);
		}
	}

	/** Component Lifecycle Method */
	public void start() {
		System.out.println("Component is starting...2222");
	}

	@Override
	public void deviceAdded(GenericDevice arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deviceEvent(GenericDevice arg0, Object arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void devicePropertyAdded(GenericDevice arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void devicePropertyModified(GenericDevice device, String propertyName, Object oldValue, Object newValue) {
		int numberOfLightOn = 0;
		if (device instanceof PresenceSensor) {
			PresenceSensor changingSensor = (PresenceSensor) device;
			if (propertyName.equals(PresenceSensor.PRESENCE_SENSOR_SENSED_PRESENCE)) {
				// get the location where the sensor is:
				String detectorLocation = (String) changingSensor.getPropertyValue(LOCATION_PROPERTY_NAME);
				// if the location is known :
				if (!detectorLocation.equals(LOCATION_UNKNOWN)) {

					// get the related binary lights
					List<BinaryLight> sameLocationBinnaryLigths = getBinaryLightFromLocation(detectorLocation);
					List<DimmerLight> sameLocationDimmerLights = getDimmerLightFromLocation(detectorLocation);
					for (BinaryLight binaryLight : sameLocationBinnaryLigths) {

						// and switch them on/off depending on the sensed presence
						if (changingSensor.getSensedPresence()) {
							if (numberOfLightOn < maxLightsToTurnOnPerRoom) {
								binaryLight.turnOn();
								numberOfLightOn++;

							}

						} else {
							binaryLight.turnOff();
						}

					}
					for (DimmerLight dimmerLight : sameLocationDimmerLights) {
						// and switch them on/off depending on the sensed presence
						if (changingSensor.getSensedPresence()) {
							dimmerLight.setPowerLevel(1);

						} else {
							dimmerLight.setPowerLevel(0);
						}

					}
				}
			}

		} //ajout de listener sur light

		else if (device instanceof BinaryLight) {
			BinaryLight changingBinaryLight = (BinaryLight) device;
			//  if (propertyName.equals(BinaryLight.BINARY_LIGHT_POWER_STATUS)) {
			// get the location where the sensor is:
			String binaryLightLocation = (String) changingBinaryLight.getPropertyValue(LOCATION_PROPERTY_NAME);
			List<PresenceSensor> sameLocationSensor = getPresenceSensorFromLocation(binaryLightLocation);
			// if the location is known :
			if (!binaryLightLocation.equals(LOCATION_UNKNOWN)) {

				// get the related binary lights
				// List<PresenceSensor> sameLocationSensor = getPresenceSensorFromLocation(LightLocation);
				for (PresenceSensor presenceSensor : sameLocationSensor) {
					// and switch them on/off depending on the sensed presence
					if (presenceSensor.getSensedPresence()) {
						changingBinaryLight.turnOn();
					} else {
						changingBinaryLight.turnOff();
					}
				}
			} else if (binaryLightLocation.equals(LOCATION_UNKNOWN) || sameLocationSensor.isEmpty()) {

				changingBinaryLight.turnOff();
			}
		}
		// ajout du listener Dimmer Light

		else if (device instanceof DimmerLight) {
			DimmerLight changingDimmerLight = (DimmerLight) device;
			//  if (propertyName.equals(BinaryLight.BINARY_LIGHT_POWER_STATUS)) {
			// get the location where the sensor is:
			String dimmerLightLocation = (String) changingDimmerLight.getPropertyValue(LOCATION_PROPERTY_NAME);
			List<PresenceSensor> sameLocationSensor = getPresenceSensorFromLocation(dimmerLightLocation);
			// if the location is known :
			if (!dimmerLightLocation.equals(LOCATION_UNKNOWN)) {

				// get the related binary lights
				// List<PresenceSensor> sameLocationSensor = getPresenceSensorFromLocation(LightLocation);
				for (PresenceSensor presenceSensor : sameLocationSensor) {
					// and switch them on/off depending on the sensed presence
					if (presenceSensor.getSensedPresence()) {
						changingDimmerLight.setPowerLevel(1);
					} else {
						changingDimmerLight.setPowerLevel(0);
					}
				}
			} else if (dimmerLightLocation.equals(LOCATION_UNKNOWN) || sameLocationSensor.isEmpty()) {

				changingDimmerLight.setPowerLevel(0);
			}
		}

	}

	@Override
	public void devicePropertyRemoved(GenericDevice arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deviceRemoved(GenericDevice arg0) {
		// TODO Auto-generated method stub

	}

	private synchronized List<BinaryLight> getBinaryLightFromLocation(String location) {
		List<BinaryLight> binaryLightsLocation = new ArrayList<BinaryLight>();
		for (BinaryLight binLight : binaryLights) {
			if (binLight.getPropertyValue(LOCATION_PROPERTY_NAME).equals(location)) {
				binaryLightsLocation.add(binLight);
			}
		}
		return binaryLightsLocation;
	}

	private synchronized List<PresenceSensor> getPresenceSensorFromLocation(String location) {
		List<PresenceSensor> presenceSensorLocation = new ArrayList<PresenceSensor>();
		for (PresenceSensor presenceSensor : presenceSensors) {
			if (presenceSensor.getPropertyValue(LOCATION_PROPERTY_NAME).equals(location)) {
				presenceSensorLocation.add(presenceSensor);
			}
		}
		return presenceSensorLocation;
	}

	private synchronized List<DimmerLight> getDimmerLightFromLocation(String location) {
		List<DimmerLight> dimmerLightLocation = new ArrayList<DimmerLight>();
		for (DimmerLight dimmerLight : dimmerLights) {
			if (dimmerLight.getPropertyValue(LOCATION_PROPERTY_NAME).equals(location)) {
				dimmerLightLocation.add(dimmerLight);
			}
		}
		return dimmerLightLocation;
	}

}
