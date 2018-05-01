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
			
			double differenzX = newTranslateX % 20;
			double differenzY = newTranslateY % 20;
			
			Teilpaket teilpaket = rect.getTeilpaket();
			for(ResGeometrie[] resAr : Diagramm.res) {
				for(ResGeometrie resG : resAr) {
					if(resG.getTeilpaket()==teilpaket) {
						System.out.println(resG.getId());
						resG.setTranslateX(resG.getTranslateX()+offsetX);
						resG.setTranslateY(resG.getTranslateY()+offsetY);
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
//			if (differenzX > 10.0) {
//				newTranslateX += differenzX;
//			} else {
//				newTranslateX -= differenzX;
//			}
//			
//			if (differenzY > 10.0) {
//				newTranslateY += differenzY;
//			} else {
//				newTranslateY -= differenzY;
//			}

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
