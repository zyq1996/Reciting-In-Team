package com.example.eden.dict;

/**
 * Created by Eden on 2017/10/5.
 */

public class WordInfo{
    private String word;
    private String interpret;
    private int wrong;
    private int right;
    private int grasp;

    public WordInfo(String word, String interpret,
                    int wrong, int right, int grasp) {
        super();
        this.word = word;
        this.interpret = interpret;
        this.wrong = wrong;
        this.right = right;
        this.grasp = grasp;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getInterpret() {
        return interpret;
    }

    public void setInterpret(String interpret) {
        this.interpret = interpret;
    }

    public int getWrong() {
        return wrong;
    }

    public void setWrong(int wrong) {
        this.wrong = wrong;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int getGrasp() {
        return grasp;
    }

    public void setGrasp(int grasp) {
        this.grasp = grasp;
    }
}