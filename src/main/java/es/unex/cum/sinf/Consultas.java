package es.unex.cum.sinf;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class Consultas {
    private final Session session;

    public Consultas(Session session) {
        this.session = session;
    }

    public List<Usuario> consulta1(String nombreUsuario) {

        List<Usuario> usuarioList = new LinkedList<>();

        System.out.println(String.format("\n\nBuscando usuario por nick: %s%n", nombreUsuario));
        Usuario usuario;
        ResultSet rs = session.execute("Select * FROM USUARIOS_NICK WHERE NICK ='" + nombreUsuario + "';");
        for (Row row : rs) {
            usuario = new Usuario(row.getString(0), row.getString(3), row.getString(1), row.getString(2));
            usuarioList.add(usuario);
            System.out.println(usuario);
        }

        return usuarioList;
    }

    public List<Usuario> consulta2(String twitter) {
        List<Usuario> usuarioList = new LinkedList<>();

        System.out.println(String.format("\n\nBuscando usuario por twitter: %s", twitter));
        Usuario usuario;
        ResultSet rs = session.execute("Select * FROM USUARIOS_TWITTER WHERE CUENTA_TWITTER ='" + twitter + "';");
        for (Row row : rs) {
            usuario = new Usuario(row.getString(2), row.getString(3), row.getString(0), row.getString(1));
            usuarioList.add(usuario);
            System.out.println(usuario);
        }

        return usuarioList;
    }

    public List<Noticia> consulta3() {

        List<Noticia> noticiaList = new LinkedList<>();

        System.out.println("\n\nLas diez ultimas noticias del día: ");

        ResultSet rs = session.execute("SELECT * FROM NOTICIAS_POR_FECHA LIMIT 10");
        for (Row fila : rs) {
            Noticia noticia = new Noticia(fila.getString("titulo"), fila.getString("cuerpo"), LocalDate.of(fila.getInt("anio"), fila.getInt("mes"), fila.getInt("dia")), fila.getSet("tags", String.class), fila.getString("autor"));
            noticiaList.add(noticia);
            System.out.println("\t- " + noticia);
        }

        return noticiaList;
    }

    public List<Noticia> consulta4(String nombreUsuario) {
        List<Noticia> noticiaList = new LinkedList<>();

        System.out.println(String.format("\n\nNoticias publicadas por %s", nombreUsuario));
        ResultSet rs = session.execute("SELECT * FROM NOTICIAS_POR_USUARIO WHERE nombre_usuario = '" + nombreUsuario + "';");
        for (Row fila : rs) {
            Noticia noticia = new Noticia(fila.getString("titulo"), fila.getString("cuerpo"), fila.getUUID("fecha"), fila.getSet("tags", String.class), fila.getString("nombre_usuario"), fila.getString("descripcion_usuario"));
            noticiaList.add(noticia);
            System.out.println("\t- " + noticia.toStringConDesc());
        }

        return noticiaList;
    }

    /**
     * @param fechaInicio Fecha desde la que busca los comentarios (inclusive)
     * @param fechaFin    Fecha hasta la que busca los comentarios (inclusive)
     */
    public List<Comentario> consulta5(LocalDate fechaInicio, LocalDate fechaFin) {
        List<Comentario> comentarioList = new LinkedList<>();

        System.out.printf("\n\nComentarios entre %s %s\n", fechaInicio, fechaFin);

        if (fechaFin.getYear() != fechaInicio.getYear()) {
            System.out.println("No se ha implementado de distintos años");
        } else if (fechaInicio.getMonthValue() != fechaFin.getMonthValue()) {
            comentarioList.addAll(imprimirMes(LocalDate.of(fechaFin.getYear(), fechaFin.getMonthValue(), 1), fechaFin));
            for (int i = fechaFin.getMonthValue() - 1; i > fechaInicio.getMonthValue(); i--) {
                comentarioList.addAll(imprimirMes(LocalDate.of(fechaFin.getYear(), i, 1), LocalDate.of(fechaFin.getYear(), i, 1).plusMonths(1).minusDays(1)));
            }
            comentarioList.addAll(imprimirMes(fechaInicio, LocalDate.of(fechaInicio.getYear(), fechaInicio.getMonthValue(), 1).plusMonths(1).minusDays(1)));

        } else { // Si están en el mismo mes
            comentarioList.addAll(imprimirMes(fechaInicio, fechaFin));
        }

        return comentarioList;
    }

    public List<Comentario> consulta6(String nombreUsuario, String titulo_noticia) {

        List<Comentario> comentarioList = new LinkedList<>();

        System.out.printf("\n\nComentarios del autor %s sobre la noticia %s\n", nombreUsuario, titulo_noticia);
        ResultSet rs = session.execute(String.format("SELECT * FROM COMENTARIOS_AUTOR_NOTICIA WHERE autor = '%s' and titulo_noticia = '%s'", nombreUsuario, titulo_noticia));
        for (Row fila : rs) {
            Comentario comentario = new Comentario(fila.getString("titulo_noticia"), fila.getString("cuerpo_comentario"), fila.getString("autor"));
            comentario = new Comentario(fila.getString("cuerpo_comentario"), fila.getString("autor"), fila.getString("titulo_noticia"), LocalDate.of(fila.getInt("anio"), fila.getInt("mes"), fila.getInt("dia")), fila.getString("autor"));
            comentarioList.add(comentario);
            System.out.println("\t- " + comentario);
        }

        return comentarioList;
    }

    // Se puede ir haciendo consultas por dia, o hacer el allowFiltering, dependiendo del rango, es mejor uno que otro,
    // cuanto mayor rango, mejor es el ALLOWFILTERING
    private List<Comentario> imprimirMes(LocalDate fecha1, LocalDate fecha2) {
        List<Comentario> comentarioList = new LinkedList<>();
//        boolean allowfiltering = false;
//        if (!allowfiltering) {
        for (int i = fecha2.getDayOfMonth(); i >= fecha1.getDayOfMonth(); i--) {
            comentarioList.addAll(imprimirComentario(LocalDate.of(fecha1.getYear(), fecha2.getMonthValue(), i)));
        }
//        } else {
//            ResultSet rs = session.execute("SELECT * FROM comentarios_usuario_noticia WHERE " +
//                    String.format("dia = %d and mes = %d and anio = %d ALLOW FILTERING",
//                            fecha1.getDayOfMonth(),
//                            fecha1.getMonthValue(),
//                            fecha1.getYear()));
//            Iterator<Row> it = rs.iterator();
//            while (it.hasNext()) {
//                Row fila = it.next();
//                System.out.println("\t-" + new Comentario(fila.getString("cuerpo"), fila.getString("nombre_usuario"), fila.getString("titulo_noticia"), LocalDate.of(fila.getInt("anio"), fila.getInt("mes"), fila.getInt("dia"))));
//            }
//        }
        return comentarioList;
    }

    private List<Comentario> imprimirComentario(LocalDate fecha1) {
        List<Comentario> comentarioList = new LinkedList<>();

        ResultSet rs = session.execute("SELECT * FROM comentarios_usuario_noticia WHERE " +
                String.format("dia = %d and mes = %d and anio = %d",
                        fecha1.getDayOfMonth(),
                        fecha1.getMonthValue(),
                        fecha1.getYear()));
        for (Row fila : rs) {
            Comentario comentario = new Comentario(fila.getString("cuerpo"), fila.getString("nombre_usuario"), fila.getString("titulo_noticia"), LocalDate.of(fila.getInt("anio"), fila.getInt("mes"), fila.getInt("dia")));
            comentarioList.add(comentario);
            System.out.println("\t-" + comentario);
        }

        return comentarioList;
    }

}
