module mg.eightgroup.docparser {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.pdfbox;
    requires org.apache.pdfbox.io;
    requires com.google.gson;


    opens mg.eightgroup.docparser to javafx.fxml;
    exports mg.eightgroup.docparser;

    opens mg.eightgroup.docparser.model to com.google.gson;
}