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
     var name = prompt("Your name: ")
     if (name != null && name.length >0) {
         alert("Thanks for your message "+name+ "! Justine will get back to you shortly.");
    }
}

function enlarge(img){
    img.style.height="400px";
    img.style.width = "350px";
}
function normal(img){
    img.style.height="300px";
    img.style.width = "300px";
}

 window.addEventListener("scroll", function() {

  var elementTarget = document.getElementById("proj");
  
    console.log(window.pageYOffset)
    console.log(elementTarget.offsetTop)
  if (window.pageYOffset>= (elementTarget.offsetTop - 100) ){
      console.log("You've scrolled past the projects");
      document.getElementById("blog").style.display = 'block';
       
  }
  else{
      document.getElementById("blog").style.display = 'none';
    
  }
});

