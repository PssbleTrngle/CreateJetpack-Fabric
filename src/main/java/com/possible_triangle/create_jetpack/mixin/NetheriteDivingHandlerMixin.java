package com.possible_triangle.create_jetpack.mixin;

import com.simibubi.create.content.equipment.armor.NetheriteDivingHandler;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(NetheriteDivingHandler.class)
public class NetheriteDivingHandlerMixin {

    @Redirect(
            method = "onLivingEquipmentChange(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/EquipmentSlot;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/tterrag/registrate/util/entry/ItemEntry;isIn(Lnet/minecraft/world/item/ItemStack;)Z"
            ),
            slice = @Slice(
                    from = @At(
                            value = "FIELD",
                            target = "Lnet/minecraft/world/entity/EquipmentSlot;CHEST:Lnet/minecraft/world/entity/EquipmentSlot;"
                    ),
                    to = @At("TAIL")
            )
    )
    private static boolean modifyNetheritePredicate(ItemEntry instance, ItemStack stack) {
        return stack.getItem() instanceof ArmorItem armor && armor.getMaterial() == ArmorMaterials.NETHERITE;
    }

}
