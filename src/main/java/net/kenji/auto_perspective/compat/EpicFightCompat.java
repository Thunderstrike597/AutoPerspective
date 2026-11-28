package net.kenji.auto_perspective.compat;

import net.kenji.auto_perspective.AutoPerspective;
import net.kenji.auto_perspective.ConfigClient;
import net.kenji.auto_perspective.PerspectiveManager;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import yesman.epicfight.api.animation.AnimationPlayer;
import yesman.epicfight.api.animation.Animator;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.DynamicAnimation;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.api.client.event.EpicFightClientHooks;
import yesman.epicfight.api.client.event.types.LockOnEvent;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.entity.eventlistener.BasicAttackEvent;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;

import java.util.UUID;

public class EpicFightCompat {
    private static final UUID BASIC_ATTACK_LISTENER = UUID.fromString("3f6c2a1e-8b4d-4c7a-9e2f-1a5b6c7d8e9f");


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
        EpicFightClientHooks.Camera.LOCK_ON_START.registerEvent(EpicFightCompat::onLockonStart);
        EpicFightClientHooks.Camera.LOCK_ON_RELEASED.registerEvent(EpicFightCompat::onLockonEnd);
    }

    public static void onPlayerJoin(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide()) return;
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        ServerPlayerPatch patch = EpicFightCapabilities.getEntityPatch(player, ServerPlayerPatch.class);
        if (patch == null) return;

        if (AutoPerspective.isClassPresent("yesman.epicfight.world.entity.eventlistener.BasicAttackEvent")) {
            patch.getEventListener().addEventListener(
                    PlayerEventListener.EventType.BASIC_ATTACK_EVENT,
                    BASIC_ATTACK_LISTENER,
                    EpicFightCompat::onBasicAttack
            );
        }
    }
    public static void tickEpicFightCompat(TickEvent.ClientTickEvent event){
        if (event.phase != TickEvent.Phase.END) return;
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
    }
    public static void onBasicAttack(BasicAttackEvent event){
        resetDidAttackTimer();
    }

    public static void onLockonStart(LockOnEvent.Tick.Start event) {
        hasLockonTarget = true;
    }

    public static void onLockonEnd(LockOnEvent.Tick.Release event) {
        hasLockonTarget = false;
    }

}
