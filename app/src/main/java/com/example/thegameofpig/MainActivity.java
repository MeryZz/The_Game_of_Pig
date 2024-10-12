package com.example.thegameofpig;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.Toast;
import com.example.thegameofpig.databinding.ActivityMainBinding;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private int scoreP1 = 0;
    private int scoreP2 = 0;
    private int playerActual = 1;  //El 1 será el jugador 1 y el 2 el jugador 2
    private int scoreActualTurnos = 0;
    private boolean juegoEmpezado = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView((binding = ActivityMainBinding.inflate(getLayoutInflater())).getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Para iniciar el juego
        empezarJuego();

        //Para configurar el botón de lanzar dado
        binding.buttonLanzar.setOnClickListener(v -> {
            if (juegoEmpezado) {
                lanzarDado();
            } else {
                Toast.makeText(MainActivity.this, "Todavía no ha empezado el juego", Toast.LENGTH_SHORT).show();
            }
        });

        //Para configurar el botón de pasar turno
        binding.buttonTurno.setOnClickListener(v -> {
            if (juegoEmpezado) {
                pasarTurno();
            } else {
                Toast.makeText(MainActivity.this, "Todavía no ha empezado el juego", Toast.LENGTH_SHORT).show();
            }
        });

        //Para configurar el botón de reiniciar juego
        binding.buttonReset.setOnClickListener(v -> resetearElJuego());


    }

    private void actualizarImagenDado(int diceValue) {
        switch (diceValue) {
            case 1:
                binding.imageViewDado.setImageResource(R.drawable.dice_one);
                break;
            case 2:
                binding.imageViewDado.setImageResource(R.drawable.dice_two);
                break;
            case 3:
                binding.imageViewDado.setImageResource(R.drawable.dice_three);
                break;
            case 4:
                binding.imageViewDado.setImageResource(R.drawable.dice_four);
                break;
            case 5:
                binding.imageViewDado.setImageResource(R.drawable.dice_five);
                break;
            case 6:
                binding.imageViewDado.setImageResource(R.drawable.dice_six);
                break;
        }
    }

    private void resetearElJuego() {
        empezarJuego(); //Para reiniciar el juego para una nueva partida
    }

    private void pasarTurno() {
        //Para sumar la puntuación actual al jugador
        if (playerActual == 1) {
            scoreP1 += scoreActualTurnos;
        } else {
            scoreP2 += scoreActualTurnos;
        }

        //Vemos si ha ganado algún jugador
        verQuienHaGanado();

        //Cambiamos de turno al otro player
        playerActual = (playerActual == 1) ? 2 : 1;
        scoreActualTurnos = 0; //Para volver a rsetear la puntuación del turno

        actualizarScores();
        Toast.makeText(this, "Es el turno del player " + playerActual, Toast.LENGTH_SHORT).show();
    }


    private void actualizarScores() {
        binding.textScoreP1.setText(String.valueOf(scoreP1));
        binding.textScoreP2.setText(String.valueOf(scoreP2));
        binding.textPuntosAcumulados.setText(String.valueOf(scoreActualTurnos));
    }

    private void verQuienHaGanado() {
        if (scoreP1 >= 100) {
            binding.textGanador.setText("Ha ganado el PLAYER 1");
            juegoEmpezado = false;
        } else if (scoreP2 >= 100) {
            binding.textGanador.setText("Ha ganado el PLAYER 2");
            juegoEmpezado = false;
        }
    }

    private void lanzarDado() {
        Random random = new Random();
        int numRandom = random.nextInt(6) + 1; //Para general el número random del 1 al 6
        actualizarImagenDado(numRandom);

        if (numRandom == 1) {
            Toast.makeText(this, "Pierdes el turno por haber sacado un 1!! ", Toast.LENGTH_SHORT).show();
            scoreActualTurnos = 0; //Para resetear la puntuación del turno actual
            pasarTurno();
        } else {
            scoreActualTurnos += numRandom; //Para acumular la puntuación del turno
            Toast.makeText(this, "El player " + playerActual + " ha sacado un " + numRandom, Toast.LENGTH_SHORT).show();
        }
        actualizarScores();
    }

    private void empezarJuego() {
        juegoEmpezado = true;
        scoreP1 = 0;
        scoreP2 = 0;
        scoreActualTurnos = 0;
        playerActual = 1;  //Empieza el player 1
        actualizarScores();
        binding.imageViewDado.setImageResource(R.drawable.dice_random); //Para la imagen inicial
        binding.textGanador.setText(""); //Para borrar el texto del ganador
        Toast.makeText(this, "Empieza el juego. Turno del PLAYER 1", Toast.LENGTH_SHORT).show();
    }

}