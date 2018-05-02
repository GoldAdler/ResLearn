package reslearn.gui.controller;

import javafx.event.EventHandler;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import reslearn.gui.ResFeld;
import reslearn.gui.Diagramm;
import reslearn.gui.View;

public class ControllerCanvas {

	double zeigerX, zeigerY;
	double translateX, translateY;
	ResFeld feld;
	ResFeld rect;
	String nameClicked;

	public void makeDraggable(ResFeld feld) {
		this.feld = feld;
		feld.setOnMousePressed(OnMousePressedEventHandler);
		feld.setOnMouseDragged(OnMouseDraggedEventHandler);
		feld.setOnContextMenuRequested(OnMouseSecondaryEventHandler);
	}

	EventHandler<MouseEvent> OnMousePressedEventHandler = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent e) {

			zeigerX = e.getSceneX();
			zeigerY = e.getSceneY();

			rect = (ResFeld) e.getSource();
			nameClicked = rect.getResEinheit().getTeilpaket().getArbeitspaket().getId();

			translateX = rect.getTranslateX();
			translateY = rect.getTranslateY();
		}
	};

	EventHandler<MouseEvent> OnMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent e) {

			double offsetX = e.getSceneX() - zeigerX;
			double offsetY = e.getSceneY() - zeigerY;

			double newTranslateX = translateX + offsetX;
			double newTranslateY = translateY + offsetY;

			newTranslateX -= newTranslateX % 20;
			newTranslateY -= newTranslateY % 20;

			for (ResFeld[] resAr : Diagramm.res) {
				for (ResFeld resG : resAr) {
					if (resG != null) {
						if (nameClicked == resG.getResEinheit().getTeilpaket().getArbeitspaket().getId()) {							
							resG.setTranslateX(newTranslateX );
							resG.setTranslateY(newTranslateY );
						}
					}
				}
			}
		}
	};

	EventHandler<ContextMenuEvent> OnMouseSecondaryEventHandler = new EventHandler<ContextMenuEvent>() {
		@Override
		public void handle(ContextMenuEvent e) {
			View.menu.show(feld, e.getSceneX(), e.getSceneY());
			View.menu.getItems().addAll(View.ap, View.farbe);
		}
	};

	// rect.setX(e.getX()-10);
	// rect.setY(e.getY()-10);

}
