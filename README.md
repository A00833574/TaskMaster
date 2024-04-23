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

[Project Plan](/documentacion/Plan_de_Proyecto.pdf)

## Dependencias del proyecto

Este proyecto utiliza principalmente las siguientes dependencias y tecnologías usadas a nivel de aplicación, infraestructura y almacenamiento

- Spring: Esta es la dependencia más importante del proyecto ya que se encarga de crear el servidor al cual nos conectamos, spring nos permite crear el servidor al cual se va a estar comunicando con telegram por medio del long pooling.
- TelegramAPI: Se requiere la instalación de esta librería para poder conectarnos con la api de Telegram, se requiere instalar la librería en el proyecto del backend donde mismo esta instalado spring
- ATP: Autonomous Transaction Proceesing es la base de datos relacional proporcionada por el entorno de nube de oracle OCI, esta nos permite definir un esquema y guardar los datos por medio de queries SQL los cuales en este caso son menajados por lal ibreria de Jakarta Persistence
- Docker: Docker es la tecnología que permite crear un contenedor de la aplicación el cual empaqueta todas las dependencias del proyecto lo que le permite ejecutarlo en cualquier ambiente, sin tener que preocuparse por la configuración del entorno, esto nos asegura el poder ejecutar el programa en cualquier sistema y permite la utlización de orquestadores como kubernetes
- Kubernetes: Kubernetes es usado para orquestar los contenedores de la aplicación, al usar kubernetes nos aseguramos de que la aplicación pueda escalar horizontalmente sin importar la cantidad de carga que reciba

Cada una de estas tecnologías ha sido seleccionada para optimizar el rendimiento, la escalabilidad y la seguridad del proyecto, garantizando así una integración robusta y eficiente de los componentes.
