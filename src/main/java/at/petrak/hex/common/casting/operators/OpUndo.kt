package at.petrak.hex.common.casting.operators

import at.petrak.hex.api.ConstManaOperator
import at.petrak.hex.api.SpellDatum
import at.petrak.hex.common.casting.CastingContext

object OpUndo : ConstManaOperator {
    override val argc = 1

    // Do literally nothing!
    override fun execute(args: List<SpellDatum<*>>, ctx: CastingContext): List<SpellDatum<*>> =
        emptyList()
}