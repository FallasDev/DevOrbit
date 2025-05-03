// url api cursos
const apiUrl = 'http://localhost:8080/api/courses'; 
const token = localStorage.getItem('jwtToken');
const container = document.getElementById('course-container');
const carouselContainer = document.getElementById('carousel-container');
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

    if (!response.ok) {
      throw new Error('Error autorizando el token');
    }

    return response.json()
  })
  .then(courses => {
    // vrea tarjetas de cursos
    courses.forEach((course, index) => {
      console.log(course);
      // creatarjeta del curso
      const col = document.createElement('div');
      col.className = 'col-md-4 mb-4';

      col.innerHTML = `
        <div class="card h-100 shadow-sm">
          <img src="${course.picture ? course.picture.url : 'default-image.jpg'}" class="card-img-top" alt="${course.title}">
          <div class="card-body">
            <h5 class="card-title">${course.title}</h5>
            <p class="card-text">${course.description}</p>
            <p class="text-muted">Precio: $${course.price}</p>
            <a href="/Frontend/course.html?courseId=${course.id_course}" class="btn btn-primary">Ver curso</a>
          </div>
        </div>
      `;

      container.appendChild(col);

      // slide del carrusel
      const carouselItem = document.createElement('div');
      carouselItem.className = `carousel-item ${index === 0 ? 'active' : ''}`;
      carouselItem.innerHTML = `
        <img src="${course.picture ? course.picture.url : 'default-image.jpg'}" class="d-block w-100" alt="${course.title}">
        <div class="carousel-caption d-none d-md-block">
          <h5>${course.title}</h5>
          <p>${course.description}</p>
          <p><strong>Precio: $${course.price}</strong></p>
        </div>
      `;
      carouselContainer.appendChild(carouselItem);

      // indicador del carrusel
      const indicator = document.createElement('button');
      indicator.type = 'button';
      indicator.setAttribute('data-bs-target', '#carouselCursos');
      indicator.setAttribute('data-bs-slide-to', index);
      indicator.className = index === 0 ? 'active' : '';
      indicator.setAttribute('aria-current', index === 0 ? 'true' : 'false');
      indicator.setAttribute('aria-label', `Slide ${index + 1}`);
      carouselIndicators.appendChild(indicator);
    });
  })
  .catch(error => {
    console.log(error);
    if (error.message === 'Error autorizando el token') {
      // Token expirado o no válido
      localStorage.removeItem('jwtToken');
      window.location.href = '/Frontend/components/login.html';
    }
    container.innerHTML = `<p class="text-danger">Error: ${error.message}</p>`;
  });
