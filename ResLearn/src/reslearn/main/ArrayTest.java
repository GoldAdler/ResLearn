package reslearn.main;

/*
 * Klasse zum Testen
 * kann später gelöscht werden!!!
 */
// TODO: Testklasse löschen wenn nicht mehr gebraucht

public class ArrayTest {

	public static void main(String[] args) {

		// 0,0 0,1 0,2
		// 1,0 1,1 1,2
		// 2,0 2,1 2,2

		int[][] arr = { { 1, 1, 10 }, { 2, 2, 20 }, { 3, 3, 30 } };

		for (int[] a : arr) {
			for (int b : a) {
				System.out.print(b + " ");
			}
			System.out.println();
		}

		System.out.println("\n" + arr[1][1]);

	}

}
