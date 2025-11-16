package com.izusan.mekanism_creategoggles.register;

import com.izusan.mekanism_creategoggles.MekanismCreateGogglesMod;

import mekanism.common.item.ItemModule;
import mekanism.common.registration.impl.ItemDeferredRegister;
import mekanism.common.registration.impl.ItemRegistryObject;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;

/**
 * Item registration for this mod.
 * Uses Mekanism's ItemDeferredRegister so that module items are correctly linked.
 */
public class ModItems {
    public static final ItemDeferredRegister ITEMS = new ItemDeferredRegister(MekanismCreateGogglesMod.MOD_ID);

    public static final ItemRegistryObject<ItemModule> GOGGLES_UNIT =
            ITEMS.registerModule(ModModules.GOGGLES_UNIT, Rarity.RARE);

    private ModItems() {
    }

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}


