////////////////////////////////////////////////////////////////////////////////////
// CardPictureStore.java
//
// Implementation of class CardPictureStore, a collection of Images indexed by
// card suit and value.
import java.util.*;
import java.awt.*;
import javax.swing.*;

public class CardPictureStore 
{
	Image[]	images = new Image[40];
	Image	imBack, imBottom, tapete;
	Vector	suits;
	int		cxCard, cyCard, xTapete, yTapete;
	String	directory;
	

	
	public CardPictureStore(String directory)
	{
		// Create a 1x1 Graphics to preload images
			
		Toolkit tk = Toolkit.getDefaultToolkit();
		
		
		this.directory = directory;
		try 
		{
			suits = Suit.AllSuits();
			for (int s = 0; s < 4; s++) 
			{
			    for (int cn = 0; cn < 10; cn++)
			    {
					
					images[s * 10 + cn] = tk.getImage(getClass().getResource(CardFile((Suit)suits.elementAt(s), cn + 1)));
					
				}
			}
			
			imBack = tk.getImage(getClass().getResource(directory + "/back2.jpg"));
			
			imBottom = tk.getImage(getClass().getResource(directory + "/bottom2.gif"));
		
			do
			{
				cxCard = imBottom.getWidth(null);
				cyCard = imBottom.getHeight(null);
			}
			while ((cxCard == -1) || (cyCard == -1));

		}
		catch (Exception e) 
		{
			System.err.println("Error " + e.getMessage());
		}
	}
	
	
	
	public CardPictureStore(java.applet.Applet a,String directory)
	{
		// Create a 1x1 Graphics to preload images
			
		//Toolkit tk = Toolkit.getDefaultToolkit();
		Image iTemp = a.createImage(1,1);
		Graphics g = iTemp.getGraphics();
		
		this.directory = directory;
		a.showStatus("Loading '" + directory + "' card images...");
		
		try 
		{
			suits = Suit.AllSuits();
			for (int s = 0; s < 4; s++) 
			{
			    for (int cn = 0; cn < 10; cn++)
				{
					images[s * 10 + cn] = a.getImage(getClass().getResource(CardFile((Suit)suits.elementAt(s), cn + 1)));
					g.drawImage(images[s * 10 + cn], 0, 0, null);
					a.showStatus("Loading '" +s+cn + "' card images...");
				}
			}
			
			imBack = a.getImage(getClass().getResource(directory + "/back2.jpg"));
			g.drawImage(imBack, 0, 0, null);
			a.showStatus("Loading back card image...");
			imBottom = a.getImage(getClass().getResource(directory + "/bottom2.gif"));
			g.drawImage(imBottom, 0, 0, null);
			a.showStatus("Loading bottom card image...");
			tapete = a.getImage(getClass().getResource("tapete.jpg"));
			g.drawImage(tapete, 0, 0, null);
			a.showStatus("Loading tapete...");
		
			do
			{
				cxCard = imBottom.getWidth(null);
				cyCard = imBottom.getHeight(null);
			}
			while ((cxCard == -1) || (cyCard == -1));
			
			do
			{
				xTapete = tapete.getWidth(null);
				yTapete = tapete.getHeight(null);
			}
			while ((cxCard == -1) || (cyCard == -1));
			
		
				
			a.showStatus("Load cards complete.");
		}
		catch (Exception e) 
		{
			System.err.println("Error " + e.getMessage());
		}
	}

	public Dimension GetCardSize()
	{
		return new Dimension(cxCard, cyCard);
	}

	public void DrawCard(Graphics g, Component c, int x, int y, Suit s, int value)
	{
		Image i = GetCardImage(s, value);
		if (i == null)
			return;
		g.drawImage(i, x, y, c);
	}
	
	public Image GetCardImage(Suit s, int value)
	{
		if ((value < 1) || (value > 10))
			return null;
		
		for (int i = 0; i < 4; i++)
		{
			if (suits.elementAt(i) == s)
				return images[i * 10 + (value - 1)];
		}
		
		return null;
	}

    protected String CardFile(Suit s, int value) throws IllegalArgumentException 
	{
		char sc;
		
		if (s.isClubs())
		    sc = 'c';
		else if (s.isDiamonds())
			sc = 'd';
		else if (s.isHearts())
		    sc = 'h';
		else if (s.isSpades())
			sc = 's';
		else
		    throw new IllegalArgumentException("Bad card suit");

		if ((value < 1) || (value > 10))
		    throw new IllegalArgumentException("Bad card number");

		if (value < 10)
		    return directory + "/0" + value + sc + ".jpg";
		else
			return directory + "/" + value + sc + ".jpg";
    }
	
	public Image GetCardBack()
	{
		return imBack;
	}
	
	public Image GetEmptyCard()
	{
		return imBottom;
	}
}
