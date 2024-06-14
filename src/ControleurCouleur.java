import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;

public class ControleurCouleur implements EventHandler<ActionEvent> {

    private Pendu appliPendu;
    private ColorPicker cp;

    /**
     * @param p vue du jeu
     */
    public ControleurCouleur(Pendu appliPendu, ColorPicker cp) {
        this.appliPendu = appliPendu;
        this.cp = cp;
    }

    /**
     * L'action consiste à afficher une fenêtre popup précisant les règles du jeu.
     * @param actionEvent l'événement action
     */
    @Override
    public void handle(ActionEvent actionEvent) {
        BorderPane ban = this.appliPendu.getBanniere();
        ban.setBackground(new Background(new BackgroundFill(cp.getValue(), CornerRadii.EMPTY, Insets.EMPTY)));
    }
}
