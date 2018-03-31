package ru.tblsk.owlz.busschedule.ui.routes.urban;


import ru.tblsk.owlz.busschedule.data.db.model.Direction;

public class ChangeDirectionUrban {

    private Direction direction;
    private int flightPosition;

    public ChangeDirectionUrban(Direction direction, int flightPosition) {
        this.direction = direction;
        this.flightPosition = flightPosition;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getFlightPosition() {
        return flightPosition;
    }

    public static class InFragment {

        private final int position;
        private final int directionType;

        public InFragment(int position, int directionType) {
            this.position = position;
            this.directionType = directionType;
        }

        public int getPosition() {
            return position;
        }

        public int getDirectionType() {
            return directionType;
        }
    }

    public static class InAdapter {

        private final int position;
        private final int directionType;

        public InAdapter(int position, int directionType) {
            this.position = position;
            this.directionType = directionType;
        }

        public int getPosition() {
            return position;
        }

        public int getDirectionType() {
            return directionType;
        }
    }
}
