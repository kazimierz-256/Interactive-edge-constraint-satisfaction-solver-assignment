/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gk1;

/**
 *
 * @author kazimierz
 */
public class ActiveEdge {

    public double y_max;
    public double x;
    public double m_inverse;

    public ActiveEdge(double y_max, double x, double m_inverse) {
        this.y_max = y_max;
        this.x = x;
        this.m_inverse = m_inverse;
    }
}
