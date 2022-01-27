# requisitos

La aplicación web podrá gestionar varios libros.
* En la página principal aparecerán los títulos de los libros.
* Cada título será un enlace que al ser pulsado abrirá una página en la que se mostrará el contenido del libro (título, resumen, autor, editorial y año de publicación).
* En la página principal habrá un enlace que llevará a una nueva página para crear un nuevo libro.
* Cada libro podrá tener comentarios asociados que se mostrarán debajo de su contenido y una puntuación de 0 a 5.
* Para poder crear un comentario, debajo del contenido del libro habrá un formulario para poder introducir el nombre del usuario, el comentario y la puntuación.
* Cuando un usuario haya creado un comentario con anterioridad y vaya a crear otro, su nombre aparecerá precargado en el formulario.
* Cada comentario se mostrará con un botón de borrar que permitirá borrar ese comentario.
* No hay ningún tipo de control de usuarios. Cualquiera podría crear un libro nuevo y añadir comentarios. Cualquiera podría borrar un comentario

Además de la interfaz web, la aplicación también permitirá el acceso mediante una API REST. Esta API REST tendrá las siguientes operaciones:
* Obtener un listado con el identificador y el título de cada uno de los libros (pero no el resto de la información)
* Obtener toda la información de un libro determinado y además sus comentarios
* Crear un libro
* Borrar un libro
* Crear un comentario asociado a un libro
* Obtener la información de un comentario concreto
* Borrar un comentario

# Casos de uso
[Casos de uso](doc/usecases.puml)


# Api doc
[API specification](api-docs/api-docs.yaml)

[API specification](api-docs/api-docs.html)

[Postman collection](OpenAPI.postman_collection.json)
