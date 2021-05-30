package com.lab2a.activity;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.lab2a.R;
import com.lab2a.execution.Perceptron;
import com.lab2a.utils.exception.ItersExceededException;
import com.lab2a.utils.exception.LabException;

import java.util.ArrayList;
import java.util.Random;


public class LabActivityInflater {

    private static final String[] SIGMAS = new String[] {
            "0.001", "0.01", "0.05", "0.1", "0.2", "0.3"
    };

    private static final String[] TIME_DEADLINES = new String[] {
            "Немає", "0.5 секунд", "1 секунда", "2 секунди", "5 секунд"
    };

    private static final String[] MAX_ITERS_VALUES = new String[] {
            "Немає", "100", "200", "500", "1000"
    };

    public static void inflate(AppCompatActivity activity) {

        final ArrayList<double[]> points = new ArrayList<>();
        final ArrayList<Boolean> moreThanP = new ArrayList<>();

        final double[] sigma = new double[1];

        final double[] P = new double[1];

        final boolean[] timeLimited = new boolean[1];
        final double[] timeDeadline = new double[1];

        final boolean[] itersLimited = new boolean[1];
        final int[] maxIters = new int[1];

        final Perceptron[] perceptron = new Perceptron[1];

        EditText edittext_input_P = activity.findViewById(R.id.edittext_input_P);

        TextView textview_points = activity.findViewById(R.id.textview_points);
        EditText edittext_input_x1 = activity.findViewById(R.id.edittext_input_x1);
        EditText edittext_input_x2 = activity.findViewById(R.id.edittext_input_x2);
        CheckBox checkbox_point_is_more_than_P =
                activity.findViewById(R.id.checkbox_point_is_more_than_P);
        Button button_add_point = activity.findViewById(R.id.button_add_point);
        Button button_add_points_on_assignment =
                activity.findViewById(R.id.button_add_points_on_assignment);
        Button button_clear_points = activity.findViewById(R.id.button_clear_points);

        Spinner spinner_select_sigma = activity.findViewById(R.id.spinner_select_sigma);
        Spinner spinner_select_deadline = activity.findViewById(R.id.spinner_select_deadline);
        Spinner spinner_select_max_iters = activity.findViewById(R.id.spinner_select_max_iters);

        Button button_train = activity.findViewById(R.id.button_train);

        Button button_train_with_different_sigmas =
                activity.findViewById(R.id.button_train_with_different_sigmas);

        EditText input_x1 = activity.findViewById(R.id.input_x1);
        EditText input_x2 = activity.findViewById(R.id.input_x2);
        Button button_check = activity.findViewById(R.id.button_check);

        TextView textViewOutputResult = activity.findViewById(R.id.textview_output_result);

        ArrayAdapter<String> adapter_select_sigma =
                new ArrayAdapter<>(
                        activity, android.R.layout.simple_spinner_item, LabActivityInflater.SIGMAS
                );
        adapter_select_sigma.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_select_sigma.setAdapter(adapter_select_sigma);

        ArrayAdapter<String> adapter_select_deadline =
                new ArrayAdapter<>(
                        activity, android.R.layout.simple_spinner_item, LabActivityInflater.TIME_DEADLINES
                );
        adapter_select_deadline.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_select_deadline.setAdapter(adapter_select_deadline);

        ArrayAdapter<String> adapter_select_max_iters =
                new ArrayAdapter<>(
                        activity, android.R.layout.simple_spinner_item, LabActivityInflater.MAX_ITERS_VALUES
                );
        adapter_select_max_iters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_select_max_iters.setAdapter(adapter_select_max_iters);

        AdapterView.OnItemSelectedListener sigmaSelectedListener = new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String item = (String) parent.getItemAtPosition(position);

                sigma[0] = Double.parseDouble(item);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}

        };

        AdapterView.OnItemSelectedListener deadlineSelectedListener = new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String item = (String) parent.getItemAtPosition(position);

                if (item.equals("Немає")) {

                    timeLimited[0] = false;

                } else {

                    timeLimited[0] = true;
                    timeDeadline[0] = Double.parseDouble(item.split(" ")[0]) * 1000000000;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}

        };

        AdapterView.OnItemSelectedListener maxItersSelectedListener = new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String item = (String) parent.getItemAtPosition(position);

                if (item.equals("Немає")) {

                    itersLimited[0] = false;

                } else {

                    itersLimited[0] = true;
                    maxIters[0] = Integer.parseInt(item);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}

        };

        spinner_select_sigma.setOnItemSelectedListener(sigmaSelectedListener);
        spinner_select_deadline.setOnItemSelectedListener(deadlineSelectedListener);
        spinner_select_max_iters.setOnItemSelectedListener(maxItersSelectedListener);

        @SuppressLint("SetTextI18n") View.OnClickListener onButtonAddPointClick = v -> {

            String x1_raw = String.valueOf(edittext_input_x1.getText());
            String x2_raw = String.valueOf(edittext_input_x2.getText());

            if (
                    x1_raw.trim().equals("") || x1_raw.trim().equals(".") || x1_raw.trim().equals("-") ||
                            x2_raw.trim().equals("") || x2_raw.trim().equals(".") || x2_raw.trim().equals("-")
            ) {

                Toast.makeText(
                        activity.getApplicationContext(),
                        "Неправильно задані координати точки",
                        Toast.LENGTH_SHORT
                ).show();

            } else {

                double x1 = Double.parseDouble(x1_raw);
                double x2 = Double.parseDouble(x2_raw);

                boolean more = checkbox_point_is_more_than_P.isChecked();

                points.add(new double[] {x1, x2});
                moreThanP.add(more);

                String out = "(" + x1 + ", " + x2 + ")" + " : ";
                out += more ? "Більше P" : "Менше P";
                out += "\n";

                textview_points.setText(textview_points.getText() + out);

            }

        };

        View.OnClickListener onButtonAddPointsOnAssignmentClick = v -> {

            points.clear();
            moreThanP.clear();

            textview_points.setText("");

            Random random = new Random();

            double[][] points_on_assignment = new double[][] {
                    new double[] {0, 6},
                    new double[] {1, 5},
                    new double[] {3, 3},
                    new double[] {2, 4},
            };

            for (double[] point : points_on_assignment) {

                points.add(point);
                moreThanP.add(random.nextBoolean());

            }

            StringBuilder out = new StringBuilder();

            for (int i = 0; i < points.size(); i++) {
                out.append("(").append(points.get(i)[0]).append(", ").append(points.get(i)[1]).append(")")
                        .append(" : ")
                        .append(moreThanP.get(i) ? "Більше P": "Менше P").append("\n");
            }

            textview_points.setText(out);

        };

        View.OnClickListener onButtonClearPointsClick = v -> {

            points.clear();
            moreThanP.clear();

            textview_points.setText("");

        };

        View.OnClickListener onButtonTrainClick = v -> {

            String P_raw = String.valueOf(edittext_input_P.getText());

            if (P_raw.trim().equals("") || P_raw.trim().equals(".") || P_raw.trim().equals(".")) {
                P[0] = 4;
            } else {
                P[0] = Double.parseDouble(P_raw);
            }

            if (timeLimited[0] && itersLimited[0]) {

                perceptron[0] = new Perceptron(0.0, 0.0, sigma[0], P[0], timeDeadline[0], maxIters[0]);

            } else if (timeLimited[0] && !itersLimited[0]) {

                perceptron[0] = new Perceptron(0.0, 0.0, sigma[0], P[0], timeDeadline[0]);

            } else if (!timeLimited[0] && itersLimited[0]) {

                perceptron[0] = new Perceptron(0.0, 0.0, sigma[0], P[0], maxIters[0]);

            } else {

                perceptron[0] = new Perceptron(0.0, 0.0, sigma[0], P[0]);

            }

            for (int i = 0; i < points.size(); i++) {
                perceptron[0].addPoint(points.get(i)[0], points.get(i)[1], moreThanP.get(i));
            }

            try {

                perceptron[0].train();

                Toast.makeText(
                        activity.getApplicationContext(),
                        "Модель натреновано",
                        Toast.LENGTH_SHORT
                ).show();

            } catch (ItersExceededException exception) {

                Toast.makeText(
                        activity.getApplicationContext(),
                        "Перевищена максимальна кількість ітерацій " + "(" + maxIters[0] + ")",
                        Toast.LENGTH_SHORT
                ).show();

            } catch (LabException exception) {

                Toast.makeText(
                        activity.getApplicationContext(),
                        "Пройшов часовий дедлайн " + "(" + timeDeadline[0] / 1000000000 + " с" + ")",
                        Toast.LENGTH_SHORT
                ).show();

            }

        };
         View.OnClickListener onButtonTrainWithDifferentSigmasClick = v -> {

            double[] sigmas = new double[] {0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8,0.9};

            double deadline = 5000000000D;
            int max_iters = 1000000;

            Perceptron[] perceptrons = new Perceptron[9];

            String P_raw = String.valueOf(edittext_input_P.getText());

            if (P_raw.trim().equals("") || P_raw.trim().equals(".") || P_raw.trim().equals(".")) {
                P[0] = 4;
            } else {
                P[0] = Double.parseDouble(P_raw);
            }

            for (int i = 0; i < sigmas.length; i++) {

                try {

                    perceptrons[i] = new Perceptron(
                            0.0, 0.0, sigmas[i], P[0], deadline, max_iters
                    );

                    for (int j = 0; j < points.size(); j++) {
                        perceptrons[i].addPoint(points.get(j)[0], points.get(j)[1], moreThanP.get(j));
                    }

                    perceptrons[i].train();

                } catch (LabException exception) {

                    exception.printStackTrace();

                }

            }

            ArrayList<Perceptron> trainedPerceptrons = new ArrayList<>();

            StringBuilder out = new StringBuilder();

            for (int i = 0; i < perceptrons.length; i++) {

                out.append("Сігма: ").append(sigmas[i]).append("\n");

                if (perceptrons[i].isTrained()) {

                    trainedPerceptrons.add(perceptrons[i]);

                    out.append("Натреновано: Так").append("\n");
                    out.append("Час: ").append(perceptrons[i].getTime() / 1000000000).append(" с").append("\n");
                    out.append("Кількість ітерацій: ").append(perceptrons[i].getIters()).append("\n\n");

                } else {

                    out.append("Натреновано: Ні").append("\n\n");

                }

            }

            ArrayList<Double> timeBests = new ArrayList<>();
            ArrayList<Double> itersBests = new ArrayList<>();

            if (!trainedPerceptrons.isEmpty()) {

                double bestTime = trainedPerceptrons.get(0).getTime();
                int bestIters = trainedPerceptrons.get(0).getIters();

                for (int i = 0; i < trainedPerceptrons.size(); i++) {

                    if (trainedPerceptrons.get(i).getTime() < bestTime)
                        bestTime = trainedPerceptrons.get(i).getTime();

                    if (trainedPerceptrons.get(i).getIters() < bestIters)
                        bestIters = trainedPerceptrons.get(i).getIters();

                }

                for (int i = 0; i < trainedPerceptrons.size(); i++) {

                    if (trainedPerceptrons.get(i).getTime() == bestTime)
                        timeBests.add(trainedPerceptrons.get(i).getSigma());

                    if (trainedPerceptrons.get(i).getIters() == bestIters)
                        itersBests.add(trainedPerceptrons.get(i).getSigma());

                }

            }

            out.append("Найкращі сігми за часом: ");

            if (!timeBests.isEmpty()) {

                for (int i = 0; i < timeBests.size() - 1; i++) {
                    out.append(timeBests.get(i)).append(", ");
                }

                out.append(timeBests.get(timeBests.size() - 1)).append("\n\n");

            } else out.append("Немає\n\n");

            out.append("Найкращі сігми за кількістю ітерацій: ");

            if (!itersBests.isEmpty()) {

                for (int i = 0; i < itersBests.size() - 1; i++) {
                    out.append(itersBests.get(i)).append(", ");
                }

                out.append(itersBests.get(itersBests.size() - 1)).append("\n");

            } else out.append("Немає\n\n");

            textViewOutputResult.setTextColor(activity.getResources().getColor(R.color.green));
            textViewOutputResult.setText(out);

        };


        View.OnClickListener onButtonCheckClick = v -> {

            if (perceptron[0].isTrained()) {

                String x1_raw = String.valueOf(input_x1.getText());
                String x2_raw = String.valueOf(input_x2.getText());

                if (
                        x1_raw.trim().equals("") || x1_raw.trim().equals(".") || x1_raw.trim().equals("-") ||
                                x2_raw.trim().equals("") || x2_raw.trim().equals(".") || x2_raw.trim().equals("-")
                ) {

                    Toast.makeText(
                            activity.getApplicationContext(),
                            "Неправильно задані координати точки",
                            Toast.LENGTH_SHORT
                    ).show();

                } else {

                    double x1 = Double.parseDouble(x1_raw);
                    double x2 = Double.parseDouble(x2_raw);

                    boolean pointMoreThanP = perceptron[0].isPointMoreThanP(x1, x2);

                    String out = "(" + x1 + ", " + x2 + ")";
                    out += " : ";
                    out += pointMoreThanP ? "Більше P" : "Менше P";

                    textViewOutputResult.setTextColor(activity.getResources().getColor(R.color.green));
                    textViewOutputResult.setText(out);

                }

            } else {

                Toast.makeText(
                        activity.getApplicationContext(),
                        "Модель не натренована",
                        Toast.LENGTH_SHORT
                ).show();

            }

        };

        button_add_point.setOnClickListener(onButtonAddPointClick);
        button_add_points_on_assignment.setOnClickListener(onButtonAddPointsOnAssignmentClick);
        button_clear_points.setOnClickListener(onButtonClearPointsClick);

        button_train.setOnClickListener(onButtonTrainClick);
        button_train_with_different_sigmas.setOnClickListener(onButtonTrainWithDifferentSigmasClick);



        button_check.setOnClickListener(onButtonCheckClick);

    }

}
