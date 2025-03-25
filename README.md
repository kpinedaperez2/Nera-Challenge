# Nera Prueba Account

## Descripción

Este proyecto implementa una API RESTful utilizando **Spring Boot** y **MongoDB** para gestionar cuentas bancarias y transacciones. La API permite crear cuentas bancarias, realizar transacciones (depósitos y retiros) y consultar el balance de una cuenta. También se utiliza **Event Sourcing** para registrar cada acción como un evento, y se proporciona un middleware para registrar en la consola depósitos mayores a 10,000 US$

Por otro lado, el desarrollo se implemento en una arquitectura limpia, respetando los principios solid y utilizando patrones como el strategy para derivar el tipo de transaccion segun lo requerido

## Endpoints Implementados

1. **POST /accounts**: Crea una nueva cuenta bancaria. 
   - **Request Body**:
     ```json
     {
       "accountName": "Oscar Perez",
       "balance": 1000.0,
       "accountType": "ahorros",
       "currency": "USD"
     }
     ```
   - **Response**:
     ```json
     {
       "accountId": "6e47e3f0-739d-4d87-823c-c3443c87ae0e"
     }
     ```

2. **GET /accounts/{id}/balance**: Obtiene el balance de una cuenta bancaria.
   - **Response**:
     ```json
     {
       "balance": 15000.50
     }
     ```

3. **POST /transactions**: Realiza una transacción bancaria (depósito o retiro).
   - **Request Body**:
     ```json
     {
       "accountId": "6e47e3f0-739d-4d87-823c-c3443c87ae0e",
       "transactionType": "DEPOSIT",
       "amount": 5000
     }
     ```

## Tecnologías

- **Backend**: Spring Boot
- **Base de Datos**: MongoDB
- **Event Sourcing**: Almacenamiento de eventos de cada acción (creación, actualización, transacciones)
- **Aspect**: Registro de transacciones de más de 10,000 US$ en la consola
- **Docker**: Docker Compose para la ejecución de la solución y dependencias
- **Java**: 17
- **Build Tool**: Gradle

## Requerimientos

1. **Java 17+**
2. **Gradle** para la construcción del proyecto.
3. **Docker** (si se desea utilizar MongoDB con Docker Compose).

## Instalación

### 1. Clonar el repositorio y levantarlo
Descargar el repo
```bash
git clone https://github.com/kpinedaperez2/Nera-Challenge.git
 ```
Levantar la imagen docker para inicializar el .jar y la base mongo
```bash
docker-compose up -d
```

### 2. Ejecutar Tests
Para la ejecucion de las pruebas unitarias de mockito e integrales de mockmvc:
```bash
./gradlew test
```

### 3. Postman
Se incluyo una collection de postman para facilitar la importacion y ejecucion de los curls, importar el archivo **Nera.postman_collection.json**

### 4. PreCondicion
Se incluyo un endpoint adicional de "login" que busca simular de manera simple lo que podria ser una eventual autenticacion por jwt requerida para poder ejecutar los servicios, a excepcion del propio login y de la creacion del account, solo se añaren simples pasos como:
*Antes de ejecutar por primera vez en el dia uno de los endpoints, se debe ejecutar el servicio de login con el curl proporcionado
*Usar el token generado como parametro de "Auth" en el resto de servicios, indicando que es de tipo "Bearer token"
*Ejecutar el servicio deseado
**NOTA: La aplicacion del token es solo demostrativa, un extra a lo requerido en la consigna, y solo a modo de reflejar la eventual necesidad de autenticaciones para operaciones de tipo bancario

<!-- CONTRIBUTING -->
## 5. Contribuciones

Si bien en el proyecto se tuvo una consigna, ofrece muchas posibilidades de implementacion que quedan en modo "potencial", como registro de logs(como el alerta de depositos grandes) en herramientas tipo grafana o datadog, subidas a cloud en AWS EC2, e incluso adicionar un sistema de mensajeria kafka para la ejecucion de las transacciones

Contribuciones y recomendaciones para ayudar a crecer son bienvenidas.

1. Realiza el fork del proyecto
2. Crea una rama feature (`git checkout -b feature/rama1`)
3. Realiza el Commit de los cambios (`git commit -m 'Cambios en la rama1'`)
4. Push a la rama (`git push origin feature/rama1`)
5. Realiza el pull request
