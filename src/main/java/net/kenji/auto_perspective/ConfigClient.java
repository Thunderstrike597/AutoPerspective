package net.kenji.auto_perspective;


import net.neoforged.neoforge.common.ModConfigSpec;

public class ConfigClient {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static ModConfigSpec.ConfigValue<Boolean> USE_ROTATION_FIX;

    public static ModConfigSpec.ConfigValue<Boolean> DISABLE_AUTO_SWITCH;
    public static ModConfigSpec.ConfigValue<Boolean> USE_SWITCH_COOLDOWN;

    public static ModConfigSpec.ConfigValue<Boolean> USE_CAMERA_OBSTRUCTION_DETECTION;
    public static ModConfigSpec.ConfigValue<Boolean> USE_SMALL_SPACE_CHECK;
    public static ModConfigSpec.ConfigValue<Boolean> USE_CEILING_CHECK;
    public static ModConfigSpec.ConfigValue<Boolean> USE_MINING_TIMER;
    public static ModConfigSpec.ConfigValue<Boolean> USE_EFFORTLESS_BUILDING_MODE;

    public static ModConfigSpec.ConfigValue<Double> MIN_CAMERA_OBSTRUCTION_DIST;

    public static ModConfigSpec.ConfigValue<Integer> SWITCH_COOLDOWN_TICKS;

    public static ModConfigSpec.ConfigValue<Integer> MAX_CEILING_CHECK_DIST;

    public static ModConfigSpec.ConfigValue<Integer> MAX_CHECK_BLOCKS;
    public static ModConfigSpec.ConfigValue<Integer> MAX_CHECK_RADIUS;
    public static ModConfigSpec.ConfigValue<Integer> CHECK_TICKS;

    public static ModConfigSpec.ConfigValue<Integer> ENTER_TICKS;

    public static ModConfigSpec.ConfigValue<Integer> ENCLOSED_EXIT_TICKS;
    public static ModConfigSpec.ConfigValue<Integer> OBSTRUCTED_EXIT_TICKS;
    public static ModConfigSpec.ConfigValue<Integer> MINING_EXIT_TICKS;
    public static ModConfigSpec.ConfigValue<Integer> BUILDING_EXIT_TICKS;



    public static ModConfigSpec.ConfigValue<Integer> MAX_MINING_TIMER;


    static {
        DISABLE_AUTO_SWITCH = BUILDER.comment(
                        "An Option To Disable All Auto Switching Checks! Why tho?")
                .define(
                        "Disable Auto-Switch",
                        false);


        BUILDER.push("Check Usage");

        USE_SWITCH_COOLDOWN = BUILDER.comment(
                        "Whether Or Not There Should Be A Cooldown For The Auto-Switch")
                .define(
                        "Use Switch Cooldown",
                        true);
        USE_CAMERA_OBSTRUCTION_DETECTION = BUILDER.comment(
                        "Whether Or Not The Player Being Too Close To The Camera Should Contribute To The Auto-Switch Check")
                .define(
                        "Use Camera Obstruction Check",
                        true);
        USE_SMALL_SPACE_CHECK = BUILDER.comment(
                        "Whether Or Not Being In Small Spaces Or Rooms Should Contribute To The Auto-Switch Check")
                .define(
                        "Use Small Space Check",
                        true);
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

        BUILDER.pop();
        BUILDER.push("Values");

        SWITCH_COOLDOWN_TICKS = BUILDER.comment(
                        "The Number Of Ticks Cooldown Should Have For The 'Use Switch Cooldown' Config Option")
                .define(
                        "Switch Cooldown Ticks",
                        12);
        MIN_CAMERA_OBSTRUCTION_DIST = BUILDER.comment(
                        "The Distance From The Player The Camera Should Be Before Switching To First Person For The Config Option 'Use Camera Obstruction Check'")
                .define(
                        "Min Camera Obstruction Check Distance",
                        4.0);
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
