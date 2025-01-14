package at.petrak.hex.common.casting.operators

import at.petrak.hex.api.OperationResult
import at.petrak.hex.api.Operator
import at.petrak.hex.api.Operator.Companion.getChecked
import at.petrak.hex.api.RenderedSpell
import at.petrak.hex.api.SpellDatum
import at.petrak.hex.common.casting.CastingContext
import at.petrak.hex.common.casting.CastingHarness

object OpEval : Operator {
    override fun modifyStack(stack: MutableList<SpellDatum<*>>, ctx: CastingContext): OperationResult {
        val instrs: List<SpellDatum<*>> = stack.getChecked(stack.lastIndex)
        stack.removeLastOrNull()

        ctx.incDepth()
        val harness = CastingHarness(ctx)
        harness.stack.addAll(stack)
        stack.clear()

        val spellsToCast = mutableListOf<RenderedSpell>()
        for (pat in instrs) {
            val res = harness.update(pat.tryGet())
            when (res) {
                is CastingHarness.CastResult.Error -> throw res.exn
                is CastingHarness.CastResult.Cast -> spellsToCast.addAll(res.spells)
                else -> {}
            }
            if (res.shouldQuit()) break
        }
        stack.addAll(harness.stack)

        return OperationResult(500_000, spellsToCast)
    }
}