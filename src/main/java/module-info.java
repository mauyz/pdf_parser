module mg.eightgroup.docparser {
    requires javafx.controls;
    requires javafx.fxml;


    opens mg.eightgroup.docparser to javafx.fxml;
    exports mg.eightgroup.docparser;
}