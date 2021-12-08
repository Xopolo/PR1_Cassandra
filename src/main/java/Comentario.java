import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import java.time.LocalDate;
import java.util.Iterator;

public class Comentario {
    private Session session;

    private LocalDate fecha;
    private String titulo_noticia;
    private String cuerpo;
    private String usuario;

    public Comentario(Session session, String cuerpo, String usuario, String titulo_noticia) {
        this.session = session;
        this.cuerpo = cuerpo;
        this.usuario = usuario;
        this.titulo_noticia = titulo_noticia;
    }

    public Comentario(String cuerpo, String usuario, String titulo_noticia, LocalDate fecha) {
        this.cuerpo = cuerpo;
        this.usuario = usuario;
        this.titulo_noticia = titulo_noticia;
        this.fecha = fecha;
    }

    public Comentario(String titulo_noticia, String cuerpo, String usuario) {
        this.titulo_noticia = titulo_noticia;
        this.cuerpo = cuerpo;
        this.usuario = usuario;
    }

    public void insertarComentario() {
        LocalDate hoy = LocalDate.now();
        session.execute(
                "insert into comentarios_usuario_noticia (fecha, cuerpo, dia, mes, anio, titulo_noticia, nombre_usuario)\n" +
                        "VALUES (\n" +
                        "        now(),\n" +
                        "'" + cuerpo + "',\n" +
                        +hoy.getDayOfMonth() + ",\n" +
                        +hoy.getMonthValue() + ",\n" +
                        +hoy.getYear() + ",\n" +
                        "'" + titulo_noticia + "',\n" +
                        "'" + usuario + "'\n" +
                        ");\n");

        boolean anadirAComentariosDeAutor = false;

        ResultSet rs = session.execute(String.format("select * from noticias_por_usuario where nombre_usuario = '%s';", usuario));
        Iterator<Row> it = rs.iterator();
        while (it.hasNext()) {
            Row fila = it.next();
            if (fila.getString("titulo").equals(titulo_noticia)) {
                anadirAComentariosDeAutor = true;
                break;
            }
        }

        if (anadirAComentariosDeAutor) {
            session.execute(
                    "insert into comentarios_autor_noticia (autor, titulo_noticia, fecha, cuerpo_comentario, dia, mes, anio)\n" +
                            "VALUES (\n" +
                            "        'Xolopo',\n" +
                            "'" + titulo_noticia + "',\n" +
                            "        now(),\n" +
                            "'" + cuerpo + "',\n" +
                            hoy.getDayOfMonth() + ",\n" +
                            hoy.getMonthValue() + ",\n" +
                            hoy.getYear() + ");"
            );
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Comentario{");
        sb.append("fecha=").append(fecha);
        sb.append(", titulo_noticia='").append(titulo_noticia).append('\'');
        sb.append(", cuerpo='").append(cuerpo).append('\'');
        sb.append(", usuario='").append(usuario).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
