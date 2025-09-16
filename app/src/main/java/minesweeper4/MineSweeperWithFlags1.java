package minesweeper4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.*;

public class MineSweeperWithFlags1 {
    

    private static final int MINE = -10;
    private static final int SIZE = 700;
    private static final double MINE_MULTIPLIER = 1.5;
    // MINE_MULTIPLIER = 2;
    private static Cell[] reusableStorage = new Cell[8];
    private final int gridSize;
    private Cell[][] cells;
    private JFrame frame;
    private JButton reset;
    private JButton giveUp;
    private JButton changeDif;
    private final static String Flagadress = "C:\\Users\\robkr\\Desktop\\flag2.png";
    

    private final ActionListener actionListener = actionEvent -> {
        Object source = actionEvent.getSource();
        if (source == reset) {
            createMines();
        } else if (source == giveUp) {
            revealBoardAndDisplay("You gave up.");
        } else if (source == changeDif){
            createMines();}
        
        else {
            // handleCell((Cell) source);
        }
    };

    private class Cell extends JButton {
        
        private final int row;
        private final int col;
        private int value;
        private boolean isFlagged;

        Cell(final int row, final int col, final ActionListener actionListener) {
            this.row = row;
            this.col = col;
            addActionListener(actionListener);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        handleLeftClick();
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        handleRightClick();
                    }
                }
            });
            setText("");
        }

        int getValue() {
            return value;
        }

        void setValue(int value) {
            this.value = value;
        }

        boolean isAMine() {
            return value == MINE;
        }

        void reset() {
            setValue(0);
            isFlagged = false;
            setEnabled(true);
            setIcon(null);
            setText("");
        }

        void reveal() {
            
            setEnabled(false);
            setText(isAMine() ? "X" : String.valueOf(value));
            //set Icon
            setIcon(null); 
// TEST set Icon?
        }

        void updateNeighbourCount() {
            getNeighbours(reusableStorage);
            for (Cell neighbour : reusableStorage) {
                if (neighbour == null) {
                    break;
                }
                if (neighbour.isAMine()) {
                    value++;
                }
            }
        }

        void getNeighbours(final Cell[] container) {
            for (int i = 0; i < reusableStorage.length; i++) {
                reusableStorage[i] = null;
            }

            int index = 0;

            for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
                for (int colOffset = -1; colOffset <= 1; colOffset++) {
                    if (rowOffset == 0 && colOffset == 0) {
                        continue;
                    }
                    int rowValue = row + rowOffset;
                    int colValue = col + colOffset;

                    if (rowValue < 0 || rowValue >= gridSize || colValue < 0 || colValue >= gridSize) {
                        continue;
                    }

                    container[index++] = cells[rowValue][colValue];
                }
            }
        }

        private void handleLeftClick() {
            if (!isEnabled() || isFlagged) {
                return;
            }

            if (isAMine()) {
                setForeground(Color.RED);
                reveal();
                revealBoardAndDisplay("You clicked on a mine!");
                return;
            }
            if (getValue() == 0) {
                Set<Cell> positions = new HashSet<>();
                positions.add(this); // = add cell
                cascade(positions);
            } else {
                reveal();
            }
            checkForWin();
        }

        private void handleRightClick() {
            if (!isEnabled()) {
                return;
            }

            if (isFlagged) {
                isFlagged = false;
                setIcon(null);
                // setText("");
            } else {
                isFlagged = true;
                // setText("#");
                setIcon(new ImageIcon(Flagadress));
                
            }
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null || getClass() != obj.getClass())
                return false;
            Cell cell = (Cell) obj;
            return row == cell.row && col == cell.col;
        }

        // @Override
        // public int hashCode() {
        //     return Objects.hash(row, col);
        // }
    }

    private MineSweeperWithFlags1(final int gridSize) {
        this.gridSize = gridSize;
        cells = new Cell[gridSize][gridSize];

        frame = new JFrame("Minesweeper");
        frame.setSize(SIZE, SIZE);
        frame.setLayout(new BorderLayout());

        initializeButtonPanel();
        initializeGrid();

        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void initializeButtonPanel() {
        JPanel buttonPanel = new JPanel();

        reset = new JButton("Reset");
        giveUp = new JButton("Give Up");
        changeDif = new JButton("change dif");

        reset.addActionListener(actionListener);
        giveUp.addActionListener(actionListener);
        changeDif.addActionListener(actionListener);

        buttonPanel.add(reset);
        buttonPanel.add(giveUp);
        buttonPanel.add(changeDif);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        
    }

    private void initializeGrid() {
        Container grid = new Container();
        grid.setLayout(new GridLayout(gridSize, gridSize));

        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                cells[row][col] = new Cell(row, col, actionListener);
                grid.add(cells[row][col]);
            }
        }
        createMines();
        frame.add(grid, BorderLayout.CENTER);
    }

    private void resetAllCells() {
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                cells[row][col].reset();
            }
        }
    }

    private void createMines() {
        resetAllCells();

        final int mineCount = (int) (MINE_MULTIPLIER * gridSize);
        final Random random = new Random();
        Set<Integer> positions = new HashSet<>(gridSize * gridSize);
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                positions.add(row * gridSize + col);
            }
        }

        for (int index = 0; index < mineCount; index++) {
            int choice = random.nextInt(positions.size());
            int row = choice / gridSize;
            int col = choice % gridSize;
            cells[row][col].setValue(MINE);
            positions.remove(choice);
        }

        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                if (!cells[row][col].isAMine()) {
                    cells[row][col].updateNeighbourCount();
                }
            }
        }
    }

    private void revealBoardAndDisplay(String message) {
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                if (!cells[row][col].isEnabled()) { //if not enabled
                    continue; // Skip already revealed cells
                }
    
                cells[row][col].reveal(); //after check reveal
            }
        }
    
        JOptionPane.showMessageDialog(
                frame, message, "Game Over",
                JOptionPane.ERROR_MESSAGE);
    
        createMines();
    }
    

    private void cascade(Set<Cell> positionsToClear) {
        while (!positionsToClear.isEmpty()) {
            Cell cell = positionsToClear.iterator().next();
            positionsToClear.remove(cell);
            cell.reveal();

            cell.getNeighbours(reusableStorage);
            for (Cell neighbour : reusableStorage) {
                if (neighbour == null) {
                    break;
                }
                if (neighbour.getValue() == 0 && neighbour.isEnabled()) {
                    positionsToClear.add(neighbour);
                } else {
                    neighbour.reveal();
                }
            }
        }
    }

    private void checkForWin() {
        boolean won = true;
        outer: for (Cell[] cellRow : cells) {
            for (Cell cell : cellRow) {
                if (!cell.isAMine() && cell.isEnabled()) {
                    won = false;
                    break outer;
                }
            }
        }

        if (won) {
            JOptionPane.showMessageDialog(
                    frame, "You have won!", "Congratulations",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private static void run(final int gridSize) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new MineSweeperWithFlags1(gridSize);
    }

    public static void main(String[] args) {
        System.out.println(new File(Flagadress).exists() ? "flag loaded correctly" : "fehler");

        final int gridSize = 15;
        SwingUtilities.invokeLater(() -> MineSweeperWithFlags1.run(gridSize));
    }
}
