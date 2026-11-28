package net.kenji.auto_perspective;

import com.github.exopandora.shouldersurfing.ShoulderSurfingCommon;
import com.github.exopandora.shouldersurfing.api.client.ShoulderSurfing;
import net.kenji.auto_perspective.compat.EpicFightCompat;

public class CompatManager {

    public static void initCompats(){
        if(isEpicFightPresent()){
            EpicFightCompat.init();
        }
    }


    protected static boolean isEpicFightPresent(){
        return AutoPerspective.isLoaded("epicfight");
    }
    protected static boolean isSmoothF5Present(){
        return AutoPerspective.isLoaded("seramicx_smooth_f5");
    }
    protected static boolean isEffortlessBuildngPresent(){
       return AutoPerspective.isLoaded("effortlessbuilding");
    }
    protected static boolean isSophistocatedBuildngPresent(){
        return AutoPerspective.isLoaded("sophisticatedbuilding");
    }
    protected static boolean isShoulderSurfingPresent(){
        return AutoPerspective.isLoaded("shouldersurfing");
    }




}
