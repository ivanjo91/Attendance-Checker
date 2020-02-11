import java.io.Serializable;

public class Tarjeta implements Serializable{
	
	private int[][] codigos;
	
	public Tarjeta() {
		this.codigos = new int[5][5];
		
		for(int i=0; i<codigos.length; i++) {
			for(int j=0; j<codigos[i].length; j++) {
				codigos[i][j] =(int)(Math.random()*(1000-100) + 100);
			}
		}
	}

	public int[][] getCodigos() {
		return codigos;
	}

	public void setCodigos(int[][] codigos) {
		this.codigos = codigos;
	}
	
	public void mostrarCodigos() {
		for(int i=0; i<codigos.length; i++) {
			for(int j=0; j<codigos[i].length; j++) {
				if(i==0&&j==0) {
					for(int k=0; k<6; k++) {
						System.out.print(k + "\t");
					}
					System.out.println();
				}
				if(j==0) {
					System.out.print(i+1 + "\t");
				}
				System.out.print(codigos[i][j] + "\t");
			}
			System.out.println();
		
		}
	}
	
	public boolean comprobarCodigo(int fila, int columna, int codigo) {
		boolean resultado = false;
		
		if(this.codigos[fila][columna]==codigo) {
			resultado = true;
		}
		
		return resultado;
	}
}
