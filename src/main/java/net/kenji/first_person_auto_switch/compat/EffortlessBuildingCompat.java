package net.kenji.first_person_auto_switch.compat;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import nl.requios.effortlessbuilding.EffortlessBuilding;
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
