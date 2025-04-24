document.addEventListener('DOMContentLoaded', initializeRegistrationForm);

const REGISTER_API_URL = 'http://localhost:8080/auth/register';
const LOGIN_URL = '../components/login.html';
const DEFAULT_ROLE = "ROLE_USER";

const formElements = {
    form: document.getElementById('registerForm'),
    fields: {
        username: document.getElementById('username'),
        email: document.getElementById('email'),
        password: document.getElementById('password'),
        confirmPassword: document.getElementById('confirmPassword')
    }
};

function initializeRegistrationForm() {
    if (!formElements.form) return;
    
    setupEventListeners();
}

function setupEventListeners() {
    formElements.fields.username.addEventListener('input', validateUsername);
    formElements.fields.email.addEventListener('input', validateEmail);
    formElements.fields.password.addEventListener('input', validatePassword);
    formElements.fields.confirmPassword.addEventListener('input', validateConfirmPassword);
    
    formElements.form.addEventListener('submit', handleFormSubmit);
}

function validateUsername() {
    const username = formElements.fields.username.value.trim();
    if (!username) return showError('username', 'El nombre de usuario es requerido');
    return clearError('username');
}

function validateEmail() {
    const email = formElements.fields.email.value.trim();
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    
    if (!email) return showError('email', 'El correo electrónico es necesario');
    if (!emailRegex.test(email)) return showError('email', 'Ingrese un correo electrónico válido');
    return clearError('email');
}

function validatePassword() {
    const password = formElements.fields.password.value;
    if (!password) return showError('password', 'La contraseña es necesaria');
    if (password.length < 6) return showError('password', 'La contraseña debe tener al menos 6 caracteres');
    return clearError('password');
}

function validateConfirmPassword() {
    const confirmPassword = formElements.fields.confirmPassword.value;
    if (!confirmPassword) return showError('confirmPassword', 'Confirme su contraseña');
    if (formElements.fields.password.value !== confirmPassword) {
        return showError('confirmPassword', 'Las contraseñas no coinciden');
    }
    return clearError('confirmPassword');
}

function validateForm() {
    return [
        validateUsername(),
        validateEmail(),
        validatePassword(),
        validateConfirmPassword()
    ].every(validation => validation === true);
}

async function handleFormSubmit(e) {
    e.preventDefault();
    
    if (!validateForm()) return;
    
    try {
        const userData = prepareUserData();
        const response = await sendRegistrationRequest(userData);
        
        if (response.ok) {
            handleRegistrationSuccess(response);
        } else {
            await handleRegistrationError(response);
        }
    } catch (error) {
        handleRegistrationException(error);
    }
}

function showError(fieldId, message) {
    const field = document.getElementById(fieldId);
    const errorElement = document.getElementById(`${fieldId}Error`);
    
    field.classList.add('is-invalid');
    errorElement.textContent = message;
    return false;
}

function clearError(fieldId) {
    const field = document.getElementById(fieldId);
    const errorElement = document.getElementById(`${fieldId}Error`);
    
    field.classList.remove('is-invalid');
    errorElement.textContent = '';
    return true;
}

function prepareUserData() {
    return {
        username: formElements.fields.username.value.trim(),
        email: formElements.fields.email.value.trim(),
        password: formElements.fields.password.value,
        role: DEFAULT_ROLE
    };
}

async function sendRegistrationRequest(userData) {
    return await fetch(REGISTER_API_URL, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(userData)
    });
}

async function parseResponse(response) {
    const contentType = response.headers.get('content-type');
    if (contentType && contentType.includes('application/json')) {
        return await response.json();
    }
    return { message: await response.text() };
}

async function handleRegistrationSuccess(response) {
    const responseData = await parseResponse(response);
    alert(responseData.message || '¡Registro exitoso! Ya puede iniciar sesión con su nueva cuenta');
    window.location.href = LOGIN_URL;
}

async function handleRegistrationError(response) {
    const responseData = await parseResponse(response);
    
    if (responseData.message) {
        if (responseData.message.includes("username")) {
            throw { errors: { username: "El usuario ya existe" } };
        } else if (responseData.message.includes("email")) {
            throw { errors: { email: "El email ya está registrado" } };
        }
    }
    throw new Error(responseData.message || 'Error en el registro');
}

function handleRegistrationException(error) {
    console.error('Error:', error);
    
    if (error.errors) {
        Object.keys(error.errors).forEach(field => {
            showError(field, error.errors[field]);
        });
    } else {
        alert(`Error: ${error.message || 'Error al registrar el usuario'}`);
    }
}