const HOST = "https://devorbit-vk2z.onrender.com";
const TOKEN = localStorage.getItem("jwtToken");
const btnFinalTest = document.getElementById("btn-final-test");
const btnEditCourse = document.getElementById("btn-edit-course");
const btnDeleteCourse = document.getElementById("btn-delete-course");
const confirmDelete = document.getElementById("confirmDelete");
const cancelDelete = document.getElementById("cancelDelete");
const formUpdateCourse = document.getElementById("form-update-course");
const addModule = document.getElementById("btn-add-module");
let memoryList = [];

document.addEventListener("DOMContentLoaded", async () => {
  memoryList =
    (await getVideosByModuleId(TOKEN, sessionStorage.getItem("idModule"))) ||
    [];
  const urlParams = new URLSearchParams(window.location.search);
  const courseId = urlParams.get("courseId");
  console.log(courseId);
  const userInscription = await getUserInscription(courseId);

  console.log(userInscription);

  if (userInscription) {
    const btnBuyCourse = document.getElementById("btn-buy-course-or-");
    btnBuyCourse.textContent = "Ver en la biblioteca";
  }
});

document.addEventListener("submit", (ev) => {
  ev.preventDefault();
  const formData = new FormData(ev.target);
  const title = formData.get("title");
  const video = formData.get("video");
  const idModule = sessionStorage.getItem("idModule");

  upload_video(title, video, idModule);
});

document.addEventListener("DOMContentLoaded", async () => {
  const urlParams = new URLSearchParams(window.location.search);
  const courseId = urlParams.get("courseId");

  const courseData = await getCourse(courseId);
  const testData = await getTestByCourse(courseId);
  if (!testData) {
    btnFinalTest.style.display = "none";
  } else {
    btnFinalTest.style.display = "block";
  }
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
  if (!res.ok && res.status === 403) {
    return false;
  }
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

  if (!res.ok && res.status === 403) {
    // Token expirado o no válido
    localStorage.removeItem("jwtToken");
    window.location.href = "./components/login.html";
  }

  const data = await res.json();
  return data;
};

const loadHeader = async (data) => {
  const title = document.getElementById("course-title");
  const description = document.getElementById("course-description");
  const price = document.getElementById("course-price");
  const coursePicture = document.getElementById("course-picture");

  title.textContent = data.title;
  description.textContent = data.description;
  price.textContent = `$${data.price}`;
  coursePicture.src = data.picture.url;

  const currentUser = await getCurrentUser();

  if (currentUser.role === "ROLE_ADMIN") {
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
  const data = await res.json();
  return data;
};

const loadModules = async (data) => {
  const accordionFlushExample = document.getElementById(
    "accordionFlushExample"
  );

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
                  ${
                    (await checkUserIsAdmin(TOKEN))
                      ? `<button style='min-width: 110px' onclick='addVideoEvent(${item.id_module})' class='btn btn-success btn-sm btn-add-video fw-semibold'>Agregar Video</button>`
                      : ""
                  }

                  ${
                    (await checkUserIsAdmin(TOKEN))
                      ? `<button style='min-width: 110px; background-color: rgb(255, 127, 14); color: white;' onclick='updateModule(${item.course.id_course},${item.id_module})' class='btn btn-sm btn-add-video fw-semibold'>Editar Modulo</button>`
                      : ""
                  }
                  
                  ${
                    (await checkUserIsAdmin(TOKEN))
                      ? `<button style='min-width: 110px' onclick='deleteModule(${item.id_module})'  class='btn btn-danger btn-sm btn-add-video fw-semibold'>Eliminar</button>`
                      : ""
                  }
                  
                </h2>
                <div id="flush-collapse-${
                  item.id_module
                }" class="accordion-collapse collapse"
                    data-bs-parent="#accordionFlushExample">
                  <div class="accordion-body text-muted">
                  ${
                    videos && videos.length > 0
                      ? (
                          await Promise.all(
                            videos.map(
                              async (video) => `
                            <a class='text-reset text-decoration-none ${
                              (await getUserInscription(item.course.id_course))
                                ? ""
                                : "not-course-access"
                            }' href='./video.html?videoId=${
                                video.video_id
                              }&courseId=${item.course.id_course}'>
                              <div class="mb-3 d-flex align-items-center gap-3 p-2 border rounded bg-light shadow-sm">
                                <div class="d-flex align-items-center gap-2">
                                  <svg xmlns="http://www.w3.org/2000/svg" width="30px" height="30px" viewBox="0 0 24 24" fill="None">
                                    <path d="M16.6582 9.28638C18.098 10.1862 18.8178 10.6361 19.0647 11.2122C19.2803 11.7152 19.2803 12.2847 19.0647 12.7878C18.8178 13.3638 18.098 13.8137 16.6582 14.7136L9.896 18.94C8.29805 19.9387 7.49907 20.4381 6.83973 20.385C6.26501 20.3388 5.73818 20.0469 5.3944 19.584C5 19.053 5 18.1108 5 16.2264V7.77357C5 5.88919 5 4.94701 5.3944 4.41598C5.73818 3.9531 6.26501 3.66111 6.83973 3.6149C7.49907 3.5619 8.29805 4.06126 9.896 5.05998L16.6582 9.28638Z" stroke="#808080" stroke-width="2" stroke-linejoin="round"/>
                                  </svg>
                                  <p class="text-muted mb-0">
                                    ${String(
                                      Math.floor(video.duration_seg / 60)
                                    ).padStart(2, "0")}:${String(
                                video.duration_seg % 60
                              ).padStart(2, "0")}
                                  </p>
                                </div>
                                <h3 class="h6 d-flex align-items-center gap-3 fw-bold mt-2">
                                ${video.title}
                                ${
                                  !(await getUserInscription(
                                    item.course.id_course
                                  ))
                                    ? `<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" fill="#808080" height="15px" width="15px" version="1.1" id="Layer_1" viewBox="0 0 330 330" xml:space="preserve">
                                    <g id="XMLID_509_">
                                      <path id="XMLID_510_" d="M65,330h200c8.284,0,15-6.716,15-15V145c0-8.284-6.716-15-15-15h-15V85c0-46.869-38.131-85-85-85   S80,38.131,80,85v45H65c-8.284,0-15,6.716-15,15v170C50,323.284,56.716,330,65,330z M180,234.986V255c0,8.284-6.716,15-15,15   s-15-6.716-15-15v-20.014c-6.068-4.565-10-11.824-10-19.986c0-13.785,11.215-25,25-25s25,11.215,25,25   C190,223.162,186.068,230.421,180,234.986z M110,85c0-30.327,24.673-55,55-55s55,24.673,55,55v45H110V85z"/>
                                    </g>
                                    </svg>`
                                    : `<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" fill="#808080" height="15px" width="15px" version="1.1" id="Layer_1" viewBox="0 0 330 330" xml:space="preserve">
                                    <g id="XMLID_516_">
                                      <path id="XMLID_517_" d="M15,160c8.284,0,15-6.716,15-15V85c0-30.327,24.673-55,55-55c30.327,0,55,24.673,55,55v45h-25   c-8.284,0-15,6.716-15,15v170c0,8.284,6.716,15,15,15h200c8.284,0,15-6.716,15-15V145c0-8.284-6.716-15-15-15H170V85   c0-46.869-38.131-85-85-85S0,38.131,0,85v60C0,153.284,6.716,160,15,160z"/>
                                    </g>
                                    </svg>`
                                }
                                </h3>
                                </div>
                                </a>
                                <div class='my-3'>
                                  ${
                                    (await checkUserIsAdmin(TOKEN))
                                      ? ` <button style='min-width: 110px; background-color: rgb(255, 127, 14); color: white;' class="btn btn-light btn-sm fw-semibold" onclick="editVideo(${video.video_id})">Editar Video</button>`
                                      : ""
                                    }
                                  ${
                                    (await checkUserIsAdmin(TOKEN))
                                      ? ` <button style='min-width: 110px' class="btn btn-danger btn-sm fw-semibold" onclick="deleteVideo(${video.video_id})">Eliminar Video</button>`
                                      : ""
                                  }
                                  
                                </div>
                          `
                            )
                          )
                        ).join("")
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
    return await response.json();
  } catch (error) {
    console.log(error);
    return null;
  }
};

const checkUserIsAdmin = async (token) => {
  try {
    const response = await fetch(`${HOST}/api/videos/user/data`, {
      method: "GET",
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    return await response.json();
  } catch (error) {
    console.log(error);
    return "";
  }
};

document.addEventListener("DOMContentLoaded", async () => {
  const urlParams = new URLSearchParams(window.location.search);
  const courseId = urlParams.get("courseId");

  const btnBuyCourse = document.getElementById("btn-buy-course-or-");

  const inscription = await getUserInscription(courseId);
  if (!inscription) {
    btnBuyCourse.addEventListener("click", async () => {
      try {
        console.log(
          `Intentando crear un pago para el curso con ID: ${courseId}`
        );

        const response = await fetch(`${HOST}/api/payments/create`, {
          method: "POST",
          headers: {
            "Content-Type": "application/x-www-form-urlencoded",
          },
          body: new URLSearchParams({
            courseId: courseId,
            jwt: TOKEN,
            currency: "USD",
          }),
        });

        if (!response.ok) {
          console.error(
            `Error en la respuesta del servidor: ${response.status}`
          );
          alert(
            "Error al intentar crear el pago. Por favor, inténtalo nuevamente."
          );
          return;
        }

        const data = await response.json();

        if (data.status === "success") {
          console.log("Pago creado exitosamente. Redirigiendo a PayPal...");
          window.location.href = data.redirect_url;
        } else {
          console.error(`Error al crear el pago: ${data.message}`);
          alert("Error al crear el pago: " + data.message);
        }
      } catch (error) {
        console.error("Error al intentar crear el pago:", error);
        alert(
          "Ocurrió un error al intentar realizar el pago. Por favor, verifica tu conexión e inténtalo nuevamente."
        );
      }
    });

    return;
  }

  btnBuyCourse.addEventListener("click", async () => {
    const courseId = urlParams.get("courseId");
    window.location.href = `./library.html?courseId=${courseId}`;
  });
});

btnEditCourse.addEventListener("click", async () => {
  const urlParams = new URLSearchParams(window.location.search);
  const courseId = urlParams.get("courseId");

  const course = await getCourse(courseId);

  window.location.href = `./editCourse.html?courseId=${course.id_course}`;
});

btnDeleteCourse.addEventListener("click", () => {
  // Mostrar el modal
  deleteModal.style.display = "flex";
});

cancelDelete.addEventListener("click", () => {
  // Ocultar el modal si se cancela
  deleteModal.style.display = "none";
});

confirmDelete.addEventListener("click", async () => {
  const urlParams = new URLSearchParams(window.location.search);
  const courseId = urlParams.get("courseId");

  const courseModalMessage = document.getElementById("course-modal-message");

  const res = await fetch(`${HOST}/api/courses/${courseId}`, {
    method: "DELETE",
    headers: {
      Authorization: `Bearer ${TOKEN}`,
    },
  });

  if (res.ok) {
    alert("Curso eliminado correctamente");
    window.location.href = "./generalCursesStudent.html"; // Cambia esto por la URL de la página que desees redirigir
  } else {
    courseModalMessage.textContent =
      "El curso no se puede eliminar porque tiene usuarios inscritos.";
  }
  // Ocultar el modal después de confirmar la eliminación
});

addModule.addEventListener("click", async () => {
  const urlParams = new URLSearchParams(window.location.search);
  const courseId = urlParams.get("courseId");

  window.location.href = `./addModule.html?courseId=${courseId}`;
});

function upload_video(title, video, idModule) {
  const formData = new FormData();
  formData.append("title", title);
  formData.append("videoFile", video);
  formData.append("idModule", idModule);
  formData.append(
    "videoOrder",
    memoryList.map((item) => item.video_id)
  );

  const xhr = new XMLHttpRequest();

  const progressContainer = document.getElementById("progressContainer");
  const progressBar = document.getElementById("progressBar");
  progressContainer.style.display = "block";
  progressBar.style.width = "0%";
  progressBar.innerText = "Subiendo...";

  xhr.upload.addEventListener("progress", (event) => {
    if (event.lengthComputable) {
      const percent = Math.round((event.loaded / event.total) * 100);
      progressBar.style.width = percent + "%";
      changeProgressPercent(percent, progressBar);
    }
  });

  xhr.addEventListener("load", () => {
    if (xhr.status >= 200 && xhr.status < 300) {
      videoUploadSuccesfully(progressBar);
    } else {
      videoUploadFailed(progressBar);
    }
  });

  xhr.addEventListener("error", () => {
    progressBar.classList.add("bg-danger");
    progressBar.innerText = "Error de red";
    alert("Error de red.");
  });

  xhr.open("POST", `${HOST}/api/videos/upload`);

  xhr.setRequestHeader("Authorization", `Bearer ${TOKEN}`);

  xhr.send(formData);
}

const videoUploadSuccesfully = (progressBar) => {
  progressBar.style.width = "100%";
  progressBar.classList.remove("bg-danger");
  progressBar.classList.add("bg-success");
  progressBar.innerText = "¡Subido!";
  alert("Video subido exitosamente");
  location.reload();
};

const videoUploadFailed = (progressBar) => {
  progressBar.classList.add("bg-danger");
  progressBar.innerText = "Error al subir";
  alert("Error al subir el video.");
};

const addVideoEvent = async (idModule) => {
  sessionStorage.setItem("idModule", idModule);
  await loadVideosSortableList();
  document.getElementById("upload-video-box").style.visibility = "visible";
};

const closeUploadVideo = () => {
  document.getElementById("upload-video-box").style.visibility = "hidden";
};

const changeProgressPercent = (percent, progressBar) => {
  if (percent < 100) {
    progressBar.innerText = percent + "%";
  } else {
    progressBar.innerText = "Procesando...";
  }
};

const loadVideosSortableList = async () => {
  const list = document.getElementById("sortable-list-videos");
  let draggingItem = null;
  list.innerHTML = ""; // Limpiar la lista antes de cargar los videos

  const nowVideo = document.createElement("li");

  nowVideo.classList.add("sortable-item");
  nowVideo.setAttribute("draggable", true);
  nowVideo.textContent = "Video actual";

  const videos = await getVideosByModuleId(
    TOKEN,
    sessionStorage.getItem("idModule")
  );

  nowVideo.item = {
    video_id: 0,
  };

  list.appendChild(nowVideo);

  videos.forEach((item, index) => {
    const li = document.createElement("li");
    li.item = item;
    li.textContent = item.title;
    li.setAttribute("draggable", true);
    li.classList.add("sortable-item");
    list.appendChild(li);
  });

  list.addEventListener("dragstart", (e) => {
    draggingItem = e.target;
    e.target.classList.add("dragging");
  });

  list.addEventListener("dragend", (e) => {
    e.target.classList.remove("dragging");
    document
      .querySelectorAll(".sortable-item")
      .forEach((item) => item.classList.remove("over"));

    updateMemoryList();
    draggingItem = null;
  });

  list.addEventListener("dragover", (e) => {
    e.preventDefault();
    const draggingOverItem = getDragAfterElement(list, e.clientY);

    // Remove .over from all items
    document
      .querySelectorAll(".sortable-item")
      .forEach((item) => item.classList.remove("over"));

    if (draggingOverItem) {
      draggingOverItem.classList.add("over"); // Add .over to the hovered item
      list.insertBefore(draggingItem, draggingOverItem);
    } else {
      list.appendChild(draggingItem); // Append to the end if no item below
    }
  });

  function getDragAfterElement(container, y) {
    const draggableElements = [
      ...container.querySelectorAll(".sortable-item:not(.dragging)"),
    ];

    return draggableElements.reduce(
      (closest, child) => {
        const box = child.getBoundingClientRect();
        const offset = y - box.top - box.height / 2;
        if (offset < 0 && offset > closest.offset) {
          return { offset: offset, element: child };
        } else {
          return closest;
        }
      },
      { offset: Number.NEGATIVE_INFINITY }
    ).element;
  }

  function updateMemoryList() {
    memoryList.length = 0;
    console.log(memoryList);
    document.querySelectorAll(".sortable-item").forEach((element) => {
      memoryList.push(element.item);
    });
  }
};

const updateModule = async (idCourse, idModule) => {
  window.location.href = `./editModule.html?idCourse=${idCourse}&idModule=${idModule}`;
};

const deleteModule = async (idModule) => {
  const res = await fetch(`${HOST}/api/modules/${idModule}`, {
    method: "DELETE",
    headers: {
      Authorization: `Bearer ${TOKEN}`,
    },
  });

  if (res.ok) {
    alert("Modulo eliminado correctamente");
    location.reload();
  } else {
    alert("Error al eliminar el modulo");
  }
};

const getUserInscription = async (idCourse) => {
  const user = await getCurrentUser();

  if (!user) {
    alert("Error al obtener el usuario");
    return null;
  }

  const res = await fetch(
    `${HOST}/api/inscriptions/user/${user.idUser}/${idCourse}`,
    {
      method: "GET",
      headers: {
        Authorization: `Bearer ${TOKEN}`,
      },
    }
  );

  console.log(res);

  if (res.ok) {
    const data = await res.json();
    return data;
  } else {
    return null;
  }
};

const editVideo = async (videoId) => {
  const urlParams = new URLSearchParams(window.location.search);
  const courseId = urlParams.get("courseId");

  window.location.href = `./editVideo.html?videoId=${videoId}&courseId=${courseId}`;
};

const deleteVideo = async (videoId) => {
  const res = await fetch(`${HOST}/api/videos/${videoId}/delete`, {
    method: "DELETE",
    headers: {
      Authorization: `Bearer ${TOKEN}`,
    },
  });

  console.log(res);

  if (res.ok) {
    alert("Video eliminado correctamente");
    location.reload();
  } else {
    alert("Error al eliminar el video");
  }
}
