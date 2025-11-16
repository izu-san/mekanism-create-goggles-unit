package com.izusan.mekanism_creategoggles.register;

import com.izusan.mekanism_creategoggles.MekanismCreateGogglesMod;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Creative tab registration for this mod.
 */
public class ModCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MekanismCreateGogglesMod.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN_TAB =
            CREATIVE_TABS.register("main", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.mekanism_creategoggles"))
                    .icon(() -> ModItems.GOGGLES_UNIT.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.GOGGLES_UNIT);
                    })
                    .build());

    private ModCreativeTabs() {
    }
}


