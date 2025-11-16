package com.izusan.mekanism_creategoggles.register;

import com.izusan.mekanism_creategoggles.MekanismCreateGogglesMod;
import com.izusan.mekanism_creategoggles.module.ModuleGogglesUnit;

import mekanism.common.registration.impl.ModuleDeferredRegister;
import mekanism.common.registration.impl.ModuleRegistryObject;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;

/**
 * Mekanism module registration for this mod.
 */
public class ModModules {
    public static final ModuleDeferredRegister MODULES = new ModuleDeferredRegister(MekanismCreateGogglesMod.MOD_ID);

    public static final ModuleRegistryObject<ModuleGogglesUnit> GOGGLES_UNIT =
            MODULES.registerInstanced("goggles_unit", ModuleGogglesUnit::new,
                    () -> ModItems.GOGGLES_UNIT,
                    builder -> builder.maxStackSize(1));

    private ModModules() {
    }

    public static void register(IEventBus bus) {
        MODULES.register(bus);
    }
}


