package backend;

class Node {
	private int to;
	private int cost;
	private String fname;
	private String tname;
	private Node parent;

	// 0 means A* will not be used.
	// 1 means A* will be used for Qalqilia. else means A* will be used for Tulkarm.
	private int hQ;
	private int hT;
	private int f;
	private int flagToTurnOnHeurestic;

	Node(String fname, Node parent) {
		this.fname = fname;
		this.parent = parent;
		f = 0;
		hT = 0;
		hQ = 0;
		flagToTurnOnHeurestic = 0;
	}

	Node(String fname, Node parent, int cost) {
		this(fname, parent);
		this.cost = cost;
	}

	Node(int to, int cost, String fname, String tname) {
		this.to = to;
		this.cost = cost;
		this.fname = fname;
		this.tname = tname;
		f = 0;
		hT = 0;
		hQ = 0;
		flagToTurnOnHeurestic = 0;
	}

	public int getF() {
		return f;
	}

	public int getHQ() {
		return hQ;
	}

	public int getHT() {
		return hQ;
	}

	public int getFlag() {
		return flagToTurnOnHeurestic;
	}

	public int getCost() {
		return cost;
	}

	public String getFname() {
		return fname;
	}

	public String getTname() {
		return tname;
	}

	public Node getParent() {
		return parent;
	}

	public void setHQ(int hQ) {
		this.hQ = hQ;
	}

	public void setHT(int hT) {
		this.hT = hT;
	}

	public void setFlag(int val) {
		if (val < 0) {
			val = 0;
		} else if (val > 1) {
			val = 2;
		}
		flagToTurnOnHeurestic = val;
	}

	public void setF(int f) {
		this.f = f;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	@Override
	public String toString() {
		return "Node [to=" + to + ", cost=" + cost + ", fname=" + fname + ", tname=" + tname + "]";
	}
}