package net.kenji.auto_perspective;

public class CompatManager {

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

}
