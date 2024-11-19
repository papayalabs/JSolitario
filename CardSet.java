////////////////////////////////////////////////////////////////////////////////////
// CardSet.java
//
// Implementation of class CardSet, a type-safe collection of Cards. 
//  Cards may be shown or not, as necessary.

import java.util.*;
import java.awt.*;

public abstract class CardSet
{
	private	Vector	vCards = new Vector();
	
	protected int	x, y;
	
	public CardSet(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public int getX()
	{
		return x;
	}
	public int getY()
	{
		return y;
	}
	
	public abstract int getWidth(CardPictureStore store);
	public abstract int getHeight(CardPictureStore store);
	
	// Test the point to see if this CardSet is under it.
	//  Sets p to the co-ordinates of the card under the point.
	public boolean HitTest(CardPictureStore store, int x, int y, Point p)
	{
		if ((x < getX()) || (x >= getX() + getWidth(store)))
			return false;
		if ((y < getY()) || (y >= getY() + getHeight(store)))
			return false;
		p.x = getX();
		p.y = getY();
		return true;
	}

	public Rectangle GetBounds(CardPictureStore store)
	{
		return new Rectangle(getX(), getY(), getWidth(store), getHeight(store));
	}

	// Must implement to draw this card set
	public abstract void Draw(CardPictureStore store, Graphics g, Component c);

	// Must implement to say whether we can add this card to the pile
	protected abstract boolean CanAddCard(Card c);
	
	protected Card GetCardAt(int nCard)
	{
		if ((nCard >= vCards.size()) || (nCard < 0))
			return null;
		return (Card)vCards.elementAt(nCard);
	}
	
	protected int GetNumCards()
	{
		return vCards.size();
	}
	
	public boolean IsEmpty()
	{
		return vCards.isEmpty();
	}
	
	public void AddCard(Card c)
	{
		if (!CanAddCard(c))
			throw new IllegalArgumentException("Card not valid on this pile");
		vCards.addElement(c);
	}
	
	public void AddCardSet(CardSet c)
	{
		for (int i = 0; i < c.GetNumCards(); i++)
			AddCard(c.GetCardAt(i));
	}
	
	public Card GetBottomCard()
	{
		if (IsEmpty())
			return null;
		return GetCardAt(0);
	}
	
	protected Card GetTopCard()
	{
		int nSize = GetNumCards();
		if (nSize == 0)
			return null;
		return GetCardAt(nSize - 1);
	}
	
	public Card RemoveTopCard()
	{
		int nSize = GetNumCards();
		if (nSize == 0)
			return null;
		Card c = GetCardAt(nSize - 1);
		vCards.removeElementAt(nSize - 1);
		return c;
	}
}
