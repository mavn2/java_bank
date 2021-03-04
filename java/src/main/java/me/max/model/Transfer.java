package me.max.model;

public class Transfer {
	private int id;
	private String uFrom;
	private String aFrom;
	private String uTo;
	private String aTo;
	private double amount;
	private boolean pending;
	private boolean approved;
	
	public Transfer() {
		super();
	}

	public Transfer(int id, String uFrom, String aFrom, String uTo, String aTo, double amount, boolean pending) {
		super();
		this.id = id;
		this.uFrom = uFrom;
		this.aFrom = aFrom;
		this.uTo = uTo;
		this.aTo = aTo;
		this.amount = amount;
		this.pending = pending;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getuFrom() {
		return uFrom;
	}

	public void setuFrom(String uFrom) {
		this.uFrom = uFrom;
	}

	public String getaFrom() {
		return aFrom;
	}

	public void setaFrom(String aFrom) {
		this.aFrom = aFrom;
	}

	public String getuTo() {
		return uTo;
	}

	public void setuTo(String uTo) {
		this.uTo = uTo;
	}

	public String getaTo() {
		return aTo;
	}

	public void setaTo(String aTo) {
		this.aTo = aTo;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public boolean isPending() {
		return pending;
	}

	public void setPending(boolean pending) {
		this.pending = pending;
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}
	
	
}
