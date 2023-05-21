module com.zedination.diffwrappertool {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.jfxtras.styles.jmetro;
    requires org.eclipse.jgit;
    requires static lombok;

    opens com.zedination.diffwrappertool to javafx.fxml;
    exports com.zedination.diffwrappertool;
    exports com.zedination.diffwrappertool.controller;
    opens com.zedination.diffwrappertool.controller to javafx.fxml;

    // fix lỗi java.lang.RuntimeException: java.lang.IllegalAccessException: module javafx.base cannot access class com.zedination.diffwrappertool.model.Commit (in module com.zedination.diffwrappertool) because module com.zedination.diffwrappertool does not open com.zedination.diffwrappertool.model to javafx.base
    opens com.zedination.diffwrappertool.model;
}