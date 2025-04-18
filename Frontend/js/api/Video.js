document.addEventListener("submit", (ev) => {
    ev.preventDefault();
    const formData = new FormData(ev.target);
    const title = formData.get("title");
    const video = formData.get("video");
    upload_video(title, video);
  });
  
  function upload_video(title, video) {
    const formData = new FormData();
    formData.append("title", title);
    formData.append("videoFile", video);
  
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
      
          if (percent < 100) {
            progressBar.innerText = percent + "%";
          } else {
            progressBar.innerText = "Procesando...";
          }
        }
      });
  
    xhr.addEventListener("load", () => {
      if (xhr.status >= 200 && xhr.status < 300) {
        progressBar.style.width = "100%";
        progressBar.classList.remove("bg-danger");
        progressBar.classList.add("bg-success");
        progressBar.innerText = "¡Subido!";
        alert("Video subido exitosamente");
      } else {
        progressBar.classList.add("bg-danger");
        progressBar.innerText = "Error al subir";
        alert("Error al subir el video.");
      }
    });
  
    xhr.addEventListener("error", () => {
      progressBar.classList.add("bg-danger");
      progressBar.innerText = "Error de red";
      alert("Error de red.");
    });
  
    xhr.open("POST", "http://localhost:8080/api/videos/upload");
    xhr.send(formData);
  }
  