function initMap() {
  // The location of user
  const mylocation = { lat: -25.344, lng: 131.031 };
  // The map, centered at Uluru
  const map = new google.maps.Map(document.getElementById("map"), {
    zoom: 4,
    center: mylocation,
  });
  // The marker, positioned at user
  const marker = new google.maps.Marker({
    position: mylocation,
    map: map,
  });
}

window.initMap = initMap;