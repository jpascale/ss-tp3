package ar.edu.itba.ss;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Simulation {
	private static final int N = 150;
	private static final int it = 1000;

	private static final double L = 0.5;

	private static final double R = 0.005;
	private static final double m = 0.1;

	private static final double Rb = 0.05;
	private static final int mb = 100;

	private final static double timePrint = 0.1;

	private static List<Particle> list;
	private static PriorityQueue<Event> events;
	
	private static List<Particle> borders = generateBorders();

	public static void main( String[] args ){
		boolean finished = false;
		events = new PriorityQueue<>();
		list = Particle.generateParticles(N, R, m, Rb, mb, L);

		//debug
		Particle.CreateFile(list);

		//Calculates initial interactions with particles
		for(Particle p: list){
			getInteractions(p);
		}
		
		for(int i = 0; i< it || !finished; i++){
			Event e = events.poll();
			if(e == null){
				finished = true;
			}
			evolveParticles(e.getTime());
			getNewVelocities(e);
			updateCollisions(e);
		}
	}
	
	

	private static void updateCollisions(Event e) {
		// TODO Auto-generated method stub
		
	}



	private static void getNewVelocities(Event e) {
		// TODO Auto-generated method stub
		
	}



	private static void getInteractions(Particle p) {
		double tc;

		//Checks collisions with wall in X
		tc = p.collideX();
		events.add(new Event(tc, p, null));

		//Checks collisions with wall in Y
		tc = p.collideY();
		events.add(new Event(tc,null,p));

		//Checks collisions with other particles
		for(int j=0;j<list.size();j++){
			Particle aux = list.get(j);
			if(!aux.equals(p)){
				tc = p.collide(aux);
				events.add(new Event(tc,p,aux));
			}
		}
	}

	private static void evolveParticles(double time){
		for(Particle p: list){
			p.setX_pos(p.getX_pos() + p.getXSpeed()*time);
			p.setY_pos(p.getY_pos() + p.getYSpeed()*time);
		}
	}
	
	private static List<Particle> generateBorders() {
		List<Particle> list = new ArrayList<>();
		list.add(new Particle(0,0,0.01));
		list.add(new Particle(0,L,0.01));
		list.add(new Particle(L,0,0.01));
		list.add(new Particle(L,L,0.01));
		return list;
	}

}
