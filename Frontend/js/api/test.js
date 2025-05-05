const TOKEN = localStorage.getItem("jwtToken");
const HOST = "http://localhost:8080";

const btnAnswers = document.getElementById("btn-answers");

document.addEventListener("DOMContentLoaded", async () => {
  const urlParams = new URLSearchParams(window.location.search);
  const testId = urlParams.get("id");
  const testData = await getTestById(testId);
  const testQuestions = await getTestQuestions(testId);
  const currentUser = await getCurrentUser();

  console.log(testData);

  sessionStorage.setItem("idUser", currentUser.idUser)
  loadHeader(testData);
  loadQuestions(testQuestions);
});

btnAnswers.addEventListener("click", async () => {
  const urlParams = new URLSearchParams(window.location.search);
  const testId = urlParams.get("id");
  const data = await sendAnswers(testId);
});

const getTestById = async (id) => {
  const res = await fetch(`${HOST}/api/tests/${id}`, {
    method: "GET",
    headers: {
      Authorization: `Bearer ${TOKEN}`,
    },
  });
  console.log(res);
  const data = await res.json();
  return data;
};

const getTestQuestions = async (id) => {
  const res = await fetch(`${HOST}/api/questions/tests/${id}`, {
    method: "GET",
    headers: {
      Authorization: `Bearer ${TOKEN}`,
    },
  });
  const data = await res.json();
  return data;
};

const getQuestionAnswers = async (id) => {
  const res = await fetch(`${HOST}/api/answers/questions/${id}`, {
    method: "GET",
    headers: {
      Authorization: `Bearer ${TOKEN}`,
    },
  });
  const data = await res.json();
  return data;
};

const loadHeader = (data) => {
  const title = document.getElementById("course-title");
  const instructions = document.getElementById("instructions");

  console.log(data.instruction);

  title.textContent = "";
  instructions.textContent = data.instruction;
};

const loadQuestions = async (data) => {
  const questionContainer = document.getElementById("questions-box");

  for (const item of data) {
    const question = document.createElement("li");
    const answers = document.createElement("form");

    const questionId = item.question_id;

    const answersData = await getQuestionAnswers(questionId);
    console.log(item);
    for (const answerItem of answersData) {
      const answer = document.createElement("div");
      answer.style.display = "flex";
      console.log(answerItem);
      if (item.type == "1") {
        answer.innerHTML += `
                    <label>
                        <input type="radio" name='answer' value='${JSON.stringify(
          {
            answer_id: answerItem.answer_id,
            question: {
              question_id: questionId,
            },
          }
        )}'> ${answerItem.title}
                    </label><br>
                `;
      }

      answers.appendChild(answer);
    }

    question.textContent = item.prompt;
    question.appendChild(answers);
    questionContainer.appendChild(question);
  }
};

const sendAnswers = async (id) => {
  const selected = document.querySelectorAll('input[name="answer"]:checked');
  const userAnswers = [];

  selected.forEach((e) => {
    const answer = JSON.parse(e.value);
    userAnswers.push(answer);
  });

  const res = await fetch(`${HOST}/api/tests/${id}/getScore`, {
    method: "POST",
    headers: {
      Authorization: `Bearer ${TOKEN}`,
      "Content-Type": "application/json",
    },
    body: JSON.stringify(userAnswers),
  });

  if (!res.ok) {
    console.error("Error al enviar respuestas:", await res.text());
    return;
  }

  const score = await res.json();
  console.log("Nota recibida del backend:", score);
  showResult(score);

  if (score > 67.5) {
    const btnCertificate = document.getElementById("btn-certificate");
    btnCertificate.style.display = "block";
  }
};

const showResult = (score) => {
  const result = document.getElementById("box-result");
  result.textContent = `Su nota: ${score}`;
};

const getCurrentUser = async () => {

  const res = await fetch(`${HOST}/api/user/me`, {
    method: "GET",
    headers: {
      Authorization: `Bearer ${TOKEN}`,
    }
  })

  const data = await res.json();
  return data;
}

const saveTestAttemp = async (score) => {

  const testAttemp = {
    "user": {
      "idUser": sessionStorage.getItem("idUser")
    },
    "test": {
      "test_id": new URLSearchParams(window.location.search).get("id")
    },
    "score": score
  }


  const res = await fetch(`${HOST}/api/test-attempts`, {
    method: "POST",

    headers: {
      Authorization: `Bearer ${TOKEN}`,
      "Content-Type": "application/json"
    },
    body: JSON.stringify(testAttemp)
  })

  if (!res.ok) {
    const errorText = await res.text();
    console.log(errorText);
    if (errorText == "Max attemps reached") {
      alert("Has alcanzado el maximo de intentos para este test");
      location.href = `/Frontend/course.html?courseId=${new URLSearchParams(window.location.search).get("courseId")}`;
    }
  }

  const data = await res.json();
  console.log(data);
}

// document.getElementById("btn-certificate").addEventListener("click", async () => {
//   const urlParams = new URLSearchParams(window.location.search);
//   const testId = urlParams.get("id");
//   const score = document.getElementById("box-result").textContent.split(": ")[1]; // Extraer la nota


//   const res = await fetch(`${HOST}/api/tests/${testId}/certificate/${user.idUser}?score=${score}`, {
//     method: "GET",
//     headers: {
//       Authorization: `Bearer ${TOKEN}`,
//     },
//   });

//   if (res.ok) {
//     const blob = await res.blob();
//     const url = window.URL.createObjectURL(blob);
//     const a = document.createElement("a");
//     a.href = url;
//     a.download = "Certificado.pdf";
//     document.body.appendChild(a);
//     a.click();
//     a.remove();
//   } else {
//     console.error("Error al generar el certificado");
//   }
// });