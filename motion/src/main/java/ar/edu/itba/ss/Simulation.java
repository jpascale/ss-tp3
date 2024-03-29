package ar.edu.itba.ss;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;

public class Simulation {
	private static final int N = 100;
	private static final double runningTime = 150; //Seconds
	private static final double ultimoTercio = runningTime - runningTime/3;

	private static final double L = 0.5;
	
	private static final double kb = 1.380648813E-23;

	private static final double radius = 0.005;
	private static final double mass = 0.1;

	private static final double bigRadius = 0.05;
	private static final int bigMass = 100;

	private final static double printInterval = 0.1;

    private static Double currTime = 0d;

    private static Integer cantCollitions = 0;
    private static int colisions = 0;
    private static int ocurances = 0;
    private static double lastdraw = -printInterval;
    
	private static ArrayList<Particle> list;
	private static List<Particle> original = new ArrayList<Particle>();
	private static PriorityQueue<Event> events = new PriorityQueue<>(new EventComparator());

	private static List<Particle> borders = generateBorders();


	public static void main( String[] args ){

        System.out.print("Creating particles... ");
		list = Particle.generateParticles(N, radius, mass, bigRadius, bigMass, L);
		original.addAll(list);
		System.out.println("Done!");
		double t = calculateTemp(list);
        System.out.print("Generating initial events... ");
        initialEventFill();
        System.out.println("Done!");
        while (events.size() > 0 && currTime < runningTime){
            Event event = events.poll();
            ocurances ++;
           // System.out.println("LOG: Event " + event);
            if (!event.isInvalid()){
              //  System.out.println("LOG: Event is valid");
                Double delta = event.getTime() - currTime;

                Double currAdv = delta % printInterval;
                if (!currAdv.equals(0d)){
                    advanceParticles(currAdv);
                    printAll(currAdv);
                }

                while (currAdv < delta && currAdv + printInterval < delta) {
                    advanceParticles(printInterval);
                    printAll(printInterval);
                    currAdv += printInterval;
                }
              advanceParticles(delta - currAdv);
                
              
                if (event.particleCollision()){

                    collision(event.getP1(), event.getP2());

                    event.getP1().increaseEventCount();
                    event.getP2().increaseEventCount();

                    currTime = event.getTime();

                    foreseeCollisions(event.getP1());
                    foreseeCollisions(event.getP2());

                } else {

                    Particle p;
                    if (event.getP1() != null){
                        //Vertical collision between p1 and a wall
                        p = event.getP1();
                        p.setYSpeed(event.getP1().getYSpeed() * -1);
                    } else {
                        //Horizontal collision p2 and a wall
                        p = event.getP2();
                        p.setXSpeed(event.getP2().getXSpeed() * -1);
                    }
                    p.increaseEventCount();
                    currTime = event.getTime();
                    foreseeCollisions(p);
                }
            }  else {
               // System.out.println("Event discarded");
            }

            System.out.println("LOG: CurrTime " + currTime);
        }

        System.out.println("T = " + t);

        System.out.println("Collision freq:" + (double)cantCollitions / currTime);

	}
	
	public static double calculateK(List<Particle> particles) {
		double K = 0.0;
		for (Particle p : particles) {
			K += p.getMass() * Math.pow(p.getSpeed(), 2);
		}
		return K/particles.size();
	}
	
	public static double calculateTemp(List<Particle> particles){
		return (2*calculateK(particles))/(3*kb);
	}
	
	public static double calculateMeanDispl(List<Particle> particles){
		double MD = 0.0;
		for(Particle p:particles){
			MD += Math.pow(p.getPosition() - original.get(p.getId()).getPosition(), 2);
		}
		return MD/particles.size();
	}

    public static void foreseeCollisions(Particle p){

        //Other particles
        for (Particle curr : list){
            if (!curr.equals(p)){
                Optional<Double> opt = getCollisionDelta(p, curr);

                if (opt.isPresent()){

                    //TODO: Check this
                    if (opt.get() > 0)
                        events.add(new Event(currTime + opt.get(), p, curr));
                }
            }
        }

        //Borders
        foreseeBorderCollitions(p);

    }

    private static void foreseeBorderCollitions(Particle p){
        //Check collision with wall in X component
        Optional<Double> opt = getCollisionDelta(p.getX(), p.getXSpeed(), p.getRadius());
        if (opt.isPresent()) {
            events.add(new Event(currTime + opt.get(), null, p));
        }

        //Check collision with wall in Y component
        opt = getCollisionDelta(p.getY(), p.getYSpeed(), p.getRadius());
        if (opt.isPresent()) {
            events.add(new Event(currTime + opt.get(), p, null));
        }
    }

    public static void advanceParticles(double time){
        for (Particle p : list) {
            p.advaceDelta(time);
        }
    }

    private static void initialEventFill(){

        for (int i = 0; i < list.size(); i++) {
            Particle p = list.get(i);

            //Check collision with other particles
            for (int j = i + 1; j < list.size(); j++){
                Optional<Double> opt = getCollisionDelta(p, list.get(j));

                if (opt.isPresent()){
                    events.add(new Event(opt.get(), p, list.get(j)));
                }
            }

            //Borders
            foreseeBorderCollitions(p);
        }

    }


    /**
     * Performs elastic collision between two particles and update velocities.
     * @param p1 The first particle.
     * @param p2 The second particle.
     */

    private static void collision(Particle p1, Particle p2) {
        double totalRadius = p1.getRadius() + p2.getRadius();

        double deltaX = p2.getX() - p1.getX();
        double deltaY = p2.getY() - p1.getY();

        double deltaVX = p2.getXSpeed() - p1.getXSpeed();
        double deltaVY = p2.getYSpeed() - p1.getYSpeed();

        double deltaVDeltaR = deltaVX * deltaX + deltaVY * deltaY;

        double J = (2 * p1.getMass() * p2.getMass() * deltaVDeltaR)/(totalRadius * (p1.getMass() + p2.getMass()));
        double Jx = J * deltaX / totalRadius;
        double Jy = J * deltaY / totalRadius;

        p1.setXSpeed(p1.getXSpeed() + Jx / p1.getMass());
        p1.setYSpeed(p1.getYSpeed() + Jy / p1.getMass());

        p2.setXSpeed(p2.getXSpeed() - Jx / p2.getMass());
        p2.setYSpeed(p2.getYSpeed() - Jy / p2.getMass());

        cantCollitions++;
    }

    /**
     * Computes the time in which two particles are going to collide.
     *
     * @param p1 The first particle.
     * @param p2 The second particle.
     * @return Optional containing or not the time.
     */

    private static Optional<Double> getCollisionDelta(Particle p1, Particle p2) {
        double totalRadius = p1.getRadius() + p2.getRadius();

        double deltaX = p2.getX() - p1.getX();
        double deltaY = p2.getY() - p1.getY();

        double deltaVX = p2.getXSpeed() - p1.getXSpeed();
        double deltaVY = p2.getYSpeed() - p1.getYSpeed();

        double deltaRSquare = Math.pow(deltaX, 2) + Math.pow(deltaY, 2);
        double deltaVSquare = Math.pow(deltaVX, 2) + Math.pow(deltaVY, 2);

        double deltaVDeltaR = deltaVX * deltaX + deltaVY * deltaY;

        double d = Math.pow(deltaVDeltaR, 2) - deltaVSquare * (deltaRSquare - Math.pow(totalRadius, 2));


        if (deltaVDeltaR >= 0 || d < 0){
            return Optional.empty();
        }

        return Optional.of(-1 * ((deltaVDeltaR + Math.sqrt(d)) / deltaVSquare));

    }

    /**
     * Computes the time in which a particle is going to collide with the wall of the
     * component that is being tested.
     *
     * @param x The position in that component
     * @param Vx The velocity in that component
     * @return Optional containing or not the time.
     */
    public static Optional<Double> getCollisionDelta(Double x, Double Vx, Double Radius){
        if (Vx.equals(0d)){
            return Optional.empty();
        }

        Double time;

        if (Vx > 0){
            time = (L - Radius - x) / Vx;
        } else {
            time = (Radius - x) / Vx;
        }

        return Optional.of(time);
    }


    private static void printAll(double time){

        StringBuilder sb = new StringBuilder();

        sb.append('\t').append(N + 5).append('\n');
        sb.append('\t').append("Comment").append('\n');

        for (Particle p : borders){
            sb.append('\t').append(-1).append('\t').append(p.getX()).append('\t').append(p.getY()).append('\t').append(p.getRadius()).append('\t').append('0') .append('\n');
        }

        for (Particle p : list){
            sb.append('\t').append(p.getId()).append('\t').append(p.getX()).append('\t').append(p.getY()).append('\t').append(p.getRadius()).append('\t');
            if (p.getId().equals(0)){
                sb.append('1').append("1\t0\t0").append('\n');
            } else {
                sb.append('0').append("0.2\t0\t1")append('\n');
            }
        }


        try {
            FileWriter fw = new FileWriter("out.txt", true);
            fw.write(sb.toString());
            fw.close();
        } catch (IOException e){
            e.printStackTrace();
        }
        
        
        int t = (int)((currTime + time)*100);
        
        try{
			FileWriter fw2 = new FileWriter("big.txt",true);
			double dist = Math.sqrt(Math.pow(list.get(0).getX(),2) + Math.pow(list.get(0).getY(), 2));
			fw2.write("\t" + t + "\t" + dist + "\n");
			fw2.close();
		}catch(IOException e){
			e.printStackTrace();
		}
        if(t > ultimoTercio){
        	try{
    			FileWriter fw2 = new FileWriter("vel.txt",true);
    			for(Particle p: list){
    				fw2.write("\t" + p.getSpeed() + "\n");
    			}
    			fw2.close();
    		}catch(IOException e){
    			e.printStackTrace();
    		}
        }
        
		try{
			FileWriter fw2 = new FileWriter("colTime.txt",true);
			fw2.write(colisions + "\n");
			fw2.close();
		}catch(IOException e){
			e.printStackTrace();
		}
        
        
        
        try{
			FileWriter fw2 = new FileWriter("bigtr.txt",true);
			fw2.write(list.get(0).getX() + "\t" + list.get(0).getY() + "\n");
			fw2.close();
		}catch(IOException e){
			e.printStackTrace();
		}
		
		try{
			FileWriter fw2 = new FileWriter("col.txt",true);
			for(int i =0;i<colisions;i++)
				fw2.write(ocurances + "\n");
			fw2.close();
		}catch(IOException e){
			e.printStackTrace();
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
