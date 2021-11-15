
public class Main_test {
    public static void main(String[] args) {
        Cliente cliente = new Cliente();
        cliente.connect("127.0.0.1");
        cliente.crearEsquema();
        cliente.insertarDatos();
        cliente.close();

    }
}
