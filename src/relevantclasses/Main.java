package relevantclasses;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

class Student {
	int age;
	double cwa;
	int papers;
	int awards;
	int extra;

	public Student() {
		
	}
}

class Pair {
	public Set<Integer> st = new HashSet<>();

	public Pair(int a, int b) {
		st.add(a);
		st.add(b);
	}

	@Override
	public int hashCode() {
		return Objects.hash(st);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pair other = (Pair) obj;
		Iterator<Integer> i = other.st.iterator();
		return st.contains(i.next()) && st.contains(i.next());
	}

}

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*
		 * boolean[][] graph = new boolean[][] { { false, false, true, true, false, true
		 * }, { true, false, true, false, true, false }, { false, false, false, false,
		 * false, true }, { false, false, false, false, true, true }, { false, false,
		 * false, false, false, false }, { false, false, false, false, false, false } };
		 * 
		 * @SuppressWarnings("unused") boolean[][] graph2 = new boolean[][] { { false,
		 * true, true }, { true, false, false }, { true, true, false } };
		 * 
		 * @SuppressWarnings("unused") boolean[][] graph3 = new boolean[][] { { false,
		 * false, false }, { true, false, false }, { true, false, false } };
		 * 
		 * System.out.println(Arrays.toString(rankPage(graph)));
		 */

		Page a = new Page("a");
		Page b = new Page("b");
		Page c = new Page("c");
		Page d = new Page("d");
		a.arriving.add(b);
		a.leaving.add(d);
		a.leaving.add(c);

		b.arriving.add(d);
		b.leaving.add(c);
		b.leaving.add(a);

		c.arriving.add(a);
		c.arriving.add(b);
		c.leaving.add(d);

		d.arriving.add(c);
		d.arriving.add(a);
		d.leaving.add(b);

		Page[] graph = { a, b, c, d };

		boolean[][] gg = { { false, true, false, false }, { false, false, true, true }, { true, true, false, false },
				{ true, false, true, false } };

		long t1 = System.currentTimeMillis();
		String ans = Arrays.toString(rankPage(gg));
		long t2 = System.currentTimeMillis();
		double time = (double) (t2 - t1) / 1000;

		long t3 = System.currentTimeMillis();
		String ans2 = Arrays.toString(rankPage2(gg));
		long t4 = System.currentTimeMillis();
		double time2 = (double) (t4 - t3) / 1000;

		System.out.println("Result : " + ans + " , Time taken : " + time);
		System.out.println("Result : " + ans2 + " , Time taken : " + time2);

	}

	public static double[] rankPage(boolean[][] graph) {
		int n = graph.length;
		if (n <= 1)
			return new double[] { 100 };

		double val = (double) 1 / n;
		double[] arr = new double[n];
		double[][] pp = new double[n][n];
		int count;
		for (int i = 0; i < n; i++) {
			count = 0;
			for (int j = 0; j < n; j++) {
				if (graph[i][j])
					count++;
			}
			arr[i] = count;
		}

		for (int x = 0; x < n; x++) {
			if (arr[x] == 0) {
				Arrays.fill(pp[x], (double) 1 / (n - 1));
			} else {
				for (int y = 0; y < n; y++) {
					if (graph[x][y])
						pp[x][y] = 1 / arr[x];
				}
			}
		}

		Arrays.fill(arr, val);

		int doTimes = 25;

		while (--doTimes > 0) {
			arr = multPlyy(pp, new double[][] { arr })[0];
		}

		return arr;
	}

	public static void printArray(double[][] array) {
		for (double[] arr : array) {
			System.out.println(Arrays.toString(arr));
		}
	}

	public static double[][] multPlyy(double[][] arr1, double[][] arr2) {
		int row2 = arr2.length;
		int col2 = arr2[0].length;

		double ans[][] = new double[row2][col2];
		for (int i = 0; i < col2; i++) {
			for (int j = 0; j < col2; j++) {
				ans[0][i] += arr1[j][i] * arr2[0][j];
			}
		}

		return ans;
	}

	public static Map<Page, Double> rankPage(Page[] graph) {
		int n = graph.length;
		HashMap<Page, Double> ranks = new HashMap<>();

		double val = (double) 1 / n;
		for (Page p : graph) {
			ranks.put(p, val);
		}

		for (int i = 0; i < n; i++) {
			Page page = graph[i];
			if (page.leaving.size() == 0) {
				for (int j = 0; j < n; j++) {
					if (i != j) {
						page.leaving.add(graph[j]);
						graph[j].arriving.add(page);
					}
				}
			}
		}

		int loop = 21;

		while (--loop > 0) {
			for (int i = 0; i < n; i++) {
				Page page = graph[i];

				ranks.put(page, 0.0);

				for (Page pp : page.arriving) {
					ranks.put(page, ranks.get(page) + (ranks.get(pp) / pp.leaving.size()));
				}

			}
		}

		double sum = 0;

		for (int i = 0; i < n; i++) {
			sum += ranks.get(graph[i]);
		}

		for (int i = 0; i < n; i++) {
			ranks.put(graph[i], 100 * ranks.get(graph[i]) / sum);
		}

		return ranks;
	}

	public static double[] rankPage2(boolean[][] graph) {
		int n = graph.length;
		double[] ranks = new double[n];
		double val = (double) 1 / n;
		int[] sumF = new int[n];

		for (int i = 0; i < n; i++) {
			ranks[i] = val;
		}

		for (int x = 0; x < n; x++) {
			for (int y = 0; y < n; y++) {
				sumF[x] += graph[x][y] ? 1 : 0;
			}
		}

		for (int x = 0; x < n; x++) {
			if (sumF[x] == 0) {
				sumF[x] = n - 1;
				for (int y = 0; y < n; y++) {
					graph[x][y] = x != y;
				}
			}
		}

		for (int i = 0; i < 25; i++) {
			for (int x = 0; x < n; x++) {
				ranks[x] = 0;
				for (int y = 0; y < n; y++) {
					if (graph[y][x]) {
						ranks[x] += ranks[y] / sumF[y];
					}
				}
			}
		}

		double sum = 0;

		for (int i = 0; i < n; i++) {
			sum += ranks[i];
		}

		for (int i = 0; i < n; i++) {
			ranks[i] = 100 * ranks[i] / sum;
		}

		return ranks;
	}

}
