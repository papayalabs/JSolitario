////////////////////////////////////////////////////////////////////////////////////
// Pack.java
//
// Implementation of class Pack, a type-safe collection of Cards

import java.util.*;

public class Pack
{
	Vector	cards;
	int		nTopCard;
	
	// Make a nice, ordered, pack of cards
	public Pack()
	{
		cards = new Vector(40);
		Vector suits = Suit.AllSuits();

		int cn, s;  // card number and suit number
		
		for (s = 0; s < 4; s++) 
		{
		    for (cn = 0; cn < 10; cn++)
			{
				cards.addElement(new Card((Suit) suits.elementAt(s), cn + 1));
		    }
		}
		nTopCard = -1;
	}
	
	// Deals a card from the pack.
	public Card Deal()
	{
		nTopCard++;
		return (Card)cards.elementAt(nTopCard);
	}

	// Shuffles the pack.
    public void Shuffle()
	{
		// Turn any upturned cards back over
		try
		{
			for (int i = 0; i < 40; i++)
			{
				if (((Card)cards.elementAt(i)).IsCardShowing())
					((Card)cards.elementAt(i)).HideCard();
			}
		}
		catch (IllegalAccessException e)
		{
			System.out.println("Logic error during pack turning: " + e.getMessage());
		}
				
		Shuffle(cards, new Random());
		nTopCard = -1;
    }

    protected void Shuffle(Vector v, Random rgen) 
	{
		int sizev = v.size();  // size of the input vector
		// dispose of the trivial cases first
		if (sizev <= 1)
		    return;

		Vector TempCopy = new Vector(sizev);
		int i, j;

		// Invariant: i elements have been selected from v
		// and moved to TempCopy, and sizev>1
		for (i = 0;  i < sizev - 1; i++) 
		{
		    // generate a random integer in the range [0, sizev-i)
		    j = (int)((sizev - i) * rgen.nextFloat());
		    // nextFloat() returns a float in [0, 1]
		    if (j >= (sizev - i)) // rather unlikely, but...
				j = sizev - i - 1;

		    // so add v[j] to TempCopy, then remove it from v
		    TempCopy.addElement(v.elementAt(j));
		    v.removeElementAt(j);
		}
		// there is exactly one element left in v at this point
		TempCopy.addElement(v.elementAt(0));
		v.removeAllElements();

		// now copy the shuffled objects back to the original
		for (i = 0; i < sizev; i++)
		    v.addElement(TempCopy.elementAt(i));
    }
}
