const API_URL = ''; //aqui api perre

//creamos las funciones para que nuestra pagina funcione correctamente



//funcion de la barra d busqueda de generalCursesStudent.html
function buscarCursos(event) {
    event.preventDefault(); // Evita que se recargue la página

    const texto = document.getElementById("inputBusqueda").value.trim();

    if (texto !== "") {
        // llamo a la api si quiero que es la idea
        window.location.href = `resultadosStudent.html?busqueda=${encodeURIComponent(texto)}`;

        // O si deseas filtrar en la misma página, puedes crear una función aparte que filtre las tarjetas.
        // filtrarCursos(texto);
    } else {
        alert("Por favor ingresa un término de búsqueda.");
    }
}

// leer la búsqueda, una idea porque todo esto se tiene que meter en un json
const params = new URLSearchParams(window.location.search);
const busqueda = params.get("busqueda") || "";
document.getElementById("terminoBusqueda").textContent = busqueda;

fetch(`http://localhost:8080/api/cursos?busqueda=${encodeURIComponent(busqueda)}`)
    .then(res => res.json())
    .then(cursos => {
        const cont = document.getElementById("resultadosCursos");
        if (cursos.length === 0) {
            cont.innerHTML = `<p class="text-muted">No se encontraron cursos para "<strong>${busqueda}</strong>".</p>`;
            return;
        }
        cursos.forEach(c => {
            const col = document.createElement("div");
            col.className = "col";
            col.innerHTML = `
      <a href="vistaCursoStudent.html?id=${c.id}" class="text-decoration-none text-dark">
        <div class="card h-100 shadow-sm border-0">
          <img src="${c.imagen}" class="card-img-top" alt="${c.nombre}">
          <div class="card-body d-flex flex-column">
            <h5 class="card-title">${c.nombre}</h5>
            <p class="card-text text-muted small">${c.descripcion}</p>
            <div class="mb-2">
              ${"★".repeat(Math.round(c.valoracion))}${"☆".repeat(5 - Math.round(c.valoracion))}
              <span class="text-muted small">(${c.totalValoraciones})</span>
            </div>
            <h6 class="mt-auto fw-bold">$${c.precio.toFixed(2)}</h6>
          </div>
        </div>
      </a>`;
            cont.appendChild(col);
        });
    })
    .catch(err => {
        console.error(err);
        document.getElementById("resultadosCursos").innerHTML = `<p class="text-danger">Error al cargar los cursos.</p>`;
    });


// consulta de los cursos según el filtro, igual no sirve porque me falta todo en json
function obtenerCursos(filtro) {
    // va API 
    let cursos = [];

    if (filtro === "mas-recientes") {
        cursos = [
            { nombre: "Curso de JavaScript", precio: "$30", tiempo: "3 horas" },
            { nombre: "Curso de HTML y CSS", precio: "$20", tiempo: "2 horas" },
        ];
    } else if (filtro === "mas-vistos") {
        cursos = [
            { nombre: "Curso de React", precio: "$40", tiempo: "5 horas" },
            { nombre: "Curso de Node.js", precio: "$35", tiempo: "4 horas" },
        ];
    } else if (filtro === "gratis") {
        cursos = [
            { nombre: "Curso de Git", precio: "Gratis", tiempo: "1 hora" },
            { nombre: "Curso de Python", precio: "Gratis", tiempo: "3 horas" },
        ];
    }

    // Limpiar el contenedor de cursos
    const contenedorCursos = document.getElementById("contenedor-cursos");
    contenedorCursos.innerHTML = '';

    // Agregar los cursos al contenedor, esta mal porque va en modo json bb
    cursos.forEach(curso => {
        const card = document.createElement('div');
        card.className = 'card mb-3';
        card.style = 'max-width: 540px;';
        card.innerHTML = `
            <div class="row g-0">
                <div class="col-md-4">
                    <img src="https://via.placeholder.com/150" class="img-fluid rounded-start" alt="${curso.nombre}">
                </div>
                <div class="col-md-8">
                    <div class="card-body">
                        <h5 class="card-title">${curso.nombre}</h5>
                        <p class="card-text">Precio: ${curso.precio}</p>
                        <p class="card-text"><small class="text-muted">Duración: ${curso.tiempo}</small></p>
                    </div>
                </div>
            </div>
        `;
        contenedorCursos.appendChild(card);
    });
}
