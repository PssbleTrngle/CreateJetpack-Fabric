package com.possible_triangle.create_jetpack.compat

import com.simibubi.create.AllTags.AllItemTags.PRESSURIZED_AIR_SOURCES
import com.simibubi.create.content.equipment.armor.BacktankUtil
import dev.emi.trinkets.api.TrinketsApi
import kotlin.jvm.optionals.getOrNull

object TrinketsCompat {

    fun register() {
        BacktankUtil.addBacktankSupplier { entity ->
            val trinkets = TrinketsApi.getTrinketComponent(entity).getOrNull()

            trinkets?.allEquipped
                ?.map { tuple -> tuple.b }
                ?.filter { PRESSURIZED_AIR_SOURCES.matches(it) }
                ?: emptyList()
        }
    }

}