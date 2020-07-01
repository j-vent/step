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
  if (name != null && name.length>0){
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
    console.log("You've scrolled past the projects");
    document.getElementById("blog").style.display = 'block';
  }
  else{
    document.getElementById("blog").style.display = 'none';
  }
});

function getComments() {
  const numComments = document.getElementById("numComments").value;
  fetch('/data?numComments='+ numComments)
  .then(response => response.json())
  .then((comments) => {
    console.log("comment");
    console.log(comments);
    const commentSection = document.getElementById('comment-list');
    commentSection.innerText = ""; // clear old comments 
    comments.forEach((comment) => {
      commentSection.appendChild(createCommentElement(comment));
    });
  });
}

function createCommentElement(comment){
    const commentElement = document.createElement("li");
    const textElement = document.createElement("span");
    textElement.innerText = comment.text;
    commentElement.appendChild(textElement);
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
      console.log(status.isLoggedIn);
      if(status.isLoggedIn){
          document.getElementById("commentSection").style.display = 'block';
      }
      else{
          console.log(status.loginUrl);
          var loginbtn = document.getElementById("loginbtn");
          // TO DO: try not to hardcode, just the path
          loginbtn.style.display="block";
          loginbtn.href ="https://8080-02745c19-09bf-48b4-a014-a5ee55f7c78e.us-west1.cloudshell.dev/_ah/login?continue=%2F";
      }
  });
}
