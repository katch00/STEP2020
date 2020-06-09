/**
 * Creates and displays a map on page
 */
function createMap() {
  const map = new google.maps.Map(
      document.getElementById('map'),
      {center: {lat: 41.834, lng: -87.626}, zoom: 16});
}
