




   // Creation of one layer of points
   var query = new L.layerGroup();
 var  sizeOfArray=0;
      //custom  icon class
      var   MarkerIcon = L.Icon.extend({
      options : {
          iconSize:[20, 20],
          shadowSize:[0, 0],
          iconAnchor: [10, 10],
          popupAnchor: [0, -10]
      }
      });

      //path to where the files are hosted
      var path = '../pictures/signaturen/'
var mbAttr = 'Map data &copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors, ' +
    'Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
mbUrl = 'https://api.mapbox.com/styles/v1/{id}/tiles/{z}/{x}/{y}?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw';

var rUrl = 'https://{s}.tile.opentopomap.org/{z}/{x}/{y}.png';

var topplusUrl = 'http://sgx.geodatenzentrum.de/wms_topplus_web_open';
var topplusAttr = '&copy Bundesamt für Kartographie und Geodäsie 2017, <a href="http://sg.geodatenzentrum.de/web_public/Datenquellen_TopPlus_Open.pdf">Datenquellen</a>';

//var verUrl = 'https://sgx.geodatenzentrum.de/wms_vg250';
//var verAttr = '&copy Bundesamt für Kartographie und Geodäsie 2017, <a href = "https://sg.geodatenzentrum.de/web_public/gdz/dokumentation/deu/vg250_01-01.pdf"</a>';

// Creation of two layers : grayscale, and streets map
// It is also possible to add a satellite map, landform map, ..
var grayscale = L.tileLayer(mbUrl, {id: 'mapbox/light-v9', tileSize: 512, zoomOffset: -1, attribution: mbAttr}),
streets = L.tileLayer(mbUrl, {id: 'mapbox/streets-v11', tileSize: 512, zoomOffset: -1, attribution: mbAttr});
relief = L.tileLayer(rUrl, {maxZoom: 17, attribution: 'Map data: &copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors, <a href="http://viewfinderpanoramas.org">SRTM</a> | Map style: &copy; <a href="https://opentopomap.org">OpenTopoMap</a> (<a href="https://creativecommons.org/licenses/by-sa/3.0/">CC-BY-SA</a>)'});
topplus = L.tileLayer.wms(topplusUrl, {layers: 'web', format: 'image/png',transparent: true, attribution: topplusAttr});
//verwa = L.tileLayer.wms(verUrl, {layers: 'Kreis', format: 'image/png',transparent: true, attribution: topplusAttr});
mini_grayscale = new L.TileLayer(mbUrl, {id: 'mapbox/light-v9', minZoom: 0, maxZoom: 13, attribution: mbAttr});
mini_streets = new L.TileLayer(mbUrl, {id: 'mapbox/streets-v11', minZoom: 0, maxZoom: 13, attribution: mbAttr});
mini_relief = new L.TileLayer(rUrl, {minZoom: 0, maxZoom: 13, attribution: mbAttr});
mini_topplus = new L.tileLayer.wms(topplusUrl, {layers: 'web', format: 'image/png',transparent: true, attribution: topplusAttr});


// Definition of the map, and setup of details : coordinates of the center, zoom level and layers that appear on loading
var map = L.map('map', {
center: [49.99, 8.24],
zoom: 6,
minZoom: 2,
fullscreenControl: true,
layers: [streets, query]
});

// Definition of layers
var baseLayers = {
"Grayscale": grayscale,
"Streets": streets,
"Relief" : relief,
"Topplus" : topplus
};

// Definition of point layers
var overlays = {
"Query": query,
};

// Add layers and points layers to the map
var layerControl = L.control.layers(baseLayers, overlays).addTo(map);
var testBoundingBox = false; // Check is bounding box was open



