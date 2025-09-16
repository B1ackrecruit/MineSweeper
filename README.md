# MineSweeper4

A simple Java implementation of the classic Minesweeper game with flagging functionality and a graphical user interface (GUI) using Swing.

## Features
- Play Minesweeper on a customizable grid
- Flag suspected mines with a right-click
- Reset, give up, and change difficulty buttons
- Automatically reveals empty cells (cascade)
- Win detection and game over dialog
- Resource-efficient icon loading (hopefully ...)

## Requirements
- Java 8 or higher
- Gradle (recommended for building and running)

## How to Run
1. Clone the repository:
   ```
   git clone https://github.com/rkreitschmann/MineSweeper
   ```
2. Navigate to the project directory:
   ```
   cd MineSweeper4
   ```
3. Build and run the project using Gradle:
   ```
   gradlew run
   ```
   Or, if you have Gradle installed:
   ```
   gradle run
   ```

## Project Structure
- `app/src/main/java/minesweeper4/` - Main Java source files
- `app/src/main/resources/` - Flag icon images
- `app/src/test/java/minesweeper4/` - Test files

## How to Play
- Left-click a cell to reveal it
- Right-click a cell to place or remove a flag
- Click "Reset" to start a new game
- Click "Give Up" to reveal the board
- Click "Change Dif" to change difficulty (WIP)

## Customization
- You can change the grid size and mine multiplier in `MineSweeperWithFlags1.java`
- Replace flag icons in `app/src/main/resources/` with your own images

## License
This project is licensed under the GNU General Public License v3.0 (GPLv3).
See the LICENSE file for details.

## Author
- B1ackrecruit
