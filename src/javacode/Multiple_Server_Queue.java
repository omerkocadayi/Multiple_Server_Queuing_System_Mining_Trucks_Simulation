package javacode;

import java.text.DecimalFormat;
import java.io.*;
import java.util.Random;
 
public class Multiple_Server_Queue{
	public static Server load1;	public static Server load2;
	public static Server scale;	public static int direct_out = 0;
	 
	public static void main(String[] args)throws IOException{
		DecimalFormat df = new DecimalFormat("##.#");
		load1 = new Server(); load1.mean_interarrival = 80;
		load2 = new Server(); load2.mean_interarrival = 80;
		scale = new Server(); 
		
		Random random = new Random();
		double rnd; int total_truck = 6;
		 
		load1.initialize(); load2.initialize();	scale.initialize();
		load1_activity(); load2_activity(load1.sim_time); scale_activity(load1.sim_time);
		
		while(load1.num_custs_delayed + load2.num_custs_delayed < 10000){
			if((load1.num_in_q + load2.num_in_q + scale.num_in_q) < total_truck) {
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
				if(load1.sim_time < load2.sim_time)
					load2_activity(load1.sim_time);
				else
					load1_activity();
			}
			else {
				load1.next_event_type = 2;load1.next_event_type = 2;scale.next_event_type = 2;
				scale.depart(); load1_activity(); load2_activity(load1.sim_time); 
			}
			
		}
		
		System.out.println("\t*** Server1 - Loading Area 1 - Report :\n\t------------------------------");
		load1.report();
		System.out.println("\t------------------------------\n\t*** Server2 - Loading Area 2 - Report :\n\t------------------------------");
		load2.report();
		System.out.println("\t------------------------------\n\t*** Server3 - Scale Area - Report :\n\t------------------------------");
		scale.report();
		System.out.println("\t*** The simulation took "+ df.format(scale.sim_time) +" minutes !!\n");
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
