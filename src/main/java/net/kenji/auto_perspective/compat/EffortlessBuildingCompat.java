package net.kenji.auto_perspective.compat;

import net.minecraft.client.Minecraft;
import nl.requios.effortlessbuilding.EffortlessBuildingClient;
import nl.requios.effortlessbuilding.systems.BuilderChain;

public class EffortlessBuildingCompat {

    public static boolean isInBuildingMode(Minecraft mc){
        BuilderChain.BuildingState buildingState = EffortlessBuildingClient.BUILDER_CHAIN.getBuildingState();
        if(buildingState == BuilderChain.BuildingState.PLACING || buildingState == BuilderChain.BuildingState.BREAKING){
            return true;
        }

        return false;
    }


}
