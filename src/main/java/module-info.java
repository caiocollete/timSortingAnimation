module com.caiocollete.sortinganimation {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.caiocollete.sortinganimation to javafx.fxml;
    exports com.caiocollete.sortinganimation;
}