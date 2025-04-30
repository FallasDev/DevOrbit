const URL_API = 'http://localhost:8080/api/user/me';
let tokenUsuario = localStorage.getItem('jwtToken');

const elementos = {
    modalPerfil: document.getElementById('profileModal'),
    formularioPerfil: document.getElementById('profileForm'),
    campoUsuario: document.getElementById('username'),
    campoCorreo: document.getElementById('email'),
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
    cargarDatosIniciales();
}

function verificarAutenticacion() {
    if (!tokenUsuario) {
        alert("No estás autenticado. Serás redirigido al login.");
        window.location.href = "login.html";
    }
}

function inicializarModal() {
    if (elementos.modalPerfil) {
        modalBootstrapPerfil = new bootstrap.Modal(elementos.modalPerfil);
    }
}

function configurarEventos() {
    document.addEventListener('DOMContentLoaded', () => {
        if (!tokenUsuario) return;

        if (elementos.botonGuardar) {
            elementos.botonGuardar.addEventListener('click', guardarPerfil);
        }

        if (elementos.formularioPerfil) {
            elementos.formularioPerfil.addEventListener('submit', guardarPerfil);
        }

        if (elementos.botonEliminar) {
            elementos.botonEliminar.addEventListener('click', eliminarCuenta);
        }

        configurarCerrarSesion();
        configurarEventosModal();
    });
}

function configurarCerrarSesion() {
    const enlaceCerrarSesion = document.getElementById('logoutLink');
    if (enlaceCerrarSesion) {
        enlaceCerrarSesion.addEventListener('click', (e) => {
            e.preventDefault();
            localStorage.removeItem('jwtToken');
            window.location.href = '../components/login.html';
        });
    }
}

function configurarEventosModal() {
    if (elementos.modalPerfil) {
        elementos.modalPerfil.addEventListener('shown.bs.modal', cargarPerfil);
        elementos.modalPerfil.addEventListener('hidden.bs.modal', () => {
            if (elementos.alertaExito) elementos.alertaExito.classList.add('d-none');
            if (elementos.alertaError) elementos.alertaError.classList.add('d-none');
        });
    }
}

function cargarDatosIniciales() {}

function mostrarAlerta(alerta, mensaje, esError = false) {
    if (!alerta) return;

    alerta.textContent = mensaje;
    alerta.className = `alert ${esError ? 'alert-danger' : 'alert-success'}`;
    alerta.classList.remove('d-none');
    setTimeout(() => alerta.classList.add('d-none'), 3000);
}

function tokenInvalido() {
    localStorage.removeItem('jwtToken');
    window.location.href = "login.html";
}

function errorDeRed(error) {
    console.error("Error de red:", error);
}

async function cargarPerfil() {
    try {
        const respuesta = await fetch(URL_API, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${tokenUsuario}`,
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
        }
    }
}

async function guardarPerfil(evento) {
    if (evento) evento.preventDefault();

    if (elementos.formularioPerfil && !elementos.formularioPerfil.checkValidity()) {
        elementos.formularioPerfil.reportValidity();
        return;
    }

    cambiarEstadoBoton(elementos.botonGuardar, true, 'Guardando...');

    try {
        const respuesta = await fetch(URL_API, {
            method: 'PUT',
            headers: {
                'Authorization': `Bearer ${tokenUsuario}`,
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
            tokenUsuario = resultado.token;
            localStorage.setItem('jwtToken', resultado.token);
        }

        mostrarAlerta(elementos.alertaExito, "Perfil actualizado correctamente. Por seguridad, vuelve a iniciar sesión.");
        setTimeout(() => {
            localStorage.removeItem('jwtToken');
            window.location.href = "login.html";
        }, 3000);

    } catch (error) {
        if (error.name === 'TypeError' && error.message.includes('network')) {
            errorDeRed(error);
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
                'Authorization': `Bearer ${tokenUsuario}`
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
        window.location.href = "login.html";

    } catch (error) {
        cambiarEstadoBoton(elementos.botonEliminar, false, 'Eliminar Cuenta');
        if (error.name === 'TypeError' && error.message.includes('network')) {
            errorDeRed(error);
        }
    }
}

function cambiarEstadoBoton(boton, cargando, texto) {
    if (!boton) return;
    boton.disabled = cargando;
    boton.innerHTML = cargando
        ? '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> ' + texto
        : texto;
}

iniciarAplicacion();
