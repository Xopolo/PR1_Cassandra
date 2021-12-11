package es.unex.cum.sinf;

import com.datastax.driver.core.*;

import java.time.LocalDate;
import java.util.*;

public class Gestor {

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
        session.execute( // CONSULTA 1
                "create table IF NOT EXISTS USUARIOS_NICK\n" +
                        "(\n" +
                        "    nick text,\n" +
                        "    cuenta_twitter text,\n" +
                        "    descripcion    text,\n" +
                        "    nombre         text,\n" +
                        "    PRIMARY KEY(nick)\n" +
                        ");");
        session.execute( // CONSULTA 2
                "create table IF NOT EXISTS USUARIOS_TWITTER\n" +
                        "(\n" +
                        "    cuenta_twitter text,\n" +
                        "    descripcion    text, \n" +
                        "    nick           text,\n" +
                        "    nombre         text," +
                        "   PRIMARY KEY(cuenta_twitter)\n" +
                        ");");
        session.execute( // CONSULTA 3
                "create table IF NOT EXISTS NOTICIAS_POR_FECHA\n" +
                        "    (\n" +
                        "    dia int,\n" +
                        "    mes int,\n" +
                        "    anio int,\n" +
                        "    titulo text,\n" +
                        "    cuerpo text,\n" +
                        "    fecha timeuuid,\n" +
                        "    tags set<text>,\n" +
                        "    autor text,\n" +
                        "    PRIMARY KEY((dia,mes,anio),fecha)\n" +
                        "    ) WITH CLUSTERING ORDER BY (fecha DESC);");

        session.execute( // CONSULTA 4
                "CREATE TABLE IF NOT EXISTS NOTICIAS_POR_USUARIO (\n" +
                        "    titulo text,\n" +
                        "    cuerpo text,\n" +
                        "    fecha timeuuid,\n" +
                        "    descripcion_usuario text,\n" +
                        "    tags set<text>,\n" +
                        "    nombre_usuario text,\n" +
                        "    PRIMARY KEY ( nombre_usuario, fecha )\n" +
                        ");");

        session.execute( // CONSULTA 5
                "CREATE TABLE IF NOT EXISTS COMENTARIOS_USUARIO_NOTICIA\n" +
                        "(\n" +
                        "    fecha          timeuuid,\n" +
                        "    cuerpo         text,\n" +
                        "    dia            int,\n" +
                        "    mes            int,\n" +
                        "    anio           int,\n" +
                        "    titulo_noticia text,\n" +
                        "    nombre_usuario text,\n" +
                        "    primary key ( (dia, mes, anio), fecha )\n" +
                        ") WITH CLUSTERING ORDER BY (fecha desc);"
        );

        session.execute("create table IF NOT EXISTS comentarios_autor_noticia\n" +
                "(\n" +
                "    autor             text,\n" +
                "    titulo_noticia    text,\n" +
                "    fecha             timeuuid,\n" +
                "    cuerpo_comentario text,\n" +
                "    dia               int,\n" +
                "    mes               int,\n" +
                "    anio              int,\n" +
                "    PRIMARY KEY ( (autor, titulo_noticia), fecha, dia, mes, anio)\n" +
                ") WITH CLUSTERING ORDER BY (fecha desc, dia desc, mes desc, anio desc);"
        );


    }

    public void insertarDatos() {

        String[] nombres_usuario = new String[]{"Pablo", "Xolopo", "Yisus", "Hiken"};
        Random rnd = new Random();


        ResultSet rs = session.execute("SELECT * FROM USUARIOS_NICK");
        if (rs.all().size() < 10) {
            System.out.println("Insertando 10 usuarios");
            for (int i = 0; i < 10; i++) {
                new Usuario(session, GeneradorStrings.loremIpsum(), GeneradorStrings.loremIpsum(), "@" + GeneradorStrings.loremIpsum(), GeneradorStrings.loremIpsum()).insertarUsuario();
            }
            new Usuario(session, "Pablo", "Pablo", "@Xolopo", "Estudiante :)").insertarUsuario();
            new Usuario(session, "Xolopo", "Pablo", "@Pablo", "Estudiante :)").insertarUsuario();
            new Usuario(session, "Yisus", "Jesus", "@Yisus", "Estudiante :)").insertarUsuario();
            new Usuario(session, "Hiken", "JJ", "@Hiken", "Estudiante :)").insertarUsuario();
        }


        SetCassandra<String> tags = new SetCassandra<>();
        tags.add("asd");
        LocalDate hoy = LocalDate.now();
        rs = session.execute(String.format("SELECT COUNT(*) from NOTICIAS_POR_FECHA WHERE dia = %d and mes = %d and anio = %d", hoy.getDayOfMonth(), hoy.getMonthValue(), hoy.getYear()));

        if (rs.one().getLong(0) < 10) {
            String[] poema;
            GeneradorStrings.leerCSV("Resources/poems.csv");
            System.out.println("Insertando noticias");
            int max_noticias = rnd.nextInt(20) + 10;

            for (int i = 0; i < max_noticias; i++) {
                poema = GeneradorStrings.getRandomPoem();
                tags.clear();
                tags.add(GeneradorStrings.loremIpsum(5));
                tags.add(GeneradorStrings.loremIpsum(5));
                new Noticia(session, poema[2], poema[1].trim().split("\n")[0], tags, nombres_usuario[rnd.nextInt(nombres_usuario.length)]).insertarNoticia();
            }
            new Noticia(session, "titulo", "Cuerpo noticia", tags, "Xolopo").insertarNoticia();
        }


        System.out.println("Insertando comentarios");
        // Se supone que el titulo de la noticia es único
        rs = session.execute("SELECT COUNT(*) from comentarios_usuario_noticia;");
        if (rs.one().getLong(0) < 50) {

            // Cojo usuarios aleatoriamente para insertar comentarios
            LinkedList<String> autores_comentarios = new LinkedList<>();

            rs = session.execute("select *\n" +
                    "from usuarios_nick;");
            for (Row r : rs) {
                autores_comentarios.add(r.getString(0)); // Cojo el nick de usuario
            }

            // Uso la consulta 3 para coger noticias existentes
            List<String> tit_noticias = new ArrayList<>();
            rs = session.execute("SELECT * FROM NOTICIAS_POR_FECHA LIMIT 10");
            for (Row fila : rs) {
                tit_noticias.add(fila.getString("titulo"));
            }

            for (int i = 0; i < 50; i++) {
                new Comentario(session, GeneradorStrings.generarPablabras(3, 5), autores_comentarios.get(rnd.nextInt(autores_comentarios.size())), tit_noticias.get(rnd.nextInt(tit_noticias.size()))).insertarComentario();
            }
            new Comentario(session, GeneradorStrings.generarPablabras(3, 5), "Xolopo", "titulo").insertarComentario();
        }
    }

    public void close() {
        session.close();
        cluster.close();
        System.out.println("Connection closed");
    }

}
