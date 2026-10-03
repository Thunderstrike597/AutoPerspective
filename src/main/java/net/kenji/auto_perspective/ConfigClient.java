package net.kenji.auto_perspective;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigClient {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static ForgeConfigSpec.ConfigValue<Boolean> USE_ROTATION_FIX;
    public static ForgeConfigSpec.ConfigValue<Boolean> USE_CEILING_CHECK;
    public static ForgeConfigSpec.ConfigValue<Boolean> USE_MINING_TIMER;

    public static ForgeConfigSpec.ConfigValue<Boolean> USE_EFFORTLESS_BUILDING_MODE;


    public static ForgeConfigSpec.ConfigValue<Integer> MAX_CEILING_CHECK_DIST;

    public static ForgeConfigSpec.ConfigValue<Integer> MAX_CHECK_BLOCKS;
    public static ForgeConfigSpec.ConfigValue<Integer> MAX_CHECK_RADIUS;
    public static ForgeConfigSpec.ConfigValue<Integer> CHECK_TICKS;

    public static ForgeConfigSpec.ConfigValue<Integer> ENTER_TICKS;

    public static ForgeConfigSpec.ConfigValue<Integer> ENCLOSED_EXIT_TICKS;
    public static ForgeConfigSpec.ConfigValue<Integer> OBSTRUCTED_EXIT_TICKS;
    public static ForgeConfigSpec.ConfigValue<Integer> MINING_EXIT_TICKS;
    public static ForgeConfigSpec.ConfigValue<Integer> BUILDING_EXIT_TICKS;



    public static ForgeConfigSpec.ConfigValue<Integer> MAX_MINING_TIMER;


    static {
        BUILDER.push("Values");

        USE_CEILING_CHECK = BUILDER.comment(
                "Whether Or Not Blocks Above The Player At Or Above <Max Ceiling Check Distance> Should Contribute To The Auto-Switch Check \n('<Max Ceiling Check Distance>' Refers To The Config Option 'Max Ceiling Check Distance')")
                .define(
                "Use Ceiling Check",
                true);
        USE_MINING_TIMER = BUILDER.comment(
                "Whether Or Not Mining Blocks Continuously For Longer Than <Max Mining Time> Should Contribute To The Auto-Switch Check \n('<Max Mining Time>' Refers To The Config Option 'Max Mining Time')")
                .define(
                "Use Mining Timer",
                true);
        MAX_CEILING_CHECK_DIST = BUILDER.comment(
                "The Distance To Check For Blocks Above The Player For The Config Option 'Use Ceiling Check'")
                .define(
                "Max Ceiling Check Distance",
                5);
        MAX_MINING_TIMER = BUILDER.comment(
                "The Amount Of Ticks Counted Of The Player Continuously Mining Blocks Before Auto-Switching To 1st Person(Only Counts While Holding Down The Attack/Mining Key/Button(Don't Worry, this won't conflict with Epic Fight))")
                .define(
                "Max Mining Time",
                14);
        ENTER_TICKS = BUILDER.comment(
                "The Amount Of Ticks To Re-Check Before Auto-Switching To 1st Person")
                .define(
                "Enter Ticks",
                8);
        MAX_CHECK_BLOCKS = BUILDER.define(
                "Max Check Blocks",
                250);
        MAX_CHECK_RADIUS = BUILDER.define(
                "Max Check Radius",
                8);
        CHECK_TICKS = BUILDER.define(
                "Max Check Ticks",
                8);

            BUILDER.push("Exit Times");
            ENCLOSED_EXIT_TICKS = BUILDER.comment(
                        "(<For When The Cause Of Switch Is Due To 'Enclosed/Small Room'>) The Amount Of Ticks To Re-Check Before Auto-Switching Back to 3rd Person (Or 2nd Person)")
                .define(
                        "Enclosed Space Exit Ticks",
                        5);
            OBSTRUCTED_EXIT_TICKS = BUILDER.comment(
                            "(<For When The Cause Of Switch Is Due To 'View Obstruction'>) The Amount Of Ticks To Re-Check Before Auto-Switching Back to 3rd Person (Or 2nd Person)")
                    .define(
                            "Obstructed Exit Ticks",
                            5);
            MINING_EXIT_TICKS = BUILDER.comment(
                            "(<For When The Cause Of Switch Is Due To 'Block Mining'>) The Amount Of Ticks To Re-Check Before Auto-Switching Back to 3rd Person (Or 2nd Person)")
                    .define(
                            "Mining Exit Ticks",
                            30);
            BUILDING_EXIT_TICKS = BUILDER.comment(
                            "(<For When The Cause Of Switch Is Due To 'Building/Build Mode' - For Effortless Building Compat>) The Amount Of Ticks To Re-Check Before Auto-Switching Back to 3rd Person (Or 2nd Person)")
                    .define(
                            "Building Exit Ticks",
                            55);
            BUILDER.pop();
        BUILDER.pop();
        BUILDER.push("Compat Values");

        USE_EFFORTLESS_BUILDING_MODE = BUILDER.comment(
                "Whether Effortless Building's, 'Build Mode' Should Force First Person Perspective")
                .define("Use 'Effortless Building Mod' Build Mode",
                true);
        BUILDER.pop();

        BUILDER.push("Fixes");

        USE_ROTATION_FIX = BUILDER.comment(
                        "If 'True' The Player Will Rotate To The Same Direction As The Camera Right When Switching To First Person \n('This Is Useful For Other Mods Which Decouple The Player From The Camera, Allowing A Perspective Switch Without Disorientating You :)')")
                .define(
                        "Use Rotation Fix",
                        true);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
