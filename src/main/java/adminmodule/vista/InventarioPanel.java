package adminmodule.vista;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Hodalys
 */
import javax.swing.*;

public class InventarioPanel extends JPanel {
    public InventarioPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new JLabel("Inventario Médico"));
        // Agrega botones/listas para ver el inventario
    }
}

