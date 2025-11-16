package com.izusan.mekanism_creategoggles.module;

import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.gear.ICustomModule;

/**
 * Goggles unit module.
 * This module itself does not apply any effects; it serves as a toggleable flag.
 * The Create-style overlay is rendered by a separate client HUD handler
 * when Mekanism reports that this module is equipped and enabled.
 */
@NothingNullByDefault
public class ModuleGogglesUnit implements ICustomModule<ModuleGogglesUnit> {
}


