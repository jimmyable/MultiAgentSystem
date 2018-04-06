package jimmy.assesment;

public class Good {
	private String name;
	private int value;
	private double increase;
	private int reserve;
	
	public Good(String name, int value, double increase, int reserve) {
		this.name = name;
		this.value = value;
		this.increase = increase;
		this.reserve = reserve;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public double getIncrease() {
		return increase;
	}
	
	public void setIncrease(double increase) {
		this.increase = increase;
	}
	
	public int getReserve(){
		return reserve;
	}
	
	public void setReserve(int reserve) {
		this.reserve = reserve;
	}
	
	//Good salmon = new Good("salmon", 1000, 0.05, 500);
	//salmon.getPrice();
	//salmon.setPrice(6969);
	
	//TODO GETTERS AND SETTERS
	//TODO something else that u want goods to have

}
