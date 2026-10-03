package net.kenji.auto_perspective.compat;

import net.minecraft.client.Minecraft;
import sophisticated.building.SophisticatedBuildingClient;
import sophisticated.building.systems.BuilderChain;

public class SophistocatedBuildingCompat {

    public static boolean isInBuildingMode(Minecraft mc){
        BuilderChain.BuildingState buildingState = SophisticatedBuildingClient.BUILDER_CHAIN.getBuildingState();
        if(buildingState == BuilderChain.BuildingState.PLACING || buildingState == BuilderChain.BuildingState.BREAKING){
            return true;
        }
        return false;
    }


}
