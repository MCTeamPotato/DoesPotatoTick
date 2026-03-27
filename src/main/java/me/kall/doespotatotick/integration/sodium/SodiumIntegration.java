package me.kall.doespotatotick.integration.sodium;

import com.google.common.collect.ImmutableList;
import me.kall.doespotatotick.DoesPotatoTick;
import me.kall.doespotatotick.config.TickConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.NeoForge;
import org.embeddedt.embeddium.api.OptionGUIConstructionEvent;
import org.embeddedt.embeddium.api.options.OptionIdentifier;
import org.embeddedt.embeddium.api.options.control.TickBoxControl;
import org.embeddedt.embeddium.api.options.structure.OptionGroup;
import org.embeddedt.embeddium.api.options.structure.OptionImpact;
import org.embeddedt.embeddium.api.options.structure.OptionImpl;
import org.embeddedt.embeddium.api.options.structure.OptionPage;
import org.embeddedt.embeddium.impl.gui.EmbeddiumOptions;
import org.embeddedt.embeddium.impl.gui.options.storage.EmbeddiumOptionsStorage;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SodiumIntegration {
    private static final EmbeddiumOptionsStorage storage = new EmbeddiumOptionsStorage();

    private static @NotNull List<OptionGroup> groups() {
        List<OptionGroup> groups = new ArrayList<>();
        OptionImpl<EmbeddiumOptions, Boolean> skipRenderingUntickable = booleanOptionFor("doespotatotick.skip.untickable", "doespotatotick.skip.untickable.tooltip", TickConfig.Client.SKIP_RENDERING_UNTICKABLE, OptionImpact.HIGH);
        OptionImpl<EmbeddiumOptions, Boolean> projectileWeaponSkip = booleanOptionFor("doespotatotick.skip.shoot", "doespotatotick.skip.shoot.tooltip", TickConfig.Client.PROJECTILE_WEAPON_SKIP, OptionImpact.LOW);
        groups.add(OptionGroup.createBuilder().setId(ResourceLocation.fromNamespaceAndPath(DoesPotatoTick.MOD_ID, "does_potato_tick_group")).add(skipRenderingUntickable).add(projectileWeaponSkip).setId(ResourceLocation.fromNamespaceAndPath(DoesPotatoTick.MOD_ID, DoesPotatoTick.MOD_ID)).build());
        return groups;
    }

    private static OptionImpl<EmbeddiumOptions, Boolean> booleanOptionFor(@NotNull String name, String tooltip, ModConfigSpec.BooleanValue config, OptionImpact impact) {
        return OptionImpl.createBuilder(Boolean.class, storage)
                .setId(ResourceLocation.fromNamespaceAndPath(DoesPotatoTick.MOD_ID, name.split("\\.")[2]))
                .setName(Component.translatable(name))
                .setTooltip(Component.translatable(tooltip))
                .setControl(TickBoxControl::new)
                .setBinding((sodiumGameOptions, value) -> config.set(value), sodiumGameOptions -> config.get())
                .setImpact(impact)
                .build();
    }

    private static void onPageLoad(@NotNull OptionGUIConstructionEvent event) {
        event.addPage(new OptionPage(OptionIdentifier.create(DoesPotatoTick.MOD_ID, "config"), Component.translatable("doespotatotick.page"), ImmutableList.copyOf(groups())));
    }

    public static void register() {
        NeoForge.EVENT_BUS.addListener(SodiumIntegration::onPageLoad);
    }
}
