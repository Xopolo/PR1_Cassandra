import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import java.time.LocalDate;
import java.util.Iterator;

public class Consultas {
    private final Session session;

    public Consultas(Session session) {
        this.session = session;
    }

    public void consulta1(String nombreUsuario) {
        System.out.println(String.format("\n\nBuscando usuario por nick: %s", nombreUsuario));
        Usuario usuario = new Usuario();
        ResultSet rs = session.execute("Select * FROM USUARIOS_NICK WHERE NICK ='" + nombreUsuario + "';");
        Iterator<Row> it = rs.iterator();
        while (it.hasNext()) {
            Row row = it.next();
            usuario = new Usuario(row.getString(0), row.getString(3), row.getString(1), row.getString(2));
        }
        System.out.println(usuario);

    }

    public void consulta2(String twitter) {
        System.out.println(String.format("\n\nBuscando usuario por twitter: %s", twitter));
        Usuario usuario = new Usuario();
        ResultSet rs = session.execute("Select * FROM USUARIOS_TWITTER WHERE CUENTA_TWITTER ='" + twitter + "';");
        Iterator<Row> it = rs.iterator();
        while (it.hasNext()) {
            Row row = it.next();
            usuario = new Usuario(row.getString(2), row.getString(3), row.getString(0), row.getString(1));
        }
        System.out.println(usuario);
    }

    public void consulta3() {
        System.out.println("\n\nLas diez ultimas noticias del día: ");

        ResultSet rs = session.execute("SELECT * FROM NOTICIAS_POR_FECHA LIMIT 10");
        Iterator<Row> it = rs.iterator();
        while (it.hasNext()) {
            Row fila = it.next();
            System.out.println("\t- " + new Noticia(fila.getString("titulo"), fila.getString("cuerpo"), fila.getUUID("fecha"), fila.getSet("tags", String.class), fila.getString("autor")));
        }
    }

    public void consulta4(String nombreUsuario) {
        System.out.println(String.format("\n\nNoticias publicadas por %s", nombreUsuario));
        ResultSet rs = session.execute("SELECT * FROM NOTICIAS_POR_USUARIO WHERE nombre_usuario = '" + nombreUsuario + "';");
        Iterator<Row> it = rs.iterator();
        while (it.hasNext()) {
            Row fila = it.next();
            System.out.println("\t- " + new Noticia(fila.getString("titulo"), fila.getString("cuerpo"), fila.getUUID("fecha"), fila.getSet("tags", String.class), fila.getString("nombre_usuario"), fila.getString("descripcion_usuario")).toStringConDesc());

        }
    }

    /**
     * @param fechaInicio Fecha desde la que busca los comentarios (inclusive)
     * @param fechaFin    Fecha hasta la que busca los comentarios (inclusive)
     */
    public void consulta5(LocalDate fechaInicio, LocalDate fechaFin) {


        System.out.printf("\n\nComentarios entre %s %s\n", fechaInicio, fechaFin);

        if (fechaFin.getYear() != fechaInicio.getYear()) {
            System.out.println("No se ha implementado de distintos años");
        } else if (fechaInicio.getMonthValue() != fechaFin.getMonthValue()) {
            imprimirMes(LocalDate.of(fechaFin.getYear(), fechaFin.getMonthValue(), 1), fechaFin);
            for (int i = fechaFin.getMonthValue() - 1; i > fechaInicio.getMonthValue(); i--) {
                imprimirMes(LocalDate.of(fechaFin.getYear(), i, 1), LocalDate.of(fechaFin.getYear(), i, 1).plusMonths(1).minusDays(1));
            }
            imprimirMes(fechaInicio, LocalDate.of(fechaInicio.getYear(), fechaInicio.getMonthValue(), 1).plusMonths(1).minusDays(1));

        } else { // Si están en el mismo mes
            imprimirMes(fechaInicio, fechaFin);
        }
    }

    public void consulta6(String nombreUsuario, String titulo_noticia) {
        System.out.printf("\n\nComentarios del autor %s sobre la noticia %s\n", nombreUsuario, titulo_noticia);
        ResultSet rs = session.execute(String.format("SELECT * FROM COMENTARIOS_AUTOR_NOTICIA WHERE autor = '%s' and titulo_noticia = '%s'", nombreUsuario, titulo_noticia));
        Iterator<Row> it = rs.iterator();
        while (it.hasNext()) {
            Row fila = it.next();
            System.out.println("\t- " + new Comentario(fila.getString("titulo_noticia"), fila.getString("cuerpo_comentario"), fila.getString("autor")));

        }
    }

    // Se puede ir haciendo consultas por dia, o hacer el allowFiltering, dependiendo del rango, es mejor uno que otro,
    // cuanto mayor rango, mejor es el ALLOWFILTERING
    private void imprimirMes(LocalDate fecha1, LocalDate fecha2) {
        boolean allowfiltering = false;
        if(!allowfiltering) {
            for (int i = fecha2.getDayOfMonth(); i >= fecha1.getDayOfMonth(); i--) {
                imprimirComentario(LocalDate.of(fecha1.getYear(), fecha2.getMonthValue(), i));
            }
        } else {
            ResultSet rs = session.execute("SELECT * FROM comentarios_usuario_noticia WHERE " +
                    String.format("dia = %d and mes = %d and anio = %d ",
                            fecha1.getDayOfMonth(),
                            fecha1.getMonthValue(),
                            fecha1.getYear()));
            Iterator<Row> it = rs.iterator();
            while (it.hasNext()) {
                Row fila = it.next();
                System.out.println("\t-" + new Comentario(fila.getString("cuerpo"), fila.getString("nombre_usuario"), fila.getString("titulo_noticia"), LocalDate.of(fila.getInt("anio"), fila.getInt("mes"), fila.getInt("dia"))));
            }
        }
    }

    private void imprimirComentario(LocalDate fecha1) {
        ResultSet rs = session.execute("SELECT * FROM comentarios_usuario_noticia WHERE " +
                String.format("dia = %d and mes = %d and anio = %d",
                        fecha1.getDayOfMonth(),
                        fecha1.getMonthValue(),
                        fecha1.getYear()));
        Iterator<Row> it = rs.iterator();
        while (it.hasNext()) {
            Row fila = it.next();
            System.out.println("\t-" + new Comentario(fila.getString("cuerpo"), fila.getString("nombre_usuario"), fila.getString("titulo_noticia"), LocalDate.of(fila.getInt("anio"), fila.getInt("mes"), fila.getInt("dia"))));
        }
    }

}
