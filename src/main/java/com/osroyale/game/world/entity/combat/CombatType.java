package com.osroyale.game.world.entity.combat;

import com.osroyale.game.world.entity.combat.formula.MagicFormula;
import com.osroyale.game.world.entity.combat.formula.MeleeFormula;
import com.osroyale.game.world.entity.combat.formula.RangedFormula;
import com.osroyale.game.world.entity.mob.Mob;

public enum CombatType {
    MELEE(new MeleeFormula()),
    RANGED(new RangedFormula()),
    MAGIC(new MagicFormula());

    final FormulaModifier<Mob> formula;

    CombatType(FormulaModifier<Mob> formula) {
        this.formula = formula;
    }

    public FormulaModifier<Mob> getFormula() {
        return formula;
    }
}
