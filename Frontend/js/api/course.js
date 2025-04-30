// const HOST = "http://localhost:8080";
// const TOKEN =
//   "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGUiOiJST0xFX0FETUlOIiwiZXhwIjoxNzQ1NDkwNzcyLCJpYXQiOjE3NDU0ODcxNzJ9.2e85dwoXfJc-oON54SvCfE1kdE1nmbZOaBleN2MoA4c";

// import { getVideosByCourseId } from "./Video";

document.addEventListener("DOMContentLoaded", () => {
  getCourse(8, TOKEN);
});

const getCourse = (id, token) => {
  fetch(`${HOST}/modules/course/${id}`, {
    method: "GET",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
  })
    .then((response) => {
      
      if (!response.ok) {
        throw new Error("No autorizado");
      }
      return response.json();
    })
    .then(async (data) => {
      const accordionFlushExample = document.getElementById(
        "accordionFlushExample"
      );

        console.log(data);

        if (Array.isArray(data)) {
          for (const item of data) {
            const videos = await getVideosByModuleId(token, item.id_module);
            if (JSON.parse(sessionStorage.getItem("videos")) === null) {
              sessionStorage.setItem("videos", JSON.stringify([]));
            }
            // if (JSON.parse(sessionStorage.getItem("videos")).contains()) {

            for (const video of videos) {
              if (JSON.parse(sessionStorage.getItem("videos")).find(v => v.video_id === video.video_id)) {
                continue;
              }
              sessionStorage.setItem("videos", JSON.stringify([...JSON.parse(sessionStorage.getItem("videos")),video]));
            }
            
            accordionFlushExample.innerHTML += `
              <div class="accordion-item border-0 mb-2 rounded shadow-sm">
                <h2 class="accordion-header d-flex gap-4 justify-content-center align-items-center p-2">
                  <button class="accordion-button collapsed bg-white d fw-semibold text-dark display-6"
                          type="button"
                          data-bs-toggle="collapse"
                          data-bs-target="#flush-collapse-${item.id_module}"
                          aria-expanded="false"
                          aria-controls="flush-collapse-${item.id_module}"
                      style='font-family: "JetBrains Mono", monospace;'>
                    ${item.title}
                  </button>
                  ${await checkUserIsAdmin(token) && `<button onclick='addVideoEvent(${item.id_module})' class='btn-add-video fw-semibold' style='max-height: 40px;'>Agregar</button>`}
                </h2>
                <div id="flush-collapse-${item.id_module}" class="accordion-collapse collapse"
                    data-bs-parent="#accordionFlushExample">
                  <div class="accordion-body text-muted">
                    ${
                      videos && videos.length > 0 
                        ? videos.map(video => `
                          <a class='text-reset text-decoration-none' href='video.html?videoId=${video.video_id}'>
                            <div class="mb-3 p-2 border rounded bg-light shadow-sm">
                              <video muted autoplay class="w-100 rounded" style="max-height: 200px; object-fit: cover;">
                                <source src="${video.url}" type="video/mp4">
                                Tu navegador no soporta la reproducción de video.
                              </video>
                              <h3 class="h6 fw-bold mt-2">${video.title}</h3>
                              <p class="text-muted mb-0">Duración: ${Math.floor(video.duration_seg / 60)} min ${video.duration_seg % 60} seg</p>
                              <small class="text-secondary">Publicado: ${new Date(video.createdAt).toLocaleDateString()}</small>
                            </div>
                          </a>
                        `).join("")
                        : "No hay videos"
                    }
                  </div>
                
                </div>
              </div>
            `;
            
          }
        }

        
        
    })
    .catch((err) => {
      console.error(err);
      if (err.message === "No autorizado") {
        window.location.href = "login.html";
      }
    });
};

const checkUserIsAdmin = async (token) => {

  try {
    const response = await fetch(`${HOST}/api/videos/admin/check`,{
      method: "GET",
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return await response.json();
  } catch (error){
    console.log(error);
    return "";
  }

}

