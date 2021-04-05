package gma.objects;

import java.util.Comparator;

import gma.entities.Statistics;

public class ScoreComparator implements Comparator<Statistics> {

    @Override
    public int compare(Statistics statistics1, Statistics statistics2) {
       return Integer.compare(statistics1.getScore(), statistics2.getScore());
    }
}