$(document).ready(initializeLogin);

const AUTH_API_URL = 'http://localhost:8080/auth/login';
const DASHBOARD_URL = '../components/dashboard.html';

function initializeLogin() {
    checkExistingToken();
    setupLoginForm();
}

function checkExistingToken() {
    const existingToken = localStorage.getItem('jwtToken');
    if (existingToken) {
        window.location.href = DASHBOARD_URL;
    }
}

function setupLoginForm() {
    $('#loginForm').submit(handleLoginSubmit);
}

function handleLoginSubmit(e) {
    e.preventDefault();
    
    const credentials = getFormCredentials();
    const submitBtn = getSubmitButton();
    const originalBtnText = submitBtn.text();
    
    setButtonLoadingState(submitBtn, true, 'Iniciando sesión...');
    
    sendLoginRequest(credentials, submitBtn, originalBtnText);
}

function getFormCredentials() {
    return {
        username: $('#username').val().trim(),
        password: $('#password').val()
    };
}

function getSubmitButton() {
    return $('#loginForm').find('button[type="submit"]');
}

function setButtonLoadingState(button, isLoading, loadingText) {
    if (isLoading) {
        button.prop('disabled', true)
              .html(`<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> ${loadingText}`);
    } else {
        button.prop('disabled', false)
              .text(loadingText);
    }
}

function sendLoginRequest(credentials, submitBtn, originalBtnText) {
    $.ajax({
        url: AUTH_API_URL,
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(credentials),
        success: (response) => handleLoginSuccess(response, submitBtn, originalBtnText),
        error: (xhr, status, error) => handleLoginError(xhr, status, error, submitBtn, originalBtnText)
    });
}

function handleLoginSuccess(response, submitBtn, originalBtnText) {
    if (response && response.token) {
        storeAuthData(response);
        redirectToDashboard();
    } else {
        resetSubmitButton(submitBtn, originalBtnText);
    }
}

function handleLoginError(xhr, status, error, submitBtn, originalBtnText) {
    console.error('Error en login:', xhr.status, xhr.responseText);
    showLoginError(xhr, error);
    resetSubmitButton(submitBtn, originalBtnText);
}

function storeAuthData(response) {
    localStorage.setItem('jwtToken', response.token);
    
    if (response.user) {
        localStorage.setItem('currentUser', JSON.stringify(response.user));
    }
}

function redirectToDashboard() {
    window.location.replace(DASHBOARD_URL);
}

function resetSubmitButton(submitBtn, originalBtnText) {
    setButtonLoadingState(submitBtn, false, originalBtnText);
}

$(document).ready(function () {
    $('#togglePassword').click(function () {
        const password = $('#password');
        const icon = $(this).find('i');

        if (password.attr('type') === 'password') {
            password.attr('type', 'text');
            icon.removeClass('fa-eye').addClass('fa-eye-slash');
        } else {
            password.attr('type', 'password');
            icon.removeClass('fa-eye-slash').addClass('fa-eye');
        }
    });
});

function showLoginError(xhr, error) {
    let errorMsg = 'Error en el login';
    
    if (xhr.responseJSON && xhr.responseJSON.message) {
        errorMsg += ': ' + xhr.responseJSON.message;
    } else if (xhr.status === 0) {
        errorMsg += ': No se pudo conectar con el servidor';
    } else {
        errorMsg += ` (${xhr.status}): ${error || xhr.statusText}`;
    }
    
    alert(errorMsg);
}