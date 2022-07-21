package relevantclasses;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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

		boolean[][] gg = { { false, false, true, true }, { true, false, true, false }, { false, false, false, true },
				{ false, true, false, false } };

		Map<Page, Double> myans;
		Thread tt = new Thread(new Runnable() {
			Map<Page, Double> ans;

			@Override
			public String toString() {
				return ans.toString();
			}

			@Override
			public void run() {
				ans = rankPage(graph);
			}
		});
		long t1 = System.currentTimeMillis();
		tt.start();
		try {
			tt.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long t2 = System.currentTimeMillis();

		Thread ttt = new Thread(new Runnable() {
			double[] ans;

			@Override
			public String toString() {
				return Arrays.toString(ans);
			}

			@Override
			public void run() {
				ans = rankPage(gg);
			}
		});

		long t3 = System.currentTimeMillis();

		ttt.start();
		try {
			ttt.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		long t4 = System.currentTimeMillis();

		System.out.println(ttt + "Time taken = " + (double) (t4 - t3) / 1000);
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

	public static double[] rankPage(boolean[][] graph) {
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

		for (int i = 0; i < 100; i++) {
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
