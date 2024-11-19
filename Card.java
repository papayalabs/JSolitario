import java.awt.*;
import java.io.*;

public class Card 
{
    private	Image	im;  // its picture
	private	Suit	s;
	private	int		value;
	private	boolean bIsShowing;

    // Load the image
    public Card(Suit s, int value) throws IllegalArgumentException
	{
		if ((value < 1) || (value > 10))
		    throw new IllegalArgumentException("Bad card number");

		this.s = s;
		this.value = value;
		this.bIsShowing = false;
    }
	
	public void TurnCard() throws IllegalAccessException
	{
		if (bIsShowing)
			throw new IllegalAccessException("Card already showing");
		bIsShowing = true;
	}
	
	public void HideCard() throws IllegalAccessException
	{
		if (!bIsShowing)
			throw new IllegalAccessException("Card already hidden");
		bIsShowing = false;
	}
	
	public boolean IsCardShowing()
	{
		return bIsShowing;
	}
	
	public Suit GetSuit()
	{
		return s;
	}
	
	public int GetValue()
	{
		return value;
	}

    public void Draw(CardPictureStore store, Graphics g, Component c, int x, int y) 
	{
		if (bIsShowing)
			store.DrawCard(g, c, x, y, s, value);
		else
			g.drawImage(store.GetCardBack(), x, y, c);
    }
}
