package net.kenji.auto_perspective.compat;

import fabric.nl.requios.effortlessbuilding.EffortlessBuildingClient;
import neoforge.nl.requios.effortlessbuilding.buildpipeline.BuildPipeline;
import neoforge.nl.requios.effortlessbuilding.buildpipeline.BuildPipelineClient;
import net.minecraft.client.Minecraft;
import sophisticated.building.systems.BuilderChain;


public class EffortlessBuildingCompat {

    public static boolean isInBuildingMode(Minecraft mc){
        BuildPipeline.BuildState buildingState = BuildPipelineClient.getBuildState();
        if(buildingState != null){
            return true;
        }

        return false;
    }


}
