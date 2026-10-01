package net.kenji.first_person_auto_switch;

import net.minecraftforge.fml.loading.FMLLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CompatMixinPlugin extends CompatManager implements IMixinConfigPlugin {



    @Override
    public List<String> getMixins() {
        List<String> mixins = new ArrayList<>();



        return mixins;
    }
    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (isEpicFightPresent()) {
            if (!isSmoothF5Present()) {
                if (mixinClassName.endsWith("SmoothCameraMixin")) {
                    return false;
                }
                if (mixinClassName.endsWith("SmoothF5GameRendererMixin")) {
                    return false;
                }
            }

            if (mixinClassName.endsWith("RenderEngineMixin")) {
                return false;
            }
        }
        if(!isEffortlessBuildngPresent()){
            if(mixinClassName.endsWith("EBFarLookMixin")){
                return false;
            }
            if(mixinClassName.endsWith("EBLineOriginMixin")){
                return false;
            }
            if(mixinClassName.endsWith("EBLookVecMixin")){
                return false;
            }
        }
        if(!isSophistocatedBuildngPresent()){
            if(mixinClassName.endsWith("SBFarLookMixin")){
                return false;
            }
            if(mixinClassName.endsWith("SBLineOriginMixin")){
                return false;
            }
            if(mixinClassName.endsWith("SBLookVecMixin")){
                return false;
            }
        }
        return true;
    }

    // unused methods:
    @Override public void onLoad(String mixinPackage) {}
    @Override public String getRefMapperConfig() { return null; }
    @Override public void acceptTargets(Set<String> a, Set<String> b) {}
    @Override
    public void preApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {}
    @Override
    public void postApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {}
}