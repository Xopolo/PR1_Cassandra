create table comentarios_autor_noticia
(
    autor             text,
    titulo_noticia    text,
    fecha             timeuuid,
    dia               int,
    mes               int,
    anio              int,
    cuerpo_comentario text,
    primary key ((autor, titulo_noticia), fecha, dia, mes, anio)
)
    with clustering order by (fecha desc, dia desc, mes desc, anio desc)
     and caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}
     and compaction = {'class': 'org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy', 'max_threshold': '32', 'min_threshold': '4'}
     and compression = {'chunk_length_in_kb': '64', 'class': 'org.apache.cassandra.io.compress.LZ4Compressor'}
     and dclocal_read_repair_chance = 0.0;

create table comentarios_usuario_noticia
(
    dia            int,
    mes            int,
    anio           int,
    fecha          timeuuid,
    cuerpo         text,
    nombre_usuario text,
    titulo_noticia text,
    primary key ((dia, mes, anio), fecha)
)
    with clustering order by (fecha desc)
     and caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}
     and compaction = {'class': 'org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy', 'max_threshold': '32', 'min_threshold': '4'}
     and compression = {'chunk_length_in_kb': '64', 'class': 'org.apache.cassandra.io.compress.LZ4Compressor'}
     and dclocal_read_repair_chance = 0.0;

create table noticias_por_fecha
(
    dia    int,
    mes    int,
    anio   int,
    fecha  timeuuid,
    autor  text,
    cuerpo text,
    tags   set<text>,
    titulo text,
    primary key ((dia, mes, anio), fecha)
)
    with clustering order by (fecha desc)
     and caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}
     and compaction = {'class': 'org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy', 'max_threshold': '32', 'min_threshold': '4'}
     and compression = {'chunk_length_in_kb': '64', 'class': 'org.apache.cassandra.io.compress.LZ4Compressor'}
     and dclocal_read_repair_chance = 0.0;

create table noticias_por_usuario
(
    nombre_usuario      text,
    fecha               timeuuid,
    cuerpo              text,
    descripcion_usuario text,
    tags                set<text>,
    titulo              text,
    primary key (nombre_usuario, fecha)
)
    with caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}
     and compaction = {'class': 'org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy', 'max_threshold': '32', 'min_threshold': '4'}
     and compression = {'chunk_length_in_kb': '64', 'class': 'org.apache.cassandra.io.compress.LZ4Compressor'}
     and dclocal_read_repair_chance = 0.0;

create table usuarios_nick
(
    nick           text primary key,
    cuenta_twitter text,
    descripcion    text,
    nombre         text
)
    with caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}
     and compaction = {'class': 'org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy', 'max_threshold': '32', 'min_threshold': '4'}
     and compression = {'chunk_length_in_kb': '64', 'class': 'org.apache.cassandra.io.compress.LZ4Compressor'}
     and dclocal_read_repair_chance = 0.0;

create table usuarios_twitter
(
    cuenta_twitter text primary key,
    descripcion    text,
    nick           text,
    nombre         text
)
    with caching = {'keys': 'ALL', 'rows_per_partition': 'NONE'}
     and compaction = {'class': 'org.apache.cassandra.db.compaction.SizeTieredCompactionStrategy', 'max_threshold': '32', 'min_threshold': '4'}
     and compression = {'chunk_length_in_kb': '64', 'class': 'org.apache.cassandra.io.compress.LZ4Compressor'}
     and dclocal_read_repair_chance = 0.0;


