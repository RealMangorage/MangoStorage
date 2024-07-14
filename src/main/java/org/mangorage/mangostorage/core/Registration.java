package org.mangorage.mangostorage.core;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.mangorage.mangostorage.blocks.BlackHoleStorageBlock;
import org.mangorage.mangostorage.blocks.TransmutationBlock;
import org.mangorage.mangostorage.blocks.entities.BlackHoleStorageBlockEntity;
import org.mangorage.mangostorage.blocks.entities.TransmutationBlockEntity;
import org.mangorage.mangostorage.menu.BlackHoleStorageUnitMenu;
import org.mangorage.mangostorage.menu.TransmutationMenu;

import java.util.function.Supplier;

import static org.mangorage.mangostorage.MangoStorage.MODID;

public class Registration {
    private static <T> DeferredRegister<T> register(DeferredRegister<T> register) {
        register.register(FMLJavaModLoadingContext.get().getModEventBus());
        return register;
    }

    public static final DeferredRegister<Block> BLOCKS = register(DeferredRegister.create(ForgeRegistries.BLOCKS, MODID));
    public static final DeferredRegister<Item> ITEMS = register(DeferredRegister.create(ForgeRegistries.ITEMS, MODID));
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = register(DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MODID));
    public static final DeferredRegister<MenuType<?>> MENU_TYPE = register(DeferredRegister.create(ForgeRegistries.MENU_TYPES, MODID));
    public static final DeferredRegister<CreativeModeTab> TABS = register(DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID));

    public static <T extends Block> RegistryObject<BlockItem> registerBlockItem(RegistryObject<T> registryObject, Supplier<Item.Properties> propertiesSupplier) {
        return ITEMS.register(registryObject.getId().getPath(), () -> new BlockItem(registryObject.get(), propertiesSupplier.get()));
    }


    // Blocks
    public static final RegistryObject<TransmutationBlock> TRANSMUTATION_BLOCK = BLOCKS.register("transmutation", () -> new TransmutationBlock(BlockBehaviour.Properties.of()));
    public static final RegistryObject<BlackHoleStorageBlock> BASIC_BLACK_HOLE_UNIT_BLOCK = BLOCKS.register("basic_black_hole_unit", () -> new BlackHoleStorageBlock(BlockBehaviour.Properties.of()));

    // Items
    public static final RegistryObject<BlockItem> TRANSMUTATION_ITEM = ITEMS.register("transmutation", () -> new BlockItem(TRANSMUTATION_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> BASIC_BLACK_HOLE_UNIT_ITEM = registerBlockItem(BASIC_BLACK_HOLE_UNIT_BLOCK, () -> new Item.Properties().stacksTo(1));

    // Block Entity
    public static final RegistryObject<BlockEntityType<TransmutationBlockEntity>> TRANSMUTATION_BLOCK_ENTITY = BLOCK_ENTITIES.register("transmutation",
            () -> BlockEntityType.Builder.of(
                    TransmutationBlockEntity::new,
                    TRANSMUTATION_BLOCK.get()
            ).build(null)
    );
    public static final RegistryObject<BlockEntityType<BlackHoleStorageBlockEntity>> BLACK_HOLE_STORAGE_UNIT_BLOCK_ENTITY = BLOCK_ENTITIES.register("black_hole_storage_unit",
            () -> BlockEntityType.Builder.of(
                    BlackHoleStorageBlockEntity::new,
                    BASIC_BLACK_HOLE_UNIT_BLOCK.get()
            ).build(null));

    // Menus
    public static final RegistryObject<MenuType<TransmutationMenu>> TRANSMUTATION_MENU = MENU_TYPE.register("transmutation", () -> IForgeMenuType.create(TransmutationMenu::new));
    public static final RegistryObject<MenuType<BlackHoleStorageUnitMenu>> BLACK_HOLE_UNIT_MENU = MENU_TYPE.register("black_hole_unit", () -> IForgeMenuType.create(BlackHoleStorageUnitMenu::new));

    // Creative Tab
    public static final RegistryObject<CreativeModeTab> MOD_TAB = TABS.register("transmutation", () -> CreativeModeTab.builder()
            .icon(() -> TRANSMUTATION_ITEM.get().getDefaultInstance())
            .displayItems(
                    ((pParameters, pOutput) -> {
                        pOutput.accept(TRANSMUTATION_ITEM.get());
                        pOutput.accept(BASIC_BLACK_HOLE_UNIT_ITEM.get());
                    })
            )
            .build()
    );

    public static void init() {
    }
}
