package com.izusan.mekanism_creategoggles;

import com.izusan.mekanism_creategoggles.client.GogglesModuleHudRenderer;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

/**
 * Client-only setup and event wiring.
 */
@Mod(value = MekanismCreateGogglesMod.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = MekanismCreateGogglesMod.MOD_ID, value = Dist.CLIENT)
public class MekanismCreateGogglesModClient {

    public MekanismCreateGogglesModClient(ModContainer container) {
        // Provide a basic config screen hook (even if unused for now).
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        // Currently unused, but kept for potential future client-only setup.
    }

    @SubscribeEvent
    static void registerGuiLayers(RegisterGuiLayersEvent event) {
        // Render our goggles overlay above the hotbar, similar to Create's own overlay.
        event.registerAbove(VanillaGuiLayers.HOTBAR,
                ResourceLocation.fromNamespaceAndPath(MekanismCreateGogglesMod.MOD_ID, "goggle_info"),
                GogglesModuleHudRenderer.OVERLAY);
    }
}
