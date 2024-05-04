/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package records;

/**
 *
 * @author user
 */
public class TestRecord {
    @SuppressWarnings("empty-statement")
    public static void main(String... args)
    {
        public record Point(float x, float y){};
        Point point = new Point(0, 0);
    }
}
