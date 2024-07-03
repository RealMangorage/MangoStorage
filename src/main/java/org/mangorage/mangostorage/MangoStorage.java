package org.mangorage.mangostorage;

import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fml.common.Mod;
import org.mangorage.mangostorage.core.Registration;

@Mod(MangoStorage.MODID)
public class MangoStorage {

    public static final String MODID = "mangostorage";

    public MangoStorage() {
        Registration.init();

        ForgeMod.enableMilkFluid();
    }
}
