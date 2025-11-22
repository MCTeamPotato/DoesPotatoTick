package me.kall.doespotatotick.mixin.client.sodium;

import com.google.common.collect.ImmutableList;
import me.jellysquid.mods.sodium.client.gui.SodiumOptionsGUI;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import me.kall.doespotatotick.integration.sodium.SodiumIntegration;
import net.minecraft.network.chat.TranslatableComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = SodiumOptionsGUI.class, remap = false)
public abstract class SodiumOptionsGUIMixin {
    @Shadow @Final private List<OptionPage> pages;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(CallbackInfo ci) {
        this.pages.add(new OptionPage(new TranslatableComponent("doespotatotick.page"), ImmutableList.copyOf(SodiumIntegration.groups())));
    }
}
