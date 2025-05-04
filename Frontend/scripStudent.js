// url api cursos
const apiUrl = 'http://localhost:8080/api/courses';
const token = localStorage.getItem('jwtToken');
const container = document.getElementById('course-container');
const carouselInner = document.getElementById('carousel-inner');
const carouselIndicators = document.getElementById('carousel-indicators');

// muestra los cursos
fetch(apiUrl, {
  method: 'GET',
  headers: {
    'Content-Type': 'application/json',
    Authorization: `Bearer ${token}`,
  },
})
  .then(response => {
      console.log(response);

    if (!response.ok) {
      throw new Error('Error autorizando el token');
    }


    return response.json()
  })
  .then(courses => {
    courses.forEach((course, index) => {
      const imgSrc = course.picture?.url
        ? course.picture.url
        : "https://via.placeholder.com/1200x400?text=Sin+imagen";



      const col = document.createElement('div');
      col.className = 'col-md-4 mb-4';
      col.innerHTML = `
            <div class="card h-100 shadow-sm">
                <img src="${imgSrc}" class="card-img-top" alt="${course.title}">
                <div class="card-body">
                    <h5 class="card-title">${course.title}</h5>
                    <p class="card-text">${course.description}</p>
                    <p class="text-muted">Precio: $${course.price}</p>
                    <a href="/Frontend/course.html?courseId=${course.id_course}" class="btn btn-primary">Ver curso</a>
                </div>
            </div>
        `;
      container.appendChild(col);



      const carouselItem = document.createElement('div');
      carouselItem.className = `carousel-item ${index === 0 ? 'active' : ''}`;
      carouselItem.innerHTML = `

      <img src="${imgSrc}" class="d-block w-100" alt="${course.title}">


            <div class="carousel-caption d-none d-md-block bg-dark bg-opacity-50 rounded p-2">
                <h5>${course.title}</h5>
                <p>${course.description}</p>
                <p><strong>Precio: $${course.price}</strong></p>
            </div>
        `;
      carouselInner.appendChild(carouselItem);

      const indicator = document.createElement('button');
      indicator.type = 'button';
      indicator.setAttribute('data-bs-target', '#carouselCursos');
      indicator.setAttribute('data-bs-slide-to', index);
      indicator.className = index === 0 ? 'active' : '';
      indicator.setAttribute('aria-current', index === 0 ? 'true' : 'false');
      indicator.setAttribute('aria-label', `Slide ${index + 1}`);
      carouselIndicators.appendChild(indicator);
    });
  }).catch(error => {
    console.log(error);
    if (error.message === 'Error autorizando el token') {
      localStorage.removeItem('jwtToken');
      window.location.href = '/Frontend/components/login.html';
    }
    container.innerHTML = `<p class="text-danger">Error: ${error.message}</p>`;
  });

  function configurarCerrarSesion() {
    const enlaceCerrarSesion = document.getElementById('logoutLink');
    if (enlaceCerrarSesion) {
        enlaceCerrarSesion.addEventListener('click', (e) => {
            e.preventDefault();
            localStorage.removeItem('jwtToken');
            window.location.href = '/Frontend/components/login.html';
        });
    }
}
configurarCerrarSesion(); 

//Frontend para crud de usuario

const URL_API = 'http://localhost:8080/api/user/me';


const elementos = {
    modalPerfil: document.getElementById('profileModal'),
    formularioPerfil: document.getElementById('profileForm'),
    campoUsuario: document.getElementById('username'),
    campoCorreo: document.getElementById('email'),
    campoPasswordActual: document.getElementById('currentPassword'),
    campoNuevoPassword: document.getElementById('newPassword'),
    campoConfirmarPassword: document.getElementById('confirmPassword'),
    botonGuardar: document.getElementById('saveProfileBtn'),
    botonEliminar: document.getElementById('deleteAccountBtn'),
    alertaExito: document.getElementById('successAlert'),
    alertaError: document.getElementById('errorAlert')
};

let modalBootstrapPerfil;

function iniciarAplicacion() {
    verificarAutenticacion();
    inicializarModal();
    configurarEventos();
}

function verificarAutenticacion() {
    if (!token) {
        alert("No estás autenticado. Serás redirigido al login.");
        window.location.href = "/Frontend/components/login.html";
    }
}

function inicializarModal() {
    if (elementos.modalPerfil) {
        modalBootstrapPerfil = new bootstrap.Modal(elementos.modalPerfil);
    }
}

function configurarEventos() {
    if (elementos.botonGuardar) {
        elementos.botonGuardar.addEventListener('click', guardarPerfil);
    }

    if (elementos.formularioPerfil) {
        elementos.formularioPerfil.addEventListener('submit', (e) => {
            e.preventDefault();
            guardarPerfil();
        });
    }

    if (elementos.botonEliminar) {
        elementos.botonEliminar.addEventListener('click', eliminarCuenta);
    }

    if (elementos.modalPerfil) {
        elementos.modalPerfil.addEventListener('shown.bs.modal', cargarPerfil);
        elementos.modalPerfil.addEventListener('hidden.bs.modal', () => {
            if (elementos.alertaExito) elementos.alertaExito.classList.add('d-none');
            if (elementos.alertaError) elementos.alertaError.classList.add('d-none');
        });
    }

    configurarCerrarSesion();
}

function configurarCerrarSesion() {
    const enlaceCerrarSesion = document.getElementById('logoutLink');
    if (enlaceCerrarSesion) {
        enlaceCerrarSesion.addEventListener('click', (e) => {
            e.preventDefault();
            localStorage.removeItem('jwtToken');
            window.location.href = '/Frontend/components/login.html';
        });
    }
}

function mostrarAlerta(alerta, mensaje, esError = false) {
    if (!alerta) return;

    alerta.textContent = mensaje;
    alerta.className = `alert ${esError ? 'alert-danger' : 'alert-success'}`;
    alerta.classList.remove('d-none');
    setTimeout(() => alerta.classList.add('d-none'), 3000);
}

function tokenInvalido() {
    localStorage.removeItem('jwtToken');
    window.location.href = "/Frontend/components/login.html";
}

function errorDeRed(error) {
    console.error("Error de red:", error);
    mostrarAlerta(elementos.alertaError, "Error de conexión. Intenta nuevamente.", true);
}

function cambiarEstadoBoton(boton, cargando, texto) {
    if (!boton) return;
    boton.disabled = cargando;
    boton.innerHTML = cargando
        ? '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> ' + texto
        : texto;
}

async function cargarPerfil() {
    try {
        const respuesta = await fetch(URL_API, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });

        if (!respuesta.ok) {
            if (respuesta.status === 401 || respuesta.status === 403) {
                tokenInvalido();
                return;
            }
            const mensajeError = await respuesta.text();
            throw new Error(mensajeError || `Error HTTP: ${respuesta.status}`);
        }

        const datosUsuario = await respuesta.json();
        elementos.campoUsuario.value = datosUsuario.username || '';
        elementos.campoCorreo.value = datosUsuario.email || '';

    } catch (error) {
        console.error("Error al cargar perfil:", error);
        if (error.name === 'TypeError' && error.message.includes('network')) {
            errorDeRed(error);
        } else {
            mostrarAlerta(elementos.alertaError, "Error al cargar el perfil", true);
        }
    }
}

async function guardarPerfil() {
    if (elementos.formularioPerfil && !elementos.formularioPerfil.checkValidity()) {
        elementos.formularioPerfil.reportValidity();
        return;
    }

    cambiarEstadoBoton(elementos.botonGuardar, true, 'Guardando...');

    try {
        const respuesta = await fetch(URL_API, {
            method: 'PUT',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                username: elementos.campoUsuario.value.trim(),
                email: elementos.campoCorreo.value.trim()
            })
        });

        cambiarEstadoBoton(elementos.botonGuardar, false, 'Guardar Cambios');

        if (!respuesta.ok) {
            if (respuesta.status === 401 || respuesta.status === 403) {
                tokenInvalido();
                return;
            }
            const mensajeError = await respuesta.text();
            throw new Error(mensajeError || `Error HTTP: ${respuesta.status}`);
        }

        const resultado = await respuesta.json();

        if (resultado.token) {
            localStorage.setItem('jwtToken', resultado.token);
        }

        mostrarAlerta(elementos.alertaExito, "Perfil actualizado correctamente. Por seguridad, vuelve a iniciar sesión.");
        setTimeout(() => {
            localStorage.removeItem('jwtToken');
            window.location.href = "/Frontend/components/login.html";
        }, 3000);

    } catch (error) {
        cambiarEstadoBoton(elementos.botonGuardar, false, 'Guardar Cambios');
        if (error.name === 'TypeError' && error.message.includes('network')) {
            errorDeRed(error);
        } else {
            mostrarAlerta(elementos.alertaError, "Error al actualizar el perfil", true);
        }
    }
}

async function eliminarCuenta() {
    if (!confirm("¿Estás seguro de que quieres eliminar tu cuenta? Esta acción no es reversible.")) return;

    cambiarEstadoBoton(elementos.botonEliminar, true, 'Eliminando...');

    try {
        const respuesta = await fetch(URL_API, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (!respuesta.ok) {
            cambiarEstadoBoton(elementos.botonEliminar, false, 'Eliminar Cuenta');
            if (respuesta.status === 401 || respuesta.status === 403) {
                tokenInvalido();
                return;
            }
            const mensajeError = await respuesta.text();
            throw new Error(mensajeError || `Error HTTP: ${respuesta.status}`);
        }

        localStorage.removeItem('jwtToken');
        window.location.href = "/Frontend/components/login.html";

    } catch (error) {
        cambiarEstadoBoton(elementos.botonEliminar, false, 'Eliminar Cuenta');
        if (error.name === 'TypeError' && error.message.includes('network')) {
            errorDeRed(error);
        } else {
            mostrarAlerta(elementos.alertaError, "Error al eliminar la cuenta", true);
        }
    }
}

document.addEventListener('DOMContentLoaded', iniciarAplicacion);


