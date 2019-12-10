package com.almasb.fxgl.pathfinding;

import com.almasb.fxgl.entity.component.Component;

import static java.lang.Math.abs;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public final class CellMoveComponent extends Component {

    private int nextCellX;
    private int nextCellY;

    private int cellWidth;
    private int cellHeight;
    private double speed;
    private boolean isAllowRotation = false;

    private boolean isMoving = false;

    public CellMoveComponent(int cellWidth, int cellHeight, double speed) {
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
        this.speed = speed;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void moveToCell(int cellX, int cellY) {
        nextCellX = cellX;
        nextCellY = cellY;
        isMoving = true;
    }

    public CellMoveComponent allowRotation(boolean isAllowRotation) {
        this.isAllowRotation = isAllowRotation;
        return this;
    }

    @Override
    public void onUpdate(double tpf) {
        if (!isMoving)
            return;

        double tpfSpeed = tpf * speed;

        // cell center
        int cx = nextCellX * cellWidth + cellWidth / 2;
        int cy = nextCellY * cellHeight + cellHeight / 2;

        var entityCenter = entity.getCenter();

        // move in x and y per frame
        double dx = cx - entityCenter.getX();
        double dy = cy - entityCenter.getY();

        if (isAllowRotation)
            updateRotation(dx, dy);

        int offsetX = (int) (entityCenter.getX() - entity.getX());
        int offsetY = (int) (entityCenter.getY() - entity.getY());

        if (abs(dx) <= tpfSpeed)
            entity.setX(cx - offsetX);
        else
            entity.translateX(tpfSpeed * Math.signum(dx));

        if (abs(dy) <= tpfSpeed)
            entity.setY(cy - offsetY);
        else
            entity.translateY(tpfSpeed * Math.signum(dy));

        // center after movement
        entityCenter = entity.getCenter();

        if ((int) entityCenter.getX() == cx && (int) entityCenter.getY() == cy) {
            isMoving = false;
        }
    }

    /**
     * @param dx move distance in X
     * @param dy move distance in Y
     */
    private void updateRotation(double dx, double dy) {
        if (dx > 0) {
            entity.setRotation(0);
        } else if (dx < 0) {
            entity.setRotation(180);
        } else if (dy > 0) {
            entity.setRotation(90);
        } else if (dy < 0) {
            entity.setRotation(270);
        }
    }
}