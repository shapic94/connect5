package generatorCvorova;

import java.util.Scanner;
import servent.StartServent;
public class Generator {
	
	public static void main(String[] args) {
		int broj;
		int port = 9000;
		Scanner sc = new Scanner(System.in);
		System.out.println("Unesi broj cvorova:");
		broj = sc.nextInt();
		try {
		for (int i = 1; i <=broj; i++) {
			String[] args1={Integer.toString(port++)};
			
				StartServent.main(args1);
			
		}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
