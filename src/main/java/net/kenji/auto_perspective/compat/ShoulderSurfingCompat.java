package net.kenji.auto_perspective.compat;

import com.github.exopandora.shouldersurfing.api.client.Perspective;
import com.github.exopandora.shouldersurfing.api.client.ShoulderSurfing;

public class ShoulderSurfingCompat {

    public static boolean isShoulderSurfing() {
        return ShoulderSurfing.getInstance().isShoulderSurfing();
    }

    public static void restoreShoulderSurfing() {
        // Also sets options.cameraType to THIRD_PERSON_BACK via SSR's setCameraTypeDirect
        ShoulderSurfing.getInstance().changePerspective(Perspective.SHOULDER_SURFING);
    }
}