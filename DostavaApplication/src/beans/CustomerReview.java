package beans;

public class CustomerReview implements IEntity<Integer>{
	private int reviewID;
	private String customerUsername;
	private int restourantID;
	private String review;
	private int rating;
	private boolean deleted;
	
	public CustomerReview(String customerUsername, int restourantID, String review, int rating) {
		super();
		this.customerUsername = customerUsername;
		this.restourantID = restourantID;
		this.review = review;
		this.rating = rating;
		this.deleted = false;
	}

	public String getCustomerUsername() {
		return customerUsername;
	}

	public void setCustomerUsername(String customerUsername) {
		this.customerUsername = customerUsername;
	}

	public int getRestourantID() {
		return restourantID;
	}

	public void setRestourantID(int restourantID) {
		this.restourantID = restourantID;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	@Override
	public Integer getID() {
		return this.reviewID;
	}

	@Override
	public void setID(Integer id) {
		this.reviewID = id;		
	}

	@Override
	public boolean isEqual(Integer id) {
		return this.reviewID == id;
	}
	
}
