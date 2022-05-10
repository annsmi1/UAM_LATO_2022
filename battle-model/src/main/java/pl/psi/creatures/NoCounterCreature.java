package pl.psi.creatures;

import com.google.common.collect.Range;
import lombok.Builder;

import java.beans.PropertyChangeEvent;
/**
 * This class should always decorate first
 */

public class NoCounterCreature extends AbstractCreature
{
    private final Creature decorated;

    public NoCounterCreature( final Creature aDecorated )
    {
        super( aDecorated );
        decorated = aDecorated;
    }

    @Override
    public CreatureStatisticIf getStats()
    {
        return decorated.getStats();
    }

    @Override
    public int getAmount()
    {
        return decorated.getAmount();
    }

    @Override
    public DamageCalculatorIf getCalculator()
    {
        return decorated.getCalculator();
    }

    @Override
    public void attack( final Creature aDefender )
    {
        if( isAlive() )
        {
            final int damage = getCalculator().calculateDamage( decorated, aDefender );
            decorated.applyDamage( aDefender, damage );
        }
    }

    @Override
    public boolean isAlive()
    {
        return decorated.isAlive();
    }


    @Override
    public void heal( final int healAmount)
    {
         decorated.heal( healAmount ) ;
    }

    @Override
    public int getCurrentHp()
    {
        return decorated.getCurrentHp();
    }

    @Override
    protected void setCurrentHp( final int aCurrentHp )
    {
        decorated.setCurrentHp( aCurrentHp );
    }

    @Override
    Range< Integer > getDamage()
    {
        return decorated.getDamage();
    }

    @Override
    int getAttack()
    {
        return decorated.getAttack();
    }

    @Override
    int getArmor()
    {
        return decorated.getArmor();
    }

    @Override
    protected void restoreCurrentHpToMax()
    {
        decorated.restoreCurrentHpToMax();
    }

    @Override
    public void propertyChange( final PropertyChangeEvent evt )
    {
        decorated.propertyChange( evt );
    }

}