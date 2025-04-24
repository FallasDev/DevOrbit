const API_URL = 'http://localhost:8080/api/user/me';
let token = localStorage.getItem('jwtToken'); 

if (!token) {
    alert("No estás autenticado. Serás redirigido al login.");
    window.location.href = "login.html";
}

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
if (elements.modal) {
    profileModal = new bootstrap.Modal(elements.modal);
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
    
    elements.saveBtn.disabled = true;
    elements.saveBtn.innerHTML = '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Guardando...';

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

        elements.saveBtn.disabled = false;
        elements.saveBtn.textContent = 'Guardar Cambios';

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
    
    elements.deleteBtn.disabled = true;
    elements.deleteBtn.innerHTML = '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Eliminando...';
    
    try {
        const response = await fetch(API_URL, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        // Restaurar botón en caso de error
        if (!response.ok) {
            elements.deleteBtn.disabled = false;
            elements.deleteBtn.textContent = 'Eliminar Cuenta';
            
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
        elements.deleteBtn.disabled = false;
        elements.deleteBtn.textContent = 'Eliminar Cuenta';
        
        if (error.name === 'TypeError' && error.message.includes('network')) {
            handleNetworkError(error);
        }
    }
}

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

    const logoutLink = document.getElementById('logoutLink');
    if (logoutLink) {
        logoutLink.addEventListener('click', (e) => {
            e.preventDefault();
            localStorage.removeItem('jwtToken');
            window.location.href = 'login.html';
        });
    }
    
    if (elements.modal) {
        elements.modal.addEventListener('show.bs.modal', cargarPerfil);
        elements.modal.addEventListener('hidden.bs.modal', () => {
            if (elements.successAlert) elements.successAlert.classList.add('d-none');
            if (elements.errorAlert) elements.errorAlert.classList.add('d-none');
        });

        
    }

    
    
    const isProfilePage = document.querySelector('#profileModal, #profileForm');
    if (isProfilePage) {
        cargarPerfil();
    }
});

