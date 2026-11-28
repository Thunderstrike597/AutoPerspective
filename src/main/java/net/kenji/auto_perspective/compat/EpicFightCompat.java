package net.kenji.auto_perspective.compat;

import net.kenji.auto_perspective.ConfigClient;
import net.kenji.auto_perspective.PerspectiveManager;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.SwordItem;

import net.neoforged.neoforge.client.event.ClientTickEvent;
import yesman.epicfight.api.animation.AnimationPlayer;
import yesman.epicfight.api.animation.Animator;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.DynamicAnimation;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.api.client.event.EpicFightClientEventHooks;
import yesman.epicfight.api.client.event.types.camera.LockOnEvent;
import yesman.epicfight.api.event.EpicFightEventHooks;
import yesman.epicfight.api.event.types.player.ComboAttackEvent;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

import java.util.UUID;

public class EpicFightCompat {

    private static long clientTicks = 0;
    private static long lastLockOnTick = Long.MIN_VALUE / 2;
    private static boolean hasLockonTarget = false;
    private static int didAttackTimer = 0;

    public static boolean getHasLockonTarget(){
        return hasLockonTarget;
    };
    public static boolean getDidAttack(){
        return didAttackTimer > 0;
    };

    public static void resetDidAttackTimer(){
        didAttackTimer = ConfigClient.EPICFIGHT_ATTACKING_EXIT_TICKS.get();
    }

    public static void init(){
        EpicFightClientEventHooks.Camera.LOCK_ON_TICK.registerEvent(EpicFightCompat::tickLockOn);
        EpicFightEventHooks.Player.COMBO_ATTACK.registerEvent(EpicFightCompat::onBasicAttack);
    }

    public static void tickEpicFightCompat(ClientTickEvent.Post event){
        Minecraft mc = Minecraft.getInstance();

        Player player = mc.player;
        if(player == null)return;

        PlayerPatch<?> patch = EpicFightCapabilities.getPlayerPatch(player);
        if(patch == null)return;

        if(didAttackTimer > 0) {
            didAttackTimer--;
        }

        if(patch.isEpicFightMode()){
            if(patch.getOriginal().getMainHandItem().getItem() instanceof SwordItem swordItem){
                PerspectiveManager.setBlockMiningTimerSwitch(true);
            }
            else PerspectiveManager.setBlockMiningTimerSwitch(false);

        }
        else PerspectiveManager.setBlockMiningTimerSwitch(false);

        Animator animator = patch.getAnimator();
        if(animator != null){
            AnimationPlayer animPlayer = animator.getPlayerFor(null);
            if(animPlayer != null){
                AssetAccessor<? extends DynamicAnimation> animationAccessor = animPlayer.getAnimation();
                if(animationAccessor != null){
                    if(animationAccessor.get() instanceof AttackAnimation){
                        resetDidAttackTimer();
                    }
                }
            }
        }

        hasLockonTarget = (clientTicks - lastLockOnTick) <= 2;

    }
    public static void onBasicAttack(ComboAttackEvent event){
        resetDidAttackTimer();
    }
    public static void tickLockOn(LockOnEvent.Tick event) {
        lastLockOnTick = clientTicks;
    }

}
