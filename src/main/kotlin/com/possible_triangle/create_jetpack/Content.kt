package com.possible_triangle.create_jetpack

import com.possible_triangle.create_jetpack.CreateJetpackMod.MOD_ID
import com.possible_triangle.create_jetpack.block.JetpackBlock
import com.possible_triangle.create_jetpack.client.ControlsDisplay
import com.possible_triangle.create_jetpack.config.Configs
import com.possible_triangle.create_jetpack.item.BrassJetpack
import com.possible_triangle.flightlib.api.IFlightApi
import com.possible_triangle.flightlib.api.IJetpack
import com.possible_triangle.flightlib.api.ISource
import com.possible_triangle.flightlib.api.sources.EquipmentSource
import com.simibubi.create.AllCreativeModeTabs
import com.simibubi.create.AllTags.AllItemTags
import com.simibubi.create.content.equipment.armor.BacktankBlockEntity
import com.simibubi.create.content.equipment.armor.BacktankInstance
import com.simibubi.create.content.equipment.armor.BacktankItem.BacktankBlockItem
import com.simibubi.create.content.equipment.armor.BacktankRenderer
import com.simibubi.create.content.kinetics.BlockStressDefaults
import com.simibubi.create.foundation.data.AssetLookup
import com.simibubi.create.foundation.data.CreateRegistrate
import com.simibubi.create.foundation.data.SharedProperties
import com.simibubi.create.foundation.data.TagGen
import com.simibubi.create.foundation.item.ItemDescription
import com.simibubi.create.foundation.item.KineticStats
import com.simibubi.create.foundation.item.TooltipHelper
import com.simibubi.create.foundation.item.TooltipModifier
import com.tterrag.registrate.builders.BlockEntityBuilder
import com.tterrag.registrate.util.entry.ItemEntry
import com.tterrag.registrate.util.nullness.NonNullFunction
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue
import net.minecraftforge.api.ModLoadingContext
import net.minecraftforge.fml.config.ModConfig
import java.util.function.BiFunction
import java.util.function.Supplier

object Content {

    private val REGISTRATE = CreateRegistrate.create(MOD_ID)
        .creativeModeTab { AllCreativeModeTabs.BASE_CREATIVE_TAB }
        .setTooltipModifierFactory {
            ItemDescription.Modifier(it, TooltipHelper.Palette.STANDARD_CREATE)
                .andThen(TooltipModifier.mapNull(KineticStats.create(it)))
        }

    val JETPACK_BLOCK = REGISTRATE.block<JetpackBlock>("jetpack", ::JetpackBlock)
        .initialProperties { SharedProperties.copperMetal() }
        .blockstate { c, p ->
            p.horizontalBlock(
                c.entry,
                AssetLookup.partialBaseModel(c, p)
            )
        }
        .transform(TagGen.pickaxeOnly())
        .addLayer { Supplier { RenderType.cutoutMipped() } }
        .transform(BlockStressDefaults.setImpact(4.0))
        .loot { lt, block ->
            val builder = LootTable.lootTable()
            val survivesExplosion = ExplosionCondition.survivesExplosion()
            lt.add(
                block, builder.withPool(
                    LootPool.lootPool()
                        .`when`(survivesExplosion)
                        .setRolls(ConstantValue.exactly(1F))
                        .add(
                            LootItem.lootTableItem(JETPACK.get())
                                .apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY))
                                .apply(
                                    CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
                                        .copy("Air", "Air")
                                )
                                .apply(
                                    CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
                                        .copy("Enchantments", "Enchantments")
                                )
                        )
                )
            )
        }
        .register()

    val JETPACK_TILE = REGISTRATE.blockEntity("jetpack", BlockEntityBuilder.BlockEntityFactory(::BacktankBlockEntity))
        .instance { BiFunction { manager, tile -> BacktankInstance(manager, tile) } }
        .validBlocks(JETPACK_BLOCK)
        .renderer {
            NonNullFunction { context: BlockEntityRendererProvider.Context? ->
                BacktankRenderer(context)
            }
        }
        .register()

    val JETPACK_PLACEABLE = REGISTRATE.item<BacktankBlockItem>("jetpack_placeable") {
        BacktankBlockItem(JETPACK_BLOCK.get(), { JETPACK.get() }, it)
    }.model { context, provider ->
        provider.withExistingParent(context.name, provider.mcLoc("item/barrier"))
    }.register()

    val JETPACK: ItemEntry<BrassJetpack> =
        REGISTRATE.item<BrassJetpack>("jetpack") { BrassJetpack(it, JETPACK_PLACEABLE) }
            .model(AssetLookup.customGenericItemModel("_", "item"))
            .tag(AllItemTags.PRESSURIZED_AIR_SOURCES.tag)
            .register()

    fun register() {
        REGISTRATE.register()

        ModLoadingContext.registerConfig(MOD_ID, ModConfig.Type.COMMON, Configs.SERVER_SPEC)
        ModLoadingContext.registerConfig(MOD_ID, ModConfig.Type.CLIENT, Configs.CLIENT_SPEC)

        Configs.Network.register()

        ServerPlayConnectionEvents.JOIN.register { it, _, _ -> Configs.syncConfig(it.player) }
    }

    fun clientInit() {
        ControlsDisplay.register()
    }

}