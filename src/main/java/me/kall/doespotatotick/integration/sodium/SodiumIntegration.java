package me.kall.doespotatotick.integration.sodium;

import com.google.common.collect.ImmutableList;
import me.jellysquid.mods.sodium.client.gui.SodiumGameOptions;
import me.jellysquid.mods.sodium.client.gui.options.OptionGroup;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpact;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpl;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.jellysquid.mods.sodium.client.gui.options.storage.SodiumOptionsStorage;
import me.kall.doespotatotick.DoesPotatoTick;
import me.kall.doespotatotick.config.ConfigConstants;
import me.kall.doespotatotick.config.TickConfig;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import org.embeddedt.embeddium.api.OptionGUIConstructionEvent;
import org.embeddedt.embeddium.client.gui.options.OptionIdentifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SodiumIntegration {
    private static final SodiumOptionsStorage storage = new SodiumOptionsStorage();

    private static @NotNull List<OptionGroup> groups() {
        List<OptionGroup> groups = new ArrayList<>();
        OptionImpl<SodiumGameOptions, Boolean> skipRenderingUntickable = booleanOptionFor("doespotatotick.skip.untickable", "doespotatotick.skip.untickable.tooltip", TickConfig.Client.SKIP_RENDERING_UNTICKABLE, OptionImpact.HIGH);
        OptionImpl<SodiumGameOptions, Boolean> projectileWeaponSkip = booleanOptionFor("doespotatotick.skip.shoot", "doespotatotick.skip.shoot.tooltip", TickConfig.Client.PROJECTILE_WEAPON_SKIP, OptionImpact.LOW);
        groups.add(OptionGroup.createBuilder().add(skipRenderingUntickable).add(projectileWeaponSkip).build());
        return groups;
    }

    private static OptionImpl<SodiumGameOptions, Boolean> booleanOptionFor(String name, String tooltip, ForgeConfigSpec.BooleanValue config, OptionImpact impact) {
        return OptionImpl.createBuilder(Boolean.class, storage)
                .setName(Component.translatable(name))
                .setTooltip(Component.translatable(tooltip))
                .setControl(TickBoxControl::new)
                .setBinding((sodiumGameOptions, value) -> {
                    config.set(value);
                    ConfigConstants.validateClient();
                }, sodiumGameOptions -> config.get())
                .setImpact(impact)
                .build();
    }

    private static void onPageLoad(@NotNull OptionGUIConstructionEvent event) {
        event.addPage(new OptionPage(OptionIdentifier.create(DoesPotatoTick.MOD_ID, "config"), Component.translatable("doespotatotick.page"), ImmutableList.copyOf(groups())));
    }

    public static void register() {
        MinecraftForge.EVENT_BUS.addListener(SodiumIntegration::onPageLoad);
    }
}
