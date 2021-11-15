import com.datastax.driver.core.*;

import java.util.Iterator;

public class Cliente {

    private Cluster cluster;
    private Session session;

    public Session getSession() {
        return this.session;
    }

    public void connect(String node) {
        cluster = Cluster.builder().addContactPoint(node).build();
        Metadata metadata = cluster.getMetadata();
        System.out.printf("Connected to cluster: %s\n",
                metadata.getClusterName());
        for (Host host : metadata.getAllHosts()) {
            System.out.printf("Datatacenter: %s; Host: %s; Rack: %s\n",
                    host.getDatacenter(), host.getAddress(), host.getRack());
        }
        session = cluster.connect("ks_pab_her_2021");
    }

    public void crearEsquema() {
        session.execute("create table IF NOT EXISTS USUARIOS\n" +
                "(\n" +
                "    id uuid PRIMARY KEY,\n" +
                "    nombre_usuario text,\n" +
                "    cuenta_twitter text,\n" +
                "    nombre         text,\n" +
                "    descripcion    text\n" +
                ");");
        session.execute(
                "create table IF NOT EXISTS USUARIOS_NICK\n" +
                        "(\n" +
                        "    nick text PRIMARY KEY,\n" +
                        "    cuenta_twitter text,\n" +
                        "    nombre         text,\n" +
                        "    descripcion    text\n" +
                        ");");
        session.execute(
                "create table IF NOT EXISTS USUARIOS_TWITTER\n" +
                        "(\n" +
                        "    nick text,\n" +
                        "    cuenta_twitter text PRIMARY KEY,\n" +
                        "    nombre         text,\n" +
                        "    descripcion    text\n" +
                        ");");
        session.execute(
                "create table IF NOT EXISTS NOTICIAS\n" +
                        "(\n" +
                        "    not_id uuid PRIMARY KEY,\n" +
                        "    titulo text,\n" +
                        "    cuerpo text,\n" +
                        "    fecha text,\n" +
                        "    tags set<text>\n" +
                        ");");
        session.execute(
                "create table IF NOT EXISTS COMENTARIO\n" +
                        "(" +
                        "    id uuid PRIMARY KEY,\n" +
                        "    fecha TIMEUUID ,\n" +
                        "    cuerpo text\n" +
                        ");");
    }

    public void insertarDatos() {
        ResultSet rs = session.execute("SELECT * FROM USUARIOS_NICK");
        if (rs.all().size() < 100) {
            System.out.println("Insertando 100 usuarios");
            for (int i = 0; i < 100; i++) {
                new Usuario(session, GeneradorStrings.loremIpsum(), GeneradorStrings.loremIpsum(), "@" + GeneradorStrings.loremIpsum(), GeneradorStrings.loremIpsum()).insertarUsuario();
            }
            Usuario usuario = new Usuario(session, "Xolopo", "Pablo", "@Xolopo", "Estudiante :)");
            usuario.insertarUsuario();
        }

    }

    public void close() {
        session.close();
        cluster.close();
        System.out.println("Connection closed");
    }

}
