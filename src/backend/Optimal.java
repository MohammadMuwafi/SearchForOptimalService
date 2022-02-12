package backend;

import java.util.Random;

public class Optimal {
	final static int INF = 1000;
	static double mnCostforOptimalT = Integer.MAX_VALUE;
	static int mnCostforOptimalD = Integer.MAX_VALUE;

	static String pathT = "0000";
	static String pathD = "0000";

	static String[] cities = { 
			"Jenin", // 0
			"Nablus", // 1
			"Ramallah", // 2
			"Salfit", // 3
			"Qalqilia", // 4
			"Tulkarm" // 5
	};

	static int[][] g = { 
			{ 0, 11, 33, 14, 41, 27 }, 
			{ 11, 0, 25, 43, 23, 13 }, 
			{ 33, 25, 0, 17, 37, 36 },
			{ 14, 43, 17, 0, 15, 45 }, 
			{ 41, 23, 37, 15, 0, 19 }, 
			{ 27, 13, 36, 45, 19, 0 } 
	};

	static double[] time = { 0.8, 3.2, 2.4, 2.6, 3.2, 0.2 };

	public static void randomGenerator() {
		for (int i = 0; i < 6; i++) {
			for (int j = i + 1; j < 6; j++) {
				int x = (int) Math.floor(Math.random() * (20 - 10 + 1) + 10);
				g[i][j] = g[j][i] = x;
			}
		}
		for (int i = 0; i < 6; i++) {
			Random rand = new Random();
			System.out.println(rand);
		}
	}

	public static void main(String[] args) {
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 6; j++) {
				if (i == j) continue;
				System.out.println(cities[i] + " " + cities[j] + " ---> " + g[i][j]);
			}
			System.out.println();
		}
	}

	public static String[] go(String start) {
		mnCostforOptimalT = Integer.MAX_VALUE;
		mnCostforOptimalD = Integer.MAX_VALUE;
		String str = "012345";
		int n = str.length();
		Optimal permutation = new Optimal();
		permutation.optimal(str, 0, n - 1, start);

		String ss[] = new String[4];
		ss[0] = mnCostforOptimalT + "";
		ss[1] = mnCostforOptimalD + "";
		StringBuilder pT = new StringBuilder();
		for (int i = 0; i < 6; i++) {
			int from = pathT.charAt(i % 6) - '0';
			int to = pathT.charAt((i + 1) % 6) - '0';
			pT.append(cities[from] + "  -->  " + cities[to] + " " + (Math.round(g[from][to] * time[i])) + "\n");
		}

		StringBuilder pD = new StringBuilder();
		for (int i = 0; i < 6; i++) {
			int from = pathD.charAt(i % 6) - '0';
			int to = pathD.charAt((i + 1) % 6) - '0';
			pD.append(cities[from] + "  -->  " + cities[to] + " " + g[from][to]  + "\n");
		}
		ss[2] = pT.toString();
		ss[3] = pD.toString();
		return ss;
	}

	private void optimal(String str, int l, int r, String start) {
		if (l == r) {
			if (!cities[str.charAt(0) - '0'].equals(start)) {
				return;
			} else {
				double tempT = 0;
				int tempD = 0;

				for (int i = 0; i < 6; i++) {
					int from = str.charAt(i % 6) - '0';
					int to = str.charAt((i + 1) % 6) - '0';
					tempT += (Math.round(g[from][to] * time[i]));
					tempD += (g[from][to]);
				}
				if (tempT <= mnCostforOptimalT) {
					mnCostforOptimalT = tempT;
					pathT = str;
				}
				if (tempD <= mnCostforOptimalD) {
					mnCostforOptimalD = tempD;
					pathD = str;
				}
				return;
			}
		}
		for (int i = l; i <= r; i++) {
			str = swap(str, l, i);
			optimal(str, l + 1, r, start);
			str = swap(str, l, i);
		}
	}

	public String swap(String a, int i, int j) {
		char temp;
		char[] charArray = a.toCharArray();
		temp = charArray[i];
		charArray[i] = charArray[j];
		charArray[j] = temp;
		return String.valueOf(charArray);
	}
}