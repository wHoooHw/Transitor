package main.hellofx;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PostcodeInputPane extends VBox {
    private double startLong;
    private double startLat;
    private double destinationLong;
    private double destinationLat;
    private double distance;
    private double cyclingTime;
    private double walkingTime;
    private Label distanceLabel;
    private Label walkingTimeLabel;
    private Label cyclingTimeLabel;
    private Label mapLoader;


    public PostcodeInputPane() {
        setSpacing(10);
        setAlignment(Pos.CENTER); // Center content horizontally in VBox
        setPadding(new Insets(10));
    
        // Create origin label and text fields
        Label originLabel = new Label("Origin");
        HBox originBox = createTextFieldBox();
        originBox.setAlignment(Pos.CENTER);
    
        // Create destination label and text fields
        Label destinationLabel = new Label("Destination");
        HBox destinationBox = createTextFieldBox();
        destinationBox.setAlignment(Pos.CENTER);
    
        // Create "Calculate Distance" button
        Button calculateButton = new Button("Calculate Distance");
        calculateButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 20px;");
        calculateButton.setOnAction(event -> calculateLogistics(getOriginPostcodes(), getDestinationPostcodes()));
    
        // Initialize labels with default values
        this.distanceLabel = getDistanceLabel(0); // Initialized with 0, will be updated later
        this.walkingTimeLabel = getWalkingTimeLabel(0); // Initialized with 0
        this.cyclingTimeLabel = getCyclingTimeLabel(0); // Initialized with 0
    
        // Add labels, text fields, and button to the pane, ensuring each is centered as needed
        getChildren().addAll(originLabel, originBox, destinationLabel, destinationBox, calculateButton, distanceLabel, walkingTimeLabel, cyclingTimeLabel);
        setMargin(calculateButton, new Insets(20, 0, 0, 0));
        setMargin(walkingTimeLabel, new Insets(20, 0, 0, 10));
        setMargin(distanceLabel, new Insets(40, 0, 0, 10));
        setMargin(cyclingTimeLabel, new Insets(20, 0, 0, 10));
    }

    private Label getCyclingTimeLabel(double cyclingTime) {
        Label cyclingTimeLabel = new Label("Cycling: " + cyclingTime + " minutes"); // + calculatedBycicleTime); //calculatedBycicleTime comes from calculator
        return cyclingTimeLabel;
    }

    private Label getDistanceLabel(double distance) {
        Label distanceLabel =  new Label("Distance: " + distance + "KM");// + distance) // distance between the 2 points comes from calculator
        return distanceLabel;
    }

    private Label getWalkingTimeLabel(double walkingTime) {
        Label walkingTimeLabel = new Label("Walking: " + walkingTime + " minutes");// + calculatedTime) // calculatedTime comes from distance calculator.
        return walkingTimeLabel;
    }

    public void calculateLogistics(String startPostCode, String endPostcode) {
        ApiReader startPoint = new ApiReader(startPostCode);
        ApiReader endPoint = new ApiReader(endPostcode);
        System.out.println(startPostCode);
        System.out.println(endPostcode);
        startLong = startPoint.getLongitude();
        startLat = startPoint.getLatitude();
        destinationLat = endPoint.getLatitude();
        destinationLong = endPoint.getLongitude();
    
        DistanceCalculator distanceCalculator = new DistanceCalculator();
        this.distance = distanceCalculator.calculateDistance(startLat, startLong, destinationLat, destinationLong);
        this.cyclingTime = distanceCalculator.calculateCyclingTime(distance);
        this.walkingTime = distanceCalculator.calculateWalkingTime(distance);
    
        System.out.println(cyclingTime);
        System.out.println(distance);
        System.out.println(walkingTime);
    
        // Update UI elements with the calculated values
        javafx.application.Platform.runLater(() -> {
            distanceLabel.setText(String.format("Distance: %.2f KM", this.distance));
            walkingTimeLabel.setText(String.format("Walking: %.2f minutes", this.walkingTime * 60)); // Convert hours to minutes
            cyclingTimeLabel.setText(String.format("Cycling: %.2f minutes", this.cyclingTime * 60)); // Convert hours to minutes

        });
    }
    
    
    private HBox createTextFieldBox() {
        HBox box = new HBox(10);
        box.setAlignment(Pos.BASELINE_LEFT);
    
        //Makes sure the first 4 boxes only accept digits and the last 2 only accept letters
        for (int i = 0; i < 6; i++) {
            boolean acceptsDigitsOnly = i < 4; 
            PostcodeTextfield textField = new PostcodeTextfield(!acceptsDigitsOnly);
            int finalI = i;
            textField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.BACK_SPACE && textField.getText().isEmpty() && finalI > 0) {
                    PostcodeTextfield previousField = (PostcodeTextfield) box.getChildren().get(finalI - 1);
                    previousField.requestFocus();
                    previousField.selectAll();
                    event.consume(); 
                }
            });
            textField.setOnKeyReleased(event -> {
                if (event.getCode() != KeyCode.BACK_SPACE && textField.getText().length() == 1) {
                    if (finalI < 5) {
                        PostcodeTextfield nextField = (PostcodeTextfield) box.getChildren().get(finalI + 1);
                        nextField.requestFocus();
                        nextField.selectAll();
                    } else {
                        textField.getParent().requestFocus(); //Leave the last box as soon as something is typed in it
                    }
                }
            });
            box.getChildren().add(textField);
        }
    
        return box;
    }
    


    public String getOriginPostcodes() {
        return getPostcodeAsString(1);
    }

    public String getDestinationPostcodes() {
        return getPostcodeAsString(3);
    }

    private String getPostcodeAsString(int startIndex) {
        StringBuilder postcode = new StringBuilder();
        for (javafx.scene.Node node : ((HBox) getChildren().get(startIndex)).getChildren()) {
            if (node instanceof PostcodeTextfield) {
                PostcodeTextfield textField = (PostcodeTextfield) node;
                postcode.append(textField.getText());
            }
        }
        return postcode.toString().toUpperCase(); //Return the concatenated postcode as a single string
    }
    
}

