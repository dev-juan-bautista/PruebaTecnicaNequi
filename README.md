# Prueba Técnica Nequi - Backend

## 📌 Descripción

Este proyecto implementa un API RESTful para la gestión de franquicias, sus sucursales y los productos ofertados en cada
sucursal.
La aplicación está desarrollada en Java 21 con Spring Boot 3 + WebFlux, siguiendo principios de arquitectura hexagonal (
Clean Architecture) para garantizar mantenibilidad, testabilidad y separación de responsabilidades.

El sistema soporta operaciones CRUD sobre franquicias, sucursales y productos, incluyendo la consulta de productos con
mayor stock.
La persistencia se implementa con AWS DynamoDB, y la aplicación está lista para ser desplegada en contenedores Docker y
tambien bajo AWS CloudFormation.

## ⚙️ Tecnologías utilizadas

* Java 21
* Gradle
* Spring Boot 3.5.4
* Spring WebFlux
* AWS SDK v2 (DynamoDB Async Client)
* Docker
* Logback / SLF4J para logging estructurado en JSON
* JUnit 5 / Mockito para pruebas unitarias

---

## 🏗️ Arquitectura

La solución sigue el enfoque Hexagonal bajo el enfoque del scaffold de bancolombia siendo una aplicacion modularizada
![Clean Architecture](https://miro.medium.com/max/1400/1*ZdlHz8B0-qu9Y-QO3AXR_w.png)

## Domain

Es el módulo más interno de la arquitectura, pertenece a la capa del dominio y encapsula la lógica y reglas del negocio
mediante modelos y entidades del dominio.

### Usecases

Este módulo gradle perteneciente a la capa del dominio, implementa los casos de uso del sistema, define lógica de
aplicación y reacciona a las invocaciones desde el módulo de entry points, orquestando los flujos hacia el módulo de
entities.

### Models

Este módulo gradle perteneciente a la capa del dominio, implementa todos los modelos que se utilizan para representar
toda la informacion perteneciente a la logica del negocio

## Infrastructure

### Helpers

En el apartado de helpers se desarrollan las utilidades generales para los modulos del Driven Adapters y Entry Points.

Estas utilidades no están arraigadas a objetos concretos, se realiza el uso de generics para modelar comportamientos
genéricos de los diferentes objetos de persistencia que puedan existir, este tipo de implementaciones se realizan
basadas en el patrón de
diseño [Unit of Work y Repository](https://medium.com/@krzychukosobudzki/repository-design-pattern-bc490b256006)

Estas clases no puede existir solas y debe heredarse su compartimiento en los **Driven Adapters**

### Driven Adapters

Los driven adapter representan implementaciones externas a nuestro sistema, como lo son conexiones a servicios rest,
soap, bases de datos, lectura de archivos planos, y en concreto cualquier origen y fuente de datos con la que debamos
interactuar.

### Entry Points

Los entry points representan los puntos de entrada de la aplicación o el inicio de los flujos de negocio.

## Application

Este módulo es el más externo de la arquitectura, es el encargado de ensamblar los distintos módulos, resolver las
dependencias y crear los beans de los casos de use (UseCases) de forma automática, inyectando en éstos instancias
concretas de las dependencias declaradas. Además inicia la aplicación (es el único módulo del proyecto donde
encontraremos la función “public static void main(String[] args)”.

**Los beans de los casos de uso se disponibilizan automaticamente gracias a un '@ComponentScan' ubicado en esta capa.**

---

## 🚀 Endpoints principales

### Franquicias

| Método | Endpoint       | Descripción                  | Headers           | Request Body (JSON)                                       |
|--------|----------------|------------------------------|-------------------|-----------------------------------------------------------|
| POST   | `/franquicias` | Crear nueva franquicia       | `traceId: <uuid>` | ```{"nombre": "Nequi - Colombia"}```                      |
| PUT    | `/franquicias` | Actualizar nombre franquicia | `traceId: <uuid>` | ```{"id": "FRQ-A1B2C3", "nombre": "Nequi - Guatemala"}``` |

### Sucursales

| Método | Endpoint      | Descripción                | Headers           | Request Body (JSON)                                              |
|--------|---------------|----------------------------|-------------------|------------------------------------------------------------------|
| POST   | `/sucursales` | Crear nueva sucursal       | `traceId: <uuid>` | ```{"nombre": "Neiva - San Pedro Plaza"}```                      |
| PUT    | `/sucursales` | Actualizar nombre sucursal | `traceId: <uuid>` | ```{"id": "SUC-A1B2C3", "nombre": "Neiva - Barrio Los Andes"}``` |

### Productos

| Método | Endpoint               | Descripción                                                   | Headers           | Request Body (JSON)                                                          |
|--------|------------------------|---------------------------------------------------------------|-------------------|------------------------------------------------------------------------------|
| POST   | `/productos`           | Crear un producto en una sucursal                             | `traceId: <uuid>` | ```{ "nombre": "Retiro", "cantidad": 8000000, "idSucursal": "SUC-A1B2C3"}``` |
| DELETE | `/productos`           | Elimina un producto de una sucursal                           | `traceId: <uuid>` | ```{"id": "PROD-A1B2C3"}```                                                  |
| PUT    | `/productos`           | Actualiza el nombre y el stock de un producto en una sucursal | `traceId: <uuid>` | ```{"id": "PROD-A1B2C3", "nombre": "Transferencia", "cantidad": 300000}```   |
| GET    | `/productos/max-stock` | Consulta los productos con mas stock por sucursal             | `traceId: <uuid>` | N.A                                                                          |

### Coleccion Postman
En el siguiente directorio del proyecto puede obtener la coleccion postman para poder probar los endpoints mencionados con anterioridad:
* `Postman -> PruebaNequi.postman_collection.json`.

--- 

## Docker

Para realiazr el empaquetamiento de la aplicacion y poder correrla en local a traves de docker debe ejecutar los siguientes comandos

### 1. Compilar la aplicacion
 
``` bash

./gradlew clean build 
```

### 2. Construir imagen docker
``` bash

docker build -t prueba-tecnica-nequi:1.0 -f deployment/Dockerfile .
```

### 3. Levantar la imagen docker en un contenedor
``` bash

docker run --rm -p 8080:8080 -e AWS_REGION=us-east-1 -e AWS_ACCESS_KEY=XXXXXXXXXX -e AWS_SECRET_KEY=XXXXXXXXXX --name prueba-nequi prueba-tecnica-nequi:1.0
```

---

## ☁️ Despliegue en AWS CloudFormation

Además del despliegue en contenedores Docker, el proyecto también fue **desplegado en AWS CloudFormation** utilizando la plantilla **`TemplateNequi.yaml`**, la cual se encuentra ubicada en la carpeta `deployment -> TemplateNequi.yaml`

En la plantilla desarrollada defino una **pila completa** que automatiza la infraestructura necesaria para ejecutar el backend de la prueba técnica en la nube, incluyendo:
- **VPC:** Configuración de red donde se despliega el servicio.
- **Internet Gateway y Route Table:** Permiten la comunicación pública hacia Internet.
- **Security Group:** Habilita el acceso externo al puerto `8080` para el servicio.
- **Rol IAM (ECSExecutionRole-PruebaNequi):** Con permisos sobre **DynamoDB** y **CloudWatch Logs**.
- **ECS Cluster (Fargate):** Donde se ejecuta el contenedor de la aplicación sin necesidad de gestionar servidores.
- **Task Definition:** Define los recursos del contenedor (`512 vCPU`, `1024 MB RAM`), la imagen Docker de la aplicación alojada en **Amazon ECR**, y las variables de entorno requeridas (`AWS_REGION`, `AWS_ACCESS_KEY`, `AWS_SECRET_KEY`).
- **CloudWatch Log Group:** Centraliza los logs generados por la aplicación para su monitoreo.

Una vez creada la pila desde CloudFormation, se genera automáticamente el servicio ECS que ejecuta la aplicación en contenedores Fargate dentro de la red configurada.

### 🔗 Acceso a la aplicación desplegada

Tras el despliegue, la aplicación queda accesible públicamente desde la IP configurada en la plantilla.  
Para efectos de prueba, el backend puede consultarse directamente reemplazando el host local por la siguiente dirección:

### `44.201.38.215:8080`

---

## 📌 Consideraciones de diseño

- Se escogió **Spring WebFlux** dado que fue un **requisito de la prueba técnica** y permite trabajar de forma **reactiva y no bloqueante**, optimizando el manejo de concurrencia.
- Para la arquitectura, se utilizó el **plugin Scaffold de Bancolombia**, también como requisito de la prueba, lo que garantiza una implementación basada en **arquitectura hexagonal (Clean Architecture)**.
- La base de datos seleccionada fue **AWS DynamoDB**, principalmente por razones de **costos y eficiencia**:
    - Posee una alta capacidad de solicitudes.
    - Su costo estimado no supera **1 USD mensual** en este escenario, lo cual es ideal para un **proyecto personal**.
- En cuanto a la seguridad, las **variables de entorno** que hacen referencia a la cuenta de AWS se gestionan mediante un **rol/perfil IAM** con los permisos necesarios para interactuar con DynamoDB.
    - Esto evita exponer credenciales en el repositorio.
    - Permite ejecutar la aplicación de forma segura tanto en local como en despliegues en la nube.
