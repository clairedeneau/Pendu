import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonType;
import java.util.Optional;
import javafx.event.EventHandler;
import javafx.event.ActionEvent ;
import javafx.scene.control.Button;
/**
 * Contrôleur à activer lorsque l'on clique sur le bouton rejouer ou Lancer une partie
 */
public class ControleurLancerPartie implements EventHandler<ActionEvent> {
    /**
     * modèle du jeu
     */
    private MotMystere modelePendu;
    /**
     * vue du jeu
     **/
    private Pendu vuePendu;

    /**
     * @param modelePendu modèle du jeu
     * @param vuePendu vue du jeu
     */
    public ControleurLancerPartie(MotMystere modelePendu, Pendu vuePendu) {
        this.modelePendu = modelePendu;
        this.vuePendu = vuePendu;
    }

    /**
     * L'action consiste à recommencer une partie. Il faut vérifier qu'il n'y a pas une partie en cours
     * @param actionEvent l'événement action
     */
    @Override
    public void handle(ActionEvent actionEvent) {
        Button button = (Button) (actionEvent.getSource());
        if (button.getText().equals("Nouveau mot")){
            this.vuePendu.lancePartie();
        } else{
            if (this.vuePendu.getPartieEnCours()){
                //la fenêtre popup est lancée et on attend la réponse
                Optional<ButtonType> reponse = this.vuePendu.popUpPartieEnCours().showAndWait();
                // si la réponse est positive
                if (reponse.isPresent() && reponse.get().equals(ButtonType.YES)){
                    System.out.println("Ok !");
                    this.vuePendu.lancePartie();
                } else{
                    if (reponse.isPresent() && reponse.get().equals(ButtonType.NO)){

                    } else {
                        this.vuePendu.modeJeu();
                    }
                }

            } else{
                this.vuePendu.lancePartie();
            }
        }

    }
        
}