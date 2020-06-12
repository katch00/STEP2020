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