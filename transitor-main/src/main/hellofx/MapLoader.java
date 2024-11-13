package main.hellofx;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebView;

public class MapLoader extends Pane {
    private WebView webView = new WebView();
    private boolean contentLoaded = false;

    public MapLoader() {
        this.getChildren().add(webView);
        webView.prefWidthProperty().bind(this.widthProperty());
        webView.prefHeightProperty().bind(this.heightProperty());
        loadMap();
        webView.getEngine().getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == javafx.concurrent.Worker.State.SUCCEEDED) {
                contentLoaded = true;
                System.out.println("Map successfully loaded.");
            }
        });
    }

    private void loadMap() {
        String htmlContent = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="utf-8">
                <title>Custom Map Markers</title>
                <meta name='viewport' content='initial-scale=1,maximum-scale=1,user-scalable=no'>
                <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/leaflet.css" />
                <style>
                    body, html { height: 100%; margin: 0; }
                    #map { height: 100%; width: 100%; }
                    .custom-marker {
                        background-color: #f00;
                        border-radius: 50%;
                        display: block;
                    }
                </style>
            </head>
            <body>
                <div id='map'></div>
                <script src='https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/leaflet.js'></script>
                <script>
                    var map = L.map('map').setView([50.8516, 5.6915], 12);
                    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                    }).addTo(map);

                    window.addMarker = function(lat, lng) {
                        var customIcon = L.divIcon({
                            className: 'custom-marker',
                            iconSize: [5, 5] 
                        });
                        L.marker([lat, lng], {icon: customIcon}).addTo(map);
                    };
                    window.drawLine = function(originLat, originLng, destinationLat, destinationLng) {
                        var pointA = L.latLng(originLat, originLng);
                        var pointB = L.latLng(destinationLat, destinationLng);
                        var pointsList = [pointA, pointB];
                        var polyline = L.polyline(pointsList, {
                            color: 'black', 
                            weight: 2,     
                            opacity: 0.7,  
                            smoothFactor: 1
                        }).addTo(map);
                    };
                </script>
            </body>
            </html>
            """;
        webView.getEngine().loadContent(htmlContent);
    }

    public void addMapMarker(double latitude, double longitude) {
        Platform.runLater(() -> {
            if (contentLoaded) {
                webView.getEngine().executeScript(String.format(
                    "if (typeof window.addMarker === 'function') { window.addMarker(%f, %f); } else { console.log('addMarker function not defined yet.'); }",
                    latitude, longitude));
                System.out.println("Marker added to the map");
            }
        });
    }

    public void drawLine(double originLat, double originLng, double destinationLat, double destinationLng) {
        Platform.runLater(() -> {
            if (contentLoaded) {
                webView.getEngine().executeScript(String.format(
                    "if (window.drawLine) { window.drawLine(%f, %f, %f, %f); } else { console.log('drawLine function not defined yet.'); }",
                    originLat, originLng, destinationLat, destinationLng));
                System.out.println("Line drawn on the map");
            }
        });
    }

    public void removeMarkers() {
        Platform.runLater(() -> {
            webView.getEngine().executeScript("map.eachLayer(function (layer) { if (layer instanceof L.Marker) map.removeLayer(layer); });");
            System.out.println("Markers removed from the map");
        });
    }

    public void removeLine() {
        Platform.runLater(() -> {
            webView.getEngine().executeScript("map.eachLayer(function (layer) { if (layer instanceof L.Polyline) map.removeLayer(layer); });");
            System.out.println("Lines removed from the map");
        });
    }
}
