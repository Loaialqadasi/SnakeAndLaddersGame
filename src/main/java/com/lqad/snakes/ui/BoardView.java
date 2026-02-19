package com.lqad.snakes.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map; 
import java.util.Random;

import com.lqad.snakes.engine.GameEngin;
import com.lqad.snakes.engine.GameRules;
import com.lqad.snakes.engine.MoveResult;
import com.lqad.snakes.model.Ability;
import com.lqad.snakes.model.Player;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

public class BoardView extends StackPane {

    private static final int SIZE = 10;
    private static final int TILE = 70;

    private final GameEngin engine;
    private final Map<Integer, Integer> snakesAndLadders;

    // LAYERS
    private final Pane backgroundLayer = new Pane();
    private final Pane boardLayer = new Pane();
    private final Pane objectLayer = new Pane();
    private final Pane particleLayer = new Pane();
    private final Pane playerLayer = new Pane();
    
    // UI LAYERS
    private final VBox uiSideBar = new VBox(20);
    private final Pane cardAnimationLayer = new Pane();
    private final StackPane victoryOverlay = new StackPane();
    private final StackPane dialogOverlay = new StackPane();

    // DATA
    private final Map<Player, Group> playerTokens = new HashMap<>();
    private final Map<Player, HBox> inventoryDisplays = new HashMap<>();
    private final Label turnLabel = new Label();
    private Group deckVisual; 
    
    private boolean isAnimating = false;
    private final Random random = new Random();

    public BoardView(GameEngin engine) {
        this.engine = engine;
        this.snakesAndLadders = engine.getBoard().getAllSnakesAndLadders();

        setPrefSize(1200, 800);

        buildBackground();
        buildBoard();
        drawObjects();
        buildPlayers();
        buildUI();
        buildVictoryOverlay();
        buildDialogOverlay();
        updateTurnLabel();

        StackPane gameLayers = new StackPane(
                backgroundLayer, boardLayer, objectLayer, particleLayer, playerLayer
        );

        BorderPane layout = new BorderPane();
        layout.setCenter(gameLayers);
        
        StackPane rightPane = new StackPane(uiSideBar, cardAnimationLayer);
        rightPane.setAlignment(Pos.TOP_LEFT);
        rightPane.setPickOnBounds(false); 
        uiSideBar.setPickOnBounds(true);
        cardAnimationLayer.setPickOnBounds(false);
        
        layout.setRight(rightPane);

        getChildren().addAll(layout, dialogOverlay, victoryOverlay);
    }

   
    private void buildBackground() {
        Rectangle bg = new Rectangle(1200, 800);
        bg.setFill(new RadialGradient(0, 0, 0.5, 0.5, 1, true, CycleMethod.NO_CYCLE, new Stop(0, Color.web("#2b5876")), new Stop(1, Color.web("#4e4376"))));
        backgroundLayer.getChildren().add(bg);
    }
    private void buildBoard() {
        Group gridGroup = new Group();
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                int number = (r % 2 == 0) ? r * SIZE + c + 1 : (r + 1) * SIZE - c;
                Rectangle tile = new Rectangle(TILE - 4, TILE - 4); tile.setArcWidth(20); tile.setArcHeight(20);
                boolean isEven = (r + c) % 2 == 0;
                tile.setFill(isEven ? new LinearGradient(0,0,1,1,true, CycleMethod.NO_CYCLE, new Stop(0, Color.web("#ffffff")), new Stop(1, Color.web("#e6e6e6"))) : new LinearGradient(0,0,1,1,true, CycleMethod.NO_CYCLE, new Stop(0, Color.web("#d4fc79")), new Stop(1, Color.web("#96e6a1"))));
                tile.setEffect(new DropShadow(5, Color.rgb(0,0,0,0.2)));
                Label label = new Label(String.valueOf(number)); label.setFont(Font.font("Verdana", FontWeight.BOLD, 14)); label.setTextFill(Color.rgb(0,0,0,0.5));
                StackPane cell = new StackPane(tile, label); cell.setLayoutX(c * TILE); cell.setLayoutY((SIZE - r - 1) * TILE);
                gridGroup.getChildren().add(cell);
            }
        }
        gridGroup.setTranslateX(50); gridGroup.setTranslateY(50);
        boardLayer.getChildren().add(gridGroup);
    }
    private void drawObjects() {
        for (var entry : snakesAndLadders.entrySet()) {
            int start = entry.getKey(); int end = entry.getValue();
            double[] s = getCenter(start); double[] e = getCenter(end);
            if (end > start) drawRealisticLadder(s[0], s[1], e[0], e[1]);
            else drawCartoonSnake(s[0], s[1], e[0], e[1]);
        }
    }
    private void drawRealisticLadder(double x1, double y1, double x2, double y2) {
        double width = 25; double dx = x2 - x1; double dy = y2 - y1;
        double len = Math.sqrt(dx * dx + dy * dy); double nx = -dy / len * (width / 2); double ny = dx / len * (width / 2);
        Line leftRail = new Line(x1 - nx, y1 - ny, x2 - nx, y2 - ny); leftRail.setStrokeWidth(6); leftRail.setStroke(Color.SADDLEBROWN); leftRail.setStrokeLineCap(StrokeLineCap.ROUND);
        Line rightRail = new Line(x1 + nx, y1 + ny, x2 + nx, y2 + ny); rightRail.setStrokeWidth(6); rightRail.setStroke(Color.SADDLEBROWN); rightRail.setStrokeLineCap(StrokeLineCap.ROUND);
        Group ladder = new Group(leftRail, rightRail);
        int steps = (int) (len / 20);
        for (int i = 1; i < steps; i++) {
            double ratio = (double) i / steps; double cx = x1 + dx * ratio; double cy = y1 + dy * ratio;
            Line rung = new Line(cx - nx, cy - ny, cx + nx, cy + ny); rung.setStrokeWidth(4); rung.setStroke(Color.PERU); ladder.getChildren().add(rung);
        }
        ladder.setEffect(new DropShadow(10, Color.BLACK)); objectLayer.getChildren().add(ladder);
    }
    private void drawCartoonSnake(double headX, double headY, double tailX, double tailY) {
        double ctrlX1 = headX + (tailX - headX) * 0.3 + 50; double ctrlY1 = headY + (tailY - headY) * 0.1;
        double ctrlX2 = headX + (tailX - headX) * 0.7 - 50; double ctrlY2 = tailY - (tailY - headY) * 0.1;
        CubicCurve body = new CubicCurve(headX, headY, ctrlX1, ctrlY1, ctrlX2, ctrlY2, tailX, tailY);
        body.setStrokeWidth(22); body.setStrokeLineCap(StrokeLineCap.ROUND); body.setFill(null);
        Stop[] stops = new Stop[] { new Stop(0, Color.LIMEGREEN), new Stop(0.5, Color.YELLOWGREEN), new Stop(1, Color.DARKGREEN)};
        body.setStroke(new LinearGradient(0, 0, 1, 1, true, CycleMethod.REFLECT, stops)); body.setEffect(new DropShadow(5, Color.rgb(0,0,0,0.5)));
        Group headGroup = createSnakeHead(headX, headY);
        objectLayer.getChildren().addAll(body, headGroup);
    }
    private Group createSnakeHead(double x, double y) {
        Circle headShape = new Circle(14, Color.LIMEGREEN);
        Circle leftEye = new Circle(4, Color.WHITE); leftEye.setTranslateX(-5); leftEye.setTranslateY(-4);
        Circle rightEye = new Circle(4, Color.WHITE); rightEye.setTranslateX(5); rightEye.setTranslateY(-4);
        Circle leftPupil = new Circle(1.5, Color.BLACK); leftPupil.setTranslateX(-5); leftPupil.setTranslateY(-4);
        Circle rightPupil = new Circle(1.5, Color.BLACK); rightPupil.setTranslateX(5); rightPupil.setTranslateY(-4);
        Path tongue = new Path(); tongue.getElements().addAll(new MoveTo(0, 5), new LineTo(0, 12), new LineTo(-3, 16), new MoveTo(0, 12), new LineTo(3, 16));
        tongue.setStroke(Color.RED); tongue.setStrokeWidth(2);
        Group head = new Group(tongue, headShape, leftEye, rightEye, leftPupil, rightPupil);
        head.setLayoutX(x); head.setLayoutY(y); head.setRotate(15); head.setEffect(new DropShadow(5, Color.BLACK));
        return head;
    }
    private void buildPlayers() {
        Color[] colors = {Color.RED, Color.BLUE, Color.ORANGE, Color.PURPLE};
        List<Player> modelPlayers = engine.getPlayers();
        for (int i = 0; i < modelPlayers.size(); i++) {
            Player mp = modelPlayers.get(i);
            Color c = colors[i % colors.length];
            Circle base = new Circle(12, c.darker()); Circle top = new Circle(8, c.brighter()); top.setTranslateY(-5);
            Group pawn = new Group(base, top); pawn.setEffect(new DropShadow(10, Color.BLACK));
            playerTokens.put(mp, pawn); playerLayer.getChildren().add(pawn);
            moveInstant(pawn, Math.max(1, mp.getPosition()), mp);
        }
    }
    private void buildUI() {
        uiSideBar.setPadding(new Insets(20));
        uiSideBar.setAlignment(Pos.TOP_CENTER);
        uiSideBar.setPrefWidth(300);
        turnLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22)); turnLabel.setTextFill(Color.WHITE);
        turnLabel.setStyle("-fx-background-color: rgba(0,0,0,0.3); -fx-padding: 10; -fx-background-radius: 10;");
        deckVisual = createDeckVisual(); deckVisual.setOnMouseClicked(e -> handleDeckClick());
        deckVisual.setOnMouseEntered(e -> deckVisual.setEffect(new Glow(0.5))); deckVisual.setOnMouseExited(e -> deckVisual.setEffect(null));
        Label instructions = new Label("Click Deck to Draw"); instructions.setTextFill(Color.LIGHTGRAY);
        VBox inventoryBox = new VBox(10); inventoryBox.setStyle("-fx-background-color: rgba(0,0,0,0.2); -fx-padding: 10; -fx-background-radius: 10;");
        for(Player p : engine.getPlayers()) {
            HBox row = new HBox(10); row.setAlignment(Pos.CENTER_LEFT);
            Label name = new Label(p.getName()); name.setTextFill(Color.WHITE); name.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            HBox items = new HBox(5); inventoryDisplays.put(p, items);
            row.getChildren().addAll(name, items); inventoryBox.getChildren().add(row);
        }
        uiSideBar.getChildren().addAll(turnLabel, deckVisual, instructions, new Label(""), new Label("Inventories:"), inventoryBox);
    }
    private void refreshInventoryUI() {
        for(Player p : engine.getPlayers()) {
            HBox container = inventoryDisplays.get(p); container.getChildren().clear();
            for(Ability a : p.getInventory()) {
                Label icon = new Label(a.getIcon());
                icon.setStyle("-fx-background-color: white; -fx-padding: 3; -fx-background-radius: 5; -fx-font-size: 16;");
                icon.setTooltip(new javafx.scene.control.Tooltip(a.getName()));
                container.getChildren().add(icon);
            }
        }
    }

    

    private void handleDeckClick() {
        if (isAnimating || engine.isGameOver()) return;
        initiateDrawSequence();
    }

    private void initiateDrawSequence() {
        isAnimating = true;
        int rolledValue = engine.rollDice();
        Ability loot = engine.tryLootDrop();
        
        if (loot != null) {
            engine.getCurrentPlayer().addAbility(loot);
            showLootAnimation(loot);
            refreshInventoryUI();
        }
        animateCardDraw(rolledValue);
    }

    private void animateCardDraw(int rollValue) {
        StackPane flyingCard = createCardBack();
        Point2D deckPos = deckVisual.localToScene(0, 0);
        Point2D containerPos = cardAnimationLayer.localToScene(0, 0);
        flyingCard.setLayoutX(deckPos.getX() - containerPos.getX() + 6);
        flyingCard.setLayoutY(deckPos.getY() - containerPos.getY() + 6);
        cardAnimationLayer.getChildren().add(flyingCard);

        TranslateTransition move = new TranslateTransition(Duration.millis(500), flyingCard);
        move.setToX(50); move.setToY(300);
        RotateTransition rotate = new RotateTransition(Duration.millis(500), flyingCard);
        rotate.setByAngle(360 + 10);
        
        ParallelTransition phase1 = new ParallelTransition(move, rotate, new ScaleTransition(Duration.millis(500), flyingCard));
        phase1.setOnFinished(e -> {
            ScaleTransition flipOut = new ScaleTransition(Duration.millis(150), flyingCard);
            flipOut.setToX(0);
            flipOut.setOnFinished(ev -> {
                StackPane front = createCardFront(rollValue);
                flyingCard.getChildren().setAll(front);
                ScaleTransition flipIn = new ScaleTransition(Duration.millis(150), flyingCard);
                flipIn.setFromX(0); flipIn.setToX(1.2);
                
                PauseTransition pause = new PauseTransition(Duration.millis(800));
                pause.setOnFinished(event -> handlePostRollDecisions(rollValue, flyingCard));
                new SequentialTransition(flipIn, pause).play();
            });
            flipOut.play();
        });
        phase1.play();
    }

    private void handlePostRollDecisions(int rollValue, StackPane card) {
        Player current = engine.getCurrentPlayer();
        if (current.hasAbility(Ability.DOUBLE_MOVE)) {
            showDialog("Rolled " + rollValue + "!", "Use 'x2' to move " + (rollValue*2) + "?",
                () -> {
                    current.useAbility(Ability.DOUBLE_MOVE);
                    refreshInventoryUI();
                    Label buff = new Label("x2!"); buff.setTextFill(Color.RED); buff.setFont(Font.font("Arial", FontWeight.BOLD, 40));
                    buff.setTranslateY(-60); card.getChildren().add(buff);
                    PauseTransition pt = new PauseTransition(Duration.millis(500));
                    pt.setOnFinished(ev -> removeCardAndMove(card, rollValue * 2));
                    pt.play();
                },
                () -> removeCardAndMove(card, rollValue)
            );
        } else {
            removeCardAndMove(card, rollValue);
        }
    }

    private void removeCardAndMove(StackPane card, int steps) {
        FadeTransition fade = new FadeTransition(Duration.millis(300), card);
        fade.setToValue(0);
        fade.setOnFinished(e -> {
            cardAnimationLayer.getChildren().remove(card);
            executeMove(steps);
        
        });
        fade.play();
    }

   
    private void executeMove(int steps) {
        Player currentPlayer = engine.getCurrentPlayer();
        Group token = playerTokens.get(currentPlayer);

       
        MoveResult result = GameRules.analyzeMove(
            currentPlayer, 
            steps, 
            engine.getBoard(), 
            engine.getPlayers()
        );

        
        SequentialTransition sequence = new SequentialTransition();
        
        for (int targetTile : result.getWalkPath()) {
            double[] c = getCenterWithOffset(targetTile, currentPlayer);
            TranslateTransition moveX = new TranslateTransition(Duration.millis(250), token); moveX.setToX(c[0]);
            TranslateTransition moveY = new TranslateTransition(Duration.millis(250), token); moveY.setToY(c[1]);
            ParallelTransition step = new ParallelTransition(moveX, moveY);
            step.setOnFinished(evt -> spawnDust(token));
            sequence.getChildren().add(step);
        }

       
        sequence.setOnFinished(e -> handleEventLogic(currentPlayer, token, result));
        sequence.play();
    }

    private void handleEventLogic(Player p, Group token, MoveResult result) {
        if (result.isLadder()) {
            

            Player blocker = result.getPotentialBlocker();
            if (blocker != null) {
                
                showDialog("Wait! " + blocker.getName(), "Block " + p.getName() + "'s ladder?", 
                    () -> {
                        
                        blocker.useAbility(Ability.BLOCK_LADDER);
                        refreshInventoryUI();
                        
                        animatePunishment(p, token, result.getPreJumpPosition() - 1);
                    }, 
                    () -> {
                       
                        animateJump(p, token, result.getFinalPosition(), false);
                    }
                );
            } else {
                
                animateJump(p, token, result.getFinalPosition(), false);
            }
        } 
        else if (result.isSnake()) {
            
            animateJump(p, token, result.getFinalPosition(), true);
        } 
        else {
            
            finalizeTurn(p, result.getFinalPosition());
        }
    }

    
    private void showLootAnimation(Ability ability) {
        StackPane lootIcon = new StackPane();
        Rectangle bg = new Rectangle(80, 80, Color.GOLD);
        bg.setArcWidth(20); bg.setArcHeight(20); bg.setEffect(new DropShadow(20, Color.GOLD));
        Label emoji = new Label(ability.getIcon()); emoji.setFont(Font.font(40));
        Label text = new Label(ability.getName()); text.setFont(Font.font("Arial", FontWeight.BOLD, 12)); text.setTranslateY(25);
        lootIcon.getChildren().addAll(bg, emoji, text);
        lootIcon.setTranslateX(450); lootIcon.setTranslateY(350);
        lootIcon.setScaleX(0); lootIcon.setScaleY(0);
        cardAnimationLayer.getChildren().add(lootIcon);

        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(500), lootIcon); scaleUp.setToX(1.5); scaleUp.setToY(1.5);
        RotateTransition spin = new RotateTransition(Duration.millis(500), lootIcon); spin.setByAngle(360);
        ParallelTransition appear = new ParallelTransition(scaleUp, spin);
        PauseTransition wait = new PauseTransition(Duration.millis(800));
        TranslateTransition fly = new TranslateTransition(Duration.millis(600), lootIcon); fly.setToX(400); fly.setToY(-300);
        ScaleTransition shrink = new ScaleTransition(Duration.millis(600), lootIcon); shrink.setToX(0); shrink.setToY(0);
        ParallelTransition disappear = new ParallelTransition(fly, shrink);
        SequentialTransition seq = new SequentialTransition(appear, wait, disappear);
        seq.setOnFinished(e -> cardAnimationLayer.getChildren().remove(lootIcon));
        seq.play();
    }

    private void animatePunishment(Player p, Group token, int punsihedPos) {
        double[] c = getCenterWithOffset(punsihedPos, p);
        TranslateTransition shake = new TranslateTransition(Duration.millis(100), token);
        shake.setByX(10); shake.setAutoReverse(true); shake.setCycleCount(4);
        TranslateTransition moveBack = new TranslateTransition(Duration.millis(500), token);
        moveBack.setToX(c[0]); moveBack.setToY(c[1]);
        SequentialTransition seq = new SequentialTransition(shake, moveBack);
        seq.setOnFinished(e -> { spawnWaterSplash(token, 5); finalizeTurn(p, punsihedPos); });
        seq.play();
    }

    private void animateJump(Player p, Group token, int finalDest, boolean isSnake) {
        double[] destCoords = getCenterWithOffset(finalDest, p);
        TranslateTransition jumpAnim = new TranslateTransition(Duration.millis(1000), token);
        jumpAnim.setToX(destCoords[0]); jumpAnim.setToY(destCoords[1]);
        if (isSnake) spawnWaterSplash(token, 15); else spawnFireSparkles(token, 15);
        jumpAnim.setOnFinished(ev -> finalizeTurn(p, finalDest));
        jumpAnim.play();
    }

    private void finalizeTurn(Player p, int finalPos) {
        p.setNewPosition(finalPos);
        if (finalPos == 100) {
            engine.setGameOver(true);
            showVictory(p.getName());
        } else {
            engine.switchTurn();
            updateTurnLabel();
            isAnimating = false;
        }
    }

    private void buildDialogOverlay() {
        dialogOverlay.setVisible(false);
        dialogOverlay.setStyle("-fx-background-color: rgba(0,0,0,0.7)");
    }

    private void showDialog(String title, String message, Runnable onYes, Runnable onNo) {
        VBox box = new VBox(20);
        box.setStyle("-fx-background-color: white; -fx-padding: 30; -fx-background-radius: 15; -fx-effect: dropshadow(three-pass-box, black, 20, 0, 0, 0);");
        box.setMaxSize(400, 200); box.setAlignment(Pos.CENTER);
        Label header = new Label(title); header.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        Label body = new Label(message); body.setFont(Font.font("Arial", 16));
        HBox buttons = new HBox(20); buttons.setAlignment(Pos.CENTER);
        Button btnYes = new Button("YES"); btnYes.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 30;");
        btnYes.setOnAction(e -> { dialogOverlay.setVisible(false); onYes.run(); });
        Button btnNo = new Button("NO"); btnNo.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 30;");
        btnNo.setOnAction(e -> { dialogOverlay.setVisible(false); onNo.run(); });
        buttons.getChildren().addAll(btnYes, btnNo);
        box.getChildren().addAll(header, body, buttons);
        dialogOverlay.getChildren().setAll(box); dialogOverlay.setVisible(true);
    }
    
    private Group createDeckVisual() {
        Group deckGroup = new Group();
        for(int i=0; i<3; i++) {
            Rectangle cardEdge = new Rectangle(140, 200, Color.web("#444"));
            cardEdge.setArcWidth(15); cardEdge.setArcHeight(15);
            cardEdge.setTranslateX(i*2); cardEdge.setTranslateY(i*2);
            deckGroup.getChildren().add(cardEdge);
        }
        StackPane topCard = createCardBack(); topCard.setTranslateX(6); topCard.setTranslateY(6);
        deckGroup.getChildren().add(topCard);
        return deckGroup;
    }
    
    private StackPane createCardBack() {
        Rectangle bg = new Rectangle(140, 200); bg.setArcWidth(15); bg.setArcHeight(15);
        bg.setFill(new LinearGradient(0,0,1,1,true, CycleMethod.REPEAT, new Stop(0, Color.DARKRED), new Stop(0.5, Color.MAROON), new Stop(1, Color.DARKRED)));
        bg.setStroke(Color.WHITE); bg.setStrokeWidth(3);
        Label logo = new Label("?"); logo.setTextFill(Color.WHITE); logo.setFont(Font.font("Times New Roman", FontWeight.BOLD, 50));
        return new StackPane(bg, logo);
    }
    
    private StackPane createCardFront(int value) {
        Rectangle bg = new Rectangle(140, 200, Color.WHITE); bg.setArcWidth(15); bg.setArcHeight(15); bg.setEffect(new DropShadow(10, Color.BLACK));
        Label num = new Label(String.valueOf(value)); num.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 80)); num.setTextFill(Color.BLACK);
        return new StackPane(bg, num);
    }
    
    private void spawnDust(Node node) {
        for(int i=0; i<3; i++) {
            Circle p = new Circle(3, Color.rgb(200,200,200, 0.5));
            p.setTranslateX(node.getTranslateX()); p.setTranslateY(node.getTranslateY() + 10);
            particleLayer.getChildren().add(p);
            Timeline t = new Timeline(new KeyFrame(Duration.millis(400), new KeyValue(p.opacityProperty(), 0), new KeyValue(p.scaleXProperty(), 2), new KeyValue(p.scaleYProperty(), 2)));
            t.setOnFinished(e -> particleLayer.getChildren().remove(p)); t.play();
        }
    }
    
    private void spawnWaterSplash(Node node, int count) {
        for (int i = 0; i < count * 3; i++) {
            Circle p = new Circle(random.nextInt(4) + 2, Color.SKYBLUE);
            p.setTranslateX(node.getTranslateX()); p.setTranslateY(node.getTranslateY());
            particleLayer.getChildren().add(p);
            TranslateTransition tt = new TranslateTransition(Duration.millis(800), p);
            tt.setByX((random.nextDouble()-0.5)*100); tt.setByY(100);
            FadeTransition ft = new FadeTransition(Duration.millis(800), p); ft.setToValue(0);
            ParallelTransition pt = new ParallelTransition(tt, ft); pt.setOnFinished(e -> particleLayer.getChildren().remove(p)); pt.play();
        }
    }
    
    private void spawnFireSparkles(Node node, int count) {
        for (int i = 0; i < count * 3; i++) {
            Circle p = new Circle(random.nextInt(3) + 2, Color.GOLD); p.setEffect(new Glow(0.8));
            p.setTranslateX(node.getTranslateX()); p.setTranslateY(node.getTranslateY());
            particleLayer.getChildren().add(p);
            TranslateTransition tt = new TranslateTransition(Duration.millis(1000), p);
            tt.setByX((random.nextDouble() - 0.5) * 60); tt.setByY(-100);
            ScaleTransition st = new ScaleTransition(Duration.millis(1000), p); st.setToX(0); st.setToY(0);
            ParallelTransition pt = new ParallelTransition(tt, st); pt.setOnFinished(e -> particleLayer.getChildren().remove(p)); pt.play();
        }
    }
    
    private void moveInstant(Node node, int position, Player p) {
        double[] c = getCenterWithOffset(position, p); 
        node.setTranslateX(c[0]); node.setTranslateY(c[1]);
    }
    
    private void updateTurnLabel() {
        if (!engine.isGameOver()) turnLabel.setText("Turn: " + engine.getCurrentPlayer().getName());
    }
    
    private void buildVictoryOverlay() {
        victoryOverlay.setVisible(false); victoryOverlay.setStyle("-fx-background-color: rgba(0,0,0,0.85)");
        VBox box = new VBox(10); box.setAlignment(Pos.CENTER);
        Label text = new Label("VICTORY!"); text.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 80));
        text.setTextFill(Color.GOLD); text.setEffect(new Glow(1.0));
        box.getChildren().add(text); victoryOverlay.getChildren().add(box);
    }
    
    private void showVictory(String winnerName) {
        victoryOverlay.setVisible(true);
        VBox box = (VBox) victoryOverlay.getChildren().get(0);
        Label l = (Label) box.getChildren().get(0); l.setText(winnerName + " WINS!");
        Timeline fireworks = new Timeline(new KeyFrame(Duration.millis(300), e -> spawnFireSparkles(l, 20)));
        fireworks.setCycleCount(20); fireworks.play();
    }
    
    private double[] getCenter(int position) {
        int row = (position - 1) / SIZE;
        int col;
        if (row % 2 == 0) col = (position - 1) % SIZE;
        else col = SIZE - 1 - ((position - 1) % SIZE);
        double x = 50 + col * TILE + TILE / 2.0;
        double y = 50 + (SIZE - row - 1) * TILE + TILE / 2.0;
        return new double[]{x, y};
    }
    
    private double[] getCenterWithOffset(int position, Player p) {
        double[] center = getCenter(position);
        int playerIndex = engine.getPlayers().indexOf(p);
        double offsetX = 0; double offsetY = 0;
        if (playerIndex == 0) { offsetX = -15; offsetY = -15; }
        else if (playerIndex == 1) { offsetX = 15; offsetY = -15; }
        else if (playerIndex == 2) { offsetX = -15; offsetY = 15; }
        else if (playerIndex == 3) { offsetX = 15; offsetY = 15; }
        return new double[] { center[0] + offsetX, center[1] + offsetY };
    }
}