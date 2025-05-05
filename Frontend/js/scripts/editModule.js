document.addEventListener("DOMContentLoaded", () => {
    const courseId = new URLSearchParams(window.location.search).get("idCourse");
    const moduleId = new URLSearchParams(window.location.search).get("idModule");
  
    const formModule = document.getElementById("form-add-module");
  
    const TOKEN = localStorage.getItem("jwtToken");
  
    formModule.addEventListener("submit", async (e) => {
      e.preventDefault();
      const formData = new FormData(e.target);
  
      addModule(formData, courseId, moduleId);
    });
  
    const addModule = async (formData, courseId, moduleId) => {
      const sendData = new FormData();
  
      sendData.append("title", formData.get("title"));
      sendData.append("descripcion", formData.get("description"));
      sendData.append("courseId", courseId);
      sendData.append(
        "moduleOrder",
        memoryList.map((item) => item.id_module)
      );
  
      console.log(memoryList.map((item) => item.id_module));
  
      console.log(sendData);
  
      const res = await fetch(`${HOST}/api/modules/${moduleId}`, {
        method: "PUT",
        headers: {
          Authorization: `Bearer ${TOKEN}`,
        },
        body: sendData,
      });
      console.log(res);
      if (res.ok) {
        console.log("Modulo creado correctamente");
        location.href = `./course.html?courseId=${courseId}`;
      } else {
        console.log("Error al crear el modulo");
      }
    };
  });
  