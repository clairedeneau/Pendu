import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;


/**
 * Permet de gérer un Text associé à une Timeline pour afficher un temps écoulé
 */
public class Chronometre extends Text{
    /**
     * timeline qui va gérer le temps
     */
    private Timeline timeline;
    /**
     * la fenêtre de temps
     */
    private KeyFrame keyFrame;
    /**
     * le contrôleur associé au chronomètre
     */
    private ControleurChronometre actionTemps;

    private Text temps;

    /**
     * Constructeur permettant de créer le chronomètre
     * avec un label initialisé à "0:0:0"
     * Ce constructeur crée la Timeline, la KeyFrame et le contrôleur
     */
    public Chronometre(){
        setFont(Font.font("Arial", 20));
        setTextAlignment(TextAlignment.CENTER);
        this.actionTemps = new ControleurChronometre(this);
        this.keyFrame = new KeyFrame(Duration.millis(1000), actionTemps);
        this.temps = new Text("0");
        this.timeline = new Timeline(keyFrame);
        timeline.setCycleCount(Timeline.INDEFINITE);

    }

    /**
     * Permet au controleur de mettre à jour le text
     * la durée est affichée sous la forme m:s
     * @param tempsMillisec la durée depuis à afficher
     */
    public void setTime(long tempsMillisec){
        // A implémenter
        this.temps = new Text(Long.toString(tempsMillisec));
        HBox root = new HBox(5);
        root.getChildren().addAll(new Text("Il s’est écoulé"),
        temps,
        new Text("secondes"));
    }
    public Text getTime(){
        return this.temps;
    }
    /**
     * Permet de démarrer le chronomètre
     */
    public void start() {
        timeline.play();
    }

    /**
     * Permet d'arrêter le chronomètre
     */
    public void stop(){
        timeline.stop();
    }

    /**
     * Permet de remettre le chronomètre à 0
     */
    public void resetTime(){
        setTime(0);
    }
}
