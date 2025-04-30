$(document).ready(function () {
    const REGISTER_API_URL = 'http://localhost:8080/auth/register';
    const LOGIN_URL = '../components/login.html';
    const DEFAULT_ROLE = "ROLE_USER";

    const $form = $('#registerForm');
    const $username = $('#username');
    const $email = $('#email');
    const $password = $('#password');
    const $confirmPassword = $('#confirmPassword');

    if ($form.length) {
        $username.on('input', validarUsername);
        $email.on('input', validarEmail);
        $password.on('input', function () {
            validarPassword();
            actualizarBarraFuerza();
        });
        $confirmPassword.on('input', validarConfirmPassword);
        $form.on('submit', manejarEnvioRegistro);
    }

    $('#togglePassword').click(function () {
        const $icon = $(this).find('i');
        const esPassword = $password.attr('type') === 'password';
        $password.attr('type', esPassword ? 'text' : 'password');
        $icon.toggleClass('fa-eye fa-eye-slash');
    });

    function mostrarError(idCampo, mensaje) {
        $('#' + idCampo).addClass('is-invalid');
        $('#' + idCampo + 'Error').text(mensaje);
        return false;
    }

    function limpiarError(idCampo) {
        $('#' + idCampo).removeClass('is-invalid');
        $('#' + idCampo + 'Error').text('');
        return true;
    }

    function validarUsername() {
        const valor = $username.val().trim();
        if (!valor) return mostrarError('username', 'El nombre de usuario es requerido');
        return limpiarError('username');
    }

    function validarEmail() {
        const valor = $email.val().trim();
        const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!valor) return mostrarError('email', 'El correo electrónico es necesario');
        if (!regex.test(valor)) return mostrarError('email', 'Ingrese un correo electrónico válido');
        return limpiarError('email');
    }

    function validarPassword() {
        const valor = $password.val();
        if (!valor) return mostrarError('password', 'La contraseña es necesaria');
        if (valor.length < 6) return mostrarError('password', 'La contraseña debe tener al menos 6 caracteres');
        return limpiarError('password');
    }

    function validarConfirmPassword() {
        const valor = $confirmPassword.val();
        if (!valor) return mostrarError('confirmPassword', 'Confirme su contraseña');
        if ($password.val() !== valor) return mostrarError('confirmPassword', 'Las contraseñas no coinciden');
        return limpiarError('confirmPassword');
    }

    function validarFormulario() {
        return validarUsername() & validarEmail() & validarPassword() & validarConfirmPassword();
    }

    function actualizarBarraFuerza() {
        const password = $password.val();
        let fuerza = 0;
        if (password.length > 0) fuerza++;
        if (password.length >= 6) fuerza++;
        if (/[a-z]/.test(password)) fuerza++;
        if (/[A-Z]/.test(password)) fuerza++;
        if (/\d/.test(password)) fuerza++;
        if (/[^a-zA-Z0-9]/.test(password)) fuerza++;

        const porcentaje = (fuerza / 6) * 100;
        const $barra = $('#passwordStrength');
        let color = '#ff3860';
        if (fuerza > 2) color = '#ffdd57';
        if (fuerza > 4) color = '#23d160';

        $barra.css({ width: porcentaje + '%', background: color });
    }

    function manejarEnvioRegistro(e) {
        e.preventDefault();
        if (!validarFormulario()) return;

        const datos = {
            username: $username.val().trim(),
            email: $email.val().trim(),
            password: $password.val(),
            role: DEFAULT_ROLE
        };

        $.ajax({
            url: REGISTER_API_URL,
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(datos),
            success: function (respuesta) {
                alert(respuesta.message || '¡Registro exitoso! Ya puede iniciar sesión.');
                window.location.href = LOGIN_URL;
            },
            error: function (xhr) {
                const mensaje = xhr.responseJSON?.message || 'Error en el registro';
                if (mensaje.includes('username')) {
                    mostrarError('username', 'El usuario ya existe');
                } else if (mensaje.includes('email')) {
                    mostrarError('email', 'El email ya está registrado');
                } else {
                    alert(`Error: ${mensaje}`);
                }
            }
        });
    }
});
