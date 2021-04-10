package Modele.Deplacements;

public enum Direction {
    gauche(0), droite(1), haut(2), bas(3);

    private int value;

    private Direction(int _value){ value = _value; }

    public int getValue(){ return value; }
}
