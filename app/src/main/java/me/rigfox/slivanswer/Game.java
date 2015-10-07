package me.rigfox.slivanswer;

import android.app.Activity;
import android.graphics.Color;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Game {
    private static final int SHOW_WORD = 786;
    private static final int SHOW_RESULT = 647;
    private static final int SHOW_RESULT_WAIT = 607;
    private static final int WAIT = 484;
    private static final int FINISH = 702;

    private static final int SUCCESS = 27;
    private static final int FAIL = 389;

    ArrayList<String> slivList;

    ArrayList<ResultItem> resultList;

    TextView screenText;
    TextView timerText;

    Sensors sensors;
    Timer timer;
    Activity activity;
    RelativeLayout body;

    boolean revertFlag;

    int status;
    int k;
    int currentWordIndex;
    int lastStatus;
    int lastK;

    int gameTimer;

    Game(TextView textView, Sensors sens, Activity act) {
        screenText = textView;
        sensors = sens;
        activity = act;

        body = (RelativeLayout) activity.findViewById(R.id.screenbody);
        timerText = (TextView) activity.findViewById(R.id.timerView);

        slivList = new ArrayList<>();

        String[] slivWords = activity.getResources().getStringArray(R.array.slivWord);
        Collections.addAll(slivList, slivWords);

        slivList = shuffle(slivList);

        resultList = new ArrayList<>();
    }

    public void start() {
        k = 5;

        timer = new Timer();
        TimerTask task = new StartTimer();
        timer.schedule(task, 0, 1000);
    }

    public void initGame() {
        if (k > 0)  {
            screenText.setText(String.format("Приготовились %d", k));
            k--;
        } else {
            k = 0;
            gameTimer = 60;

            status = SHOW_WORD;
            currentWordIndex = 0;

            revertFlag = false;

            timer.cancel();

            timer = new Timer();
            TimerTask task = new UpdateTimer();
            timer.schedule(task, 0, 100);
        }
    }

    public void gameUpdate() {
        k++;

        if (k % 10 == 0) {
            timerText.setText(String.format("До слива: %d", gameTimer));
            gameTimer--;
        }
        if (gameTimer < 1) {
            status = FINISH;
        }


        switch (status) {
            case SHOW_WORD:
                screenText.setText(slivList.get(currentWordIndex));
                body.setBackgroundColor(Color.WHITE);
                status = WAIT;
                break;
            case WAIT:
                int screenPosition = sensors.getScreenPosition();

                if (screenPosition == Sensors.ScreenFace) {
                    revertFlag = true;
                }
                if (revertFlag) {
                    if (screenPosition == Sensors.ScreenUP) {
                        lastStatus = SUCCESS;
                        status = SHOW_RESULT;
                    }
                    if (screenPosition == Sensors.ScreenDown) {
                        lastStatus = FAIL;
                        status = SHOW_RESULT;
                    }
                }
                break;
            case SHOW_RESULT:
                lastK = k;
                revertFlag = false;

                if (lastStatus == SUCCESS) {
                    screenText.setText("Угадано!");
                    body.setBackgroundColor(Color.GREEN);
                    resultList.add(new ResultItem(slivList.get(currentWordIndex), true));
                }
                if (lastStatus == FAIL) {
                    screenText.setText("Слито");
                    body.setBackgroundColor(Color.RED);
                    resultList.add(new ResultItem(slivList.get(currentWordIndex), false));
                }
                status = SHOW_RESULT_WAIT;
                break;
            case SHOW_RESULT_WAIT:
                if (k-lastK > 20) {
                    if (slivList.size()-1 == currentWordIndex) {
                        status = FINISH;
                    } else {
                        status = SHOW_WORD;
                        currentWordIndex++;
                    }
                }
                break;
            case FINISH:
                activity.setContentView(R.layout.result);
                ResultAdapter resultAdapter = new ResultAdapter(activity.getApplicationContext(), resultList);

                ListView resultList = (ListView) activity.findViewById(R.id.listView);
                resultList.setAdapter(resultAdapter);

                timer.cancel();
        }
    }

    class StartTimer extends TimerTask {
        @Override
        public void run() {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    initGame();
                }
            });
        }
    }

    class UpdateTimer extends TimerTask {
        @Override
        public void run() {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    gameUpdate();
                }
            });
        }
    }

    ArrayList<String> shuffle(ArrayList<String> currentArray) {
        ArrayList<String> returnArray = new ArrayList<>();

        Random random = new Random();

        int ArraySize = currentArray.size();

        int maxRandom = ArraySize;

        for (int i = 0; i < ArraySize; i++) {
            int rand = random.nextInt(maxRandom);
            returnArray.add(currentArray.get(rand));
            currentArray.remove(rand);
            maxRandom--;
        }

        return returnArray;
    }


}
