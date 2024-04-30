<img src='https://javier.rodriguez.org.mx/itesm/2014/simbolo-tec-black.png' width="200">


# Task Master

A Telegram Chatbot developed for Oracle developers by ITESM Students

## Contributors

 - [Milton Vera de la Torre](https://github.com/MiltonVeraTorre)
 - [Juan Pablo Madrigal Magaña](https://github.com/JuanPabloMadrigal)
 - [Ana Carolina Ramírez González](https://github.com/Caroramirezz)
 - [Diego Velazquez Saldaña](https://github.com/diegovelsal)
 - [Gabriel Eduardo Diaz Roa](https://github.com/A00833574)


## Documentation

[SRS File](/documentacion/SRS.pdf)

[Project Plan](/documentacion/Plan_de_Projecto.pdf)

## Dependencias del projecto

Este projecto utiliza principalmente las siguientes dependencias y tecnologías usadas a nivel de aplicación, infraestructura y almacenamiento

- Spring: Esta es la dependencia más importante del projecto ya que se encarga de crear el servidor al cual nos conectamos, spring nos permite crear el servidor al cual se va a estar comunicando con telegram por medio del long pooling.
- TelegramAPI: Se requiere la instalación de esta librería para poder conectarnos con la api de Telegram, se requiere instalar la librería en el projecto del backend donde mismo esta instalado spring
- ATP: Autonomous Transaction Proceesing es la base de datos relacional proporcionada por el entorno de nube de oracle OCI, esta nos permite definir un esquema y guardar los datos por medio de queries SQL los cuales en este caso son menajados por lal ibreria de Jakarta Persistence
- Docker: Docker es la tecnología que permite crear un contenedor de la aplicación el cual empaqueta todas las dependencias del projecto lo que le permite ejecutarlo en cualquier ambiente, sin tener que preocuparse por la configuración del entorno, esto nos asegura el poder ejecutar el programa en cualquier sistema y permite la utlización de orquestadores como kubernetes
- Kubernetes: Kubernetes es usado para orquestar los contenedores de la aplicación, al usar kubernetes nos aseguramos de que la aplicación pueda escalar horizontalmente sin importar la cantidad de carga que reciba

Cada una de estas tecnologías ha sido seleccionada para optimizar el rendimiento, la escalabilidad y la seguridad del projecto, garantizando así una integración robusta y eficiente de los componentes.

## Servicios web

Funcionalidades Generales del Back-end

El back-end se encargará de gestionar la creación, actualización, recuperación y eliminación de datos relacionados con Developers, Managers, Projects y Tasks. Se utilizarán técnicas de REST API para interactuar con una base de datos, permitiendo las siguientes acciones:

- CRUD de Developers
- CRUD de Managers
- CRUD de Projects
- CRUD de Tasks

### Endpoints y Métodos HTTP

- Developer
 - Crear un nuevo Developer
    POST /developer
    Body: JSON con los detalles del Developer.
    Respuestas:
     201 Created si se crea correctamente.
     400 Bad Request si faltan datos necesarios o están mal formados.
   
  -Obtener todos los Developers
    GET /developer
    Respuestas:
     200 OK con la lista de Developers.
     404 Not Found si no se encuentran datos.
     
  -Obtener un Developer por ID
    GET /developer/{id}
    Respuestas:
     200 OK con los detalles del Developer.
     404 Not Found si el Developer con el ID especificado no existe.
     
   -Actualizar un Developer
     PUT /developer
     Body: JSON con los detalles del Developer actualizados.
     Respuestas:
      200 OK si se actualiza correctamente.
      400 Bad Request si la solicitud está mal formada.
      
   -Eliminar un Developer por ID
     DELETE /developer/{id}
     Respuestas:
      200 OK si se elimina correctamente.
      404 Not Found si no existe el Developer con el ID especificado.
Manager
 - Crear un nuevo Manager
    POST /manager
    Body: JSON con los detalles del Manager.
    Respuestas:
     201 Created si se crea correctamente.
     400 Bad Request si faltan datos necesarios o están mal formados.
   
  -Obtener todos los Managers
    GET /manager
    Respuestas:
     200 OK con la lista de Managers.
     404 Not Found si no se encuentran datos.
     
  -Obtener un Manager por ID
    GET /manager/{id}
    Respuestas:
     200 OK con los detalles del Manager.
     404 Not Found si el Manager con el ID especificado no existe.
     
   -Actualizar un Manager
     PUT /manager
     Body: JSON con los detalles del Manager actualizados.
     Respuestas:
      200 OK si se actualiza correctamente.
      400 Bad Request si la solicitud está mal formada.
      
   -Eliminar un Manager por ID
     DELETE /manager/{id}
     Respuestas:
      200 OK si se elimina correctamente.
      404 Not Found si no existe el Manager con el ID especificado.
Project
 - Crear un nuevo Project
    POST /project
    Body: JSON con los detalles del Project.
    Respuestas:
     201 Created si se crea correctamente.
     400 Bad Request si faltan datos necesarios o están mal formados.
   
  -Obtener todos los Projects
    GET /project
    Respuestas:
     200 OK con la lista de Projects.
     404 Not Found si no se encuentran datos.
     
  -Obtener un Project por ID
    GET /project/{id}
    Respuestas:
     200 OK con los detalles del Project.
     404 Not Found si el Project con el ID especificado no existe.
     
   -Actualizar un Project
     PUT /project
     Body: JSON con los detalles del Project actualizados.
     Respuestas:
      200 OK si se actualiza correctamente.
      400 Bad Request si la solicitud está mal formada.
      
   -Eliminar un Project por ID
     DELETE /project/{id}
     Respuestas:
      200 OK si se elimina correctamente.
      404 Not Found si no existe el Project con el ID especificado.
Task
 - Crear un nuevo Task
    POST /task
    Body: JSON con los detalles del Task.
    Respuestas:
     201 Created si se crea correctamente.
     400 Bad Request si faltan datos necesarios o están mal formados.
   
  -Obtener todos los Developers
    GET /task
    Respuestas:
     200 OK con la lista de Tasks.
     404 Not Found si no se encuentran datos.
     
  -Obtener un Task por ID
    GET /task/{id}
    Respuestas:
     200 OK con los detalles del Task.
     404 Not Found si el Task con el ID especificado no existe.
     
   -Actualizar un Task
     PUT /developer
     Body: JSON con los detalles del Task actualizados.
     Respuestas:
      200 OK si se actualiza correctamente.
      400 Bad Request si la solicitud está mal formada.
      
   -Eliminar un Task por ID
     DELETE /task/{id}
     Respuestas:
      200 OK si se elimina correctamente.
      404 Not Found si no existe el Task con el ID especificado.



### Validaciones, Códigos de Error y Respuestas

Cada endpoint realizará validaciones para asegurarse de que los datos recibidos son válidos y completos. Las validaciones incluyen:

- Verificación de que los campos obligatorios estén presentes y sean del tipo correcto.
- Validación de rangos de valores aceptables para ciertos campos.
- Confirmación de que los IDs proporcionados existen en la base de datos antes de realizar operaciones de actualización o eliminación.

#### Los códigos de error comunes incluyen:

- 400 Bad Request para solicitudes con datos mal formados.
- 404 Not Found para solicitudes que hacen referencia a IDs que no existen en la base de datos.
- 500 Internal Server Error para errores inesperados del servidor.
