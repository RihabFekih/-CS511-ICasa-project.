package org.example.follow.me.manager;

import org.example.follow.me.manager.impl.FollowMeManagerImplImpl.IlluminanceGoal;

public interface FollowMeAdministration {

	
	
    public void setIlluminancePreference(IlluminanceGoal illuminanceGoal);
    
   
    public IlluminanceGoal getIlluminancePreference();
}
