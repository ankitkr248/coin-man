package com.coinman.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;


import java.util.ArrayList;
import java.util.Random;

public class CoinMan extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] man;
	int manstate=0;
	int pause=0;
	int manY=0;
	float gravity=0.6f;
	float velocity=0;
	ArrayList<Integer> coinx=new ArrayList<Integer>();
	ArrayList<Integer> coiny=new ArrayList<Integer>();
	ArrayList<Rectangle> coinrectangles=new ArrayList<Rectangle>();
	Texture coin;
	Random random;
	int coincount=0;
	int score=0,gamestate=0;
	BitmapFont font;

	ArrayList<Integer> bombx=new ArrayList<Integer>();
	ArrayList<Integer> bomby=new ArrayList<Integer>();
	ArrayList<Rectangle>bombrectangles = new ArrayList<Rectangle>();
	Texture bomb;

	int bombcount=0;
	Rectangle manRectangle;

	@Override
	public void create () {
		batch = new SpriteBatch();
		background =new Texture("bg.png");
		man =new Texture[7];
		man[0]=new Texture("stand.png");
		man[1]=new Texture("run1.png");
		man[2]=new Texture("run2.png");
		man[3]=new Texture("run3.png");
		man[4]=new Texture("slide.png");
		man[5]=new Texture("jump.png");
		man[6]=new Texture("dead.png");
		manY=Gdx.graphics.getHeight()/2;
		coin=new Texture("Coin.png");
		bomb=new Texture("bomb.png");
		random=new Random();

		font=new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);

	}
	public  void makecoin()
	{
		float height = random.nextFloat()*Gdx.graphics.getHeight();
		 coiny.add((int)height);
		 coinx.add(Gdx.graphics.getWidth());
	}
	public  void makebomb()
	{
		float height = random.nextFloat()*Gdx.graphics.getHeight();
		bomby.add((int)height);
		bombx.add(Gdx.graphics.getWidth());
	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background,0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

		if(gamestate==1)
		{
			// game is live
			if(pause<6)
			{
				pause++;
			}
			else
			{
				pause=0;
				if(manstate<3)
				{
					manstate++;
				}
				else
				{
					manstate=0;
				}
			}
			velocity+=gravity;
			manY-=velocity;
			if(manY<=0)
				manY=0;
			//bombs
			if(bombcount<600)
			{
				bombcount++;
			}
			else
			{
				bombcount=0;
				makebomb();
			}

			bombrectangles.clear();
			for(int i=0;i<bombx.size();i++)
			{
				batch.draw(bomb,bombx.get(i),bomby.get(i));
				bombx.set(i,bombx.get(i)-8);
				bombrectangles.add(new Rectangle(bombx.get(i),bomby.get(i),bomb.getWidth(),bomb.getHeight()));

			}

			//coins
			if(coincount<100)
			{
				coincount++;
			}
			else
			{
				coincount=0;
				makecoin();
			}

			coinrectangles.clear();
			for(int i=0;i<coinx.size();i++)
			{
				batch.draw(coin,coinx.get(i),coiny.get(i));
				coinx.set(i,coinx.get(i)-4);
				coinrectangles.add(new Rectangle(coinx.get(i),coiny.get(i),coin.getWidth(),coin.getHeight()));
			}
		}

		else if(gamestate==0)
		{
			//waiting to start
			if(Gdx.input.justTouched())
			{
				gamestate=1;
			}

		}

		else if(gamestate==2)
		{
			//game over
			if(Gdx.input.justTouched())
			{
				gamestate=1;
				manY=Gdx.graphics.getHeight()/2;
				score=0;
				velocity=0;
				coiny.clear();
				coinx.clear();
				coinrectangles.clear();
				coincount=0;
				bomby.clear();
				bombx.clear();
				bombrectangles.clear();
				bombcount=0;
			}
		}

		if(Gdx.input.justTouched())
		{
			velocity=-20;
			manstate=5;
		}




		batch.draw(man[manstate],Gdx.graphics.getWidth()/2-man[manstate].getWidth(),manY);

		manRectangle = new Rectangle(Gdx.graphics.getWidth()/2-man[manstate].getWidth(),manY,man[manstate].getWidth(),man[manstate].getHeight());

		for(int i=0;i<coinrectangles.size();i++)
		{
			if(Intersector.overlaps(manRectangle,coinrectangles.get(i)))
			{
				score++;
				Gdx.app.log("coin","collision");
				coinrectangles.remove(i);
				coinx.remove(i);
				coiny.remove(i);
				break;
			}
		}

		for(int i=0;i<bombrectangles.size();i++)
		{
			if(Intersector.overlaps(manRectangle,bombrectangles.get(i)))
			{
				Gdx.app.log("bomb","collision");
				gamestate=2;

			}
		}

		font.draw(batch,String.valueOf(score),100,200);

		batch.end();

	}
	
	@Override
	public void dispose () {
		batch.dispose();

	}
}
