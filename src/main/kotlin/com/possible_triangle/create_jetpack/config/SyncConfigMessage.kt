package com.possible_triangle.create_jetpack.config

import com.possible_triangle.create_jetpack.CreateJetpackMod
import net.minecraft.network.FriendlyByteBuf

class SyncConfigMessage(private val config: IServerConfig) {

    companion object {

        fun decode(buf: FriendlyByteBuf): SyncConfigMessage {
            val config = SyncedConfig(
                usesPerTank = buf.readInt(),
                usesPerTankHover = buf.readInt(),
                horizontalSpeed = buf.readDouble(),
                verticalSpeed = buf.readDouble(),
                acceleration = buf.readDouble(),
                hoverSpeed = buf.readDouble(),
                swimModifier = buf.readDouble(),
                elytraBoostEnabled = buf.readBoolean(),
            )
            return SyncConfigMessage(config)
        }
    }

    fun encode(buf: FriendlyByteBuf) {
        buf.writeInt(config.usesPerTank)
        buf.writeInt(config.usesPerTankHover)
        buf.writeDouble(config.horizontalSpeed)
        buf.writeDouble(config.verticalSpeed)
        buf.writeDouble(config.acceleration)
        buf.writeDouble(config.hoverSpeed)
        buf.writeDouble(config.swimModifier)
        buf.writeBoolean(config.elytraBoostEnabled)
    }

    fun handle() {
        CreateJetpackMod.LOGGER.debug("Hover speed: ${config.hoverSpeed}")
        Configs.SYNCED_SERVER = config
    }

}