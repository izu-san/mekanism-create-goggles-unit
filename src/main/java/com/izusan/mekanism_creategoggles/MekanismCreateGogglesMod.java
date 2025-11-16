package com.izusan.mekanism_creategoggles;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.izusan.mekanism_creategoggles.register.ModCreativeTabs;
import com.izusan.mekanism_creategoggles.register.ModItems;
import com.izusan.mekanism_creategoggles.register.ModModules;

import mekanism.api.MekanismIMC;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

/**
 * Mekanism-Create Goggles Unit
 * - Adds an Engineer's Goggles-style HUD to Mekanism's MekaSuit helmet when Create is present.
 */
@Mod(MekanismCreateGogglesMod.MOD_ID)
public class MekanismCreateGogglesMod {
    public static final String MOD_ID = "mekanism_creategoggles";
    public static final Logger LOGGER = LogUtils.getLogger();

    public MekanismCreateGogglesMod(IEventBus modEventBus, ModContainer modContainer) {
        // Common setup
        modEventBus.addListener(this::commonSetup);

        // Registries
        ModItems.register(modEventBus);
        ModModules.register(modEventBus);
        ModCreativeTabs.CREATIVE_TABS.register(modEventBus);

        // IMC
        modEventBus.addListener(this::imcQueue);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("Mekanism-Create Goggles Unit mod initializing");
    }

    /**
     * Register our module as a valid MekaSuit helmet module with Mekanism via IMC.
     */
    private void imcQueue(InterModEnqueueEvent event) {
        MekanismIMC.addMekaSuitHelmetModules(ModModules.GOGGLES_UNIT);
    }

}


