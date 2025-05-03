const TOKEN =
  "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZSI6IlJPTEVfVVNFUiIsImV4cCI6MTc0NjI0MTg0OCwiaWF0IjoxNzQ2MjM4MjQ4fQ.C5b3Kzk_c6Lr3h_iumxNWmxlU9huO6ev8aUAu_la22c";
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
  const data = await sendAnswers(1);
  showResult(data);
});

const getTestById = async (id) => {
  const res = await fetch(`${HOST}/api/tests/${id}`, {
    method: "GET",
    headers: {
      Authorization: `Bearer ${TOKEN}`,
    },
  });
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
  console.log(res);
  const data = await res.json();
  saveTestAttemp(data);
  return data;
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
      "test_id": testId
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

  if(!res.ok){
    const errorText = await res.text();
    console.log(errorText);
    if (errorText == "Max attemps reached"){
      alert(errorText);
    }
  }

  const data = await res.json();
  console.log(data);
}


const showResult = (data) => {
  const result = document.getElementById("box-result");
  result.textContent = `Su nota: ${data}`;
};
