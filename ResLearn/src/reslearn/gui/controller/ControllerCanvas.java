package reslearn.gui.controller;

import javafx.event.EventHandler;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import reslearn.gui.ResGeometrie;
import reslearn.gui.Diagramm;
import reslearn.gui.View;
import reslearn.model.paket.ResEinheit;
import reslearn.model.paket.Teilpaket;

public class ControllerCanvas {

	double zeigerX, zeigerY;
	double translateX, translateY;
	ResGeometrie feld;
	ResGeometrie rect;
	String nameClicked;

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

			rect = (ResGeometrie) e.getSource();
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

			newTranslateX = newTranslateX - newTranslateX % 20;
			newTranslateY = newTranslateY - newTranslateY % 20;

			for (ResGeometrie[] resAr : Diagramm.res) {
				for (ResGeometrie resG : resAr) {
					if (resG != null) {
						if (nameClicked == resG.getResEinheit().getTeilpaket().getArbeitspaket().getId()) {
							double differenzX = translateX - resG.getTranslateX();
							double differenzY = translateY - resG.getTranslateY();
							resG.setTranslateX(newTranslateX + differenzX);
							resG.setTranslateY(newTranslateY + differenzY);
						}
					}
				}
			}

			// if(differenzX<-10.0 && differenzY<-10.0) {
			// newTranslateX += differenzX;
			// newTranslateY += differenzY;
			// }else {
			// newTranslateX -= differenzX;
			// newTranslateY -= differenzY;
			// }
			// if (differenzX > 10.0) {
			// newTranslateX += differenzX;
			// } else {
			// newTranslateX -= differenzX;
			// }
			//
			// if (differenzY > 10.0) {
			// newTranslateY += differenzY;
			// } else {
			// newTranslateY -= differenzY;
			// }

			// rect.setTranslateX(newTranslateX);
			// rect.setTranslateY(newTranslateY);
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
