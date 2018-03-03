package org.rsi.model;

public class Cereal {
    private String name;
    private int numOfBoxes;

    public Cereal() {

    }

    public Cereal(String name, int numOfBoxes) {
        this.name = name;
        this.numOfBoxes = numOfBoxes;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


	public int getNumOfBoxes() {
		return numOfBoxes;
	}

	public void setNumOfBoxes(int numOfBoxes) {
		this.numOfBoxes = numOfBoxes;
	}

    @Override
    public String toString() {
        return "name: " + name + ", numOfBoxes: " + numOfBoxes;
    }


}
