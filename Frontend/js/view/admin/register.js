document.addEventListener('DOMContentLoaded', function() {

    const registerForm = document.getElementById('registerForm');
    const passwordField = document.getElementById('password');
    const confirmPasswordField = document.getElementById('confirmPassword');

   
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
    
    function validateUsername() {
        const username = document.getElementById('username').value.trim();
        if (!username) return showError('username', 'El nombre de usuario es requerido');
        return clearError('username');
    }
    
    function validateEmail() {
        const email = document.getElementById('email').value.trim();
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        
        if (!email) return showError('email', 'El correo electrónico es necesario');
        if (!emailRegex.test(email)) return showError('email', 'Ingrese un correo electrónico válido');
        return clearError('email');
    }
    
    function validatePassword() {
        const password = passwordField.value;
        if (!password) return showError('password', 'La contraseña es necesaria');
        if (password.length < 6) return showError('password', 'La contraseña debe tener al menos 6 valores para que sea valida');
        return clearError('password');
    }
    
    function validateConfirmPassword() {
        const confirmPassword = confirmPasswordField.value;
        if (!confirmPassword) return showError('confirmPassword', 'Confirme su contraseña');
        if (passwordField.value !== confirmPasswordField.value) {
            return showError('confirmPassword', 'Las contraseñas no coinciden');
        }
        return clearError('confirmPassword');
    }
    
    function validateForm() {
        let isValid = true;
        isValid = validateUsername() && isValid;
        isValid = validateEmail() && isValid;
        isValid = validatePassword() && isValid;
        isValid = validateConfirmPassword() && isValid;
        return isValid;
    }
    
    document.getElementById('username').addEventListener('input', validateUsername);
    document.getElementById('email').addEventListener('input', validateEmail);
    passwordField.addEventListener('input', validatePassword);
    confirmPasswordField.addEventListener('input', validateConfirmPassword);
    
    registerForm.addEventListener('submit', async function(e) {
        e.preventDefault();
        
        if (validateForm()) {
            try {
                const userData = {
                    username: document.getElementById('username').value.trim(),
                    email: document.getElementById('email').value.trim(),
                    password: passwordField.value,
                    role: "ROLE_USER"
                };
                
                const response = await fetch('http://localhost:8080/auth/register', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(userData)
                });
                
                let responseData;
                const contentType = response.headers.get('content-type');
                if (contentType && contentType.includes('application/json')) {
                    responseData = await response.json();
                } else {
                    const text = await response.text();
                    responseData = { message: text };
                }
                
                if (!response.ok) {
                    if (responseData.message) {
                        if (responseData.message.includes("username")) {
                            throw { errors: { username: "El usuario ya existe" } };
                        } else if (responseData.message.includes("email")) {
                            throw { errors: { email: "El email ya está registrado" } };
                        } else {
                            throw new Error(responseData.message || 'Error en el registro');
                        }
                    }
                    throw responseData;
                }
                
                alert(responseData.message || '¡Registro exitoso! Ya puede iniciar sesión con su nueva cuenta');
                window.location.href = 'login.html';
                
            } catch (error) {
                console.error('Error:', error);
                
                if (error.errors) {
                    Object.keys(error.errors).forEach(field => {
                        showError(field, error.errors[field]);
                    });
                } else {
                    alert(`Error: ${error.message || 'Error al registrar el usuario'}`);
                }
            }
        }
    });
});