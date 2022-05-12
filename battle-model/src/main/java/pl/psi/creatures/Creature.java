package pl.psi.creatures;//  ******************************************************************

//
//  Copyright 2022 PSI Software AG. All rights reserved.
//  PSI PROPRIETARY/CONFIDENTIAL. Use is subject to license terms
//
//  ******************************************************************

import com.google.common.collect.Range;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Random;
import lombok.Getter;
import pl.psi.TurnQueue;
import pl.psi.artifacts.Artifact;
import pl.psi.artifacts.ArtifactApplierTarget;
import pl.psi.artifacts.ArtifactApplyingMode;
import pl.psi.artifacts.ArtifactEffect;
import pl.psi.artifacts.ArtifactInvalidException;
import pl.psi.artifacts.ArtifactType;

/**
 * TODO: Describe this class (The first line - until the first dot - will interpret as the brief description).
 */
@Getter
public class Creature implements PropertyChangeListener {

    private CreatureStatistic stats;
    private int amount;
    private int currentHp;
    private int counterAttackCounter = 1;
    private DamageCalculatorIf calculator;

    Creature() {
    }

    public Creature(final CreatureStatistic aStats, final DamageCalculatorIf aCalculator,
        final int aAmount) {
        stats = aStats;
        amount = aAmount;
        currentHp = stats.getMaxHp();
        calculator = aCalculator;
    }

    public void attack(final Creature aDefender) {
        if (isAlive()) {
            final int damage = getCalculator().calculateDamage(this, aDefender);
            applyDamage(aDefender, damage);
            if (canCounterAttack(aDefender)) {
                counterAttack(aDefender);
            }
        }
    }

    public boolean isAlive() {
        return getAmount() > 0;
    }

    private void applyDamage(final Creature aDefender, final int aDamage) {
        aDefender.setCurrentHp(aDefender.getCurrentHp() - aDamage);
    }

    protected void setCurrentHp(final int aCurrentHp) {
        currentHp = aCurrentHp;
    }

    private boolean canCounterAttack(final Creature aDefender) {
        return aDefender.getCounterAttackCounter() > 0 && aDefender.getCurrentHp() > 0;
    }

    private void counterAttack(final Creature aAttacker) {
        final int damage = aAttacker.getCalculator()
            .calculateDamage(aAttacker, this);
        applyDamage(this, damage);
        aAttacker.counterAttackCounter--;
    }

    Range<Integer> getDamage() {
        return stats.getDamage();
    }

    int getAttack() {
        return stats.getAttack();
    }

    int getArmor() {
        return stats.getArmor();
    }

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        if (TurnQueue.END_OF_TURN.equals(evt.getPropertyName())) {
            counterAttackCounter = 1;
        }
    }

    protected void restoreCurrentHpToMax() {
        currentHp = stats.getMaxHp();
    }

    public String getName() {
        return stats.getName();
    }

    public int getMoveRange() {
        return stats.getMoveRange();
    }

    public void applyArtifact(final Artifact aArtifact) {
        if (!ArtifactType.PRIMARY.equals(aArtifact.getType())) {
            throw new ArtifactInvalidException("Artifact must be primary.");
        }

        aArtifact.getEffects().forEach(this::applySpecificEffectToCreatureStatistic);
    }

    private void applySpecificEffectToCreatureStatistic(final ArtifactEffect aArtifactEffect) {
        final ArtifactApplierTarget applierTarget = aArtifactEffect.getApplierTarget();
        final double effectValue = aArtifactEffect.getEffectValue();

        if (ArtifactApplyingMode.MULTIPLY.equals(aArtifactEffect.getEffectApplyingMode())) {

            if (ArtifactApplierTarget.ATTACK.equals(applierTarget)) {
                stats.setAttack((int) (stats.getAttack() * effectValue));
            } else if (ArtifactApplierTarget.DEFENCE.equals(applierTarget)) {
                stats.setAttack((int) (stats.getArmor() * effectValue));
            } else if (ArtifactApplierTarget.HEALTH.equals(applierTarget)) {
                stats.setMaxHp((int) (stats.getMaxHp() * effectValue));
            }

        } else if (ArtifactApplyingMode.ADD.equals(aArtifactEffect.getEffectApplyingMode())) {

            if (ArtifactApplierTarget.ATTACK.equals(applierTarget)) {
                stats.setAttack((int) (stats.getAttack() + effectValue));
            } else if (ArtifactApplierTarget.DEFENCE.equals(applierTarget)) {
                stats.setAttack((int) (stats.getArmor() + effectValue));
            } else if (ArtifactApplierTarget.HEALTH.equals(applierTarget)) {
                stats.setMaxHp((int) (stats.getMaxHp() + effectValue));
            }

        } else {
            throw new UnsupportedOperationException("Use MULTIPLY OR ADD");
        }
    }

    public static class Builder {

        private int amount = 1;
        private DamageCalculatorIf calculator = new DefaultDamageCalculator(new Random());
        private CreatureStatistic statistic;

        public Builder statistic(final CreatureStatistic aStatistic) {
            statistic = aStatistic;
            return this;
        }

        public Builder amount(final int aAmount) {
            amount = aAmount;
            return this;
        }

        Builder calculator(final DamageCalculatorIf aCalc) {
            calculator = aCalc;
            return this;
        }

        Builder hp(final int aNotImportant) {
            return this;
        }

        Builder attack(final int aI) {
            return this;
        }

        Builder defence(final int aNotImportant) {
            return this;
        }

        Builder damage(final Range<Integer> aClosed) {
            return this;
        }

        public Builder moveRange(final int aI) {
            return this;
        }

        public Builder name(final String aHero1) {
            return this;
        }

        public Creature build() {
            return new Creature(statistic, calculator, amount);
        }
    }
}
