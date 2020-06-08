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

/**
 * Adds a random greeting to the page.
 */
function addRandomGreeting() {
  const greetings =
      ['Hello world!', '¡Hola Mundo!', '你好，世界！', 'Bonjour le monde!'];

  // Pick a random greeting.
  const greeting = greetings[Math.floor(Math.random() * greetings.length)];

  // Add it to the page.
  const greetingContainer = document.getElementById('greeting-container');
  greetingContainer.innerText = greeting;
}

/**
 * Gets a random surprised pikachu meme and displays on page
 */
function randomPik() {
  const imageIndex = Math.floor(Math.random() * 6) + 1;
  const imgUrl = 'images/suprisedimgs/sup' + imageIndex + '.jpg';

  const imgElement = document.createElement('img');
  imgElement.src = imgUrl;

  const imageContainer = document.getElementById('random-pik-container');
  // Remove the previous image.
  imageContainer.innerHTML = '';
  imageContainer.appendChild(imgElement);
}


function getMessage() {
  fetch('/comments').then(response => response.json()).then((messages) => {
    const cmmtListEl = document.getElementById('message-container');
    messages.forEach((message) => {
        cmmtListEl.appendChild(createCommentElement(message));
    })
  });

}

function createCommentElement(comment) {
  const commentElement = document.createElement('li');
  commentElement.className = 'comment';

  const titleElement = document.createElement('span');
  titleElement.innerText = comment.message;

  const deleteButtonElement = document.createElement('button');
  deleteButtonElement.innerText = 'Delete';
  deleteButtonElement.className = "button";
  deleteButtonElement.addEventListener('click', () => {
    deleteComment(comment);

    // Remove the task from the DOM.
    commentElement.remove(); 
  });

  commentElement.appendChild(titleElement);
  commentElement.appendChild(deleteButtonElement);
  return commentElement;
}

function deleteComment(comment) {
  const params = new URLSearchParams();
  params.append('id', comment.id);
  fetch('/delete-data', {method: 'POST', body: params});
}

/**
 * Creates and adds a hardcoded piechart to the page 
 */
google.charts.load('current', {'packages':['corechart']});
google.charts.setOnLoadCallback(drawChart);

function drawChart() {
  var data = new google.visualization.DataTable();
  data.addColumn('string', 'Genre');
  data.addColumn('number', 'Count');
  
    data.addRows([
      //['Genre', 'Number played'],
      ['Sci-Fi', 3],
      ['Fantasy', 3],
      ['Simulation', 2],
      ['Mixed/Other', 2]
    ]);

  var options = {
    'title': 'Top 10 Favorite Game Genres',
    is3D: true,
    'width':500,
    'height':400
  };

  var chart = new google.visualization.PieChart(document.getElementById('chart-container'));
  chart.draw(data, options);

} 

google.charts.load('current', {'packages':['corechart']});
google.charts.setOnLoadCallback(drawGameChart);

/** Fetches game data and uses it to create a chart. */
function drawGameChart() {
  fetch('/game-data').then(response => response.json())
  .then((gameVotes) => {
    var data = new google.visualization.DataTable();
    data.addColumn('string', 'Game');
    data.addColumn('number', 'Votes');
    Object.keys(gameVotes).forEach((game) => {
      data.addRow([game, gameVotes[game]]);
    });

    var options = {
      'title': 'Favorite Games',
      'width':700,
      'height':600
    };

    var chart = new google.visualization.ColumnChart(
        document.getElementById('gamesChart-container'));
    chart.draw(data, options);
  });
}