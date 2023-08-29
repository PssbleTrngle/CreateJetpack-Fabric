package com.possible_triangle.create_jetpack.client

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.PoseStack
import com.possible_triangle.create_jetpack.CreateJetpackMod.MOD_ID
import com.possible_triangle.create_jetpack.config.Configs
import com.possible_triangle.flightlib.api.ControlType
import com.possible_triangle.flightlib.api.FlightKey
import com.possible_triangle.flightlib.api.IFlightApi
import com.possible_triangle.flightlib.api.IJetpack
import com.simibubi.create.content.equipment.armor.BacktankUtil
import io.github.fabricators_of_create.porting_lib.event.client.OverlayRenderCallback
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiComponent
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.phys.Vec2
import kotlin.math.ceil


object ControlsDisplay {

    private val controls = ResourceLocation(MOD_ID, "textures/gui/controls.png")
    private val airIndicator = ResourceLocation(MOD_ID, "textures/gui/air_indicator.png")

    private fun spritePos(index: Int): Vec2 {
        return Vec2(
            index % 2 * 16F,
            index / 2 * 16F,
        )
    }

    private val ICONS = mapOf<FlightKey, (IJetpack.Context) -> ControlType>(
        FlightKey.TOGGLE_ACTIVE to { it.jetpack.activeType(it) },
        FlightKey.TOGGLE_HOVER to { it.jetpack.hoverType(it) },
    )

    fun register() {
        OverlayRenderCallback.EVENT.register(OverlayRenderCallback { stack, partialTicks, window, type ->
            if (type == OverlayRenderCallback.Types.AIR) {
                render(
                    stack,
                    partialTicks,
                    window.guiScaledWidth,
                    window.guiScaledHeight
                )
            }
            false
        })
    }

    fun render(poseStack: PoseStack, partialTicks: Float, width: Int, height: Int) {
        val mc = Minecraft.getInstance()
        if (!Configs.CLIENT.SHOW_OVERLAY.get()) return
        if (mc.options.hideGui) return
        val player = mc.player ?: return
        val context = IFlightApi.INSTANCE.findJetpack(player) ?: return

        val margin = 6
        val scale = Configs.CLIENT.OVERLAY_DISTANCE_SCALE.get().toFloat()
        val spriteWidth = 16 + margin

        val startX = Configs.CLIENT.OVERLAY_DISTANCE_X.get().let {
            if (it >= 0) it
            else width + it - 50
        }.let { it / scale }.toInt()

        val startY = Configs.CLIENT.OVERLAY_DISTANCE_Y.get().let {
            if (it >= 0) it
            else height + it - 24
        }.let { it / scale }.toInt()

        fun renderSprite(index: Int, x: Int) {
            val sprite = spritePos(index)
            RenderSystem.setShaderTexture(0, controls)
            poseStack.scale(scale, scale, scale)
            GuiComponent.blit(poseStack, startX + x, startY, 0, sprite.x, sprite.y, 16, 16, 32, 32)
        }

        val engineActive = FlightKey.TOGGLE_ACTIVE.isPressed(player)

        val renderedIcons = ICONS.filterKeys { it == FlightKey.TOGGLE_ACTIVE || engineActive }
            .filterValues { getType -> getType(context) == ControlType.TOGGLE }
            .keys.mapIndexed { index, key ->
                poseStack.pushPose()

                val active = key.isPressed(player)
                renderSprite(index + if (active) 0 else 2, spriteWidth * index)

                val textScale = 0.5F
                poseStack.scale(textScale, textScale, textScale)
                val textMargin = (startX + 8 + spriteWidth * index) * (1 / textScale)
                val text = Component.translatable("overlay.flightlib.control.${key.name.lowercase()}")
                val color = if (active) 0xFFFFFF else 0xBBBBBB
                GuiComponent.drawCenteredString(
                    poseStack, mc.font, text, textMargin.toInt(), startY * 2 + 36, color
                )
                poseStack.popPose()

            }.count()

        if (engineActive) {
            poseStack.pushPose()

            poseStack.scale(scale, scale, scale)
            RenderSystem.setShaderTexture(0, airIndicator)
            RenderSystem.enableBlend()

            val barWidth = 5
            fun renderBar(index: Int, barHeight: Int = 16, spriteOffset: Int = 0) {
                GuiComponent.blit(
                    poseStack,
                    startX + spriteWidth * renderedIcons,
                    startY + (19 - barHeight) - spriteOffset,
                    (barWidth * index).toFloat(),
                    16F - barHeight - spriteOffset,
                    barWidth,
                    barHeight,
                    16,
                    16
                )
            }

            val blink = player.level.gameTime % 20 < 5
            val airSource = BacktankUtil.getAllWithAir(player).firstOrNull() ?: ItemStack.EMPTY
            val maxAir = BacktankUtil.maxAir(airSource)
            val air = BacktankUtil.getAir(airSource)
            val barHeight = ceil(air / maxAir * 14).toInt()
            val shrinking = context.jetpack.isThrusting(context)

            renderBar(1)
            if (shrinking && barHeight > 0 && blink) renderBar(0, barHeight - 1, 1)
            else renderBar(0, barHeight, 1)

            RenderSystem.disableBlend()
            poseStack.popPose()
        }
    }
}