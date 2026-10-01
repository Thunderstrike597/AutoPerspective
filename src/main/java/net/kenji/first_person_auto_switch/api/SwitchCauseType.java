package net.kenji.first_person_auto_switch.api;

import net.kenji.first_person_auto_switch.ConfigClient;
import net.minecraft.client.Minecraft;

public enum SwitchCauseType {
    NONE,
    CLOSED_SPACE,
    VIEW_OBSTRUCTED,
    BLOCK_MINED,
    BUILD_MODE;

    public int getExitTicksFromSwitchType(){
        return switch (this){
            case CLOSED_SPACE -> ConfigClient.ENCLOSED_EXIT_TICKS.get();
            case VIEW_OBSTRUCTED -> ConfigClient.OBSTRUCTED_EXIT_TICKS.get();
            case BLOCK_MINED -> ConfigClient.MINING_EXIT_TICKS.get();
            case BUILD_MODE -> ConfigClient.BUILDING_EXIT_TICKS.get();
            default -> 8;
        };
    }

}
