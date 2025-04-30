function previewVideo(event) {
    const file = event.target.files[0];
    const video = document.getElementById("videoPreview");
    const removeBtn = document.getElementById("removeBtn");

    if (file) {
      const videoURL = URL.createObjectURL(file);
      video.src = videoURL;
      video.style.display = "block";
      removeBtn.style.display = "block";
    }
  }

  function removeVideo(e) {
    e.stopPropagation();
    const video = document.getElementById("videoPreview");
    const fileInput = document.getElementById("fileInput");
    const removeBtn = document.getElementById("removeBtn");

    video.src = "";
    video.style.display = "none";
    removeBtn.style.display = "none";
    fileInput.value = "";
  }