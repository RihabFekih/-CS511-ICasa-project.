package org.example.follow.me.manager.impl;

import org.example.follow.me.manager.FollowMeAdministration;

import com.example.binary.follow.me.configuration.FollowMeConfiguration;

public class FollowMeManagerImplImpl  implements FollowMeAdministration{

	/** Field for followMeConfigurations dependency */
	private FollowMeConfiguration followMeConfigurations;

	public enum IlluminanceGoal {

		/** The goal associated with soft illuminance. */
		SOFT(1),
		/** The goal associated with medium illuminance. */
		MEDIUM(2),
		/** The goal associated with full illuminance. */
		FULL(3);

		/** The number of lights to turn on. */
		private int numberOfLightsToTurnOn;

		/**
		 * Gets the number of lights to turn On.
		 * 
		 * @return the number of lights to turn On.
		 */
		public int getNumberOfLightsToTurnOn() {
			return numberOfLightsToTurnOn;
		}

		/**
		 * Instantiates a new illuminance goal.
		 * 
		 * @param numberOfLightsToTurnOn
		 *            the number of lights to turn on.
		 */
		private IlluminanceGoal(int numberOfLightsToTurnOn) {
			this.numberOfLightsToTurnOn = numberOfLightsToTurnOn;
		}
	}

	@Override
	public void setIlluminancePreference(IlluminanceGoal illuminanceGoal) {
		
		followMeConfigurations.setMaximumNumberOfLightsToTurnOn( illuminanceGoal.getNumberOfLightsToTurnOn());
		
		
	}

	public IlluminanceGoal getIlluminancePreference() {
	
		if( followMeConfigurations.getMaximumNumberOfLightsToTurnOn()==1){return IlluminanceGoal.SOFT;}
		else if( followMeConfigurations.getMaximumNumberOfLightsToTurnOn()==2){return IlluminanceGoal.MEDIUM;}
		else return IlluminanceGoal.FULL;
	}


	
	/** Component Lifecycle Method */
	public void stop() {
		System.out.println("Manager is starting...333333");
	}

	/** Component Lifecycle Method */
	public void start() {
		System.out.println("Manager is stopping...333333");
	}
}
