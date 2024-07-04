package org.mangorage.mangostorage;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.mangorage.mangostorage.core.Registration;
import org.mangorage.mangostorage.menu.TransmutationMenu;
import org.mangorage.mangostorage.screen.TransmutationScreen;

@Mod(MangoStorage.MODID)
public class MangoStorage {

    public static final String MODID = "mangostorage";

    public MangoStorage() {
        Registration.init();
        ForgeMod.enableMilkFluid();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::client);
    }

    public void client(FMLClientSetupEvent event) {
        MenuScreens.<TransmutationMenu, TransmutationScreen>register(Registration.TRANSMUTATION_MENU.get(), TransmutationScreen::new);
    }
}
