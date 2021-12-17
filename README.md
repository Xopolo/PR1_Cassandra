# PR1_Cassandra - Sistemas de Informacion - UNIVERSIDAD DE EXTREMADURA
## Autor Pablo Hernández

### Lanzar el tunel automaticamente

Para esta práctica se nos da la opcion de trabajar con un sistema remoto con cassandra en docker. Así es como he configurado yo mi entorno de trabajo.

*Los pasos 1-3 se pueden saltar si sabes como autenticarte mediante la ejecucion desde un fichero 

1. Primero, debemos generar un par de claves rsa para conectarnos al servidor ssh, para ello, abrimos el terminal y introducimos el comando ``ssh-keygen``,
una vez generadas las claves, copiamos la ruta de la clave **PUBLICA**, es decir la que tiene extension ``.pub`` <br>
![Comando ssh-keygen](https://i.imgur.com/Dil2xq6.png)

2. Ahora procedemos a subir la clave al servidor remoto, para ello utilizaremos ``scp``, introduciendo el comando:<br>
``scp RUTA_ABSOLUTA_DE_LA_CLAVE_PUBLICA NOMBRE_USUARIO@IP_DESTINO:$HOME/nombre_de_tu_clave.pub``<br>
  Donde:
    - RUTA_ABSOLUTA_DE_LA_CLAVE_PUBLICA, será la ruta donde tenemos almacenada la clave pública generada anteriormente
    - NOMBRE_USUARIO, el nombre de usuario con el que iniciaremos sesion en el sistema remoto
    - IP_DESTINO, la ip del sistema remoto
    
    ![EJEMPLO](https://i.imgur.com/CztUT6g.png)
    
3. Accedemos al sistema remoto mediante ssh y metemos la clave en la lista de claves autorizadas con el comando:

    ``cat nombre_fichero_clave.pub >> .ssh/authorized_keys``

    De esta manera ya podremos acceder sin necesidad de autenticarnos, estos tres primeros pasos se pueden saltar 

    *Hay que crear la carpeta *``.ssh``* en caso de que no exista

    ![Ejemplo](https://i.imgur.com/fTB1uNn.png)
    
4. Nos creamos un fichero que ejecute el comando para conectarnos a la maquina remota con la extension ``.bat`` para windows y ``.sh`` para linux
    Dentro del fichero introducimos el siguiente comando:
    - Windows:
       ``start /b ssh -L 9042:localhost:9042 USUARIO@IP_REMOTA sleep 60``
    - Linux:
      ``ssh -L 9042:localhost:9042 USUARIO@IP_REMOTA sleep 60 &``
      
    Estos comandos lo que hacen es crear un túnel para **CASSANDRA** (Mongo utiliza otro puerto), que dure 60 segundos, una vez pasado ese tiempo el tunel muere
    
5. En IntelliJ, para lanzar el tunel antes de la ejecucion, nos vamos al menu de ejecuciones de nuestro proyecto, 
Menu > "Edit Configurations"
Seleccionamos o creamos una ejecucion y en las opciones de la ejecucion, Hay un menu de "Modify Options", lo desplegamos y clicamos en "Add before launch task" > "Run External Tool"

![Menu](https://i.imgur.com/N9YZuca.png)    

  Ahora vamos a añadir una nueva tarea antes de la ejecución, para ello le damos al simbolo "+" y se desplegará una ventana así, en esta ventana, 
tenemos que indicar en el campo Program, el fichero que creamos anteriormente y aceptamos todo.

![Tarea](https://i.imgur.com/PmMhNpo.png)

  Y de esta manera el tunel se creará antes que el programa se ejecute
  
![Ejecucion](https://i.imgur.com/yUUizZC.png)

### Conexión con la BASE DE DATOS

Una manera comoda de trabajar con la base de datos, es con un entorno grafico del que dispone IntelliJ. Para configurarlo, debemos:

  1. En la parte derecha del editor, hay una pestaña llamada ``database``, la desplegamos y añadimos la base de datos del tipo que necesitemos, en este caso Apache Cassandra
  
  ![Anadir BD](https://i.imgur.com/TxWAO1s.png)
  
  2. Ahora tenemos que elegir la forma de conectarnos a esta BD, en nuestro caso es a traves de ssh, por ello, seleccionamos la pestaña SSH/SSL, marcamos la opcion de túnel y tenemos que configurarla, tal como viene en la imagen. Podemos elegir el metodo de autenticacion,
de tal manera que podemos usar la clave ssh generada anteriormente o podemos autenticarnos con las credenciales usuario contraseña

  ![Conectar BD](https://i.imgur.com/OVS31uI.png)
  

