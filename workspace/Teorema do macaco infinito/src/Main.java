import java.util.ArrayList;
import java.util.List;


public class Main {
	
	private static final int NUM_MACACOS = 10;
	
	MesaDeShakespeare mesa;
	List<Macaco> macacos_na_mesa = new ArrayList<Macaco>();
	Shakespeare shakespeare;

	public static void main(String[] args) {
		new Main().execute();	
	}

	private void execute() {
		mesa = new MesaDeShakespeare();
		
		for (int i = 0; i < NUM_MACACOS; i++) {
			macacos_na_mesa.add(new Macaco(i, mesa));	
		}
		
		shakespeare = new Shakespeare(mesa, macacos_na_mesa);
		
		for (Macaco macaco : macacos_na_mesa) {
			macaco.start();
		}
		
		shakespeare.start();
		
	}

}
