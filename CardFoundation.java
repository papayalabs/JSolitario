////////////////////////////////////////////////////////////////////////////////////
// CardFoundation.java
//
// Implementation of class CardFoundation, a type-safe collection of Cards
//  which is displayed as a top card only.
//  Cards are removed from the top and added to the top.
//  Foundation card-adding rules are enforced.

public class CardFoundation extends CardPile
{
	private static int FIRST_CARD;//First Card = 1 = Ace
	private static boolean FIRST_CARD_SELECTED;


	public CardFoundation(int x, int y)
	{
		super(x, y);
		FIRST_CARD = 1;
		FIRST_CARD_SELECTED = false;
	
	}
	
	public boolean CanAddCard(Card c)
	{
		int topComparison;
		
		if(!FIRST_CARD_SELECTED)
		{
			FIRST_CARD = c.GetValue();
			FIRST_CARD_SELECTED = true;
			return true;
			
		}
		else
		{
		// Add cards if: 
		//  we're empty and it's an ace
		//  it's the next in sequence
		
			int nSize = GetNumCards();
			if (nSize == 0)
				return (c.GetValue() == FIRST_CARD);
		
			Card top = GetTopCard();
			if (c.GetSuit() != top.GetSuit())
				return false;
			if (top.GetValue() == 10)
				topComparison = 1;
			else
				topComparison = top.GetValue()+1;
						
			if (c.GetValue() != topComparison)
				return false;
			return true;
		}
	}
}
