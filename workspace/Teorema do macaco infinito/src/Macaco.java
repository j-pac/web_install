import java.util.Random;

public class Macaco extends Thread {

	private static final int STRINGLENGTH = 1000;

	private int id;
	private MesaDeShakespeare mesa;

	public Macaco(int id, MesaDeShakespeare mesa) {
		this.id = id;
		this.mesa = mesa;
	}

	private String generateString() {
		Random r = new Random();

		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < STRINGLENGTH; i++) {
			if (r.nextInt() == 0) {
				sb.append(' ');
			} else
				sb.append(Character.toChars(r.nextInt(26) + 'a'));
		}
		return sb.toString();
	}

	@Override
	public void run() {
		try {
			while (true) {
				mesa.putString(generateString());
			}
		} catch (InterruptedException e) {
			System.out.println("-x- Macaco id " + id + " terminado!");
		}
	}

}
