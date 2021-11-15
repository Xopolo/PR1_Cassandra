import com.datastax.driver.core.Session;

import java.util.UUID;

public class Usuario {

    Session session;
    UUID uuid;
    String nombre_usuario;
    String nombre;
    String cuenta_twitter;
    String descripcion;

    public Usuario(Session session) {
        this.session = session;
    }

    public Usuario(Session session, String nombre_usuario, String nombre, String cuenta_twitter, String descripcion) {
        this.session = session;
        this.nombre_usuario = nombre_usuario;
        this.nombre = nombre;
        this.cuenta_twitter = cuenta_twitter;
        this.descripcion = descripcion;
        this.uuid = UUID.randomUUID();
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

}
