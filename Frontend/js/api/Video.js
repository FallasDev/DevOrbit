const HOST = "http://localhost:8080";
const TOKEN = localStorage.getItem('jwtToken');

document.addEventListener("DOMContentLoaded", () => {
  const params = new URLSearchParams(window.location.search);
  const id = parseInt(params.get("videoId"));
  console.log(id);
  if (!id) {
    window.location.href = "video.html?videoId=1";
  }

  getVideoById(id, TOKEN);
});

document.addEventListener("submit", (ev) => {
  ev.preventDefault();
  const formData = new FormData(ev.target);
  const title = formData.get("title");
  const video = formData.get("video");
  const idModule = sessionStorage.getItem("idModule");

  upload_video(title, video, idModule);
});

function upload_video(title, video, idModule) {
  const formData = new FormData();
  formData.append("title", title);
  formData.append("videoFile", video);
  formData.append("idModule", idModule);
  formData.append("videoOrder", memoryList.map(item => item.video_id));

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

const changeProgressPercent = (percent, progressBar) => {
  if (percent < 100) {
    progressBar.innerText = percent + "%";
  } else {
    progressBar.innerText = "Procesando...";
  }
};

const videoUploadSuccesfully = (progressBar) => {
  progressBar.style.width = "100%";
  progressBar.classList.remove("bg-danger");
  progressBar.classList.add("bg-success");
  progressBar.innerText = "¡Subido!";
  alert("Video subido exitosamente");
};

const videoUploadFailed = (progressBar) => {
  progressBar.classList.add("bg-danger");
  progressBar.innerText = "Error al subir";
  alert("Error al subir el video.");
};

const getVideoById = (id, token) => {
  fetch(`${HOST}/api/videos/${id}`, {
    method: "GET",
    headers: {
      Authorization: `Bearer ${token}`,
    },
  })
    .then((response) => response.json())
    .then((data) => {
      const videoContainer = document.getElementById("video-container");
      const video = document.getElementById("video");
      const title = document.getElementById("videoTitle");
      const createdAt = document.getElementById("video-created-at");

      const videoContent = document.getElementById("video-content");

      console.log(data);

      video.src = data.url;
      videoContainer.style.width = window.innerWidth / 1.8 + "px";
      video.classList.add("rounded-2");
      title.innerText = data.title;

      sessionStorage.setItem("nowVideo", data.videoOrder);

      makeControls(video);

      const date = new Date(data.createdAt).toLocaleDateString();

      createdAt.innerHTML = `<strong class='text-black'>Fecha publicación:</strong> ${date}`;

      console.log(videoContent);

      videoContent.appendChild(video);
    })
    .catch((error) => {
      console.log(error);
    });
};

const makeControls = (video) => {
  const playPause = document.getElementById("playPause");
  const progress = document.getElementById("progress");
  const mute = document.getElementById("mute");
  const volBar = document.getElementById("vol-bar");
  const fullScreen = document.getElementById("full-screen");

  video.addEventListener("click", () => {
    playAndPause(video, playPause);
  });

  playPause.addEventListener("click", () => {
    playAndPause(video, playPause);
  });

  document.addEventListener("keydown", (ev) => {
    const IS_VISIBLE =
      document.getElementById("upload-video-box").style.visibility == "visible";

    if (ev.code == "Space" && !IS_VISIBLE) {
      playAndPause(video, playPause);
    }

    if (ev.code == "f" || (ev.code == "KeyF" && !IS_VISIBLE)) {
      const container = document.getElementById("video-container");
      if (!document.fullscreenElement) {
        container.requestFullscreen();
      } else {
        document.exitFullscreen();
      }
    }

    if (ev.code == "m" || (ev.code == "KeyM" && !IS_VISIBLE)) {
      if (video.muted) {
        video.muted = false;
        mute.innerHTML = `
        <svg
        xmlns="http://www.w3.org/2000/svg"
        width="20px"
        height="20px"
        viewBox="0 0 16 16"
        fill="none"
      >
        <path d="M6 1H8V15H6L2 11H0V5H2L6 1Z" fill="#000000" />
        <path
          d="M14 8C14 5.79086 12.2091 4 10 4V2C13.3137 2 16 4.68629 16 8C16 11.3137 13.3137 14 10 14V12C12.2091 12 14 10.2091 14 8Z"
          fill="#000000"
        />
        <path
          d="M12 8C12 9.10457 11.1046 10 10 10V6C11.1046 6 12 6.89543 12 8Z"
          fill="#000000"
        />
      </svg>
        `;
      } else {
        video.muted = true;
        mute.innerHTML = `
        <svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" fill="#000000" height="20px" width="20px" version="1.1" id="Layer_1" viewBox="0 0 512 512" xml:space="preserve">
        <g>
          <g>
            <path d="M453.666,251.642l50.055-50.055c11.039-11.04,11.039-28.939,0-39.978c-11.04-11.04-28.938-11.04-39.978,0l-50.055,50.055    l-50.055-50.055c-11.039-11.038-28.939-11.038-39.978,0c-11.039,11.039-11.039,28.938,0,39.978l50.055,50.055l-50.055,50.055    c-11.039,11.04-11.039,28.939,0,39.978c11.039,11.039,28.938,11.04,39.978,0l50.055-50.055l50.055,50.055    c11.039,11.039,28.938,11.04,39.978,0c11.039-11.039,11.039-28.938,0-39.978L453.666,251.642z"/>
          </g>
        </g>
        <g>
          <g>
            <path d="M231.648,109.542l-97.114,51.088c0.135,1.384,0.084-10.703,0.084,190.786l97.03,51.045    c11.243,5.912,24.858-2.213,24.858-15.011V124.554C256.506,111.806,242.928,103.607,231.648,109.542z"/>
          </g>
        </g>
        <g>
          <g>
            <path d="M86.83,168.954H16.961C7.594,168.954,0,176.548,0,185.916v140.171c0,9.367,7.594,16.961,16.961,16.961H86.83    c9.43,0,16.961-7.7,16.961-16.961V185.916C103.79,176.646,96.252,168.954,86.83,168.954z"/>
          </g>
        </g>
        </svg>

        `;
      }
    }
  });

  video.addEventListener("loadedmetadata", () => {
    progress.max = 1;

    video.addEventListener("timeupdate", () => {
      progress.value = video.currentTime / video.duration;

      const mins = Math.floor(video.currentTime / 60);
      const secs = Math.floor(video.currentTime % 60)
        .toString()
        .padStart(2, "0");
      currentTime.textContent = `${mins}:${secs}`;
    });

    progress.addEventListener("input", () => {
      video.currentTime = progress.value * video.duration;
    });
  });

  mute.addEventListener("click", () => {
    if (volBar.style.visibility == "hidden") {
      volBar.style.visibility = "visible";
    } else {
      volBar.style.visibility = "hidden";
    }
  });

  mute.addEventListener("input", () => {
    const value = volBar.value;
    video.volume = value;
  });

  fullScreen.addEventListener("click", (ev) => {
    const container = document.getElementById("video-container");
    if (!document.fullscreenElement) {
      container.requestFullscreen();
    } else {
      document.exitFullscreen();
    }
  });

  document.addEventListener("fullscreenchange", () => {
    const video = document.getElementById("video");

    if (!document.fullscreenElement) {
      video.classList.add("not-full-screen");
    } else {
      video.classList.remove("not-full-screen");
    }
  });
};

const playAndPause = (video, playPause) => {
  if (video.paused) {
    video.play();
    playPause.innerHTML = `
    <svg xmlns="http://www.w3.org/2000/svg" width="20px" height="20px" viewBox="0 0 24 24" fill="none">
      <path d="M2 6C2 4.11438 2 3.17157 2.58579 2.58579C3.17157 2 4.11438 2 6 2C7.88562 2 8.82843 2 9.41421 2.58579C10 3.17157 10 4.11438 10 6V18C10 19.8856 10 20.8284 9.41421 21.4142C8.82843 22 7.88562 22 6 22C4.11438 22 3.17157 22 2.58579 21.4142C2 20.8284 2 19.8856 2 18V6Z" fill="#1C274C"/>
      <path d="M14 6C14 4.11438 14 3.17157 14.5858 2.58579C15.1716 2 16.1144 2 18 2C19.8856 2 20.8284 2 21.4142 2.58579C22 3.17157 22 4.11438 22 6V18C22 19.8856 22 20.8284 21.4142 21.4142C20.8284 22 19.8856 22 18 22C16.1144 22 15.1716 22 14.5858 21.4142C14 20.8284 14 19.8856 14 18V6Z" fill="#1C274C"/>
    </svg>`;
  } else {
    video.pause();
    playPause.innerHTML = `
    <svg xmlns="http://www.w3.org/2000/svg" width="20px" height="20px" viewBox="0 0 24 24" fill="none">
      <path d="M21.4086 9.35258C23.5305 10.5065 23.5305 13.4935 21.4086 14.6474L8.59662 21.6145C6.53435 22.736 4 21.2763 4 18.9671L4 5.0329C4 2.72368 6.53435 1.26402 8.59661 2.38548L21.4086 9.35258Z" fill="#1C274C"/>
    </svg>
    `;
  }
};

const getVideosByModuleId = async (token, id) => {
  console.log(id);
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

const addVideoEvent = (idModule) => {
  sessionStorage.setItem("idModule", idModule);
  document.getElementById("upload-video-box").style.visibility = "visible";
};

const closeUploadVideo = () => {
  document.getElementById("upload-video-box").style.visibility = "hidden";
};

const previousVideo = () => {
  const params = new URLSearchParams(window.location.search);
  const id = parseInt(params.get("videoId"));
  const videos = JSON.parse(sessionStorage.getItem("videos"));
  const nowVideo = videos.find((video) => video.video_id === id);

  const previousVideo = videos[videos.indexOf(nowVideo) - 1];

  if (previousVideo) {
    window.location.href = `video.html?videoId=${previousVideo.video_id}`;
  } else {
    const previousVideoButton = document.getElementById("btn-previous-video");
    previousVideoButton.disabled = true;
  }
}

const nextVideo = () => {
  const params = new URLSearchParams(window.location.search);
  const id = parseInt(params.get("videoId"));
  const videos = JSON.parse(sessionStorage.getItem("videos"));
  const nowVideo = videos.find((video) => video.video_id === id);

  const nextVideo = videos[videos.indexOf(nowVideo) + 1];

  if (nextVideo) {
    window.location.href = `video.html?videoId=${nextVideo.video_id}`;
  } else {
    const nextVideoButton = document.getElementById("btn-next-video");
    nextVideoButton.disabled = true;
  }
};

window.addEventListener("keydown", function (e) {
  const isVisible =
    document.getElementById("upload-video-box").style.visibility == "visible";

  if ((e.code === "Space" || e.keyCode === 32) && !isVisible) {
    e.preventDefault();
  }
});

const loadVideosSortableList = async () => {

  const list = document.getElementById("sortable-list-videos");
  const nowVideo = document.getElementById("now-element");

  const videos = await getVideosByModuleId(TOKEN,sessionStorage.getItem("idModule"));

  nowVideo.item = {
    "video_id": 0
  }


  videos.forEach((item,index)=> {

    const li = document.createElement("li");
    li.item = item;
    li.textContent = item.title;
    li.setAttribute("draggable",true);
    li.classList.add("sortable-item");
    console.log(item);
    list.appendChild(li);    

  })

}