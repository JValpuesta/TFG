package com.valpuestajorge.conecta4.tablero.business;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.ArrayList;
import java.util.List;

@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tablero {

    @Id
    private Integer idTablero;
    @Column
    private String nombreJugador1;
    @Column
    private String ipCliente1;
    @Column
    private String nombreJugador2;
    @Column
    private String ipCliente2;
    @Column
    private int[][] posicion = new int[6][7]; //0 -> casilla vacía; 1 -> ficha amarilla; 2 -> ficha roja
    @Column
    private List<Integer> historial = new ArrayList<>();
    @Column
    private Integer turno;
    @Column
    private String ganador = ""; //empty -> partida sin acabar; nombreJugador que ha ganado o empate

    public Tablero(String nombre, String ip) {

        this.nombreJugador1 = nombre;
        this.ipCliente1 = ip;
        this.nombreJugador2 = "";
        this.ipCliente2 = "";
        this.turno = 0;
        this.ganador = "";

    }

    public void anyadirFicha(int columna) { //los casos de columnaNoValida y columnaLlena se controlan en el front
        int fila = 0;
        while (fila < this.posicion.length) {
            if (posicion[fila][columna] == 0) {
                if(this.getTurno()%2==0){
                    posicion[fila][columna] = 1;
                } else {
                    posicion[fila][columna] = 2;
                }
                fila = this.posicion.length;
            } else {
                fila++;
            }
        }
        if(this.checkConnect4()){
            if(this.turno%2==0){
                this.ganador = getNombreJugador1();
            } else {
                this.ganador = getNombreJugador2();
            }
        }else {
            this.turno = getTurno() + 1;
        }
    }

    public void addIntToHistorial(int idMovimiento){
        getHistorial().add(idMovimiento);
    }

    public boolean checkConnect4() {
        if(this.getTurno()==this.posicion.length*this.posicion[0].length){
            setGanador("Empate");
            return true;
        }
        int turno = (this.getTurno()%2==0) ? 1 : 2;
        int rows = this.posicion.length;
        int cols = this.posicion[0].length;

        // Verificación de filas
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols - 3; col++) {
                boolean hasConnect4 = true;
                for (int i = 0; i < 4; i++) {
                    if (this.posicion[row][col + i] != turno) {
                        hasConnect4 = false;
                        break;
                    }
                }
                if (hasConnect4) {
                    return true;
                }
            }
        }

        // Verificación de columnas
        for (int col = 0; col < cols; col++) {
            for (int row = 0; row < rows - 3; row++) {
                boolean hasConnect4 = true;
                for (int i = 0; i < 4; i++) {
                    if (this.posicion[row + i][col] != turno) {
                        hasConnect4 = false;
                        break;
                    }
                }
                if (hasConnect4) {
                    return true;
                }
            }
        }

        // Verificación de diagonales hacia abajo y hacia la derecha
        for (int row = 0; row < rows - 3; row++) {
            for (int col = 0; col < cols - 3; col++) {
                boolean hasConnect4 = true;
                for (int i = 0; i < 4; i++) {
                    if (this.posicion[row + i][col + i] != turno) {
                        hasConnect4 = false;
                        break;
                    }
                }
                if (hasConnect4) {
                    return true;
                }
            }
        }

        // Verificación de diagonales hacia arriba y hacia la derecha
        for (int row = 3; row < rows; row++) {
            for (int col = 0; col < cols - 3; col++) {
                boolean hasConnect4 = true;
                for (int i = 0; i < 4; i++) {
                    if (this.posicion[row - i][col + i] != turno) {
                        hasConnect4 = false;
                        break;
                    }
                }
                if (hasConnect4) {
                    return true;
                }
            }
        }
        return false;
    }

}
