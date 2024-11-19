////////////////////////////////////////////////////////////////////////////////////
// CardRun.java
//
// Implementation of class CardRun, a type-safe collection of Cards
//  which is displayed as a stacked pile. Cards are always shown, and must be in order.

import java.util.*;
import java.awt.*;

public class CardRun extends CardStack
{
	public CardRun(int x, int y)
	{
		super(x, y);
	}
	
	public void MoveTo(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void AddCard(Card c)
	{
		super.AddCard(c);
	}
	
	public static CardRun RunFromCard(Card c)
	{
		CardRun run = new CardRun(0, 0);
		run.AddCard(c);
		return run;
	}
}
