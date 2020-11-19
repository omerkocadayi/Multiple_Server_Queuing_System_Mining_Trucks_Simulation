package javacode;

import java.io.*;
import java.util.Random;
import java.util.*;
 
public class Multiple_Server_Queue{
	public static Server load1;
	public static Server load2;
	public static Server scale;
	public static int direct_out = 0;
	 
	public static void main(String[] args)throws IOException{
		Scanner sc = new Scanner(System.in);
		load1 = new Server(); load1.mean_interarrival = 80;
		load2 = new Server(); 
		scale = new Server(); 
		
		/*
		System.out.println("Enter mean inter arrival time = ");
		server1.mean_interarrival=sc.nextDouble();
		System.out.println("\nEnter mean service time of server1 = ");
		server1.mean_service=sc.nextDouble();
		System.out.println("\nEnter mean service time of server2 = ");
		server2.mean_service=sc.nextDouble();
		
		System.out.println("\nEnter Number of Customer = ");
		int total_customer = sc.nextInt();
		*/
		
		int total_truck = 6;
		
		Random random = new Random();
		double rnd;
		 
		load1.initialize(); load2.initialize();	scale.initialize();
		
		double sim_time = 11000;
		while(scale.num_custs_delayed < 10000){
			rnd = random.nextDouble();
			if(rnd <0.3) {
				load1.mean_service = 5; load2.mean_service = 5;
			}
			else if((rnd >= 03) && rnd < 0.8){
				load1.mean_service = 10; load2.mean_service = 10;
			}
			else {
				load1.mean_service = 15; load2.mean_service = 15;
			}
			if(load1.time_last_event <= load2.time_last_event)
				load1_activity();
			else
				load2_activity(load1.sim_time);
			
		}
		
		
		System.out.println("\n\nReport for server 1:\n--------------------\n");
		load1.report();
		System.out.println("\n\nReport for server 2:\n--------------------\n");
		load2.report();
		System.out.println("\n\nTime simulation ended = "+ (scale.sim_time) +"\n");
	}
	 
	private static void load1_activity(){
		load1.timing();
		 
		load1.update_time_avg_stats();
		 
		switch (load1.next_event_type){
			case 1: 
				load1.arrive(load1.sim_time+expon(load1.mean_interarrival));
				break;
			case 2: 
				load1.depart();
				scale_activity(load1.sim_time);
			load2_activity(load1.sim_time);
			break;	
		}
	}
	 
	private static void load2_activity(double time){
		load2.timing();
		 
		load2.update_time_avg_stats();
		 
		switch (load2.next_event_type){
			case 1: 
				load2.arrive(time);
				break;
			case 2: 
				load2.depart();
				scale_activity(time);
			load2_activity(time);
		}
	}
	
	private static void scale_activity(double time){
		Random random = new Random();
		double rnd = random.nextDouble();
		if(rnd < 0.7)
			scale.mean_service=12;
		else
			scale.mean_service=16;
		
		scale.timing();		 
		scale.update_time_avg_stats();
		 
		switch (scale.next_event_type){
			case 1: 
				scale.arrive(time);
				break;
			case 2: 
				scale.depart();
			scale_activity(time);
		}
	}
	
	 
	public static double expon(double  mean){
		Random random = new Random();
		return -mean * Math.log(random.nextDouble());
	}
	
	public static double uniform(double a, double b){
		Random ran = new Random();
		return (double) (a+((b-a)*ran.nextDouble()));
	}
}