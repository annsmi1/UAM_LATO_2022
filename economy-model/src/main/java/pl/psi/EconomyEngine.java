package pl.psi;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import pl.psi.products.BuyProductInterface;
import pl.psi.products.Products;
import pl.psi.hero.CreatureShop;
import pl.psi.hero.EconomyHero;

public class EconomyEngine
{
    public static final String HERO_BOUGHT_CREATURE = "HERO_BOUGHT_CREATURE";
    public static final String ACTIVE_HERO_CHANGED = "ACTIVE_HERO_CHANGED";
    public static final String NEXT_ROUND = "NEXT_ROUND";
    public static final String END_SHOPPING = "END_SHOPPING";
    private final EconomyHero hero1;
    private final EconomyHero hero2;
    private final CreatureShop creatureShop = new CreatureShop();
    private final PropertyChangeSupport observerSupport;
    private EconomyHero activeHero;
    private boolean end;

    private int roundNumber;

    public EconomyEngine( final EconomyHero aHero1, final EconomyHero aHero2 )
    {
        hero1 = aHero1;
        hero2 = aHero2;
        activeHero = hero1;
        roundNumber = 1;
        observerSupport = new PropertyChangeSupport( this );
    }

    public void buy(Products product , final BuyProductInterface buyProduct )
    {
        creatureShop.buy( product ,activeHero, buyProduct );
        observerSupport.firePropertyChange( HERO_BOUGHT_CREATURE, null, null );
    }

    public EconomyHero getActiveHero()
    {
        return activeHero;
    }

    public void pass()
    {

        if ( activeHero.getCreatureList().size() == 0){
            throw new IllegalStateException( "hero cannot pass round if he didn't buy any creature" );
        }
        else {
            activeHero = hero2;
            observerSupport.firePropertyChange(ACTIVE_HERO_CHANGED, hero1, activeHero);
            endTurn();
        }
    }

    private void endTurn()
    {
        if(roundNumber == 2 ){
            endShopping();
        }
        else {
            roundNumber += 1;
            observerSupport.firePropertyChange( NEXT_ROUND, roundNumber - 1, roundNumber );
        }
    }

    public int getRoundNumber()
    {
        return roundNumber;
    }

    public void addObserver( final String aPropertyName, final PropertyChangeListener aObserver )
    {
        observerSupport.addPropertyChangeListener( aPropertyName, aObserver );
    }

    public EconomyHero getPlayer1()
    {
        // TODO make copy
        return hero1;
    }

    public EconomyHero getPlayer2()
    {
        // TODO make copy
        return hero2;
    }

    private void endShopping(){
        observerSupport.firePropertyChange( END_SHOPPING, null, null );
        end = true;
    }

    public boolean isEnd() {
        return end;
    }
}
