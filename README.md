🐍 Snakes & Ladders: JavaFX Enhanced Edition

A modern, interactive, and strategic interpretation of the classic Snakes and Ladders board game, built using JavaFX and Maven.

This project goes beyond the standard game by introducing an Inventory System, Ability Cards, physics-based particle effects, and procedural animations.
✨ Key Features

    🎮 Interactive UI:

        Smooth character movement animations.

        Procedurally drawn snakes and ladders (no static images).

        Dynamic card flipping and deck animations.

    🎒 Strategy & Inventory System:

        Loot Drops: Random chance to find items while playing.

        Double Speed (x2): Multiply your dice roll by 2.

        Ladder Trap: Block an opponent from climbing a ladder and send them back!

        Visual Inventory: Track your items in the sidebar.

    ✨ Visual Effects:

        Particle Systems: Gold sparkles for ladders, water splashes for snakes, and dust clouds for walking.

        Feedback Animations: "Too High" warnings for overshooting the win condition and victory fireworks.

    📏 Game Logic:

        Exact 100 Rule: Players must land exactly on tile 100 to win. If the roll is too high, the player stays put.

        2-4 Player Support: Dynamic turn switching.

🛠️ Prerequisites

To run this project, ensure you have the following installed:

    Java Development Kit (JDK) 17 or higher.

    Apache Maven (for dependency management and building).

🚀 How to Run

    Clone the Repository:
    code Bash

    git clone https://github.com/Loaialqadasi/SnakeAndLaddersGame.git
    cd snakes-and-ladders

    Run with Maven:
    The project is configured with the JavaFX Maven plugin. Run the following command in your terminal (inside the project root):
    code Bash

    mvn clean javafx:run

🕹️ How to Play

    Start the Game: Select the number of players (2 to 4) from the popup dialog.

    Roll the Dice: Click the Card Deck on the right sidebar to draw a number.

    Abilities:

        If you have a Double Move card, the game will ask if you want to use it after you see your roll.

        If an opponent lands on a Ladder and you have a Trap Card, the game will pause and ask if you want to block them.

    Winning: The first player to land exactly on Tile 100 wins.

📂 Project Structure

The project follows a clean MVC (Model-View-Controller) architecture:
code Code

src/main/java/com/lqad/snakes
├── engine
│   └── GameEngin.java      # Core game loop and turn management
├── model
│   ├── Ability.java        # Enum for special cards (Double Move, Trap)
│   ├── Board.java          # Logic for board layout, snakes, and ladders
│   ├── Card.java           # Data structure for movement cards
│   ├── Deck.java           # Random number generation logic
│   └── Player.java         # Player state (position, inventory)
├── ui
│   ├── BoardView.java      # Main Game UI, Animations, and Event Handling
│   └── GameApp.java        # JavaFX Entry Point
└── Main.java               # Application Launcher

📸 Screenshots

(You can upload screenshots to your repo and link them here later)
👨‍💻 Author

Developed by Loai AlQadasi.
Built as a demonstration of advanced JavaFX animation and object-oriented game logic.