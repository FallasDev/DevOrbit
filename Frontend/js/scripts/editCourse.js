const HOST = "http://localhost:8080";
 const TOKEN = localStorage.getItem("jwtToken");
 
 const formUpdateCourse = document.getElementById("form-edit-course");
 console.log(formUpdateCourse);
 
 formUpdateCourse.addEventListener("submit", async (e) => {
   e.preventDefault();
   const formData = new FormData(e.target);
   const sendData = new FormData();


   const courseId = new URLSearchParams(window.location.search).get("courseId");
 
   const course = {
     title: formData.get("title"),
     description: formData.get("description"),
     price: formData.get("price"),
     test: {
         test_id: formData.get("testId"),
     }
   };
 
   sendData.append("title", course.title);
    sendData.append("description", course.description);
    sendData.append("price", course.price);
    sendData.append("testId", course.test.test_id);

 
   editCourse(courseId, sendData);
 });
 
 const editCourse = async (courseId, sendData) => {

   const res = await fetch(`${HOST}/api/courses/${courseId}`, {
     method: "PUT",
     headers: {
       Authorization: `Bearer ${TOKEN}`,
     },
     body: sendData,
   });
   console.log(res);
   if (res.ok) {
     console.log("Curso editado correctamente");
     location.href = `/Frontend/course.html?courseId=${courseId}`;
   } else {
     console.log("Error al editar el curso");
   }
 };