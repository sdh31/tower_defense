package main.java.schema;

public abstract class TowerSchema {
	private String myName;
	private String myConcreteType;

	public String getMyConcreteType() {
		return myConcreteType;
	}
	
	public void setMyConcreteType(String myConcreteType) {
		this.myConcreteType = myConcreteType;
	}
	
	public String getMyName() {
		return myName;
	}

	public void setMyName(String myName) {
		this.myName = myName;
	}
}
