////////////////////////////////////////////////////////////////////////////////////
// CardPile.java
//
// Implementation of class CardPile, a type-safe collection of Cards
//  which is displayed as a top card only.
//  Cards are removed from the top and added to the top.

import java.awt.*;

public class CardPile extends CardSet
{
	public CardPile(int x, int y)
	{
		super(x, y);
	}
	
	public int getWidth(CardPictureStore store)
	{
		return store.GetCardSize().width;
	}

	public int getHeight(CardPictureStore store)
	{
		return store.GetCardSize().height;
	}
	
	public boolean CanAddCard(Card c)
	{
		return true;
	}

	public void Draw(CardPictureStore store, Graphics g, Component c)
	{
		int nSize = GetNumCards();
		if (nSize == 0)
			g.drawImage(store.GetEmptyCard(), x, y, c);
		else
			GetTopCard().Draw(store, g, c, x, y);
	}
}
