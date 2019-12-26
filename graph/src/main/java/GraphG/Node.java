package GraphG;

class Node {

	boolean infected, new_infected;
	
	public boolean immune = false;
	
	public boolean isInfected() {
		return infected;
	}
	 
	public void setInfected(boolean new_infected) {
		infected = new_infected;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "";
	}
}