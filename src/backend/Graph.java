package backend;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

public class Graph {
	private static Map<String, Boolean> vis;
	private static Map<String, Integer> mp;
	private static ArrayList<Node>[] g;
	private static int[] hQalqilia;
	private static int[] hTulkarm;

	// return map of graph of cities.
	public Map<String, Integer> getMap() {
		Map<String, Integer> temp = mp;
		return temp;
	}

	// read cities, and mapping the cities with numbers.
	public static void mappingCities() {
		mp = new HashMap<String, Integer>();
		vis = new HashMap<String, Boolean>();
		hQalqilia = new int[20];
		hTulkarm = new int[20];

		// Cities file contains [<City_Name> <H_Qalqilia> <H_Tulkarm>]
		File citiesFile = new File("D:\\java projects\\ENCS434-ArtificiaIntelligence\\src\\backend\\Cities.txt");

		Scanner in;

		try {
			int cnt = 0;
			in = new Scanner(citiesFile);
			while (in.hasNext()) {
				String city = in.next();
				String hQ = in.next();
				String hT = in.next();
				hQalqilia[cnt] = Integer.parseInt(hQ);
				hTulkarm[cnt] = Integer.parseInt(hT);
				mp.put(city, cnt++);
			}
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	// read edges between cities, and mapping the cities with numbers.
	public static void generateLinks() {
		g = new ArrayList[20];
		for (int i = 0; i < 20; i++) {
			g[i] = new ArrayList<Node>();
		}

		File linksFile = new File("D:\\java projects\\ENCS434-ArtificiaIntelligence\\src\\backend\\links.txt");
		Scanner in;
		try {
			in = new Scanner(linksFile);
			while (in.hasNext()) {
				String from = in.next();
				String to = in.next();
				String cost = in.next();
				Node a = new Node(mp.get(to), Integer.parseInt(cost), from, to);
				Node b = new Node(mp.get(from), Integer.parseInt(cost), to, from);

				a.setHQ(hQalqilia[mp.get(from)]);
				b.setHQ(hQalqilia[mp.get(to)]);

				a.setHT(hTulkarm[mp.get(from)]);
				b.setHT(hTulkarm[mp.get(to)]);

				g[mp.get(from)].add(a);
				g[mp.get(to)].add(b);
			}
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	// return string of path.
	public static String retPath(Node goal) {
		Stack<Node> path = new Stack<>();
		while (goal != null) {
			path.push(goal);
			goal = goal.getParent();
		}
		StringBuilder str = new StringBuilder();
		while (!path.isEmpty()) {
			str.append(path.pop().getFname());
			if (!path.isEmpty()) {
				str.append(" ");
			}
		}
		return str.toString();
	}

	// Depth first search algorithm
	public static ArrayList<Object> dfs(String start, String end) {
		for (String name : mp.keySet()) {
			vis.put(name, false);
		}

		Stack<Node> stack = new Stack<>();
		stack.push(new Node(start, null));
		vis.put(start, true);
		Node goal = null;

		ArrayList<Object> ret = new ArrayList<>();
		ArrayList<String> expansionNodes = new ArrayList<>();

		while (!stack.isEmpty()) {
			Node current = stack.pop();
			int idx = mp.get(current.getFname());

			if ((current.getFname()).equals(end)) {
				goal = current;
				break;
			}

			expansionNodes.add(current.getFname());
			for (int j = 0; j < g[idx].size(); j++) {
				if (vis.get(g[idx].get(j).getTname()) == false) {
					vis.put(g[idx].get(j).getTname(), true);
					stack.push(new Node(g[idx].get(j).getTname(), current));
				}
			}
		}

		ret.add(retPath(goal));
		ret.add(expansionNodes);
		return ret;
	}

	// Breadth first search algorithm
	public static ArrayList<Object> bfs(String start, String end) {
		for (String name : mp.keySet()) {
			vis.put(name, false);
		}

		Queue<Node> queue = new LinkedList<>();
		queue.add(new Node(start, null));
		vis.put(start, true);
		Node goal = null;

		ArrayList<Object> ret = new ArrayList<>();
		ArrayList<String> expansionNodes = new ArrayList<>();

		while (!queue.isEmpty()) {
			Node current = queue.poll();
			int idx = mp.get(current.getFname());

			if ((current.getFname()).equals(end)) {
				goal = current;
				break;
			}

			expansionNodes.add(current.getFname());
			for (int j = 0; j < g[idx].size(); j++) {
				if (vis.get(g[idx].get(j).getTname()) == false) {
					vis.put(g[idx].get(j).getTname(), true);
					queue.add(new Node(g[idx].get(j).getTname(), current));

				}
			}
		}

		ret.add(retPath(goal));
		ret.add(expansionNodes);
		return ret;
	}

	// Uniform cost algorithm
	public static ArrayList<Object> uniform(String start, String end) {
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < g[i].size(); j++) {
				g[i].get(j).setFlag(0);
			}
		}

		// initialize cost with infinity.
		Map<String, Integer> cost = new HashMap<>();
		for (String name : mp.keySet()) {
			cost.put(name, Integer.MAX_VALUE);
		}

		cost.put(start, 0);
		PriorityQueue<Node> pq;
		pq = new PriorityQueue<Node>((Node x1, Node x2) -> (x1.getCost() - x2.getCost()));
		pq.add(new Node(start, null, 0));
		Node goal = null;

		ArrayList<Object> ret = new ArrayList<>();
		ArrayList<String> expansionNodes = new ArrayList<>();

		while (!pq.isEmpty()) {
			Node current = pq.poll();
			int idx = mp.get(current.getFname());

			if ((current.getFname()).equals(end)) {
				ret.add(current.getCost());
				goal = current;
				break;
			}

			expansionNodes.add(current.getFname());
			for (int j = 0; j < g[idx].size(); j++) {
				String to = g[idx].get(j).getTname();
				int curCost = g[idx].get(j).getCost();
				if (curCost + cost.get(current.getFname()) < cost.get(to)) {
					cost.put(to, curCost + cost.get(current.getFname()));
					pq.add(new Node(to, current, cost.get(to)));
				}
			}
		}

		ret.add(retPath(goal));
		ret.add(expansionNodes);
		return ret;
	}

	// A* algorithm with heuristic where goal = Qalqilia | Tulkarm
	public static ArrayList<Object> aStar(String start, String end, int flag) {
		// select flag && h for Qalqilia or Tulkarm.
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < g[i].size(); j++) {
				g[i].get(j).setFlag(flag);
			}
		}

		// initialize cost with infinity.
		Map<String, Integer> cost = new HashMap<>();
		for (String name : mp.keySet()) {
			cost.put(name, Integer.MAX_VALUE);
		}

		int h = 0;
		if (flag == 1) {
			h = hQalqilia[mp.get(start)];
		} else {
			h = hTulkarm[mp.get(start)];
		}

		PriorityQueue<Node> pq;
		PriorityQueue<Node> openList;
		PriorityQueue<Node> xopenList;
		PriorityQueue<Node> closedList;
		PriorityQueue<Node> xclosedList;
		pq = new PriorityQueue<Node>((Node x1, Node x2) -> (x1.getF() - x2.getF()));
		openList = new PriorityQueue<Node>((Node x1, Node x2) -> (x1.getF() - x2.getF()));
		xopenList = new PriorityQueue<Node>((Node x1, Node x2) -> (x1.getF() - x2.getF()));
		closedList = new PriorityQueue<Node>((Node x1, Node x2) -> (x1.getF() - x2.getF()));
		xclosedList = new PriorityQueue<Node>((Node x1, Node x2) -> (x1.getF() - x2.getF()));

		cost.put(start, 0);
		Node firstNode = new Node(start, null, 0);
		firstNode.setF(0 + h);
		pq.add(firstNode);
		openList.add(firstNode);
		Node goal = null;

		ArrayList<Object> ret = new ArrayList<>();
		ArrayList<String> expansionNodes = new ArrayList<>();

		while (!openList.isEmpty()) {
			Node top = openList.peek();
			if ((top.getFname()).equals(end)) {
				ret.add(top.getCost());
				goal = top;
				break;
			}

			int idx = mp.get(top.getFname());
			expansionNodes.add(top.getFname());

			for (int j = 0, heuristic; j < g[idx].size(); j++) {
				if (flag == 1) {
					heuristic = hQalqilia[mp.get(g[idx].get(j).getTname())];
				} else {
					heuristic = hTulkarm[mp.get(g[idx].get(j).getTname())];
				}
				String to = g[idx].get(j).getTname();
				Node newNode = new Node(to, top); // name, parent

				int curgCost = g[idx].get(j).getCost() + top.getCost();

				xclosedList.clear();
				xopenList.clear();
				boolean test1 = false;
				boolean test2 = false;
				while (!openList.isEmpty()) {
					Node temp = openList.poll();
					if (temp.getFname().equals(newNode.getFname())) {
						test1 = true;
					}
					xopenList.add(temp);
				}
				while (!closedList.isEmpty()) {
					Node temp = closedList.poll();
					if (temp.getFname().equals(newNode.getFname())) {
						test2 = true;
					}
					xclosedList.add(temp);
				}
				while (!xopenList.isEmpty()) {
					openList.add(xopenList.poll());
				}
				while (!xclosedList.isEmpty()) {
					closedList.add(xclosedList.poll());
				}

				if (test1 == false && test2 == false) {
					newNode.setCost(curgCost);
					newNode.setF(newNode.getCost() + heuristic);
					openList.add(newNode);
				} else if (curgCost < cost.get(to)) {
					newNode.setCost(curgCost);
					newNode.setF(newNode.getCost() + heuristic);
					cost.put(to, curgCost);
					if (test1 == true) {
						closedList.remove(newNode);
						openList.add(newNode);
					}
				}
			}
			openList.remove(top);
			closedList.add(top);
		}
		ret.add(retPath(goal));
		ret.add(expansionNodes);
		return ret;
	}

	protected void cout(String str, String str2) {
		System.out.print(str);
		System.out.print(str2);
	}

	public void run(String from, String to, int flag) {

		ArrayList<Object> s1 = Graph.dfs(from, to);
		ArrayList<Object> s2 = Graph.bfs(from, to);
		ArrayList<Object> s3 = Graph.uniform(from, to);
		ArrayList<Object> s4 = Graph.aStar(from, to, flag);

		cout("\t\t\t\t[Path from " + from + " to " + to + "]", "\n");
		cout(" [DFS: ", "");
		cout(s1.get(0) + "]", "\n");
		cout(" [BFS: ", "");
		cout(s2.get(0) + "]", "\n");
		cout(" [UNI: ", "");
		cout("#cnt=" + s3.get(2).toString().length() + ", Costs=" + s3.get(0) + ", " + s3.get(1) + "]", "\n");
		cout(" [A* : ", "");
		cout("#cnt=" + s4.get(2).toString().length() + ", Costs=" + s4.get(0) + ", " + s4.get(1) + "]", "\n");
		cout("========================================================================================\n", "");
	}
}
