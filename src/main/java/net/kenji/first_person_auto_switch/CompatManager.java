package net.kenji.first_person_auto_switch;

public class CompatManager {

    protected static boolean isEpicFightPresent(){
        return FirstPersonAutoSwitch.isLoaded("epicfight");
    }
    protected static boolean isSmoothF5Present(){
        return FirstPersonAutoSwitch.isLoaded("seramicx_smooth_f5");
    }
    protected static boolean isEffortlessBuildngPresent(){
       return FirstPersonAutoSwitch.isLoaded("effortlessbuilding");
    }
    protected static boolean isSophistocatedBuildngPresent(){
        return FirstPersonAutoSwitch.isLoaded("sophisticatedbuilding");
    }

}
