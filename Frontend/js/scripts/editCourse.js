
const HOST = "http://localhost:8080";
const TOKEN = localStorage.getItem("jwtToken");

const formUpdateCourse = document.getElementById("form-edit-course");
console.log(formUpdateCourse);

formUpdateCourse.addEventListener("submit", async (e) => {
  e.preventDefault();
  const formData = new FormData(e.target);

  const courseId = new URLSearchParams(window.location.search).get("courseId");

  const course = {
    title: formData.get("title"),
    description: formData.get("description"),
    price: formData.get("price"),
    test: {
        test_id: formData.get("testId"),
    }
  };

  console.log(courseId);

  console.log(course);

  editCourse(courseId, course);
});

const editCourse = async (courseId, course) => {
  const res = await fetch(`${HOST}/api/courses/${courseId}`, {
    method: "PUT",
    headers: {
      Authorization: `Bearer ${TOKEN}`,
      "Content-Type": "application/json",
    },
    body: JSON.stringify(course),
  });
  if (res.ok) {
    console.log("Curso editado correctamente");
    location.href = `/Frontend/course.html?courseId=${courseId}`;
  } else {
    console.log("Error al editar el curso");
  }
};