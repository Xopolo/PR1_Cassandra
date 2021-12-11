package es.unex.cum.sinf;

import es.unex.cum.sinf.Reportes.GeneradorReportes;
import es.unex.cum.sinf.Reportes.TipoReporte;

import java.time.LocalDate;

public class Main_test {
    public static void main(String[] args) {
        Gestor gestor = new Gestor();
        gestor.connect("127.0.0.1");
        gestor.crearEsquema();
        gestor.insertarDatos();

        Consultas consultas = new Consultas(gestor.getSession());
        GeneradorReportes generadorReportes = new GeneradorReportes();


        String xolopo = "Xolopo";
        generadorReportes.generarReporte("Consulta1", TipoReporte.USUARIO, consultas.consulta1(xolopo),
                "Consulta 1", String.format("Nombres de usuario con nick: %s", xolopo));

        generadorReportes.generarReporte("Consulta2", TipoReporte.USUARIO, consultas.consulta2("@" + xolopo),
                "Consulta 2", String.format("Nombres de usuario con cuenta de twitter:\n %s", xolopo));

        generadorReportes.generarReporte("Consulta3", TipoReporte.NOTICIA, consultas.consulta3(),
                "Consulta 3", "Ultimas noticias del día");

        generadorReportes.generarReporte("Consulta4", TipoReporte.NOTICIA, consultas.consulta4(xolopo),
                "Consulta 4", String.format("Noticias publicadas por:\n%s", xolopo));

        LocalDate fecha1 = LocalDate.of(2021, 12, 1);
        LocalDate fecha2 = LocalDate.of(2021, 12, 24);

        generadorReportes.generarReporte("Consulta5", TipoReporte.COMENTARIO, consultas.consulta5(fecha1, fecha2),
                "Consulta 5", String.format("Comentarios entre rango de fechas\n%s - %s", fecha1, fecha2));

        generadorReportes.generarReporte("Consulta6", TipoReporte.COMENTARIO, consultas.consulta6(xolopo,"titulo"),
                "Consulta 6", String.format("Comentarios entre rango de fechas\n%s - %s", fecha1, fecha2));

        gestor.close();
    }
}
