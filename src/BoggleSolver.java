/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import edu.princeton.cs.algs4.In;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author Chengheng Li Chen
 */
public class BoggleSolver {

    private final int R = 26;
    private final int A = 'A';
    private Node root = new Node();
    private HashSet<String> valids;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {

        for (String string : dictionary) {
            add(string);
        }

    }

    private class Node {

        public boolean inDict = false;
        public Node[] hijos = new Node[R];
    }

    private void add(String s) {
        root = add(root, s, 0);
    }

    private Node add(Node node, String s, int d) {
        if (node == null) {
            node = new Node();
        }

        if (s.length() == d) {
            node.inDict = true;
            return node;
        }

        int character = s.charAt(d) - A;

        node.hijos[character] = add(node.hijos[character], s, d + 1);

        return node;
    }

    private boolean isWord(String s) {

        Node node = search(root, s, 0);

        return (node == null) ? false : node.inDict;
    }

    private boolean isPreffix(String s) {
        return search(root, s, 0) != null;
    }

    private Node search(Node node, String s, int d) {
        if (node == null) {
            return null;
        }
        if (s.length() == d) {
            return node;
        }

        int character = s.charAt(d) - A;

        return search(node.hijos[character], s, d + 1);
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        if (board == null) {
            throw new IllegalArgumentException("the argument to getAllValidWords() is null\n");
        }
        valids = new HashSet<>();
        int rows = board.rows(), cols = board.cols();
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                
                dfs(board, x, y);
            }
        }
        return valids;
    }

    private void dfs(BoggleBoard board, int x, int y) {
        int rows = board.rows(), cols = board.cols();
        boolean[][] marked = new boolean[rows][cols];
        
        String s = board.getLetter(x, y) + "";

        dfs(board, s, marked, x, y);
    }

    private void dfs(BoggleBoard board, String preffix, boolean[][] marked, int x, int y) {
        marked[x][y] = true;

        if (preffix.charAt(preffix.length() - 1) == 'Q') {
            preffix += "U";
        }

        if (isWord(preffix) && preffix.length() > 2) {
            valids.add(preffix);
        }

        for (int[] i : neighbor(board, x, y)) {
            if (!marked[i[0]][i[1]]) {
                String word = preffix + board.getLetter(i[0], i[1]);
                if (isPreffix(word)) {
                    dfs(board, word, marked, i[0], i[1]);
                }
            }
        }

        marked[x][y] = false;

    }

    private ArrayList<int[]> neighbor(BoggleBoard board, int x, int y) {

        ArrayList<int[]> neighbour = new ArrayList<>();

        int rows = board.rows(), cols = board.cols();
        int[][] coordanadas = {{x + 1, y}, {x - 1, y}, {x, y + 1}, {x, y - 1}, {x + 1, y + 1}, {x + 1, y - 1}, {x - 1, y + 1}, {x - 1, y - 1}};

        for (int[] coordanada : coordanadas) {
            if (coordanada[0] < 0 || coordanada[1] < 0 || coordanada[0] >= rows || coordanada[1] >= cols) {
                continue;
            }
            neighbour.add(coordanada);
        }
        return neighbour;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (word == null) {
            throw new IllegalArgumentException("the argument to scoreOf() is null\n");
        }
        if (!isWord(word)) {
            return 0;
        } else if (word.length() < 3) {
            return 0;
        } else if (word.length() < 5) {
            return 1;
        } else if (word.length() == 5) {
            return 2;
        } else if (word.length() == 6) {
            return 3;
        } else if (word.length() == 7) {
            return 5;
        } else {
            return 11;
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        BoggleBoard board = new BoggleBoard("board.txt");
        In input = new In("dict.txt");
        ArrayList<String> str = new ArrayList<>();

        while (input.hasNextLine()) {
            str.add(input.readLine());
        }

        String[] dict = new String[str.size()];

        for (int i = 0; i < str.size(); i++) {
            dict[i] = str.get(i);
        }

        BoggleSolver solver = new BoggleSolver(dict);

        int C = 0;
        List<String> sort;
        sort = new ArrayList<>();
        int score = 0;
        for (String allValidWord : solver.getAllValidWords(board)) {
            score += solver.scoreOf(allValidWord);
            sort.add(allValidWord);
            C++;

        }
        Collections.sort(sort);
        System.out.println(sort.toString());
        System.out.println(C);
        System.out.println(score);
    }

}
