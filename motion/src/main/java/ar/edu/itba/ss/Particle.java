package ar.edu.itba.ss;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Particle {

	private Integer id;

	private Double radius = 0.0;
	private Double property = 0.0;

	private Double x_pos = 0.0;
	private Double y_pos = 0.0;
	
	private Double x_speed = 0.0;
	private Double y_speed = 0.0;

	public Particle(final Integer id){
		this.id = id;
	}


	public Particle(int id, double radius, double property, double x, double y, double x_speed, double y_speed,double speedModule, double heading){
		this.id = id;
		this.radius = radius;
		this.property = property;
		this.x_pos = x;
		this.y_pos = y;
		this.x_speed = x_speed;
		this.y_speed = y_speed;
	}

	public Particle(int id, double radius, double property, double x, double y, double x_speed, double y_speed){
		this.id = id;
		this.radius = radius;
		this.property = property;
		this.x_pos = x;
		this.y_pos = y;
		this.x_speed = x_speed;
		this.y_speed = y_speed;
	}
	
	public Particle(double x, double y,double radius){
		this.x_pos = x;
		this.y_pos = y;
		this.radius = radius;
	}

	public static List<Particle> generateParticles(int N, double r, double m, double rb, int mb, double L) {
		ArrayList<Particle> list = new ArrayList<>();
		Random rand = new Random();
		double randomX,randomY;
		//BIG PARTICLE
		do{
			randomX = L * rand.nextDouble();
			randomY = L * rand.nextDouble();
		}while(!valid(randomX,randomY,list,L,rb));
		double randomXS = rand.nextDouble() * 0.2 - 0.1;
		double randomYS = rand.nextDouble() * 0.2 - 0.1;
		list.add(new Particle(0,rb,mb,randomX,randomY,randomXS,randomYS));
		
		//ALL THE N SMALL PARTICLES
		for(int i=1 ; i<=N ; i++){
			do{
				randomX = L * rand.nextDouble();
				randomY = L * rand.nextDouble();
			}while(!valid(randomX,randomY,list,L,r));
			randomXS = rand.nextDouble() * 0.2 - 0.1;
			randomYS = rand.nextDouble() * 0.2 - 0.1;
			list.add(new Particle(i,r,m,randomX,randomY,randomXS,randomYS));
		}
		return list;
	}
	
	public static void CreateFile(final List<Particle> list){
		File file;
		FileOutputStream fop = null;
		file = new File("out.txt");
		try {
			fop = new FileOutputStream(file);
			if(!file.exists()){
				file.createNewFile();
			}
			int time = 0;
			String init = "\t" + list.size() + "\n" +"\t" + time +"\n";
			fop.write(init.getBytes());
			for(Particle p: list){
				String aux = "\t" + p.getId() + "\t" + p.getX_pos() + "\t" + p.getY_pos() + "\t" + p.getXSpeed() + "\t" + p.getYSpeed() + "\t" + p.getRadius() + "\n";
				fop.write(aux.getBytes());
				fop.flush();
			}
			fop.flush();
			fop.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	private static boolean valid(final double x,final  double y,final  ArrayList<Particle> list,final  double l,final  double r) {
		if(x - r <= 0 || x + r >= l || y - r <= 0 || y + r >= l){
			return false;
		}
		for(Particle p:list){
			double dist =Math.pow(x - p.getX_pos(), 2) + Math.pow(y - p.getY_pos(), 2);
			double rad = Math.pow(r + p.getRadius(), 2);
			if(Double.compare(dist, rad) <= 0){
				return false;
			}
		}
		return true;
	}
	
	public double collideX() {
		if(x_speed > 0){
			return 0;
		}
		// TODO
		return 0;
	}

	public double collideY() {
		if(y_speed > 0){
			return 0;
		}
		// TODO
		return 0;
	}

	public double collide(final Particle particle) {
		// TODO 
		return 0;
	}
	
	public void bounceX(){
		// TODO 
	}
	
	public void bounceY(){
		// TODO 
	}
	
	public void bounce(Particle p){
		// TODO 
	}
	

	public double getXSpeed(){
		return x_speed;
	}

	public double getYSpeed(){
		return y_speed;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Particle particle = (Particle) o;

		return id.equals(particle.id);

	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	public Double getY_pos() {
		return y_pos;
	}
	public void setY_pos(double y_pos) {
		this.y_pos = y_pos;
	}

	public Double getX_pos() {
		return x_pos;
	}

	public void setX_pos(double x_pos) {
		this.x_pos = x_pos;
	}

	public Double getProperty() {
		return property;
	}

	public void setProperty(double property) {
		this.property = property;
	}

	public Double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	@Override
	public String toString() {
		return "Particle " + this.id + " (" + this.x_pos + "," + this.y_pos + ")";
	}

	public Integer getId() {
		return id;
	}

}

