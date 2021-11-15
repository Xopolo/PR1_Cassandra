create table IF NOT EXISTS USUARIOS
(
    id uuid PRIMARY KEY,
    nombre_usuario text,
    cuenta_twitter text,
    nombre         text,
    descripcion    text
);

create table IF NOT EXISTS USUARIOS_NICK
(
    nick           text,
    cuenta_twitter text,
    nombre         text,
    descripcion    text,
    PRIMARY KEY ( nick )
    );

create table IF NOT EXISTS USUARIOS_TWITTER
(
    nick           text,
    cuenta_twitter text,
    nombre         text,
    descripcion    text,
    PRIMARY KEY (cuenta_twitter )
);


create table IF NOT EXISTS NOTICIAS
(
    id uuid PRIMARY KEY,
    titulo text,
    cuerpo text,
    fecha         TIMEUUID,
    tags set<text>
);

create table IF NOT EXISTS COMENTARIO
(
    id uuid PRIMARY KEY,
    fecha TIMEUUID ,
    cuerpo text
);
