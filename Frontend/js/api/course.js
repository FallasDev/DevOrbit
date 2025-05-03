const HOST = "http://localhost:8080";
const TOKEN =
  "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZSI6IlJPTEVfVVNFUiIsImV4cCI6MTc0NjI0MTg0OCwiaWF0IjoxNzQ2MjM4MjQ4fQ.C5b3Kzk_c6Lr3h_iumxNWmxlU9huO6ev8aUAu_la22c";
const btnFinalTest = document.getElementById("btn-final-test");

document.addEventListener("DOMContentLoaded", async () => {
  const urlParams = new URLSearchParams(window.location.search);
  const courseId = urlParams.get("courseId");

  const courseData = await getCourse(courseId);
  const testData = await getTestByCourse(courseId);
  const modulesData = await getModulesByCourse(courseId);
  loadHeader(courseData);
  loadModules(modulesData);
  sessionStorage.setItem("test_id", JSON.stringify(testData.test_id));
});

btnFinalTest.addEventListener("click", async () => {
  const testId = JSON.parse(sessionStorage.getItem("test_id"));
  window.location.href = `test.html?id=${testId}`; // Cambia esto por el ID del curso que deseas obtener
});

const getTestByCourse = async (id) => {
  const res = await fetch(`${HOST}/api/tests/course/${id}`, {
    method: "GET",
    headers: {
      Authorization: `Bearer ${TOKEN}`,
    },
  });
  console.log(res);
  const data = await res.json();
  return data;
};

const getCourse = async (id) => {
  const res = await fetch(`${HOST}/api/courses/${id}`, {
    method: "GET",
    headers: {
      Authorization: `Bearer ${TOKEN}`,
    },
  });

  console.log(res);
  const data = await res.json();
  return data;
};

const loadHeader = async (data) => {
  const title = document.getElementById("course-title");
  const description = document.getElementById("course-description");
  const price = document.getElementById("course-price");

  title.textContent = data.title;
  description.textContent = data.description;
  price.textContent = `$${data.price}`;

  const currentUser = await getCurrentUser();

  if (currentUser.role === "ROLE_ADMIN") {
    console.log(currentUser);
    document.getElementById("btn-edit-course").style.display = "block";
    document.getElementById("btn-delete-course").style.display = "block";
    document.getElementById("btn-add-module").style.display = "block";
  }
};

const getCurrentUser = async () => {
  const res = await fetch(`${HOST}/api/user/me`, {
    method: "GET",
    headers: {
      Authorization: `Bearer ${TOKEN}`,
    },
  });

  const data = await res.json();
  return data;
};

const getModulesByCourse = async (id) => {
  const res = await fetch(`${HOST}/api/modules/course/${id}`, {
    method: "GET",
    headers: {
      Authorization: `Bearer ${TOKEN}`,
    },
  });
  console.log(res);
  const data = await res.json();
  console.log(data);
  return data;
};

const loadModules = async (data) => {
  const accordionFlushExample = document.getElementById(
    "accordionFlushExample"
  );

  console.log(data);

  for (const item of data) {
    const videos = await getVideosByModuleId(TOKEN, item.id_module);
    if (JSON.parse(sessionStorage.getItem("videos")) === null) {
      sessionStorage.setItem("videos", JSON.stringify([]));
    }

    for (const video of videos) {
      if (
        JSON.parse(sessionStorage.getItem("videos")).find(
          (v) => v.video_id === video.video_id
        )
      ) {
        continue;
      }
      sessionStorage.setItem(
        "videos",
        JSON.stringify([...JSON.parse(sessionStorage.getItem("videos")), video])
      );
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
                  ${await checkUserIsAdmin(TOKEN) ? `<button style='min-width: 110px' onclick='addVideoEvent(${item.id_module})' class='btn btn-success btn-sm btn-add-video fw-semibold'>Agregar Video</button>` : ''}
                  ${await checkUserIsAdmin(TOKEN) ? `<button style='background-color: #ff7f0e; color: white; min-width: 110px' onclick='addTestEvent(${item.id_module})' class='btn btn-sm btn-add-video fw-semibold'>Editar Modulo</button>` : ''}
                  ${await checkUserIsAdmin(TOKEN) ? `<button style='min-width: 125px;' onclick='addTestEvent(${item.id_module})' class='btn btn-danger btn-sm btn-add-video fw-semibold'>Eliminar Modulo</button>` : ''}
                </h2>
                <div id="flush-collapse-${
                  item.id_module
                }" class="accordion-collapse collapse"
                    data-bs-parent="#accordionFlushExample">
                  <div class="accordion-body text-muted">
                    ${
                      videos && videos.length > 0
                        ? videos
                            .map(
                              (video) => `
                          <a class='text-reset text-decoration-none' href='/Frontend/video.html?videoId=${
                            video.video_id
                          }'>
                            <div class="mb-3 d-flex align-items-center gap-3 p-2 border rounded bg-light shadow-sm">
                            <div class="d-flex align-items-center gap-2">
                            <svg xmlns="http://www.w3.org/2000/svg" width="30px" height="30px" viewBox="0 0 24 24" fill="None">
                                <path d="M16.6582 9.28638C18.098 10.1862 18.8178 10.6361 19.0647 11.2122C19.2803 11.7152 19.2803 12.2847 19.0647 12.7878C18.8178 13.3638 18.098 13.8137 16.6582 14.7136L9.896 18.94C8.29805 19.9387 7.49907 20.4381 6.83973 20.385C6.26501 20.3388 5.73818 20.0469 5.3944 19.584C5 19.053 5 18.1108 5 16.2264V7.77357C5 5.88919 5 4.94701 5.3944 4.41598C5.73818 3.9531 6.26501 3.66111 6.83973 3.6149C7.49907 3.5619 8.29805 4.06126 9.896 5.05998L16.6582 9.28638Z" stroke="#808080" stroke-width="2" stroke-linejoin="round"/>
                            </svg>
                            <p class="text-muted mb-0">
                            ${String(
                                Math.floor(video.duration_seg / 60)
                            ).padStart(2, "0")}:${String(video.duration_seg % 60).padStart(2, "0")}
                            </p>
                            </div>
                            <h3 class="h6 fw-bold mt-2">${video.title}</h3>
                               
                            
                              
                            </div>
                          </a>
                        `
                            )
                            .join("")
                        : "No hay videos"
                    }
                  </div>
                
                </div>
              </div>
            `;
  }
};

const getVideosByModuleId = async (token, id) => {
  try {
    const response = await fetch(`${HOST}/api/videos/module/${id}`, {
      method: "GET",
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    console.log(response);
    return await response.json();
  } catch (error) {
    console.log(error);
    return null;
  }
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
  
