import java.util.Vector;

/** A class to distinguish between the 4 suits in a way that
    can be type-checked */

public final class Suit 
{
    public static Suit Clubs 	 = new Suit();
    public static Suit Diamonds = new Suit();
    public static Suit Hearts 	 = new Suit();
    public static Suit Spades 	 = new Suit();

    Suit() {}  // only a private constructor!

    public static Vector AllSuits() 
	{
		Vector allsuits = new Vector(4);
		allsuits.addElement(Suit.Clubs);
		allsuits.addElement(Suit.Diamonds);
		allsuits.addElement(Suit.Hearts);
		allsuits.addElement(Suit.Spades);
	
		return allsuits;
    }
	
	public boolean isComplementary(Suit s) 
	{ 
		if ((isClubs() && s.isClubs()))
			return false;
		if ((isDiamonds() && s.isDiamonds()))
			return false;
		if ((isHearts() && s.isHearts()))
			return false;
		if ((isSpades() && s.isSpades()))
			return false;
		return true;	
			
	}
    public boolean isClubs()	{ return this==Clubs; }
    public boolean isDiamonds()	{ return this==Diamonds; }
    public boolean isHearts()	{ return this==Hearts; }
    public boolean isSpades()	{ return this==Spades; }
}

