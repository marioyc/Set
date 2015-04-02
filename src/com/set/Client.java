package com.set;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

import android.graphics.Color;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;

class Client extends Thread{
	Handler handler;
	MainActivity main;
	
	Client(MainActivity main, Handler handler){
		this.main = main;
		this.handler = handler;
	}
	
	public void run(){
		handler.post(new Runnable(){
			public void run(){
				main.clean();
			}
		});
		
		Socket s = Net.establishConnection("10.0.2.2", 7777);
		final BufferedReader in = Net.connectionIn(s);
		final PrintWriter out = Net.connectionOut(s);
		
		System.out.println("Connected!");
		
		ArrayList<Integer> deck = new ArrayList<Integer>();
		
		try {
			final String aux = in.readLine();
			
			ArrayList<String> list = new ArrayList<String>(Arrays.asList(aux.split(" ")));
			
			for(String x : list){
				deck.add(Integer.parseInt(x));
			}
			
			main.N = 12;
			main.posDeck = 12;
			main.deck = deck;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		handler.post(new Runnable(){
			public void run(){
				main.paintCards(main.deck);
				
        		main.chrono.setBase(SystemClock.elapsedRealtime());
        		main.chrono.start();
        		
				for(int i = 0;i < 15;++i){
					final int i2 = i;
					
					main.cards[i].setOnClickListener(new OnClickListener () {
				        @Override
				        public void onClick(View v) {
				        	if(i2 >= main.N || main.active[i2] || main.cont >= 3) return;
				        	
				        
				        	//System.out.println("cont = " + main.cont);
				        	
				        	if(!main.marked[i2]){
				        		main.cards[i2].setColorFilter(Color.argb(50, 0, 0, 0));
				        		main.cards[i2].invalidate();
				        		main.marked[i2] = true;
				                ++main.cont;
				                
				                if(main.cont == 3){
				                	int pos = 0;
				                	
				                	for(int k = 0;k < main.N;++k){
				                		if(main.marked[k]){
				                			main.id[pos++] = k;
				                		}
				                	}
				                	
				                	long elapsedMillis = SystemClock.elapsedRealtime() - main.chrono.getBase();
				                	out.println(main.id[0] + " " + main.id[1] + " " + main.id[2] + " " + elapsedMillis);
				                	//System.out.println("ok1");
				                	String result = "";
				                	
				                	class getResult extends Thread{
										String result;
										BufferedReader in;
										
										getResult(String result, BufferedReader in){
											this.result = result;
											this.in = in;
										}
										
										public void run(){
											try {
												this.result = this.in.readLine();
											} catch (IOException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										}
									}
									System.out.println("create getResult");
									getResult t = new getResult(result,in);
									System.out.println("get Result");
									t.start();
									try {
										t.join();
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									
									//System.out.println("Result = " + t.result);
				                	
				                	String[] tokens = t.result.split(" ");
				                	int sc1 = 0,sc2 = 0;
				                	
				                	//System.out.println(tokens);
				                	//System.out.println(tokens[0]);
				                	
				                	for(int i = 0;i < main.N;++i)
				                		main.cards[i].setColorFilter(Color.argb(0, 0, 0, 0));
				                	
				                	if(tokens[0].equals("ERASE")){
					                	int c[] = new int[3];
					                	
					                	for(int i = 0;i < 3;++i)
					                		c[i] = Integer.parseInt(tokens[i + 1]);
					                	//System.out.println(c[0] + " " + c[1] + " " + c[2]);
					                	
					                	sc1 = Integer.parseInt(tokens[4]);
					                	sc2 = Integer.parseInt(tokens[5]);
					                	
					                	for(int i = 0;i < 3;++i){
				                			main.cards[ c[i] ].setColorFilter(Color.argb(100, 0, 200, 0));
				                			main.active[ c[i] ] = true;
				                			main.scoreImage[i].setImageDrawable(new CardDrawable(main.value[ c[i] ]));
				                		}
					                	
					                	main.N -= 3;
					                	
					                	final int[] c2 = c;
					                	
					                	handler.postDelayed(new Runnable(){
				                			public void run(){
				                				for(int k = 0;k < 3;++k){
				                					main.cards[ c2[k] ].setColorFilter(Color.argb(0, 0, 0, 0));
				                					main.active[ c2[k] ] = false;
						                		}
				                				
				                				if(main.N == 12)
				                					main.cleanCards(15);
						                		int [] c3=new int[3];
						                		//main.endText.setText("posDeck = " + main.posDeck);
						                		if((main.N == 9 && 81 - main.posDeck >= 3) || (main.N == 12 && !Cards.test(main.value,main.N))){
						                			for(int k = 0;k < 3;++k){
					                					c3[k]=main.deck.get(main.posDeck); main.posDeck++;
					                					//main.endText.setText(main.endText.getText() + " " + c3[k]);
					                					main.value[ c2[k] ] = c3[k];
					                					main.cards[ c2[k] ].setImageDrawable(new CardDrawable(main.value[ c2[k] ]));
					                					//main.marked[ main.id[k] ] = false;
					                				}
					                				
					                				main.N += 3;
					                					
					                				if(!Cards.test(main.value,main.N) && 81 - main.posDeck >= 3){
					                					for(int k = 0;k < 3;++k){
					                						main.value[12 + k] = main.deck.get(main.posDeck); main.posDeck++;
					                						main.cards[12 + k].setImageDrawable(new CardDrawable(main.value[12 + k]));
					                					}
					                					
					                					main.N += 3;
					                					
					                					if(!Cards.test(main.value,main.N)){
					                						//TODO:finir le jeu
					                						main.endGame();
					                					}
					                				}
						                		}else if(main.posDeck == 81){
						                			main.cleanCards(main.N + 3);
						                			
						                			if(!Cards.test(main.value,main.N)){
						                				//TODO:finir le jeu
						                				main.endGame();
						                			}
						                		}
						                		
							                	/*String auxValue = "N = " + main.N + ":";
							                	
							                	for(int i = 0;i < main.N;++i)
							                		auxValue = auxValue + " " + main.value[i];
							                	
							                	main.endText.setText(main.endText.getText() + " " + auxValue);*/
				                			}
				                		}, 500);
				                	}else if(tokens[0].equals("CONTINUE")){
				                		for(int i = 0;i < main.N;++i)
				                			if(main.marked[i]){
				                				main.cards[i].setColorFilter(Color.argb(100, 200, 0, 0));
				                				main.active[i] = true;
				                			}
				                		
				                		sc1 = -1;
				                		sc2 = -1;
				                		
				                		handler.postDelayed(new Runnable(){
				                			public void run(){
				                				for(int i = 0;i < main.N;++i){
				                					if(main.active[i]){
					                					main.cards[i].setColorFilter(Color.argb(0, 0, 0, 0));
					                					main.active[i] = false;
				                					}
				                				}
				                			}
				                		}, 500);
				                	}
				                	//System.out.println(sc1 + " " + sc2);
				                	
				                	main.score += sc1;
				                	main.score2 += sc2;
				                	//System.out.println(main.score + " " + main.score2);
				                	main.scoreText.setText("Score : " + main.score + " | " + main.score2);
				                	for(int i = 0;i < 15;++i){
				                		main.marked[i] = false;
				                		//TODO:erase
				                		//main.active[i] = false;
				                	}
				                	main.cont = 0;
				                }
				        	}else{
				        		main.cards[i2].setColorFilter(Color.argb(0, 0, 0, 0));
				        		main.marked[i2] = false;
				        		--main.cont;
				        	}
				        }
				   });
				}
			}
		});
	}
};
