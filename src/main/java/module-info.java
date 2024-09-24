module com.download.file.filedownloader {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.net.http;

    opens com.download.file.filedownloader to javafx.fxml;
    exports com.download.file.filedownloader;
}