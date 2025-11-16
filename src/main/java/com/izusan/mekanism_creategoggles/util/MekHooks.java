package com.izusan.mekanism_creategoggles.util;

import com.izusan.mekanism_creategoggles.register.ModModules;

import mekanism.api.gear.IModuleHelper;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;

/**
 * Small Mekanism-related helper utilities.
 */
public class MekHooks {

    /**
     * Returns true if the player has our Goggles module equipped on their MekaSuit helmet
     * and the module is currently enabled.
     */
    public static boolean hasGogglesModuleEquipped(Player player) {
        if (player == null) {
            return false;
        }
        return IModuleHelper.INSTANCE.getIfEnabled(player, EquipmentSlot.HEAD, ModModules.GOGGLES_UNIT) != null;
    }

    private MekHooks() {
    }
}


