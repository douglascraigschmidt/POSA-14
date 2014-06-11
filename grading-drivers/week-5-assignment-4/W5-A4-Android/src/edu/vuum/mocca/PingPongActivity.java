package edu.vuum.mocca;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * @class MainActivity
 * 
 * @brief Initial start up screen for the android GUI.
 */
public class PingPongActivity extends Activity {
    /** TextView that PingPong will be "played" upon */
    private TextView mAndroidPingPongOutput;

    /** Button that allows playing and resetting of the game */
    private Button mPlayButton;

    /** Variables to track state of the game */
    private static int PLAY = 0;
    private static int RESET = 1;
    private int mGameState = PLAY;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Sets the content view to the xml file, activity_ping_pong.
        setContentView(R.layout.activity_ping_pong);
        mAndroidPingPongOutput =
            (TextView) findViewById(R.id.pingpong_output);
        mPlayButton = (Button) findViewById(R.id.play_button);

        // Initializes the Platform singleton with the appropriate
        // Platform strategy, which in this case will be the
        // AndroidPlatform.
        PlatformStrategy.instance
            (new PlatformStrategyFactory
             (mAndroidPingPongOutput,
              this).makePlatformStrategy());

        // Initializes the Options singleton. 
        Options.instance().parseArgs(null);
    }

    /** Sets the action of the button on click state. */
    public void playButtonClicked(View view) {
        if (mGameState == PLAY) {
            // Use a factory method to create the appropriate type of
            // OutputStrategy.
            PlayPingPong pingPong =
                new PlayPingPong(PlatformStrategy.instance(),
                                 Options.instance().maxIterations(),
                                 Options.instance().maxTurns(),
                                 Options.instance().syncMechanism());

            // Play ping-pong with the designated number of
            // iterations.
            new Thread(pingPong).start();
            mPlayButton.setText(R.string.reset_button);
            mGameState = RESET;
        } else if (mGameState == RESET) {

            // Empty TextView and prepare the UI to play another game.
            mAndroidPingPongOutput.setText(R.string.empty_string);
            mPlayButton.setText(R.string.play_button);
            mGameState = PLAY;
        } else {
            // Notify the player that something has gone wrong and
            // reset.
            mAndroidPingPongOutput.setText("Unknown State entered!");
            mGameState = RESET;
        }
    }
}
