import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.text.FontWeight;
import java.awt.image.Raster;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.plaf.ToolTipUI;
import java.util.Arrays;
import java.io.File;
import java.util.ArrayList;



/**
 * Vue du jeu du pendu
 */
public class Pendu extends Application {
    /**
     * modèle du jeu
     **/
    private MotMystere modelePendu;
    /**
     * Liste qui contient les images du jeu
     */
    private ArrayList<Image> lesImages;
    /**
     * Liste qui contient les noms des niveaux
     */    
    public List<String> niveaux;
    // les différents contrôles qui seront mis à jour ou consultés pour l'affichage
    /**
     * le dessin du pendu
     */
    private ImageView dessin;
    /**
     * le mot à trouver avec les lettres déjà trouvé
     */
    private Text motCrypte;
    /**
     * la barre de progression qui indique le nombre de tentatives
     */
    private ProgressBar pg;
    /**
     * le clavier qui sera géré par une classe à implémenter
     */
    private Clavier clavier;
    /**
     * le text qui indique le niveau de difficulté
     */
    private Text leNiveau;
    /**
     * le chronomètre qui sera géré par une clasee à implémenter
     */
    private Chronometre chrono;
    /**
     * le panel Central qui pourra être modifié selon le mode (accueil ou jeu)
     */
    private Pane panelCentral;
    /**
     * le bouton Paramètre / Engrenage
     */
    private Button boutonParametres;
    /**
     * le bouton Accueil / Maison
     */    
    private Button boutonMaison;
    /** 
     * le bouton d'informations
     */
    private Button boutonInfo;
    /**
     * le bouton qui permet de lancer/relancer une partie
     */ 
    private Button bJouer;
    /**
     * partie de pendu en cours d'execution
     */
    private boolean partieEnCours;

    /**
     * initialise les attributs (créer le modèle, charge les images, crée le chrono ...)
     */


    @Override
    public void init() {
        this.modelePendu = new MotMystere("src/french", 3, 10, MotMystere.FACILE, 10);
        this.lesImages = new ArrayList<Image>();
        this.chargerImages();

        this.niveaux=new ArrayList<>();
        this.niveaux.add("Facile");
        this.niveaux.add("Moyen");
        this.niveaux.add("Difficile");
        this.niveaux.add("Expert");

        this.motCrypte = new Text();
        this.partieEnCours=false;
        

        this.panelCentral= new Pane();

        this.boutonParametres = new Button();
        Image imgp = new Image("file:img/parametres.png");
        ImageView imageParam = new ImageView(imgp);
        imageParam.setFitWidth(40);
        imageParam.setFitHeight(40);
        this.boutonParametres.setGraphic(imageParam);
        this.boutonParametres.setOnAction(new ControleurParametre(this));
        this.boutonParametres.setTooltip(new Tooltip("Accéder aux paramètres"));



        this.boutonMaison = new Button();
        ImageView imageMaison = new ImageView(new Image("file:img/home.png"));
        imageMaison.setFitWidth(40);
        imageMaison.setFitHeight(40);
        this.boutonMaison.setGraphic(imageMaison);
        this.boutonMaison.setOnAction(new ControlleurRetourAccueil(modelePendu, this));
        this.boutonMaison.setTooltip(new Tooltip("Retour à l'accueil"));
        

        this.boutonInfo = new Button();
        ImageView imageInfo = new ImageView(new Image("file:img/info.png"));
        imageInfo.setFitWidth(40);
        imageInfo.setFitHeight(40);
        this.boutonInfo.setGraphic(imageInfo);
        this.boutonInfo.setOnAction(new ControleurInfos(this));
        this.boutonInfo.setTooltip(new Tooltip("Afficher les règles du jeu"));

        this.bJouer = new Button("Jouer");
        this.bJouer.setPrefSize(100, 50);
        this.bJouer.setOnAction(new ControleurLancerPartie(modelePendu, this));
        this.bJouer.setTooltip(new Tooltip("Lance la partie"));

        this.pg = new ProgressBar();
        this.pg.setProgress(0);

        this.chrono=new Chronometre();
        this.dessin=new ImageView();

        this.clavier=new Clavier("ABCDEFGHIJKLMNOPQRSTUVWXYZ-", new ControleurLettres(modelePendu, this));
    }


    /**
     * @return si la partie est en cours ou non
     */
    public boolean getPartieEnCours(){
        return this.partieEnCours;
    }

    /**
     * débute ou arrête la partie
     */
    public void setPartieEnCours(boolean bool){
        this.partieEnCours=bool;
    }

    /**
     * @return  le graphe de scène de la vue à partir de methodes précédantes
     */
    private Scene laScene(){
        BorderPane fenetre = new BorderPane();
        fenetre.setTop(this.titre());
        fenetre.setCenter(this.panelCentral);
        return new Scene(fenetre, 800, 1000);
    }

    /**
     * @return le panel contenant le titre du jeu
     */
    private BorderPane titre(){
        // A implementer          
        BorderPane banniere = new BorderPane();
        Text text = new Text("Jeu du Pendu");
        text.setFont(new Font(30));
        text.setTextAlignment(TextAlignment.CENTER);
        banniere.setLeft(text);
        HBox hbox= new HBox();
        hbox.getChildren().add(this.boutonMaison);
        hbox.getChildren().add(this.boutonParametres);
        hbox.getChildren().add(this.boutonInfo);
        hbox.setSpacing(10);
        banniere.setRight(hbox);
        banniere.setPadding(new Insets(10, 10, 10, 10));
        banniere.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        return banniere;
    }

    /**
     * @return le panel du chronomètre
     */
    private TitledPane leChrono(){
        TitledPane res = new TitledPane();
        res.setText("Chronomètre");
        res.setAlignment(Pos.CENTER);
        res.setContent(this.chrono);
        res.setCollapsible(false);
        return res;
    }

    /**
     * @return la fenêtre de jeu avec le mot crypté, l'image, la barre de progression et le clavier
     */
    private Pane fenetreJeu(){
        BorderPane res = new BorderPane();
        BorderPane border= new BorderPane();
        VBox gauche = new VBox();
        VBox droite = new VBox();
        this.modelePendu.setMotATrouver();
        this.motCrypte.setText(this.modelePendu.getMotCrypte());
        this.leNiveau=new Text("Niveau : "+this.niveaux.get(modelePendu.getNiveau()));

        //Construction de la partie gauche de la fenêtre de jeu
        this.motCrypte.setFont(new Font(30));
        gauche.getChildren().addAll(this.motCrypte,this.dessin,this.pg,this.clavier);
        gauche.setSpacing(10);
        gauche.setAlignment(Pos.CENTER);
        gauche.setPadding(new Insets(10, 10, 10, 10));
        gauche.setPrefSize(400,800);
        

        //Construction de la partie droite de la fenêtre de jeu
        Button nvMot= new Button("Nouveau mot");
        nvMot.setOnAction(new ControleurLancerPartie(modelePendu, this));
        this.leNiveau=new Text("Niveau : "+this.niveaux.get(modelePendu.getNiveau()));
        this.leNiveau.setFont(new Font(25));
        droite.getChildren().addAll(this.leNiveau ,this.leChrono(),nvMot);
        droite.setSpacing(10);
        droite.setPrefSize(375,800);

        //Désactivation/ Activation des boutons
        this.boutonParametres.setDisable(true);
        this.boutonMaison.setDisable(false);

        //Construction de la fenêtre de jeu
        border.setLeft(gauche);
        border.setRight(droite);
        border.setPadding(new Insets(10, 10, 10, 10));
        res.setCenter(border);
        this.majAffichage();

        return border;
    }

    /**
     * @return la fenêtre d'accueil sur laquelle on peut choisir les paramètres de jeu
     */
    private Pane fenetreAccueil(){
        BorderPane res = new BorderPane();
        VBox vb = new VBox();
        vb.setSpacing(15);

        this.boutonParametres.setDisable(false);
        this.boutonMaison.setDisable(true);
        TitledPane tp = new TitledPane();
        tp.setCollapsible(false);
        tp.setText("Choisissez votre niveau de difficulté");
        VBox vbtp = new VBox();
        RadioButton rb1 = new RadioButton("Facile");
        RadioButton rb2 = new RadioButton("Moyen");
        RadioButton rb3 = new RadioButton("Difficile");
        RadioButton rb4 = new RadioButton("Expert");
        rb1.setSelected(true);
        rb1.setOnAction(new ControleurNiveau(this.modelePendu));
        rb2.setOnAction(new ControleurNiveau(this.modelePendu));
        rb3.setOnAction(new ControleurNiveau(this.modelePendu));
        rb4.setOnAction(new ControleurNiveau(this.modelePendu));
        ToggleGroup niveaux = new ToggleGroup();
        rb1.setToggleGroup(niveaux);
        rb2.setToggleGroup(niveaux);
        rb3.setToggleGroup(niveaux);
        rb4.setToggleGroup(niveaux);
        vbtp.getChildren().addAll(rb1, rb2, rb3, rb4);
        vbtp.setSpacing(10);
        tp.setContent(vbtp);
        tp.setPrefSize(750, 150);
        vb.getChildren().addAll(this.bJouer,tp);
        vb.setPadding(new Insets(15, 15, 15, 15));

        res.setCenter(vb);

        return res;
    }

    /**
     * charge les images à afficher en fonction des erreurs
     */
    private void chargerImages(){
        //for (int i=0; i<this.modelePendu.getNbErreursMax()+1; i++){
        //    this.lesImages.add(new Image("img/pendu"+i+".png"));
        //}

        for (int i=0; i<this.modelePendu.getNbErreursMax()+1; i++){
            File file = new File("img/pendu"+i+".png");
            System.out.println(file.toURI().toString());
            this.lesImages.add(new Image(file.toURI().toString()));
        }
    }

    public void modeAccueil(){
        this.panelCentral.getChildren().clear();
        this.chrono.stop();
        this.panelCentral.getChildren().add(this.fenetreAccueil());
    }
    
    public void modeJeu(){
        this.panelCentral.getChildren().clear();
        this.chrono.start();
        this.panelCentral.getChildren().add(this.fenetreJeu());
    }
    
    
    /** lance une partie */
    public void lancePartie(){
        this.partieEnCours=true;
        this.modelePendu.setMotATrouver();
        this.clavier.activeTouches();
        this.chrono.resetTime();
        this.chrono.setTime(0);
        this.pg.setProgress(0);

        this.modeJeu();
    }

    /**
     * raffraichit l'affichage selon les données du modèle
     */
    public void majAffichage(){

        //Affichage de la bonne image
        int i = this.modelePendu.getNbErreursMax()-this.modelePendu.getNbErreursRestants();
        this.dessin.setImage(this.lesImages.get(i));

        //Calculs du pourcentage de lettres découvertes pour la barre de progression
        double nbLettresDecouverte= this.modelePendu.getMotATrouve().length()-this.modelePendu.getNbLettresRestantes();
        double longueurMot=this.modelePendu.getMotATrouve().length();
        this.pg.setProgress(nbLettresDecouverte/longueurMot);

        this.motCrypte.setText(this.modelePendu.getMotCrypte());
        
        //Désactivation des touches déjà essayées
        this.clavier.desactiveTouches(this.modelePendu.getLettresEssayees());
        System.out.println(this.modelePendu.getLettresEssayees());
        
        //Affiche les différents messages en fonction de l'état de la partie
        if (this.modelePendu.gagne()){
            this.chrono.stop();
            this.popUpMessageGagne().showAndWait();
        } else if (this.modelePendu.perdu()) {
            this.chrono.stop();
            this.popUpMessagePerdu().showAndWait();
        }

        // Peu importe si c'est perdu ou gagné, on set à false la partie en cours et on retourne à l'acceuil
        if (this.modelePendu.gagne() || this.modelePendu.perdu()){
            this.partieEnCours=false;
            this.modeAccueil();
        }

    }

    /**
     * Affiche une pop-up pour indiquer que la partie est perdu (cas où le temps est écoulé)
     */
    public void gameOver(){

        if (this.partieEnCours){    //Si la partie est en cours, on affiche une pop-up (simple sécurité pour éviter d'afficher plusieurs fois la même pop-up)
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Partie perdue");
            alert.setHeaderText(null);
            alert.setContentText("Dommage, vous avez utilisé toutes vos chances ! Le mot était " + this.modelePendu.getMotATrouve()+".");
            Platform.runLater(alert::showAndWait);
            this.modeAccueil();
        }

        this.partieEnCours=false;
    }

    /**
     * accesseur du chronomètre (pour les controleur du jeu)
     * @return le chronomètre du jeu
     */
    public Chronometre getChrono(){
        return this.chrono;
    }

    /**
     * Permet de créer une pop-up pour afficher un message pour prévenir que la partie est en cours
     * @return Une pop-up avec un message et des boutons pour continuer ou non la partie
     */
    public Alert popUpPartieEnCours(){
        String message = "La partie est en cours ! Etes-vous sûr de l'interrompre ?";
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,message, ButtonType.YES, new ButtonType("Continuer la partie"), ButtonType.NO);
        alert.setTitle("Partie en cours");
        alert.setHeaderText(null);
        return alert;
    }
        
    /** 
     * Permet de créer une pop-up pour afficher une pop-up contenant les règles du jeu
     * @return Une pop-up avec un message et un bouton pour fermer la pop-up
     */
    public Alert popUpReglesDuJeu(){
        String message= "Le but du jeu est simple : deviner toutes les lettres du mot mystère.\n"+
                "Quelques lettres sont déjà données, en fonction du niveau de jeu choisi.\n"+
                "Chaque lettre selectionnée est affichée dans le mot s'il la contient.\n"+
                "Dans le cas contraire, le dessin d’un pendu se met à apparaître";
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle("Règles du jeu");
        alert.setHeaderText("Règles du jeu");
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        return alert;
    }
    
    /**
     * Permet de créer une pop-up pour afficher un message pour prévenir que la partie est gagnée
     * @return Une pop-up avec un message et un bouton pour fermer la pop-up
     */
    public Alert popUpMessageGagne(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Partie finie");
        alert.setHeaderText(null);
        alert.setContentText("Bravo, vous avez gagné en " + this.chrono.getText() + " !");
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        return alert;
    }
    
    /**
     * Permet de créer une pop-up pour afficher un message pour prévenir que la partie est perdue
     * @return Une pop-up avec un message et un bouton pour fermer la pop-up
     */
    public Alert popUpMessagePerdu(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Partie finie");
        alert.setHeaderText(null);
        alert.setContentText("Dommage, vous avez perdu ! Le mot était " + this.modelePendu.getMotATrouve()+".");
        return alert;
    }

    /**
     * Permet de créer une pop-up pour afficher les paramètres
     * @return Une pop-up avec un message et un bouton pour fermer la pop-up
     */
    public Alert modeParametres(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Parametres");
        alert.setHeaderText(null);
        alert.setContentText("Ici on retrouve les paramètres");
        return alert;
    }

    /**
     * créer le graphe de scène et lance le jeu
     * @param stage la fenêtre principale
     */
    @Override
    public void start(Stage stage) {
        stage.setTitle("IUTEAM'S - La plateforme de jeux de l'IUTO");
        stage.setScene(this.laScene());
        this.modeAccueil();
        stage.show();
    }

    /**
     * Programme principal
     * @param args inutilisé
     */
    public static void main(String[] args) {
        launch(args);
    }    
}

