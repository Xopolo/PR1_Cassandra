import com.datastax.driver.core.Session;

import java.util.UUID;

public class Usuario {

    Session session;
    String nombre_usuario;
    String nombre;
    String cuenta_twitter;
    String descripcion;

    public Usuario() {}

    public Usuario(String nombre_usuario, String nombre, String cuenta_twitter, String descripcion) {
        this.nombre_usuario = nombre_usuario;
        this.nombre = nombre;
        this.cuenta_twitter = cuenta_twitter;
        this.descripcion = descripcion;
    }

    public Usuario(Session session, String nombre_usuario, String nombre, String cuenta_twitter, String descripcion) {
        this.session = session;
        this.nombre_usuario = nombre_usuario;
        this.nombre = nombre;
        this.cuenta_twitter = cuenta_twitter;
        this.descripcion = descripcion;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public void insertarUsuario() {
        session.execute("INSERT INTO USUARIOS_NICK (nick,cuenta_twitter,descripcion,nombre) VALUES" + " ('" +
                nombre_usuario + "','" +
                cuenta_twitter + "','" +
                descripcion + "','" +
                nombre + "');");

        session.execute("INSERT INTO USUARIOS_TWITTER (nick,cuenta_twitter,descripcion,nombre) VALUES" + " ('" +
                nombre_usuario + "','" +
                cuenta_twitter + "','" +
                descripcion + "','" +
                nombre + "');");
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Usuario{");
        sb.append("nombre_usuario='").append(nombre_usuario).append('\'');
        sb.append(", nombre='").append(nombre).append('\'');
        sb.append(", cuenta_twitter='").append(cuenta_twitter).append('\'');
        sb.append(", descripcion='").append(descripcion).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario)) return false;

        Usuario usuario = (Usuario) o;

        if (nombre_usuario != null ? !nombre_usuario.equals(usuario.nombre_usuario) : usuario.nombre_usuario != null)
            return false;
        if (nombre != null ? !nombre.equals(usuario.nombre) : usuario.nombre != null) return false;
        if (cuenta_twitter != null ? !cuenta_twitter.equals(usuario.cuenta_twitter) : usuario.cuenta_twitter != null)
            return false;
        return descripcion != null ? descripcion.equals(usuario.descripcion) : usuario.descripcion == null;
    }

    @Override
    public int hashCode() {
        int result = nombre_usuario != null ? nombre_usuario.hashCode() : 0;
        result = 31 * result + (nombre != null ? nombre.hashCode() : 0);
        result = 31 * result + (cuenta_twitter != null ? cuenta_twitter.hashCode() : 0);
        result = 31 * result + (descripcion != null ? descripcion.hashCode() : 0);
        return result;
    }
}
