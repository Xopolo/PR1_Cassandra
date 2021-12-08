import java.time.LocalDate;

public class Main_test {
    public static void main(String[] args) {
        Gestor gestor = new Gestor();
        gestor.connect("127.0.0.1");
        gestor.crearEsquema();
        gestor.insertarDatos();

        Consultas consultas = new Consultas(gestor.getSession());
        consultas.consulta1("Xolopo");
        consultas.consulta2("@Xolopo");
        consultas.consulta3();
        consultas.consulta4("Xolopo");

        LocalDate fecha1 = LocalDate.of(2021, 12, 1);
        LocalDate fecha2 = LocalDate.of(2021, 12, 24);
        consultas.consulta5(fecha1,fecha2);

        consultas.consulta6("Xolopo", "titulo");

        gestor.close();
    }
}
