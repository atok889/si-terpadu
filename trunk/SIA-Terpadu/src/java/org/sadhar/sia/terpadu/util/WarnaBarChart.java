/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.util;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Hendro Steven
 */
public class WarnaBarChart {

    private List<Color> colors;

    public WarnaBarChart() {
        createColor();
    }

    private void createColor() {
        colors = new ArrayList<Color>();
        colors.add(new Color(205, 201, 201));
        colors.add(new Color(255, 222, 173));
        colors.add(new Color(0, 0, 0));
        colors.add(new Color(49, 79, 79));
        colors.add(new Color(25, 25, 112));
        colors.add(new Color(100, 149, 237));
        colors.add(new Color(72, 61, 139));
        colors.add(new Color(0, 0, 205));
        colors.add(new Color(0, 191, 255));
        colors.add(new Color(0, 100, 0));
        colors.add(new Color(85, 107, 47));
        colors.add(new Color(152, 251, 152));
        colors.add(new Color(124, 252, 0));
        colors.add(new Color(189, 183, 107));
        colors.add(new Color(240, 230, 140));
        colors.add(new Color(255, 255, 0));
        colors.add(new Color(188, 143, 143));
        colors.add(new Color(205, 92, 92));
        colors.add(new Color(255, 0, 0));
        colors.add(new Color(255, 69, 0));
        colors.add(new Color(255, 20, 147));
        colors.add(new Color(186, 85, 211));
        colors.add(new Color(147, 112, 219));
        colors.add(new Color(139, 10, 80));
        colors.add(new Color(205, 140, 149));
        colors.add(new Color(0, 201, 87));
        colors.add(new Color(154, 255, 154));
        colors.add(new Color(173, 255, 47));
        colors.add(new Color(205, 198, 115));
        colors.add(new Color(255, 97, 3));
        colors.add(new Color(154, 205, 50));
        colors.add(new Color(219, 112, 147));
        colors.add(new Color(61, 89, 171));
        colors.add(new Color(0, 201, 87));
        colors.add(new Color(128, 128, 0));
    }

    public Color getColor(int index) {
        return colors.get(index);
    }
}
