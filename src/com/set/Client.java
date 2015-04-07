package com.set;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
		/*handler.post(new Runnable(){
			public void run(){
				main.clean();
			}
		});*/
		
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
		
		//System.out.println(deck.toString());
		System.out.println("start posDeck = " + main.posDeck);
		
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
				        	
				        
				        	System.out.println("click posDeck = " + main.posDeck);
				        	
				        	if(!main.marked[i2]){
				        		main.cards[i2].setColorFilter(Color.argb(50, 0, 0, 0));
				        		main.cards[i2].invalidate();
				        		main.marked[i2] = true;
				                ++main.cont;
				                
				                if(main.cont == 3){
				                	System.out.println("Round : " + (main.round + 1));
				                	++main.round;
				                	
				                	int pos = 0;
				                	
				                	for(int k = 0;k < main.N;++k){
				                		if(main.marked[k]){
				                			main.id[pos++] = k;
				                		}
				                	}
				                	
				                	long elapsedMillis = SystemClock.elapsedRealtime() - main.chrono.getBase();
				                	out.println(main.id[0] + " " + main.id[1] + " " + main.id[2] + " " + elapsedMillis);
				                	
				                	for(int i = 0;i < 3;++i)
				                		main.marked[ main.id[i] ] = false;
				                	
				                	class getResult extends Thread{
										String result;
										
										public void run(){
											try {
												result = in.readLine();
											} catch (IOException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										}
									}
									
									getResult t = new getResult();
									
									t.start();
									try {
										t.join();
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

									System.out.println("Result = " + t.result);
									System.out.println("result posDeck = " + main.posDeck);
				                	
				                	String[] tokens = t.result.split(" ");
				                	int sc1 = 0,sc2 = 0;
				                	
				                	//System.out.println(tokens);
				                	//System.out.println(tokens[0]);
				                	
				                	for(int i = 0;i < main.N;++i)
				                		main.cards[i].setColorFilter(Color.argb(0, 0, 0, 0));
				                	
				                	if(tokens[0].equals("ERASE")){
				                		System.out.println("erase posDeck = " + main.posDeck);
					                	int c[] = new int[3];
					                	
					                	for(int i = 0;i < 3;++i){
					                		c[i] = Integer.parseInt(tokens[i + 1]);
					                		main.marked[ c[i] ] = true;
					                	}
					                	System.out.println("c = " + c[0] + " " + c[1] + " " + c[2]);
					                	
					                	sc1 = Integer.parseInt(tokens[4]);
					                	sc2 = Integer.parseInt(tokens[5]);
					                	
					                	for(int i = 0;i < 3;++i){
				                			main.cards[ c[i] ].setColorFilter(Color.argb(100, 0, 200, 0));
				                			main.active[ c[i] ] = true;
				                			main.scoreImage[i].setImageDrawable(new CardDrawable(main.value[ c[i] ]));
				                		}
					                	
					                	main.N -= 3;
					                	
					                	final int[] c2 = c;
					                	System.out.println("c2 = " + c2[0] + " " + c2[1] + " " + c2[2]);
					                	System.out.println(main.marked[ c2[0] ] + " " + main.marked[ c2[1] ] + " " + main.marked[ c2[2] ]);
					                	
					                	// START POST DELAYED
					                	main.handler.postDelayed(new Runnable(){
				                			public void run(){
				                				System.out.println(this);
				                				System.out.println("postDelayed posDeck = " + main.posDeck);
				                				for(int k = 0;k < 3;++k){
				                					main.cards[ c2[k] ].setColorFilter(Color.argb(0, 0, 0, 0));
				                					main.active[ c2[k] ] = false;
						                		}

				                				for(int i = 0;i < main.N + 3;++i)
				                					System.out.print(main.value[i] + " ");
				                				System.out.println();
				                				
				                				for(int i = 0;i < main.N + 3;++i)
				                					System.out.print(main.marked[i] + " ");
				                				System.out.println();
				                				//if(main.N == 12)
				                				//	main.cleanCards(15);
				                				main.cleanCards(main.N + 3);
				                				
				                				System.out.println("clean posDeck = " + main.posDeck);
				                				
				                				for(int i = 0;i < main.N;++i)
				                					System.out.print(main.value[i] + " ");
				                				System.out.println();
				                				
					                			while(81 - main.posDeck >= 3 && (main.N < 12 || (main.N == 12 && !Cards.test(main.value, main.N)))){
					                				System.out.println("N = " + main.N);
					                				for(int k = 0;k < 3;++k){
					                					main.value[main.N + k] = main.deck.get(main.posDeck); main.posDeck++;
						                				main.cards[main.N + k].setImageDrawable(new CardDrawable(main.value[main.N + k]));
					                				}
					                				
					                				main.N += 3;
					                			}
					                			
					                			if(!Cards.test(main.value,main.N)){
			                						main.endGame();
			                					}
						                		
							                	String auxValue = "N = " + main.N + ":";
							                	
							                	for(int i = 0;i < main.N;++i)
							                		auxValue = auxValue + " " + main.value[i];
							                	
							                	/*new AlertDialog.Builder(main)
							                    .setTitle("Values")
							                    .setMessage(auxValue)
							                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
							                        public void onClick(DialogInterface dialog, int which) { 
							                            // continue with delete
							                        }
							                     })
							                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
							                        public void onClick(DialogInterface dialog, int which) { 
							                            // do nothing
							                        }
							                     })
							                    .setIcon(android.R.drawable.ic_dialog_alert)
							                     .show();*/
				                			}
				                		}, 500);
					                	// END POST DELAYED
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
				                	
				                	main.score += sc1;
				                	main.score2 += sc2;
				                	main.scoreText.setText("Score :\n" + main.score + " | " + main.score2);
				                	
				                	/*for(int i = 0;i < 15;++i)
				                		main.marked[i] = false;*/
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
