
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Minesweeper {
    int tileSize = 70;
    int numRows = 10;
    int numCols;
    int boardWidth;
    int boardHeight;
    JFrame frame;
    JLabel textLabel;
    JPanel textPanel;
    JPanel boardPanel;
    int mineCount;
    MineTile[][] board;
    ArrayList<MineTile> mineList;
    Random random;
    int tilesClicked;
    boolean gameOver;

    Minesweeper() {
        this.numCols = this.numRows;
        this.boardWidth = this.numCols * this.tileSize;
        this.boardHeight = this.numRows * this.tileSize;
        this.frame = new JFrame("MineSweeper");
        this.textLabel = new JLabel();
        this.textPanel = new JPanel();
        this.boardPanel = new JPanel();
        this.mineCount = 14;
        this.board = new MineTile[this.numRows][this.numCols];
        this.random = new Random();
        this.tilesClicked = 0;
        this.gameOver = false;
        this.frame.setSize(this.boardWidth, this.boardHeight);
        this.frame.setLocationRelativeTo((Component)null);
        this.frame.setResizable(false);
        this.frame.setDefaultCloseOperation(3);
        this.frame.setLayout(new BorderLayout());
        this.textLabel.setFont(new Font("Arial", 1, 25));
        this.textLabel.setHorizontalAlignment(0);
        this.textLabel.setText("MineSweeper 1.0");
        this.textLabel.setOpaque(true);
        this.textPanel.setLayout(new BorderLayout());
        this.textPanel.add(this.textLabel);
        this.frame.add(this.textPanel, "North");
        this.boardPanel.setLayout(new GridLayout(this.numRows, this.numCols));
        this.boardPanel.setBackground(Color.getHSBColor(127.0F, 63.0F, 0.0F));
        this.frame.add(this.boardPanel);

        for(int r = 0; r < this.numRows; ++r) {
            for(int c = 0; c < this.numCols; ++c) {
                MineTile tile = new MineTile(r, c);
                this.board[r][c] = tile;
                tile.setFocusable(false);
                tile.setMargin(new Insets(0, 0, 0, 0));
                tile.setFont(new Font("Arial Unicode MS", 0, 45));
                tile.setBackground(Color.green);
                tile.addMouseListener(new MouseAdapter() {
                    public void mousePressed(MouseEvent e) {
                        if (!Minesweeper.this.gameOver) {
                            MineTile tile = (MineTile)e.getSource();
                            if (e.getButton() == 1) {
                                if (tile.getText() == "") {
                                    if (Minesweeper.this.mineList.contains(tile)) {
                                        Minesweeper.this.revealMines();
                                    } else {
                                        Minesweeper.this.checkMine(tile.r, tile.c);
                                    }
                                }
                            } else if (e.getButton() == 3) {
                                if (tile.getText() == "" && tile.isEnabled()) {
                                    tile.setText("\ud83c\udff4");
                                } else if (tile.getText() == "\ud83c\udff4") {
                                    tile.setText("");
                                }
                            }

                        }
                    }
                });
                this.boardPanel.add(tile);
            }
        }

        this.frame.setVisible(true);
        this.setMines();
    }

    void setMines() {
        this.mineList = new ArrayList();
        int mineLeft = this.mineCount;

        while(mineLeft > 0) {
            int r = this.random.nextInt(this.numRows);
            int c = this.random.nextInt(this.numCols);
            MineTile tile = this.board[r][c];
            if (!this.mineList.contains(tile)) {
                this.mineList.add(tile);
                --mineLeft;
            }
        }

    }

    void revealMines() {
        for(int i = 0; i < this.mineList.size(); ++i) {
            MineTile tile = (MineTile)this.mineList.get(i);
            tile.setText("\ud83d\udca3");
            tile.setBackground(Color.black);
        }

        this.gameOver = true;
        this.textLabel.setText("Uh Oh... BOOM");
    }

    void checkMine(int r, int c) {
        if (r >= 0 && r < this.numRows && c >= 0 && c < this.numCols) {
            MineTile tile = this.board[r][c];
            if (tile.isEnabled()) {
                tile.setEnabled(false);
                tile.setBackground(Color.white);
                ++this.tilesClicked;
                int minesFound = 0;
                minesFound += this.countMines(r - 1, c - 1);
                minesFound += this.countMines(r - 1, c);
                minesFound += this.countMines(r - 1, c + 1);
                minesFound += this.countMines(r, c - 1);
                minesFound += this.countMines(r, c + 1);
                minesFound += this.countMines(r + 1, c - 1);
                minesFound += this.countMines(r + 1, c);
                minesFound += this.countMines(r + 1, c + 1);
                if (minesFound > 0) {
                    tile.setText(Integer.toString(minesFound));
                } else {
                    tile.setText("");
                    this.checkMine(r - 1, c - 1);
                    this.checkMine(r - 1, c);
                    this.checkMine(r - 1, c + 1);
                    this.checkMine(r, c - 1);
                    this.checkMine(r, c + 1);
                    this.checkMine(r + 1, c - 1);
                    this.checkMine(r + 1, c);
                    this.checkMine(r + 1, c + 1);
                }

                if (this.tilesClicked == this.numRows * this.numCols - this.mineList.size()) {
                    this.gameOver = true;
                    this.textLabel.setText("PHEW all bombs found");
                }

            }
        }
    }

    int countMines(int r, int c) {
        if (r >= 0 && r < this.numRows && c >= 0 && c < this.numCols) {
            return this.mineList.contains(this.board[r][c]) ? 1 : 0;
        } else {
            return 0;
        }
    }

    private class MineTile extends JButton {
        int r;
        int c;

        public MineTile(int r, int c) {
            this.r = r;
            this.c = c;
        }
    }
}
