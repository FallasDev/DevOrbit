const API_URL = 'http://localhost:8080/api/user/me';
let token = localStorage.getItem('jwtToken'); 

const elements = {
    modal: document.getElementById('profileModal'),
    form: document.getElementById('profileForm'),
    usernameInput: document.getElementById('username'),
    emailInput: document.getElementById('email'),
    saveBtn: document.getElementById('saveProfileBtn'),
    deleteBtn: document.getElementById('deleteAccountBtn'),
    successAlert: document.getElementById('successAlert'),
    errorAlert: document.getElementById('errorAlert')
};

let profileModal;

function initialize() {
    checkAuthentication();
    initializeModal();
    setupEventListeners();
    loadInitialData();
}

function checkAuthentication() {
    if (!token) {
        alert("No estás autenticado. Serás redirigido al login.");
        window.location.href = "login.html";
    }
}

function initializeModal() {
    if (elements.modal) {
        profileModal = new bootstrap.Modal(elements.modal);
    }
}

function setupEventListeners() {
    document.addEventListener('DOMContentLoaded', () => {
        if (!token) return;

        if (elements.saveBtn) {
            elements.saveBtn.addEventListener('click', actualizarPerfil);
        }
        
        if (elements.form) {
            elements.form.addEventListener('submit', actualizarPerfil);
        }
        
        if (elements.deleteBtn) {
            elements.deleteBtn.addEventListener('click', eliminarCuenta);
        }

        setupLogoutLink();
        setupModalEvents();
    });
}

function setupLogoutLink() {
    const logoutLink = document.getElementById('logoutLink');
    if (logoutLink) {
        logoutLink.addEventListener('click', (e) => {
            e.preventDefault();
            localStorage.removeItem('jwtToken');
            window.location.href = '../components/login.html';
        });
    }
}

function setupModalEvents() {
    if (elements.modal) {
        elements.modal.addEventListener('shown.bs.modal', cargarPerfil);
        elements.modal.addEventListener('hidden.bs.modal', () => {
            if (elements.successAlert) elements.successAlert.classList.add('d-none');
            if (elements.errorAlert) elements.errorAlert.classList.add('d-none');
        });
    }
}

function loadInitialData() {
}

function showAlert(alertElement, message, isError = false) {
    if (!alertElement) return;
    
    alertElement.textContent = message;
    alertElement.className = `alert ${isError ? 'alert-danger' : 'alert-success'}`;
    alertElement.classList.remove('d-none');
    setTimeout(() => alertElement.classList.add('d-none'), 3000);
}

function handleInvalidToken() {
    localStorage.removeItem('jwtToken');
    window.location.href = "login.html";
}

function handleNetworkError(error) {
    console.error("Error de red:", error);
}

async function cargarPerfil() {
    try {
        const response = await fetch(API_URL, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) {
            if (response.status === 401 || response.status === 403) {
                handleInvalidToken();
                return;
            }
            const errorData = await response.text();
            throw new Error(errorData || `Error HTTP: ${response.status}`);
        }

        const userData = await response.json();
        elements.usernameInput.value = userData.username || '';
        elements.emailInput.value = userData.email || '';

    } catch (error) {
        console.error("Error al cargar perfil:", error);
        if (error.name === 'TypeError' && error.message.includes('network')) {
            handleNetworkError(error);
        } 
    }
}

async function actualizarPerfil(event) {
    if (event) {
        event.preventDefault();
    }
    
    if (elements.form && !elements.form.checkValidity()) {
        elements.form.reportValidity();
        return;
    }
    
    setButtonLoadingState(elements.saveBtn, true, 'Guardando...');

    try {
        const response = await fetch(API_URL, {
            method: 'PUT',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                username: elements.usernameInput.value.trim(),
                email: elements.emailInput.value.trim()
            })
        });

        setButtonLoadingState(elements.saveBtn, false, 'Guardar Cambios');

        if (!response.ok) {
            if (response.status === 401 || response.status === 403) {
                handleInvalidToken();
                return;
            }
            const errorData = await response.text();
            throw new Error(errorData || `Error HTTP: ${response.status}`);
        }

        const result = await response.json();
        
        if (result.token) {
            token = result.token;
            localStorage.setItem('jwtToken', result.token); 
        }

        showAlert(elements.successAlert, "Perfil actualizado correctamente. Por motivos de seguridad, es necesario iniciar sesión nuevamente.");
        
        setTimeout(() => {
            localStorage.removeItem('jwtToken');
            window.location.href = "login.html";
        }, 3000);

    } catch (error) {
        if (error.name === 'TypeError' && error.message.includes('network')) {
            handleNetworkError(error);
        }
    }
}

async function eliminarCuenta() {
    if (!confirm("¿Estás seguro de que quieres eliminar tu cuenta? Esta acción no es reversible.")) return;
    
    setButtonLoadingState(elements.deleteBtn, true, 'Eliminando...');
    
    try {
        const response = await fetch(API_URL, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (!response.ok) {
            setButtonLoadingState(elements.deleteBtn, false, 'Eliminar Cuenta');
            
            if (response.status === 401 || response.status === 403) {
                handleInvalidToken();
                return;
            }
            const errorData = await response.text();
            throw new Error(errorData || `Error HTTP: ${response.status}`);
        }

        localStorage.removeItem('jwtToken');
        window.location.href = "login.html";

    } catch (error) {
        setButtonLoadingState(elements.deleteBtn, false, 'Eliminar Cuenta');
        
        if (error.name === 'TypeError' && error.message.includes('network')) {
            handleNetworkError(error);
        }
    }
}

function setButtonLoadingState(button, isLoading, text) {
    if (!button) return;
    
    button.disabled = isLoading;
    button.innerHTML = isLoading 
        ? '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> ' + text 
        : text;
}

initialize();