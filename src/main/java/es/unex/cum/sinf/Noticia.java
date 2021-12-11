package es.unex.cum.sinf;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public class Noticia {

    Session session;


    String titulo;
    String cuerpo;
    UUID fecha;
    LocalDate fechaLocalDate;
    Set<String> etiquetas;
    String nick_autor;
    String desc_autor;

    public Noticia(Session session) {
        this.session = session;
    }

    public Noticia(Session session, String titulo, String cuerpo, Set<String> etiquetas, String nick_autor) {
        this.session = session;
        this.titulo = titulo;
        this.cuerpo = cuerpo;
        this.etiquetas = etiquetas;
        this.nick_autor = nick_autor;
    }

    public Noticia(String titulo, String cuerpo, UUID fecha, Set<String> etiquetas, String nick_autor) {
        this.titulo = titulo;
        this.cuerpo = cuerpo;
        this.fecha = fecha;
        this.etiquetas = etiquetas;
        this.nick_autor = nick_autor;
    }

    public Noticia(String titulo, String cuerpo, UUID fecha, Set<String> etiquetas, String nick_autor, String desc_autor) {
        this.titulo = titulo;
        this.cuerpo = cuerpo;
        this.fecha = fecha;
        this.etiquetas = etiquetas;
        this.nick_autor = nick_autor;
        this.desc_autor = desc_autor;
    }

    public Noticia(String titulo, String cuerpo, LocalDate of, Set<String> tags, String autor) {
        this.titulo = titulo;
        this.cuerpo = cuerpo;
        this.fechaLocalDate = of;
        this.etiquetas = tags;
        this.nick_autor = autor;
    }

    public void insertarNoticia() {
        LocalDate ahora = LocalDate.now();
        session.execute("insert into noticias_por_fecha (dia, mes, anio, fecha, autor, cuerpo, tags, titulo)\n" +
                "VALUES (\n" +
                ahora.getDayOfMonth() + ",\n" +
                ahora.getMonthValue() + ",\n" +
                ahora.getYear() + ",\n" +
                "        now(),\n" +
                "'" + nick_autor + "',\n" +
                "'" + cuerpo + "',\n" +
                etiquetas + ",\n" +
                "'" + titulo + "'\n" +
        ");");

        ResultSet resultSet = session.execute("SELECT * FROM usuarios_nick WHERE nick = '" + nick_autor + "';");
        String desc_autor = resultSet.one().getString(2); // Descripcion del autor

        session.execute("INSERT INTO noticias_por_usuario (titulo, cuerpo, fecha, descripcion_usuario, tags, nombre_usuario)\n" +
                "VALUES (\n'" +
                titulo + "',\n'" +
                cuerpo + "',\n" +
                "        now(),\n'" +
                desc_autor + "',\n" +
                etiquetas + ",\n'" +
                nick_autor + "'\n);");
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("es.unex.cum.sinf.Noticia{");
        sb.append("titulo='").append(titulo).append('\'');
        sb.append(", cuerpo='").append(cuerpo).append('\'');
        sb.append(", fecha=").append(fecha);
        sb.append(", etiquetas=").append(etiquetas);
        sb.append(", nick_autor='").append(nick_autor).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public String toStringConDesc() {
        final StringBuilder sb = new StringBuilder("es.unex.cum.sinf.Noticia{");
        sb.append("titulo='").append(titulo).append('\'');
        sb.append(", cuerpo='").append(cuerpo).append('\'');
        sb.append(", fecha=").append(fecha);
        sb.append(", etiquetas=").append(etiquetas);
        sb.append(", nick_autor='").append(nick_autor).append('\'');
        sb.append(", desc_autor='").append(desc_autor).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public String getTitulo() {
        return titulo;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public UUID getFecha() {
        return fecha;
    }

    public Set<String> getEtiquetas() {
        return etiquetas;
    }

    public String getNick_autor() {
        return nick_autor;
    }

    public String getDesc_autor() {
        return desc_autor;
    }

    public LocalDate getFechaLocalDate() {
        return fechaLocalDate;
    }
}
