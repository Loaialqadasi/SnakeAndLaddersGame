🐍 Snakes & Ladders: JavaFX Enhanced Edition

A modern, interactive, and strategic interpretation of the classic Snakes and Ladders board game, built using JavaFX and Maven.

This project goes beyond the standard game by introducing an Inventory System, Ability Cards, physics-based particle effects, and procedural animations.


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




👨‍💻 Author
Developed by Loai AlQadasi.
Built as a demonstration of advanced JavaFX animation and object-oriented game logic.