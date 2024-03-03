package com.msaggik.eighthlessondrawofsportscompetitions;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    // поля
    private EditText input; // поле ввода количества участников
    private TextView output; // поле вывода порядка выступления участников
    private SensorManager sensorManager; // менеджер сенсоров устройства
    private Sensor accelerometer; // создание поля акселерометра
    private int[] numbers; // массив порядка выступления участников

    Random random = new Random(); // создание объекта класса Random
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // привязка разметки к полям
        input = findViewById(R.id.input);
        output = findViewById(R.id.output);

        // получение доступа к сенсорам
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        // инициализация сенсора
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }
    // создание слушателя для сенсора (акселерометра)
    private SensorEventListener sensorEventListener = new SensorEventListener() {

        // обработчик события (вызывается всякий раз при измерении показаний сенсора)
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            // получаем мультиссылку на сенсоры
            Sensor multiSensor = sensorEvent.sensor;
            // действие при получении данных с акселерометра
            if (multiSensor.getType() == Sensor.TYPE_ACCELEROMETER) { // если изменения произшли на акселерометре, то
                float xAccelerometer = sensorEvent.values[0]; // ускорение по оси X (поперечное направление)
                float yAccelerometer = sensorEvent.values[1]; // ускорение по оси Y (продольное направление)
                float zAccelerometer = sensorEvent.values[2]; // ускорение по оси Z (вертикальное направление)
                // определим среднее значение ускорения по всем осям
                float medianAccelerometer = (xAccelerometer + yAccelerometer + zAccelerometer) / 3;
                // условия определения порядка участников
                if (medianAccelerometer >= 0) { //Ставлю на 0, ведь у меня только эмулятор и малое кол-во времени
                   try {
                       output.setText("Последовательность участия:\n");
                       int number = Integer.parseInt(input.getText().toString()); // считывание количества введённых участников
                       numbers = valueArrayRandom(number); // генерация массива последовательности участия
                       for (int n: numbers) { // цикл вывода списка последовательности участия на экран
                           output.append(n + " ");
                       }
                   } catch (Exception e) {
                       output.setText("Последовательность участия:\n");
                          int testnumber;
                          testnumber = random.nextInt(10)+1;//Вывод радномного числа, к сожалению для корректной работы
                       //Требуется функция встрясывания телефона, но у меня её нет. По логике программа должна работать
                          output.append(testnumber + " ");
                   }
                }
            }
        }

//    На основе материалов прошлых занятий необходимо
//    обработать исключение на случай отсутствия ввода при
//    встряске смартфона
//    И необходимо вывести только один случайный номер участника

        // метод задания точности сенсора
        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        // регистрация сенсоров (задание слушателя)
        sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL); // (слушатель, сенсор - аксерометр, время обновления - среднее)
    }


    @Override
    protected void onPause() {
        super.onPause();
        // отзыв регистрации сенсоров (отключение слушателя)
        sensorManager.unregisterListener(sensorEventListener);
    }

    // метод генерации массива N случайных чисел (для трёх примеров)
    private int[] valueArrayRandom(int number) {

        int[] arrayValue = new int[number]; // создание массива для заполнения
        for (int i = 0; i < arrayValue.length; i++) { // цикл заполнения массива случайными числами
            boolean flag = true; // флаг проверки на повторение чисел
            // цикл повторной генерации случайного числа в случае совпадения
            while (flag) {
                arrayValue[i] = random.nextInt(number) + 1; // случайного числа в заданном интервале
                flag = false; // сброс флага
                for (int k = 0; k < i; k++) { // цикл проверки повторений
                    if (arrayValue[i] == arrayValue[k]) { // если хотя бы одно значение совпало, то цикл прерывается и снова генерируется случайное значение
                        flag = true;
                        break;
                    }
                }
            }
        }
        return arrayValue;
    }
}