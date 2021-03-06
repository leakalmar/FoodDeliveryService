package beans;

public class CustomerCategory {
	private CustomerType type;
	private int discount;
	private int pointsRequired;
	
	public CustomerCategory() {
		super();
		this.type = CustomerType.Bronze;
		this.discount = 0;
		this.pointsRequired = 0;
	}
	
	public CustomerCategory(CustomerType type, int discount, int pointsRequired) {
		super();
		this.type = type;
		this.discount = discount;
		this.pointsRequired = pointsRequired;
	}
	
	public CustomerType getType() {
		return type;
	}
	public void setType(CustomerType type) {
		this.type = type;
	}
	public int getDiscount() {
		return discount;
	}
	public void setDiscount(int discount) {
		this.discount = discount;
	}
	public int getPointsRequired() {
		return pointsRequired;
	}
	public void setPointsRequired(int pointsRequired) {
		this.pointsRequired = pointsRequired;
	}
	
	
}
