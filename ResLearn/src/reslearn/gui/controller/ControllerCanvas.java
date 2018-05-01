package reslearn.gui.controller;

import javafx.event.EventHandler;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import reslearn.gui.ResGeometrie;
import reslearn.gui.View;

public class ControllerCanvas {
	
	double zeigerX, zeigerY;
	double translateX, translateY;
	ResGeometrie feld;
			
	public void makeDraggable(ResGeometrie feld) {
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

			ResGeometrie rect = (ResGeometrie) e.getSource();

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
			
			newTranslateX = newTranslateX - newTranslateX%20;
			newTranslateY = newTranslateY - newTranslateY%20;
			
			ResGeometrie rect = (ResGeometrie) e.getSource();

			rect.setTranslateX(newTranslateX);
			rect.setTranslateY(newTranslateY);
		}
	};

	EventHandler<ContextMenuEvent> OnMouseSecondaryEventHandler = new EventHandler<ContextMenuEvent>() {
		@Override
		public void handle(ContextMenuEvent e) {
			View.menu.show(feld, e.getSceneX(), e.getSceneY());
			View.menu.getItems().addAll(View.ap,View.farbe);
		}
	};
	 
//	 rect.setX(e.getX()-10);
//	 rect.setY(e.getY()-10);

}
