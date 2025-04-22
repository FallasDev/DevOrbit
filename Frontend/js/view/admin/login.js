$(document).ready(function() {
    const existingToken = localStorage.getItem('jwtToken');
    if (existingToken) {
        window.location.href = 'dashboard.html';
        return;
    }

    $('#loginForm').submit(function(e) {
        e.preventDefault();
        
        const username = $('#username').val().trim();
        const password = $('#password').val();
        
        const submitBtn = $(this).find('button[type="submit"]');
        const originalBtnText = submitBtn.text();
        submitBtn.prop('disabled', true).html('<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Iniciando sesión...');
        
        $.ajax({
            url: 'http://localhost:8080/auth/login',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ username, password }),
            success: function(response) {
                if (response && response.token) {
                    
                    localStorage.setItem('jwtToken', response.token);
                    
                    if (response.user) {
                        localStorage.setItem('currentUser', JSON.stringify(response.user));
                    }
                    
                    window.location.replace('dashboard.html'); 
                } else {
                    submitBtn.prop('disabled', false).text(originalBtnText);
                }
            },
            error: function(xhr, status, error) {
                console.error('Error en login:', xhr.status, xhr.responseText);
                let errorMsg = 'Error en el login';
                
                if (xhr.responseJSON && xhr.responseJSON.message) {
                    errorMsg += ': ' + xhr.responseJSON.message;
                } else if (xhr.status === 0) {
                    errorMsg += ': No se pudo conectar con el servidor';
                } else {
                    errorMsg += ` (${xhr.status}): ${error || xhr.statusText}`;
                }
                
                alert(errorMsg);
                submitBtn.prop('disabled', false).text(originalBtnText);
            }
        });
    });
});