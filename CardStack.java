////////////////////////////////////////////////////////////////////////////////////
// CardStack.java
//
// Implementation of class CardStack, a collection of Cards
//  which is displayed as a stacked pile. Cards may be shown or not, as necessary.
//  Card ordering rules are enforced.

import java.util.*;
import java.awt.*;

public class CardStack extends CardSet
{
	private int CARDSPACING = 20;
	private boolean CONDITION = true;
	
	public CardStack(int x, int y)
	{
		super(x, y);
	}
	
	public boolean HitTest(CardPictureStore store, int x, int y, Point p)
	{
		if (!super.HitTest(store, x, y, p))
			return false;

		int nCard = (y - getY()) / CARDSPACING;
		if (nCard >= GetNumCards())
			nCard = GetNumCards() - 1;
		
		p.y = nCard * CARDSPACING + getY();
		return true;
	}

	public int getWidth(CardPictureStore store)
	{
		return store.GetCardSize().width + CARDSPACING;
	}

	public int getHeight(CardPictureStore store)
	{
		if (IsEmpty())
			return store.GetCardSize().height;
		return (GetNumCards() - 1) * CARDSPACING + store.GetCardSize().height;
	}
	
	public boolean CanAddCardRun(CardRun run)
	{
		Card	bottom = run.GetBottomCard();
		// Check to see if there are no cards *AND* we are a King
		if (IsEmpty())
			return true;//(bottom.GetValue() == 13);

		// Can't add runs on columns without cards showing
		if (!GetTopCard().IsCardShowing())
			return false;
	
		return (CONDITION&&CanAddCard(bottom));
	 	
	
	}
	
	protected boolean CanAddCard(Card c)
	{
	
		if (c.IsCardShowing())
		{
			// Either the top card must be face down (i.e. we are adding back a pile or initial dealing),
			//  there are no cards (empty stack), 
			//  or we must be the next in the logical sequence
			if (IsEmpty())
				return true;
			Card top = GetTopCard();
			if (!top.IsCardShowing())
				return true;
		
			if (!top.GetSuit().isComplementary(c.GetSuit()))
				return false;
		
			if (top.GetValue() != (c.GetValue() + 1))
				return false;
		
			return true;
		}
		else
		{
			// Either the top card must be face down, or there are no cards.
			if (IsEmpty())
				return true;

			Card top = GetTopCard();
			if (!top.IsCardShowing())
				return true;
			
			return false;
		}
	
	}	
	
	
	public void Draw(CardPictureStore store, Graphics g, Component c)
	{
		int	i;
		
		if (!IsEmpty())
		{
			for (i = 0; i < GetNumCards(); i++)
			{
				Card card = GetCardAt(i);
				card.Draw(store, g, c, x, y + CARDSPACING * i);
			}
		}
		else
			g.drawImage(store.GetEmptyCard(), x, y, c);
	}
	
	public boolean IsFaceUp()
	{
		if (IsEmpty())
			return false;
		
		return (this.GetTopCard().IsCardShowing());
	}
	
	// Find a run of cards at the given point, remove it from the pile and return it.
	public CardRun PickUpRunAtPoint(int x, int y)
	{
		x -= this.x;
		y -= this.y;
		
		if (y < 0)
			return null;
		
		if (IsEmpty())
			return null;
	/*	
		// Get card at point
		int nCard = y / CARDSPACING;
		if (nCard >= GetNumCards())
			nCard = GetNumCards() - 1;
		Card card = GetCardAt(nCard);
		if (card == null)
			return null;
		// Can't return a run if it's not showing
		if (!card.IsCardShowing())
			return null;
	
*/	
				
		CardRun	run = new CardRun(0, 0);
		
		int nSize = GetNumCards();
	//	for (int i = nCard; i < nSize; i++)
			run.AddCard(GetCardAt(nSize-1));
	//	for (int i = nCard; i < nSize; i++)
			RemoveTopCard();
		
		return run;
	}
	
	// Turn over the top card.
	public void TurnTopCard() throws IllegalAccessException
	{
		if (IsEmpty())
			return;
		GetTopCard().TurnCard();
	}

	// Add a run of cards to this pile.
	public boolean DropRun(CardRun run)
	{
		if (!CanAddCardRun(run))
			return false;
		
		for (int i = 0; i < run.GetNumCards(); i++)
			AddCard(run.GetCardAt(i));
	
		return true;
	}

	public void setCardSpacing(int size)
	{
		CARDSPACING = size;
	}

	public void setConditionFalse()
	{
		CONDITION = false;
	}

}
