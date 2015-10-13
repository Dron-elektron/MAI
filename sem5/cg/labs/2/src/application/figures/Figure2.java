package application.figures;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;
import math.Vector;

public class Figure2 extends Figure {
	public Figure2(Slider[] params) {
		super();

		this.params = params;
	}

	@Override
	public void generate() {
		double paramXStart = params[0].getValue();
		double paramXEnd = params[1].getValue();
		double paramYStart = params[2].getValue();
		double paramYEnd = params[3].getValue();
		double paramA = params[4].getValue();
		int paramXStep = (int)params[5].getValue();
		int paramYStep = (int)params[6].getValue();
		double deltaX = (paramXEnd - paramXStart) / paramXStep;
		double deltaY = (paramYEnd - paramYStart) / paramYStep;

		points.clear();
		indexes.clear();

		for (int i = 0; i <= paramYStep; ++i) {
			for (int j = 0; j <= paramXStep; ++j) {
				double x = j * deltaX + paramXStart;
				double y = i * deltaY + paramYStart;
				double z = paramA * (x * x + y * y) / 400.0; // Чтобы не вылезло за nearPlane

				points.add(new Vector(x, y, z, 1.0));
			}
		}

		int pointsInRow = paramXStep + 1;

		for (int i = 0; i < paramYStep; ++i) {
			for (int j = 0; j < paramXStep; ++j) {
				indexes.add(i * pointsInRow + j);
				indexes.add((i + 1) * pointsInRow + j);
				indexes.add(i * pointsInRow + j + 1);
				indexes.add((i + 1) * pointsInRow + j);
				indexes.add((i + 1) * pointsInRow + j + 1);
				indexes.add(i * pointsInRow + j + 1);
			}
		}
	}

	@Override
	public void draw(Canvas canvas) {
		GraphicsContext gc = canvas.getGraphicsContext2D();

		gc.setFill(Color.BLACK);
		gc.fillRect(0.0, 0.0, canvas.getWidth(), canvas.getHeight());
		gc.setStroke(Color.WHITE);

		for (int i = 0; i < indexes.size(); i += 3) {
			double width = canvas.getWidth();
			double height = canvas.getHeight();
			Vector p1 = mat.transform(points.get(indexes.get(i))).perspectiveDivide();
			Vector p2 = mat.transform(points.get(indexes.get(i + 1))).perspectiveDivide();
			Vector p3 = mat.transform(points.get(indexes.get(i + 2))).perspectiveDivide();

			if (!inRange(0.0, 0.0, width - 1.0, height - 1.0, p1.getX(), p1.getY()) ||
					!inRange(0.0, 0.0, width - 1.0, height - 1.0, p2.getX(), p2.getY()) ||
					!inRange(0.0, 0.0, width - 1.0, height - 1.0, p3.getX(), p3.getY())) {
				continue;
			}

			drawTriangle(gc, p1, p2, p3);
		}
	}
}
