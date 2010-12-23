/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package orion.tapestry.menu.lib;

/**
 * Weight and Label pair
 */
public class WeightAndLabel {

    public String label;
    public int weight;

    WeightAndLabel(String _label, int _weight) {
        this.label = _label;
        this.weight = _weight;
    }

    WeightAndLabel(String _label) {
        this(_label, 0);
    }

    @Override
    public String toString() {
        if (this.weight == 0) {
            return this.label;
        } else {
            return this.weight + this.label;
        }
    }
}
