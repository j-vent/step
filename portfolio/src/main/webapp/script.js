// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

function msg(){
  var text = prompt("Leave Justine a message: ");
  var name = prompt("Your name: ");
  if (name != null && name.length > 0){
    alert("Thanks for your message "+name+ "! Justine will get back to you shortly.");
  }
}

function bunnymsg(){
  var bunny=document.getElementById('bunny');
  document.getElementById("bun_msg").textContent="*bunny noises*";
}


window.addEventListener("scroll", function() {
  var elementTarget = document.getElementById("proj");
  if (window.pageYOffset>= (elementTarget.offsetTop ) ){
    document.getElementById("blog").style.display = 'block';
  }
  else{
    document.getElementById("blog").style.display = 'none';
  }
});

function getComments() {
  const language=document.getElementById("languages").value;
  const numComments = document.getElementById("numComments").value;
  fetch('/data?numComments='+ numComments+ '&language=' + language)
  .then(response => response.json())
  .then((comments) => {
    const commentSection = document.getElementById('comment-list');
    // clear old comments 
    commentSection.innerText = ""; 
    comments.forEach((comment) => {
    commentSection.appendChild(createCommentElement(comment)); 
    });
  });
}

function createCommentElement(comment){
    const commentElement = document.createElement("li");
    const textElement = document.createElement("span");
    const userElement = document.createElement("span");
    const scoreElement = document.createElement("span");
    textElement.className = "commenttxt";
    userElement.className = "emailtxt";
    scoreElement.className = "scoretxt";
    textElement.innerText = comment.text;
    userElement.innerText = "-" + comment.nickname;
    scoreElement.innerText = comment.score; 
    commentElement.appendChild(textElement);
    commentElement.appendChild(scoreElement);
    commentElement.appendChild(userElement);

    return commentElement;
}

function deleteComments(){
  fetch('/data?numComments=10')
  .then(response => response.json())
  .then((comments) =>{
    comments.forEach((comment) => {
    deleteComment(comment);
    });

  }); 
 }

function deleteComment(comment) {
  const params = new URLSearchParams();
  params.append("id", comment.id);

  fetch('/delete-comment', {method: 'POST', body: params})
  .then(() => getComments());
}

function getLogin(){
  fetch('/login')
  .then(response => response.json())
  .then(status =>{
      if(status.isLoggedIn){
          document.getElementById("commentSection").style.display="block";
          var logoutbtn = document.getElementById("logoutbtn");
          logoutbtn.style.display="block";
          logoutbtn.href = status.url;
      }
      else{
          var loginbtn = document.getElementById("loginbtn");
          loginbtn.style.display="block";
          loginbtn.href = status.url;
      }
  });
}
