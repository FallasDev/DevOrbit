// url api cursos
const apiUrl = 'http://localhost:8080/api/courses';
const token = localStorage.getItem('jwtToken');
const container = document.getElementById('course-container');
const carouselInner = document.getElementById('carousel-inner');
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
    return response.json();
  })
  .then(courses => {
    courses.forEach((course, index) => {
      const imgSrc = course.picture?.url
        ? course.picture.url
        : "https://via.placeholder.com/1200x400?text=Sin+imagen";



      const col = document.createElement('div');
      col.className = 'col-md-4 mb-4';
      col.innerHTML = `
            <div class="card h-100 shadow-sm">
                <img src="${imgSrc}" class="card-img-top" alt="${course.title}">
                <div class="card-body">
                    <h5 class="card-title">${course.title}</h5>
                    <p class="card-text">${course.description}</p>
                    <p class="text-muted">Precio: $${course.price}</p>
                    <a href="/Frontend/course.html?courseId=${course.id_course}" class="btn btn-primary">Ver curso</a>
                </div>
            </div>
        `;
      container.appendChild(col);



      const carouselItem = document.createElement('div');
      carouselItem.className = `carousel-item ${index === 0 ? 'active' : ''}`;
      carouselItem.innerHTML = `

      <img src="${imgSrc}" class="d-block w-100" alt="${course.title}">


            <div class="carousel-caption d-none d-md-block bg-dark bg-opacity-50 rounded p-2">
                <h5>${course.title}</h5>
                <p>${course.description}</p>
                <p><strong>Precio: $${course.price}</strong></p>
            </div>
        `;
      carouselInner.appendChild(carouselItem);

      const indicator = document.createElement('button');
      indicator.type = 'button';
      indicator.setAttribute('data-bs-target', '#carouselCursos');
      indicator.setAttribute('data-bs-slide-to', index);
      indicator.className = index === 0 ? 'active' : '';
      indicator.setAttribute('aria-current', index === 0 ? 'true' : 'false');
      indicator.setAttribute('aria-label', `Slide ${index + 1}`);
      carouselIndicators.appendChild(indicator);
    });
  }).catch(error => {
    console.log(error);
    if (error.message === 'Error autorizando el token') {
      localStorage.removeItem('jwtToken');
      window.location.href = '/Frontend/components/login.html';
    }
    container.innerHTML = `<p class="text-danger">Error: ${error.message}</p>`;
  });
