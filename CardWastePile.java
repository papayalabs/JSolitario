////////////////////////////////////////////////////////////////////////////////////
// CardWastePile.java
//
// Implementation of class CardPile, a type-safe collection of Cards
//  which is displayed as up to n cards in a horizontal stack.

import java.awt.*;

public class CardWastePile extends CardPile
{
	int nCardsToDisplay, nMaxCards;
	
	static final int HORZSPACING = 20;
	
	public CardWastePile(int x, int y, int nMaxCardsToDisplay)
	{
		super(x, y);
		this.nMaxCards = nMaxCardsToDisplay;
	}
	
	public int getWidth(CardPictureStore store)
	{
		return store.GetCardSize().width + (nMaxCards - 1) * HORZSPACING;
	}
	
	// Reset the display so we can add a pile of cards again...
	public void ResetDisplayCards()
	{
		nCardsToDisplay = 0;
	}

	public void AddCard(Card c)
	{
		super.AddCard(c);
		nCardsToDisplay %= nMaxCards;
		nCardsToDisplay++;
	}
	
	public Card RemoveTopCard()
	{
		Card c = super.RemoveTopCard();
		if (c != null)
		{
			nCardsToDisplay--;
			if (nCardsToDisplay == 0)
				nCardsToDisplay = nMaxCards;
		}
		return c;
	}

	public boolean HitTest(CardPictureStore store, int x, int y, Point p)
	{
		if (x < (getX() + (nCardsToDisplay - 1) * HORZSPACING))
			return false;
		if (!super.HitTest(store, x, y, p))
			return false;
		p.x += (nCardsToDisplay - 1) * HORZSPACING;
		return true;
	}
	
	public void Draw(CardPictureStore store, Graphics g, Component c)
	{
		int nSize = GetNumCards();
		if (nSize == 0)
			g.drawImage(store.GetEmptyCard(), x, y, c);
		else
		{
			if (nCardsToDisplay > nSize)
				nCardsToDisplay = nSize;
			for (int i = 0; i < nCardsToDisplay; i++)
				GetCardAt(nSize - (nCardsToDisplay - i)).Draw(store, g, c, x + (i * HORZSPACING), y);
		}
	}
}

