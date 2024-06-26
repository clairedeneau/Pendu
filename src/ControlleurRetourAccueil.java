import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonType;
import java.util.Optional;

/**
 * Contrôleur à activer lorsque l'on clique sur le bouton Accueil
 */
public class ControlleurRetourAccueil implements EventHandler<ActionEvent> {
    /**
     * vue du jeu
     **/
    private Pendu vuePendu;

    /**
     * constructeur du controlleur du bouton 
     * @param modelePendu modèle du jeu
     * @param vuePendu vue du jeu
     */
    public ControlleurRetourAccueil(MotMystere modelePendu, Pendu vuePendu) {
        this.vuePendu = vuePendu;
    }


    /**
     * L'action consiste à retourner sur la page d'accueil. Il faut vérifier qu'il n'y avait pas une partie en cours.
     * @param actionEvent l'événement action
     */
    @Override
    public void handle(ActionEvent actionEvent) {
        this.vuePendu.modeAccueil();
    }
}
