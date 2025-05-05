$(document).ready(inicializarLogin);

const URL_AUTENTICACION = 'https://devorbit-vk2z.onrender.com/auth/login';
const URL_PANEL = '/generalCursesStudent.html';

function inicializarLogin() {
    verificarTokenExistente();
    configurarFormularioLogin();
    configurarMostrarClave();
}

function verificarTokenExistente() {
    const tokenExistente = localStorage.getItem('jwtToken');
    if (tokenExistente) {
        window.location.href = URL_PANEL;
    }
}

function configurarFormularioLogin() {
    $('#loginForm').submit(manejarEnvioLogin);
}

function manejarEnvioLogin(e) {
    e.preventDefault();

    const credenciales = obtenerCredencialesFormulario();
    const botonEnviar = obtenerBotonEnvio();
    const textoOriginal = botonEnviar.text();

    establecerEstadoCargando(botonEnviar, true, 'Iniciando sesión...');
    enviarSolicitudLogin(credenciales, botonEnviar, textoOriginal);
}

function obtenerCredencialesFormulario() {
    return {
        username: $('#username').val().trim(),
        password: $('#password').val()
    };
}

function obtenerBotonEnvio() {
    return $('#loginForm').find('button[type="submit"]');
}

function establecerEstadoCargando(boton, cargando, texto) {
    if (cargando) {
        boton.prop('disabled', true)
             .html(`<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> ${texto}`);
    } else {
        boton.prop('disabled', false).text(texto);
    }
}

function enviarSolicitudLogin(credenciales, botonEnviar, textoOriginal) {
    $.ajax({
        url: URL_AUTENTICACION,
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(credenciales),
        success: (respuesta) => manejarExitoLogin(respuesta, botonEnviar, textoOriginal),
        error: (xhr, estado, error) => manejarErrorLogin(xhr, estado, error, botonEnviar, textoOriginal)
    });
}

function manejarExitoLogin(respuesta, botonEnviar, textoOriginal) {
    if (respuesta && respuesta.token) {
        guardarDatosAutenticacion(respuesta);
        redirigirAlPanel();
    } else {
        restablecerBoton(botonEnviar, textoOriginal);
    }
}

function manejarErrorLogin(xhr, estado, error, botonEnviar, textoOriginal) {
    console.error('Error en login:', xhr.status, xhr.responseText);
    mostrarMensajeErrorLogin(xhr, error);
    restablecerBoton(botonEnviar, textoOriginal);
}

function guardarDatosAutenticacion(respuesta) {
    localStorage.setItem('jwtToken', respuesta.token);
    if (respuesta.user) {
        localStorage.setItem('usuarioActual', JSON.stringify(respuesta.user));
    }
}

function redirigirAlPanel() {
    window.location.replace(URL_PANEL);
}

function restablecerBoton(botonEnviar, textoOriginal) {
    establecerEstadoCargando(botonEnviar, false, textoOriginal);
}

function configurarMostrarClave() {
    $('#togglePassword').click(function () {
        const campoClave = $('#password');
        const icono = $(this).find('i');

        if (campoClave.attr('type') === 'password') {
            campoClave.attr('type', 'text');
            icono.removeClass('fa-eye').addClass('fa-eye-slash');
        } else {
            campoClave.attr('type', 'password');
            icono.removeClass('fa-eye-slash').addClass('fa-eye');
        }
    });
}

function mostrarMensajeErrorLogin(xhr, error) {
    let mensaje = 'Error en el login';

    if (xhr.responseJSON && xhr.responseJSON.message) {
        mensaje += ': ' + xhr.responseJSON.message;
    } else if (xhr.status === 0) {
        mensaje += ': No se pudo conectar con el servidor';
    } else {
        mensaje += ` (${xhr.status}): ${error || xhr.statusText}`;
    }

    alert(mensaje);
}
