package reslearn.gui.controller;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

public class VideoPlayer extends BorderPane {

	private MediaPlayer mp;
	private MediaView mediaView;
	private final boolean repeat = false;
	private boolean stopRequested = false;
	private boolean atEndOfMedia = false;
	private Duration duration;
	private Slider timeSlider;
	private Label playTime;
	private Slider volumeSlider;
	private HBox mediaBar;

	public VideoPlayer(final MediaPlayer mp) {
		// Video Player von Oracle, mit kleinen Verbesserungen
		this.mp = mp;
		setStyle("-fx-background-color: #bfc2c7;");
		mediaView = new MediaView(mp);
		Pane mvPane = new Pane();
		mvPane.getChildren().add(mediaView);
		mvPane.setStyle("-fx-background-color: black;");
		setCenter(mvPane);
		mediaBar = new HBox();
		mediaBar.setAlignment(Pos.CENTER);
		mediaBar.setPadding(new Insets(5, 10, 5, 10));
		BorderPane.setAlignment(mediaBar, Pos.CENTER);

		Button playButton = new Button();
		ImageView playIcon = new ImageView(new Image(
				getClass().getResource("/reslearn/gui/images/play.png").toExternalForm(), 20, 30, true, true));
		ImageView pauseIcon = new ImageView(new Image(
				getClass().getResource("/reslearn/gui/images/pause.png").toExternalForm(), 20, 30, true, true));
		ImageView repeatIcon = new ImageView(new Image(
				getClass().getResource("/reslearn/gui/images/repeat.png").toExternalForm(), 20, 30, true, true));

		playButton.setGraphic(playIcon);

		playButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				Status status = mp.getStatus();

				if (status == Status.UNKNOWN || status == Status.HALTED) {
					// don't do anything in these states
					return;
				}

				if (status == Status.PAUSED || status == Status.READY || status == Status.STOPPED) {
					// rewind the movie if we're sitting at the end
					if (atEndOfMedia) {
						mp.seek(mp.getStartTime());
						atEndOfMedia = false;
					}
					mp.play();
				} else {
					mp.pause();
				}
			}
		});

		mp.currentTimeProperty().addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable ov) {
				updateValues();
			}
		});

		mp.setOnPlaying(new Runnable() {
			@Override
			public void run() {
				if (stopRequested) {
					mp.pause();
					stopRequested = false;
				} else {
					playButton.setGraphic(pauseIcon);
				}
			}
		});

		mp.setOnPaused(new Runnable() {
			@Override
			public void run() {
				playButton.setGraphic(playIcon);
			}
		});

		mp.setOnReady(new Runnable() {
			@Override
			public void run() {
				duration = mp.getMedia().getDuration();
				updateValues();
			}
		});

		mp.setCycleCount(repeat ? MediaPlayer.INDEFINITE : 1);
		mp.setOnEndOfMedia(new Runnable() {
			@Override
			public void run() {
				if (!repeat) {
					playButton.setGraphic(repeatIcon);
					stopRequested = true;
					atEndOfMedia = true;
				}
			}
		});

		mediaBar.getChildren().add(playButton);

		// Add spacer
		Label spacer = new Label("   ");
		mediaBar.getChildren().add(spacer);

		// Add Time label
		Label timeLabel = new Label("Time: ");
		mediaBar.getChildren().add(timeLabel);

		// Add time slider
		timeSlider = new Slider();
		HBox.setHgrow(timeSlider, Priority.ALWAYS);
		timeSlider.setMinWidth(50);
		timeSlider.setMaxWidth(Double.MAX_VALUE);

		timeSlider.valueProperty().addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable ov) {
				if (timeSlider.isValueChanging()) {
					// multiply duration by percentage calculated by slider position
					mp.seek(duration.multiply(timeSlider.getValue() / 100.0));
				}
			}
		});

		mediaBar.getChildren().add(timeSlider);

		// Add Play label
		playTime = new Label();
		playTime.setPrefWidth(130);
		playTime.setMinWidth(50);
		mediaBar.getChildren().add(playTime);

		// Add the volume label
		Label volumeLabel = new Label("Vol: ");
		mediaBar.getChildren().add(volumeLabel);

		// Add Volume slider
		volumeSlider = new Slider();
		volumeSlider.setPrefWidth(70);
		volumeSlider.setMaxWidth(Region.USE_PREF_SIZE);
		volumeSlider.setMinWidth(30);

		volumeSlider.valueProperty().addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable ov) {
				if (volumeSlider.isValueChanging()) {
					mp.setVolume(volumeSlider.getValue() / 100.0);
				}
			}
		});

		mediaBar.getChildren().add(volumeSlider);

		setBottom(mediaBar);

	}

	protected void updateValues() {
		if (playTime != null && timeSlider != null && volumeSlider != null) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Duration currentTime = mp.getCurrentTime();
					playTime.setText(formatTime(currentTime, duration));
					timeSlider.setDisable(duration.isUnknown());
					if (!timeSlider.isDisabled() && duration.greaterThan(Duration.ZERO)
							&& !timeSlider.isValueChanging()) {
						timeSlider.setValue(currentTime.divide(duration).toMillis() * 100.0);
					}
					if (!volumeSlider.isValueChanging()) {
						volumeSlider.setValue((int) Math.round(mp.getVolume() * 100));
					}
				}
			});
		}
	}

	private static String formatTime(Duration elapsed, Duration duration) {
		int intElapsed = (int) Math.floor(elapsed.toSeconds());
		int elapsedHours = intElapsed / (60 * 60);
		if (elapsedHours > 0) {
			intElapsed -= elapsedHours * 60 * 60;
		}
		int elapsedMinutes = intElapsed / 60;
		int elapsedSeconds = intElapsed - elapsedHours * 60 * 60 - elapsedMinutes * 60;

		if (duration.greaterThan(Duration.ZERO)) {
			int intDuration = (int) Math.floor(duration.toSeconds());
			int durationHours = intDuration / (60 * 60);
			if (durationHours > 0) {
				intDuration -= durationHours * 60 * 60;
			}
			int durationMinutes = intDuration / 60;
			int durationSeconds = intDuration - durationHours * 60 * 60 - durationMinutes * 60;
			if (durationHours > 0) {
				return String.format("%d:%02d:%02d/%d:%02d:%02d", elapsedHours, elapsedMinutes, elapsedSeconds,
						durationHours, durationMinutes, durationSeconds);
			} else {
				return String.format("%02d:%02d/%02d:%02d", elapsedMinutes, elapsedSeconds, durationMinutes,
						durationSeconds);
			}
		} else {
			if (elapsedHours > 0) {
				return String.format("%d:%02d:%02d", elapsedHours, elapsedMinutes, elapsedSeconds);
			} else {
				return String.format("%02d:%02d", elapsedMinutes, elapsedSeconds);
			}
		}
	}
}
